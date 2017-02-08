/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.integration;

import net.loxal.quizzer.PollTests;
import net.loxal.quizzer.controller.CertificateController;
import net.loxal.quizzer.controller.VoteController;
import net.loxal.quizzer.dto.Certificate;
import net.loxal.quizzer.dto.Vote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static net.loxal.quizzer.init.InitSimpsonsQuiz.SIMPSON_QUIZ_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PollTests.Config.class)
public class TakeSimpsonsQuiz {
    public static final String TEST_USER_NAME = "Test User Name " + UUID.randomUUID();
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void takeSimpsonsQuizAllCorrect() throws Exception {
        final String session = UUID.randomUUID().toString();

        Vote vote0 = new Vote(
                UUID.randomUUID().toString(),
                session,
                SIMPSON_QUIZ_PREFIX + "0",
                Collections.singletonList(0),
                TEST_USER_NAME
        );

        Vote vote1 = new Vote(
                UUID.randomUUID().toString(),
                session,
                SIMPSON_QUIZ_PREFIX + "1",
                Collections.singletonList(1),
                TEST_USER_NAME
        );

        Vote vote2 = new Vote(
                UUID.randomUUID().toString(),
                session,
                SIMPSON_QUIZ_PREFIX + "2",
                Collections.singletonList(0),
                TEST_USER_NAME
        );
        Vote vote3 = new Vote(
                UUID.randomUUID().toString(),
                session,
                SIMPSON_QUIZ_PREFIX + "3",
                Collections.singletonList(0),
                TEST_USER_NAME
        );
        Vote vote4 = new Vote(
                UUID.randomUUID().toString(),
                session,
                SIMPSON_QUIZ_PREFIX + "4",
                Collections.singletonList(1),
                TEST_USER_NAME
        );
        Vote vote5 = new Vote(
                UUID.randomUUID().toString(),
                session,
                SIMPSON_QUIZ_PREFIX + "5",
                Collections.singletonList(2),
                TEST_USER_NAME
        );

        Vote vote6 = new Vote(
                UUID.randomUUID().toString(),
                session,
                SIMPSON_QUIZ_PREFIX + "6",
                Collections.singletonList(2),
                TEST_USER_NAME
        );
        Vote vote7 = new Vote(
                UUID.randomUUID().toString(),
                session,
                SIMPSON_QUIZ_PREFIX + "7",
                Collections.singletonList(0),
                TEST_USER_NAME
        );
        Vote vote8 = new Vote(
                UUID.randomUUID().toString(),
                session,
                SIMPSON_QUIZ_PREFIX + "8",
                Collections.singletonList(2),
                TEST_USER_NAME
        );
        Vote vote9 = new Vote(
                UUID.randomUUID().toString(),
                session,
                SIMPSON_QUIZ_PREFIX + "9",
                Collections.singletonList(0),
                TEST_USER_NAME
        );

        final List<Vote> votes = Arrays.asList(
                vote0, vote1, vote2,
                vote3, vote4, vote5,
                vote6, vote7, vote8, vote9
        );

        validateAllVotesAsCorrect(votes);
        validateCertificate(session, 1.0f, 10, 0, true);
        validateCertificate();
    }

