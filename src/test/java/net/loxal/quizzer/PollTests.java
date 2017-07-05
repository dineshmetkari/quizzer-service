/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer;

import net.loxal.quizzer.controller.PollController;
import net.loxal.quizzer.dto.Poll;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PollTests.Config.class)
public class PollTests {
    private final static Logger LOG = LoggerFactory.getLogger(PollTests.class);

    private static final Poll EXPECTED = new Poll(
            "create-id",
            "What is the significance of life?",
            Arrays.asList("First Option", "Second Option", "Third Option"),
            Collections.singletonList(2)
    );
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void create() throws Exception {
        createPoll();
    }

    @Test
    public void retrieve() throws Exception {
        createPoll();

        ResponseEntity<Poll> response = this.testRestTemplate.getForEntity(PollController.ENDPOINT + "/create-id", Poll.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        Poll actual = response.getBody();
        assertThat(actual).isEqualToComparingFieldByField(EXPECTED);
        assertThat(actual).isExactlyInstanceOf(Poll.class);
    }

    @Test
    public void update() throws Exception {
        Poll update = new Poll(
                "update-id",
                "What is the purpose of life?",
                Arrays.asList("First Option", "Second Option", "Third Option"),
                Collections.singletonList(1)
        );

        this.testRestTemplate.put(PollController.ENDPOINT, update);

        ResponseEntity<Poll> updated = this.testRestTemplate.getForEntity(PollController.ENDPOINT + "/update-id", Poll.class);

        assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updated.getBody()).isNotNull();

        Poll actual = updated.getBody();
        assertThat(actual).isEqualToComparingFieldByField(update);
        assertThat(actual).isExactlyInstanceOf(Poll.class);
    }

    @Test
    public void delete() throws Exception {
        createPoll();

        this.testRestTemplate.delete(PollController.ENDPOINT + "/create-id");

        ResponseEntity<Poll> deleted = this.testRestTemplate.getForEntity(PollController.ENDPOINT + "/create-id", Poll.class);

        assertThat(deleted.getBody()).isNull();
    }

    private void createPoll() {
        ResponseEntity<Poll> response = this.testRestTemplate.postForEntity(PollController.ENDPOINT, EXPECTED, Poll.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }

    @TestConfiguration
    public static class Config {
        @Value("${security.user.name}")
        private String username;

        @Value("${security.user.password}")
        private String password;

        @Bean
        public RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder().basicAuthorization(username, password);
        }
    }
}
