/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.repository;

import net.loxal.quizzer.dto.Certificate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UptimeRepository extends CrudRepository<Certificate, String> {
    Set<Certificate> findByUser(String user);
}