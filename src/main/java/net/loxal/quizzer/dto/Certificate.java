/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.dto;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Collections;
import java.util.Set;

@Document
public class Certificate {

    @Id
    @Field
    private String id;
    @Field
    @NotNull
    private String user;
    @Field
    @NotNull
    private String session;
    @Field
    @NotNull
    private String timestamp;
    @Field
    @NotNull
    private Integer correctAnswers = 0;
    @Field
    @NotNull
    private Integer incorrectAnswers = 0;

    private transient Integer totalAnswers;

    @Field
    @NotNull
    private Set<Vote> givenAnswers = Collections.emptySet();

    @JsonCreator
    public Certificate(
            @JsonProperty("id") String id,
            @JsonProperty("user") String user,
            @JsonProperty("session") String session
    ) {
        this.id = id;
        this.user = user;
        this.session = session;
        this.timestamp = Instant.now().toString();
    }

    public boolean hasPassed() {
        return Poll.PASSING_SCORE <= calculateScore();
    }

    public float calculateScore() {
        return (float) correctAnswers / (correctAnswers + incorrectAnswers);
    }

    public Integer getTotalAnswers() {
        return getCorrectAnswers() + getIncorrectAnswers();
    }

    public Set<Vote> getGivenAnswers() {
        return givenAnswers;
    }

    public void setGivenAnswers(Set<Vote> givenAnswers) {
        this.givenAnswers = givenAnswers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Integer getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Integer correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Integer getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(Integer incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }
}
