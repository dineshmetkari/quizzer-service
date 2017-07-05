/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.dto;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

import static net.loxal.quizzer.controller.VoteController.ANONYMOUS_USER_NAME;

@Document
public class Vote {
    @Id
    private String id;
    @NotNull
    @Field
    private String poll = "";
    @NotNull
    @Field
    private String session;
    //    @Min(value = 0) // not supported since spring-data-couchbase:2.2.0
    @NotNull
    @Field
    private List<Integer> answers = Collections.emptyList();
    @NotNull
    private String user = ANONYMOUS_USER_NAME;
    @Field
    private Boolean correct;

    @JsonCreator
    public Vote(@JsonProperty("id") String id, @JsonProperty("session") String session, @JsonProperty("poll") String poll,
                @JsonProperty("answers") List<Integer> answers, @JsonProperty("user") String user) {
        this.id = id;
        this.session = session;
        this.poll = poll;
        this.answers = answers;
        this.user = user;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoll() {
        return poll;
    }

    public void setPoll(String poll) {
        this.poll = poll;
    }

    public List<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Integer> answers) {
        this.answers = answers;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    @Override
    public String toString() {
        return "Vote{" + "id='" + id + '\'' +
                ", poll='" + poll + '\'' +
                ", session='" + session + '\'' +
                ", answers=" + answers +
                ", user='" + user + '\'' +
                ", correct=" + correct +
                '}';
    }
}
