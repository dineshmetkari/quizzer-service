/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.controller;

import net.loxal.quizzer.dto.Certificate;
import net.loxal.quizzer.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(CertificateController.ENDPOINT)
public class CertificateController {
    public static final String ENDPOINT = "/certificates";
    private final CertificateService service;

    @Autowired
    CertificateController(CertificateService service) {
        this.service = service;
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
