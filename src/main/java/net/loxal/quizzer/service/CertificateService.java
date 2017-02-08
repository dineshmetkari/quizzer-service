/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.service;

import net.loxal.quizzer.dto.Certificate;
import net.loxal.quizzer.dto.Vote;
import net.loxal.quizzer.repository.CertificateRepository;
import net.loxal.quizzer.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CertificateService {
    private final CertificateRepository repository;
    private final VoteRepository voteRepository;

    @Autowired
    public CertificateService(final CertificateRepository repository, final VoteRepository voteRepository) {
        this.repository = repository;
        this.voteRepository = voteRepository;
    }
    public Certificate create(Certificate creation) {
        return repository.save(creation);
    }

    public Certificate retrieve(String id) {
        return repository.findOne(id);
    }

    public Certificate update(Certificate update) {
        return repository.save(update);
    }

    public void delete(String id) {
        repository.delete(id);
    }

    Certificate issue(String sessionId) {
        final Set<Vote> answersOfSession = voteRepository.findBySession(sessionId);

        final String user = answersOfSession.stream().findAny().get().getUser();
        final Certificate certificate = new Certificate(
                sessionId,
                user,
                sessionId
        );

        answersOfSession.forEach(answer -> updateScoreDetails(certificate, answer));
        certificate.setGivenAnswers(answersOfSession);

        return repository.save(certificate);
    }

    private void updateScoreDetails(Certificate certificate, Vote answer) {
        if (answer.getCorrect()) {
            certificate.setCorrectAnswers(certificate.getCorrectAnswers() + 1);
        } else {
            certificate.setIncorrectAnswers(certificate.getIncorrectAnswers() + 1);
        }
    }

    public Set<Certificate> retrieveByUser(String user) {
        return repository.findByUser(user);
    }
}