    private void validateCertificate(String session, float score, int correctAnswers, int incorrectAnswers, boolean hasPassed) {
        final ResponseEntity<Certificate> issuedCertificate = this.testRestTemplate.getForEntity(CertificateController.ENDPOINT + "/" + session, Certificate.class);
        assertThat(issuedCertificate.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(issuedCertificate.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        final Certificate certificate = issuedCertificate.getBody();

        assertThat(certificate.calculateScore()).isEqualTo(score);
        assertThat(certificate.hasPassed()).isEqualTo(hasPassed);
        assertThat(certificate.getIncorrectAnswers()).isEqualTo(incorrectAnswers);
        assertThat(certificate.getSession()).isEqualTo(session);
        assertThat(certificate.getId()).isEqualTo(session);

        int numberOfTotalAnswers = 10;
        assertThat(certificate.getCorrectAnswers()).isEqualTo(correctAnswers);
        assertThat(certificate.getTotalAnswers()).isEqualTo(numberOfTotalAnswers);

        assertThat(certificate.getGivenAnswers().size()).isEqualTo(numberOfTotalAnswers);
    }

    private void validateAllVotesAsCorrect(List<Vote> votes) {
        votes.forEach(vote -> {
            ResponseEntity<Vote> response = this.testRestTemplate.postForEntity(VoteController.ENDPOINT, vote, Vote.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            final Vote voted = response.getBody();
            assertThat(voted).isNotNull();
            assertThat(voted.getCorrect()).isEqualTo(true);
        });
    }

    @Test
    public void takeSimpsonsQuizSomeIncorrect() throws Exception {
        final String session = UUID.randomUUID().toString();

        Vote vote0 = new Vote(
                UUID.randomUUID().toString(),
                session,
                SIMPSON_QUIZ_PREFIX + "0",
                Collections.singletonList(0),
                TEST_USER_NAME
        );

        Vote vote1Incorrect = new Vote(
                UUID.randomUUID().toString(),
                session,
                SIMPSON_QUIZ_PREFIX + "1",
                Collections.singletonList(2),
                TEST_USER_NAME
        );

        Vote vote2 = new Vote(
                UUID.randomUUID().toString(),
                session,
                SIMPSON_QUIZ_PREFIX + "2",
                Collections.singletonList(0),
                TEST_USER_NAME
        );
        Vote vote3Incorrect = new Vote(
                UUID.randomUUID().toString(),
                session,
                SIMPSON_QUIZ_PREFIX + "3",
                Collections.singletonList(1),
                TEST_USER_NAME
        );
        Vote vote4 = new Vote(
                UUID.randomUUID().toString(),
                session,
                SIMPSON_QUIZ_PREFIX + "4",
                Collections.singletonList(1),
                TEST_USER_NAME
        );
        Vote vote5 = new Vote(
                UUID.randomUUID().toString(),
                session,
                SIMPSON_QUIZ_PREFIX + "5",
                Collections.singletonList(2),
                TEST_USER_NAME
        );

        Vote vote6 = new Vote(
                UUID.randomUUID().toString(),
                session,
                SIMPSON_QUIZ_PREFIX + "6",
                Collections.singletonList(2),
                TEST_USER_NAME
        );
        Vote vote7 = new Vote(
                UUID.randomUUID().toString(),
                session,
                SIMPSON_QUIZ_PREFIX + "7",
                Collections.singletonList(0),
                TEST_USER_NAME
        );
        Vote vote8 = new Vote(
                UUID.randomUUID().toString(),
                session,
                SIMPSON_QUIZ_PREFIX + "8",
                Collections.singletonList(2),
                TEST_USER_NAME
        );
        Vote vote9Incorrect = new Vote(
                UUID.randomUUID().toString(),
                session,
                SIMPSON_QUIZ_PREFIX + "9",
                Arrays.asList(0, 1),
                TEST_USER_NAME
        );

        final List<Vote> votes = Arrays.asList(
                vote0, vote1Incorrect, vote2,
                vote3Incorrect, vote4, vote5,
                vote6, vote7, vote8, vote9Incorrect
        );

        validateSomeVotesAsIncorrect(votes);
        validateCertificate(session, 0.7f, 7, 3, false);
    }

    private void validateSomeVotesAsIncorrect(List<Vote> votes) {
        votes.forEach(vote -> {
            ResponseEntity<Vote> response = this.testRestTemplate.postForEntity(VoteController.ENDPOINT, vote, Vote.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            final Vote voted = response.getBody();
            assertThat(voted).isNotNull();
            if (voted.getPoll().endsWith("1") || voted.getPoll().endsWith("3") || voted.getPoll().endsWith("9"))
                assertThat(voted.getCorrect()).isEqualTo(false);
            else
                assertThat(voted.getCorrect()).isEqualTo(true);
        });
    }

    private void validateCertificate() {
        ResponseEntity<Certificate[]> response = this.testRestTemplate
                .getForEntity(CertificateController.ENDPOINT + "?user=" + TEST_USER_NAME, Certificate[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        final Certificate[] certificates = response.getBody();

        assertThat(certificates.length).isNotNull();
        assertThat(certificates.length).isNotZero();
        assertThat(certificates.length).isNotNegative();
        assertThat(certificates.length).isEqualTo(2);

        for (Certificate certificate : certificates) {
            assertThat(certificate.getUser()).isEqualTo(TEST_USER_NAME);
        }
    }
}


