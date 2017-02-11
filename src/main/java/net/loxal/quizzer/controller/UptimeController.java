/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.controller;

import net.loxal.quizzer.dto.Uptime;
import net.loxal.quizzer.service.UptimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UptimeController.ENDPOINT)
public class UptimeController {
    public static final String ENDPOINT = "/uptimes";
    private final UptimeService service;

    private final CassandraOperations cassandraOperations;

    @Autowired
    UptimeController(UptimeService service, CassandraOperations cassandraOperations) {
        this.service = service;
        this.cassandraOperations = cassandraOperations;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Uptime create(@RequestBody Uptime creation) {
        return service.create(creation);
    }

    @GetMapping(path = "{id}")
    Uptime retrieve(@PathVariable("id") String id) {
        String cqlAll = "select * from usertable";
        List<Object> results = cassandraOperations.select(cqlAll, Object.class);
        for (Object p : results) {
            System.out.println("p = " + p);
        }

        return service.retrieve(id);
    }

    @PutMapping
    Uptime update(@RequestBody Uptime update) {
        return service.update(update);
    }

    @DeleteMapping(path = "{id}")
    void delete(@PathVariable("id") String id) {
        service.delete(id);
    }
}
