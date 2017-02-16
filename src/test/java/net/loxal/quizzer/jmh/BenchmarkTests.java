/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.jmh;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openjdk.jmh.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@State(Scope.Benchmark)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BenchmarkTests {
    private final static Logger LOG = LoggerFactory.getLogger(BenchmarkTests.class);

    @BenchmarkMode(Mode.SingleShotTime)
    @Benchmark
    @Test
    public void benchmark() throws Exception {
        LOG.info("benchmarked");
    }
}
