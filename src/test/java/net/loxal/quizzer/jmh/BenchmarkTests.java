/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.jmh;

import net.loxal.quizzer.VoteTests;
import net.loxal.quizzer.controller.VoteController;
import net.loxal.quizzer.dto.Vote;
import org.openjdk.jmh.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@State(Scope.Benchmark)
public class BenchmarkTests {
    private final static Logger LOG = LoggerFactory.getLogger(BenchmarkTests.class);

    private static final Properties PROPERTIES = new Properties();
    private static String USER_NAME;
    private static String USER_PASSWORD;

    static {
        try {
            PROPERTIES.load(BenchmarkTests.class.getResourceAsStream("/application.properties"));
            USER_NAME = PROPERTIES.getProperty("security.user.name");
            USER_PASSWORD = PROPERTIES.getProperty("security.user.password");
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }

    @BenchmarkMode(Mode.SingleShotTime)
    @Threads(5)
    @Benchmark
    public void benchmark() throws Exception {
        final TestRestTemplate testRestTemplate = new TestRestTemplate();
        final ResponseEntity<Vote> vote = testRestTemplate
                .withBasicAuth(USER_NAME, USER_PASSWORD)
                .postForEntity("http://localhost:8200" + VoteController.ENDPOINT, VoteTests.EXPECTED_MULTIPLE_ANSWERS_CORRECT, Vote.class);

        assertThat(vote.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(vote.getBody()).isNotNull();
    }
}
