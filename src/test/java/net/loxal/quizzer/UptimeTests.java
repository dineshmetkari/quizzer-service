/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer;

import net.loxal.quizzer.dto.Uptime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PollTests.Config.class)
public class UptimeTests {
    private final static Logger LOG = LoggerFactory.getLogger(UptimeTests.class);

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CassandraOperations cassandraOperations;

    @Test
    public void sometest() throws Exception {
        try {
            cassandraOperations.execute("create keyspace test_keyspace with replication = {'class':'SimpleStrategy','replication_factor' : 2}");
        } catch (DataAccessException e) {
            LOG.warn(e.getMessage());
        }
        final String createTable = "create table uptime_test (endpoint varchar primary key, intervalInSeconds int)";
        try {
            cassandraOperations.execute(createTable);
        } catch (DataAccessException e) {
            LOG.warn(e.getMessage());
            cassandraOperations.execute("drop table uptime_test");
            cassandraOperations.execute(createTable);
        }

        cassandraOperations.execute("insert into uptime_test (endpoint, intervalInSeconds) values ('http://example.com', 9)");
        cassandraOperations.execute("update uptime_test set intervalInSeconds = 5 where endpoint = 'http://example.com'");

        final List<Uptime> selection = cassandraOperations.select("select * from uptime_test", Uptime.class);
        assertFalse(selection.isEmpty());
        selection.forEach(e -> LOG.info(e.toString()));

        cassandraOperations.execute("delete from uptime_test where endpoint = 'http://example.com'");

        LOG.info("Table content after deletion");
        final List<Uptime> selectAfterDelete = cassandraOperations.select("select * from uptime_test", Uptime.class);
        selectAfterDelete.forEach(e -> LOG.info(e.toString()));
        assertTrue(selectAfterDelete.isEmpty());
    }
}
