/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.controller;

import net.loxal.quizzer.dto.Poll;
import net.loxal.quizzer.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PollController.ENDPOINT)
public class PollController {
    public static final String ENDPOINT = "/polls";
    private final PollService service;

    @Autowired
    PollController(PollService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Poll create(@RequestBody Poll creation) {
        return service.create(creation);
    }

    @GetMapping(path = "{id}")
    Poll retrieve(@PathVariable("id") String id) {
        return service.retrieve(id);
    }

    @PutMapping
    Poll update(@RequestBody Poll update) {
        return service.update(update);
    }

    @DeleteMapping(path = "{id}")
    void delete(@PathVariable("id") String id) {
        service.delete(id);
    }
}
