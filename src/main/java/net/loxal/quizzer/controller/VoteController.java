/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.controller;

import net.loxal.quizzer.dto.Vote;
import net.loxal.quizzer.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(VoteController.ENDPOINT)
public class VoteController {
    public static final String ANONYMOUS_USER_NAME = "anonymous";
    public static final String ENDPOINT = "/votes";
    private final VoteService service;

    @Autowired
    VoteController(VoteService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    Vote create(@RequestBody Vote creation) {
        return service.create(creation);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    Vote retrieve(@PathVariable("id") String id) {
        return service.retrieve(id);
    }

    @RequestMapping(method = RequestMethod.PUT)
    Vote update(@RequestBody Vote update) {
        return service.update(update);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    void delete(@PathVariable("id") String id) {
        service.delete(id);
    }
}
