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
import java.util.Collections;
import java.util.List;

@Document
public class Poll {
    static final float PASSING_SCORE = 0.75f;
    @Id
    private String id;
    @Field
    @NotNull
    private String question = "";
    @Field
    @NotNull
    private List<String> options = Collections.emptyList();
    @Field
    @NotNull
    private List<Integer> correctAnswers = Collections.emptyList(); // TODO make them invisible for non-admins
    /**
     * Provide a hint for the user & UI.
     */
    @Field
    @NotNull
    private Boolean multipleAnswers = false;

    /**
     * Triggers certificate issuance.
     */
    @Field
    private boolean lastQuestion;

    @Field
    @NotNull
    private float passScore = PASSING_SCORE;

    /**
     * Duration in seconds; negative number signifies unlimited question without timeout.
     */
    @Field
    private int timeout = -1;

    /**
     * Importance of the question in relation to other question.
     * By default all questions are equally important, signified by weight of “1”.
     * A weight of “2” would mean that a question is twice as important as a question with a weight of “1”.
     * The weight should be considered while calculating the score in the certificate.
     */
    private int weight = 1;

    @JsonCreator
    public Poll(
            @JsonProperty("id") String id,
            @JsonProperty("question") String question,
            @JsonProperty("options") List<String> options,
            @JsonProperty("correctAnswers") List<Integer> correctAnswers
    ) {
        this.id = id;

        this.question = question;
        this.options = options;
        this.correctAnswers = correctAnswers;
        this.multipleAnswers = correctAnswers.size() > 1;
    }

    public boolean getLastQuestion() {
        return lastQuestion;
    }

    public void setLastQuestion(boolean lastQuestion) {
        this.lastQuestion = lastQuestion;
    }

    public float getPassScore() {
        return passScore;
    }

    public void setPassScore(float passScore) {
        this.passScore = passScore;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getMultipleAnswers() {
        return multipleAnswers;
    }

    public List<Integer> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<Integer> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "Poll{" + "id='" + id + '\'' +
                ", question='" + question + '\'' +
                ", options=" + options +
                ", correctAnswers=" + correctAnswers +
                ", multipleAnswers=" + multipleAnswers +
                '}';
    }
}
