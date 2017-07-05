/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.jmh;

import net.loxal.quizzer.VoteTests;
import net.loxal.quizzer.controller.VoteController;
import net.loxal.quizzer.dto.Vote;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Threads;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class BenchmarkTests {
    private final static Logger LOG = LoggerFactory.getLogger(BenchmarkTests.class);

    @BenchmarkMode(Mode.SingleShotTime)
    @Threads(2)
    @Benchmark
    public void benchmark() throws Exception {
        final TestRestTemplate testRestTemplate = new TestRestTemplate();

        final ResponseEntity<Vote> vote = testRestTemplate
                .postForEntity("http://localhost:8200" + VoteController.ENDPOINT,
                        VoteTests.EXPECTED_MULTIPLE_ANSWERS_CORRECT, Vote.class);

        assertEquals(new URI("http://localhost:8200/"), vote.getHeaders().getLocation());
        assertThat(vote.getStatusCodeValue()).isEqualTo(HttpStatus.FOUND.value());
        assertThat(vote.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(vote.getBody()).isNull();
    }
}
