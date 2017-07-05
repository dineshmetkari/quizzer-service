/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer;

import net.loxal.quizzer.controller.CertificateController;
import net.loxal.quizzer.dto.Certificate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PollTests.Config.class)
public class CertificateTests {
    private static final Certificate EXPECTED = new Certificate(
            "certificate-id",
            "user-id",
            "session-id"
    );

    private static final Certificate UPDATED = new Certificate(
            EXPECTED.getId(),
            EXPECTED.getUser(),
            EXPECTED.getSession()
    );

    static {
        EXPECTED.setCorrectAnswers(1);
        EXPECTED.setIncorrectAnswers(2);

        UPDATED.setCorrectAnswers(EXPECTED.getCorrectAnswers() + 1);
        UPDATED.setIncorrectAnswers(EXPECTED.getIncorrectAnswers() + 1);
    }

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void createAndRetrieve() throws Exception {
        createEntity();

        ResponseEntity<Certificate> retrieved = retrieveEntity();
        validate(retrieved, EXPECTED);
    }

    private void createEntity() {
        ResponseEntity<Certificate> response = this.testRestTemplate.postForEntity(CertificateController.ENDPOINT, EXPECTED, Certificate.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        validate(response, EXPECTED);
    }

    private void validate(ResponseEntity<Certificate> response, Certificate reference) {
        final Certificate actual = response.getBody();
        assertThat(actual).isNotNull();
        assertThat(actual).isInstanceOf(Certificate.class);
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(reference);
        assertThat(actual).isEqualToComparingFieldByField(reference);
    }

    @Test
    public void update() throws Exception {
        createEntity();

        this.testRestTemplate.put(CertificateController.ENDPOINT, UPDATED);

        ResponseEntity<Certificate> retrieved = retrieveEntity();
        validate(retrieved, UPDATED);
    }

    @Test
    public void delete() throws Exception {
        this.testRestTemplate.delete(CertificateController.ENDPOINT + "/" + EXPECTED.getId());

        ResponseEntity<Certificate> retrieved = retrieveEntity();
        assertThat(retrieved.getBody()).isNull();
    }

    private ResponseEntity<Certificate> retrieveEntity() {
        ResponseEntity<Certificate> retrieved = this.testRestTemplate.getForEntity(CertificateController.ENDPOINT + "/" + EXPECTED.getId(), Certificate.class);
        assertThat(retrieved.getStatusCode()).isEqualTo(HttpStatus.OK);
        return retrieved;
    }
}
