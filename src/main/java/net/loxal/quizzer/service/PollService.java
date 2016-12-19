/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.service;

import net.loxal.quizzer.dto.Poll;
import net.loxal.quizzer.repository.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PollService {
    private final PollRepository repository;

    @Autowired
    public PollService(final PollRepository repository) {
        this.repository = repository;
    }

    public Poll create(Poll creation) {
        return repository.save(creation);
    }

    public Poll retrieve(String id) {
        return repository.findOne(id);
    }

    public Poll update(Poll update) {
        return repository.save(update);
    }

    public void delete(String id) {
        repository.delete(id);
    }
}