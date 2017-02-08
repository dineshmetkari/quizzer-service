/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.service;

import net.loxal.quizzer.dto.Certificate;
import net.loxal.quizzer.repository.UptimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UptimeService {
    private final UptimeRepository repository;

    @Autowired
    public UptimeService(final UptimeRepository repository) {
        this.repository = repository;
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
}