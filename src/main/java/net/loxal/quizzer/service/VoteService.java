/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.service;

import net.loxal.quizzer.dto.Poll;
import net.loxal.quizzer.dto.Vote;
import net.loxal.quizzer.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {
    private final VoteRepository repository;
    private final PollService pollService;
    private final CertificateService certificateService;

    @Autowired
    public VoteService(final VoteRepository repository, final PollService pollService, final CertificateService certificateService) {
        this.repository = repository;
        this.pollService = pollService;
        this.certificateService = certificateService;
    }

    public Vote create(Vote creation) {
        return answerQuestion(creation);
    }

    private Vote answerQuestion(Vote answer) {
        final Poll lastQuestionSignifier = review(answer);
        final Vote saved = repository.save(answer);

        if (lastQuestionSignifier.getLastQuestion()) {
            certificateService.issue(saved.getSession());
        }

        return saved;
    }

    private Poll review(Vote review) {
        final String pollId = review.getPoll();
        final Poll reviewReference = pollService.retrieve(pollId);

        if (reviewReference.getCorrectAnswers().isEmpty()) return reviewReference;

        final boolean isCorrect = reviewReference.getCorrectAnswers().equals(review.getAnswers());
        review.setCorrect(isCorrect);

        return reviewReference;
    }

    public Vote retrieve(String id) {
        return repository.findOne(id);
    }

    public Vote update(Vote update) {
        return answerQuestion(update);
    }

    public void delete(String id) {
        repository.delete(id);
    }
}