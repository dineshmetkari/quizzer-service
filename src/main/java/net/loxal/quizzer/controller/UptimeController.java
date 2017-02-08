/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.controller;

import net.loxal.quizzer.dto.Certificate;
import net.loxal.quizzer.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(UptimeController.ENDPOINT)
public class UptimeController {
    public static final String ENDPOINT = "/uptimes";
    private final CertificateService service;

    private final CassandraOperations cassandraOperations;

    @Autowired
    UptimeController(CertificateService service, CassandraOperations cassandraOperations) {
        this.service = service;
        this.cassandraOperations = cassandraOperations;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Certificate create(@RequestBody Certificate creation) {
        return service.create(creation);
    }

    @GetMapping(path = "{id}")
    Certificate retrieve(@PathVariable("id") String id) {
        return service.retrieve(id);
    }

    @GetMapping
    Set<Certificate> retrieveFor(
            @RequestParam(value = "user", required = true, defaultValue = "anonymous")
                    String user
    ) {
        String cqlAll = "select * from usertable";
        List<Object> results = cassandraOperations.select(cqlAll, Object.class);
        for (Object p : results) {
            System.out.println("p = " + p);
        }

        return service.retrieveByUser(user);
    }

    @PutMapping
    Certificate update(@RequestBody Certificate update) {
        return service.update(update);
    }

    @DeleteMapping(path = "{id}")
    void delete(@PathVariable("id") String id) {
        service.delete(id);
    }
}
