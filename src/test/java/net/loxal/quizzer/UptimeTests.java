/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import net.loxal.quizzer.dto.Uptime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cassandra.core.Cancellable;
import org.springframework.cassandra.core.WriteOptions;
import org.springframework.dao.DataAccessException;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.WriteListener;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UptimeTests {
    private final static Logger LOG = LoggerFactory.getLogger(UptimeTests.class);
    private static final Uptime EXPECTED = new Uptime("http://example.com", 9);
    private static final Uptime EXPECTED_UPDATE = new Uptime(EXPECTED.getEndpoint(), 5);
    private static final Select SELECTION = QueryBuilder.select().from(Uptime.class.getSimpleName());

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CassandraOperations cassandraOperations;

    @Test
    public void create() throws Exception {
        Uptime inserted = cassandraOperations.insert(EXPECTED);
        assertNotNull(inserted);

        List<Uptime> created = cassandraOperations.select(SELECTION, Uptime.class);

        assertFalse(created.isEmpty());
        assertEquals(EXPECTED, created.get(0));
    }

    @Test
    public void delete() throws Exception {
        cassandraOperations.deleteAll(Uptime.class);

        List<Uptime> readFromEmptyTable = cassandraOperations.select(SELECTION, Uptime.class);

        assertTrue(readFromEmptyTable.isEmpty());
    }

    @Test
    public void update() throws Exception {
        Cancellable updateable = cassandraOperations.updateAsynchronously(EXPECTED_UPDATE, new WriteListener<Uptime>() {
            @Override
            public void onWriteComplete(Collection<Uptime> entities) {
                assertFalse(entities.isEmpty());
                entities.forEach(e -> assertEquals(EXPECTED_UPDATE, e));
            }

            @Override
            public void onException(Exception x) {
                LOG.error(x.getMessage());
            }
        }, WriteOptions.builder()
                .consistencyLevel(ConsistencyLevel.ANY)
                .fetchSize(1)
                .tracing(false)
                .readTimeout(100, TimeUnit.MILLISECONDS)
                .ttl(10)
                .withTracing()
                .build());
    }

    @Test
    public void init() throws Exception {
        try {
            cassandraOperations.execute("create keyspace quizzer with replication = {'class':'SimpleStrategy','replication_factor' : 2}");
        } catch (DataAccessException e) {
            LOG.warn(e.getMessage());
        }
        final String createTable = "create table uptime (endpoint varchar primary key, intervalInSeconds int)";
        try {
            cassandraOperations.execute(createTable);
        } catch (DataAccessException e) {
            LOG.warn(e.getMessage());
            cassandraOperations.execute("drop table uptime");
            cassandraOperations.execute(createTable);
        }
    }
}
