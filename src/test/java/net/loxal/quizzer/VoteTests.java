/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer;

import net.loxal.quizzer.controller.PollController;
import net.loxal.quizzer.controller.VoteController;
import net.loxal.quizzer.dto.Poll;
import net.loxal.quizzer.dto.Vote;
import net.loxal.quizzer.integration.TakeSimpsonsQuiz;
import org.junit.Before;
import org.junit.BeforeClass;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PollTests.Config.class)
public class VoteTests {
    private static final List<Integer> CORRECT_ANSWERS = Arrays.asList(0, 3);
    private static final List<Integer> INCORRECT_ANSWERS = Arrays.asList(0, 2);
    private static final List<Integer> CORRECT_ANSWER = Collections.singletonList(2);
    private static final List<Integer> INCORRECT_ANSWER = Collections.singletonList(0);
    private static final String UNREVIEWABLE_POLL_ID = "unreviewable-poll-id";
    private static final String SINGLE_ANSWER_VOTE_ID = "single-answer-vote-id";
    private static final String SINGLE_ANSWER_POLL_ID = "single-answer-poll-id";
    private static final String MULTIPLE_ANSWERS_POLL_ID = "multiple-answers-poll-id";
    private static final String MULTIPLE_ANSWERS_CORRECT_ID = "multiple-answers-correct-vote-id";
    private static final String MULTIPLE_ANSWERS_INCORRECT_ID = "multiple-answers-incorrect-vote-id";
    private static final String SESSION_SINGLE_ANSWER_ID = "session-single-answer-id";
    private static final String SESSION_MULTIPLE_ANSWERS_ID = "session-multiple-answers-id";
    public static final Vote EXPECTED_MULTIPLE_ANSWERS_CORRECT = new Vote(
            MULTIPLE_ANSWERS_CORRECT_ID,
            SESSION_MULTIPLE_ANSWERS_ID,
            MULTIPLE_ANSWERS_POLL_ID,
            CORRECT_ANSWERS,
            TakeSimpsonsQuiz.TEST_USER_NAME
    );
    private static final Vote EXPECTED_UNREVIEWABLE = new Vote(
            SINGLE_ANSWER_VOTE_ID,
            SESSION_SINGLE_ANSWER_ID,
            UNREVIEWABLE_POLL_ID,
            CORRECT_ANSWER,
            TakeSimpsonsQuiz.TEST_USER_NAME
    );
    private static final Vote EXPECTED_UNREVIEWABLE_UPDATE = new Vote(
            SINGLE_ANSWER_VOTE_ID,
            SESSION_SINGLE_ANSWER_ID,
            UNREVIEWABLE_POLL_ID,
            INCORRECT_ANSWER,
            TakeSimpsonsQuiz.TEST_USER_NAME
    );
    private static final Vote EXPECTED_SINGLE_ANSWER_CORRECT = new Vote(
            SINGLE_ANSWER_VOTE_ID,
            SESSION_SINGLE_ANSWER_ID,
            SINGLE_ANSWER_POLL_ID,
            CORRECT_ANSWER,
            TakeSimpsonsQuiz.TEST_USER_NAME
    );
    private static final Vote EXPECTED_SINGLE_ANSWER_CORRECT_UPDATE_TO_INCORRECT = new Vote(
            SINGLE_ANSWER_VOTE_ID,
            SESSION_SINGLE_ANSWER_ID,
            SINGLE_ANSWER_POLL_ID,
            INCORRECT_ANSWER,
            TakeSimpsonsQuiz.TEST_USER_NAME
    );
    private static final Vote EXPECTED_SINGLE_ANSWER_INCORRECT = new Vote(
            SINGLE_ANSWER_VOTE_ID,
            SESSION_SINGLE_ANSWER_ID,
            SINGLE_ANSWER_POLL_ID,
            INCORRECT_ANSWER,
            TakeSimpsonsQuiz.TEST_USER_NAME
    );
    private static final Vote EXPECTED_SINGLE_ANSWER_INCORRECT_UPDATE_TO_CORRECT = new Vote(
            SINGLE_ANSWER_VOTE_ID,
            SESSION_SINGLE_ANSWER_ID,
            SINGLE_ANSWER_POLL_ID,
            CORRECT_ANSWER,
            TakeSimpsonsQuiz.TEST_USER_NAME
    );
    private static final Vote EXPECTED_MULTIPLE_ANSWERS_CORRECT_UPDATE_TO_INCORRECT = new Vote(
            MULTIPLE_ANSWERS_CORRECT_ID,
            SESSION_MULTIPLE_ANSWERS_ID,
            MULTIPLE_ANSWERS_POLL_ID,
            INCORRECT_ANSWERS,
            TakeSimpsonsQuiz.TEST_USER_NAME
    );
    private static final Vote EXPECTED_MULTIPLE_ANSWERS_INCORRECT = new Vote(
            MULTIPLE_ANSWERS_INCORRECT_ID,
            SESSION_MULTIPLE_ANSWERS_ID,
            MULTIPLE_ANSWERS_POLL_ID,
            INCORRECT_ANSWERS,
            TakeSimpsonsQuiz.TEST_USER_NAME
    );
    private static final Vote EXPECTED_MULTIPLE_ANSWERS_INCORRECT_UPDATE_TO_CORRECT = new Vote(
            MULTIPLE_ANSWERS_INCORRECT_ID,
            SESSION_MULTIPLE_ANSWERS_ID,
            MULTIPLE_ANSWERS_POLL_ID,
            CORRECT_ANSWERS,
            TakeSimpsonsQuiz.TEST_USER_NAME
    );
    private static final Poll REFERENCE_SINGLE_ANSWER = new Poll(
            SINGLE_ANSWER_POLL_ID,
            "What is the significance of life?",
            Arrays.asList("First Option", "Second Option", "Third Option", "Fourth Option"),
            CORRECT_ANSWER
    );
    private static final Poll REFERENCE_MULTIPLE_ANSWERS = new Poll(
            MULTIPLE_ANSWERS_POLL_ID,
            "What are the goals of life?",
            Arrays.asList("First Option", "Second Option", "Third Option", "Fourth Option"),
            CORRECT_ANSWERS
    );
    private static final Poll REFERENCE_UNREVIEWABLE = new Poll(
            UNREVIEWABLE_POLL_ID,
            "What is your favorite food?",
            Arrays.asList("First Option", "Second Option", "Third Option", "Fourth Option"),
            Collections.emptyList()
    );

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeClass
    public static void prepare() throws Exception {
        EXPECTED_MULTIPLE_ANSWERS_CORRECT.setCorrect(true);
        EXPECTED_MULTIPLE_ANSWERS_INCORRECT.setCorrect(false);

        EXPECTED_SINGLE_ANSWER_CORRECT.setCorrect(true);
        EXPECTED_SINGLE_ANSWER_INCORRECT.setCorrect(false);

        EXPECTED_MULTIPLE_ANSWERS_CORRECT_UPDATE_TO_INCORRECT.setCorrect(false);
        EXPECTED_MULTIPLE_ANSWERS_INCORRECT_UPDATE_TO_CORRECT.setCorrect(true);

        EXPECTED_SINGLE_ANSWER_CORRECT_UPDATE_TO_INCORRECT.setCorrect(false);
        EXPECTED_SINGLE_ANSWER_INCORRECT_UPDATE_TO_CORRECT.setCorrect(true);
    }

    @Before
    public void setUp() throws Exception {
        createPoll(REFERENCE_MULTIPLE_ANSWERS);
        createPoll(REFERENCE_SINGLE_ANSWER);
        createPoll(REFERENCE_UNREVIEWABLE);
    }

    @Test
    public void createCorrectVoteMultipleAnswers() throws Exception {
        createVote(EXPECTED_MULTIPLE_ANSWERS_CORRECT);

        validateFetched(EXPECTED_MULTIPLE_ANSWERS_CORRECT);
    }

    @Test
    public void createIncorrectVoteMultipleAnswers() throws Exception {
        createVote(EXPECTED_MULTIPLE_ANSWERS_INCORRECT);

        validateFetched(EXPECTED_MULTIPLE_ANSWERS_INCORRECT);
    }

    @Test
    public void createCorrectVoteSingleAnswer() throws Exception {
        createVote(EXPECTED_SINGLE_ANSWER_CORRECT);

        validateFetched(EXPECTED_SINGLE_ANSWER_CORRECT);
    }

    @Test
    public void createIncorrectVoteSingleAnswer() throws Exception {
        createVote(EXPECTED_SINGLE_ANSWER_INCORRECT);

        validateFetched(EXPECTED_SINGLE_ANSWER_INCORRECT);
    }

    @Test
    public void updateCorrectVoteMultipleAnswers() throws Exception {
        createVote(EXPECTED_MULTIPLE_ANSWERS_CORRECT);
        updateVote(EXPECTED_MULTIPLE_ANSWERS_CORRECT);

        validateFetched(EXPECTED_MULTIPLE_ANSWERS_CORRECT_UPDATE_TO_INCORRECT);
    }

    @Test
    public void updateIncorrectVoteMultipleAnswers() throws Exception {
        createVote(EXPECTED_MULTIPLE_ANSWERS_INCORRECT);
        updateVote(EXPECTED_MULTIPLE_ANSWERS_INCORRECT);

        validateFetched(EXPECTED_MULTIPLE_ANSWERS_INCORRECT_UPDATE_TO_CORRECT);
    }

    @Test
    public void updateCorrectVoteSingleAnswer() throws Exception {
        createVote(EXPECTED_SINGLE_ANSWER_CORRECT);
        updateVote(EXPECTED_SINGLE_ANSWER_CORRECT);

        validateFetched(EXPECTED_SINGLE_ANSWER_CORRECT_UPDATE_TO_INCORRECT);
    }

    @Test
    public void updateIncorrectVoteSingleAnswer() throws Exception {
        createVote(EXPECTED_SINGLE_ANSWER_INCORRECT);
        updateVote(EXPECTED_SINGLE_ANSWER_INCORRECT);

        validateFetched(EXPECTED_SINGLE_ANSWER_INCORRECT_UPDATE_TO_CORRECT);
    }

    @Test
    public void createUnreviewableVote() throws Exception {
        createVote(EXPECTED_UNREVIEWABLE);

        validateFetched(EXPECTED_UNREVIEWABLE);
    }

    @Test
    public void updateUnreviewableVote() throws Exception {
        createVote(EXPECTED_UNREVIEWABLE);
        updateVote(EXPECTED_UNREVIEWABLE);

        validateFetched(EXPECTED_UNREVIEWABLE_UPDATE);
    }

    @Test
    public void delete() throws Exception {
        createVote(EXPECTED_MULTIPLE_ANSWERS_CORRECT);

        testRestTemplate.delete(VoteController.ENDPOINT + "/" + EXPECTED_MULTIPLE_ANSWERS_CORRECT.getId());

        ResponseEntity<Vote> deleted = testRestTemplate.getForEntity(VoteController.ENDPOINT + "/" + EXPECTED_MULTIPLE_ANSWERS_CORRECT.getId(), Vote.class);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(deleted.getBody()).isNull();
    }

    private void validateFetched(final Vote expected) {
        ResponseEntity<Vote> retrieved = testRestTemplate.getForEntity(VoteController.ENDPOINT + "/" + expected.getId(), Vote.class);

        assertThat(retrieved.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(retrieved.getBody()).isNotNull();

        Vote actual = retrieved.getBody();
        assertThat(actual).isEqualToComparingFieldByField(expected);
        assertThat(actual).isExactlyInstanceOf(Vote.class);
    }

    private void createPoll(Poll poll) {
        ResponseEntity<Poll> response = testRestTemplate.postForEntity(PollController.ENDPOINT, poll, Poll.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }

    private void createVote(Vote vote) {
        ResponseEntity<Vote> response = testRestTemplate.postForEntity(VoteController.ENDPOINT, vote, Vote.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }

    private void updateVote(Vote existing) {
        Vote updated = new Vote(
                existing.getId(),
                existing.getSession(),
                existing.getPoll(),
                invertCorrectAnswerSet(existing),
                TakeSimpsonsQuiz.TEST_USER_NAME
        );
        if (existing.getCorrect() != null)
            updated.setCorrect(!existing.getCorrect());
        updated.setUser(existing.getUser());
        testRestTemplate.put(VoteController.ENDPOINT, updated);
    }

    private List<Integer> invertCorrectAnswerSet(Vote existing) {
        if (existing.getAnswers().size() == 1)
            return existing.getAnswers()
                    .equals(INCORRECT_ANSWER) ? CORRECT_ANSWER : INCORRECT_ANSWER;
        else
            return existing.getAnswers()
                    .equals(INCORRECT_ANSWERS) ? CORRECT_ANSWERS : INCORRECT_ANSWERS;
    }
}
