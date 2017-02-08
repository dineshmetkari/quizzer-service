/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@RestController
public class Application {

    private final static Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(final String... args) {
        SpringApplication.run(Application.class, args);
    }

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }

    @EnableCassandraRepositories
    @Configuration
    static class ConfigurationCassandra extends AbstractCassandraConfiguration {

        @Value("${spring.data.cassandra.contact-points}")
        private String contactPoints;

        @Value("${spring.data.cassandra.keyspace-name}")
        private String keyspaceName;

        @Override
        public String getContactPoints() {
            return contactPoints;
        }

        @Override
        protected String getKeyspaceName() {
            return keyspaceName;
        }
    }

    @EnableCouchbaseRepositories
    @Configuration
    static class ConfigurationCouchbase extends AbstractCouchbaseConfiguration {

        @Value("${couchbase.cluster.bucket}")
        private String bucketName;

        @Value("${couchbase.cluster.password}")
        private String password;

        @Value("${couchbase.cluster.ip}")
        private String ip;

        @Override
        protected List<String> getBootstrapHosts() {
            return Collections.singletonList(this.ip);
        }

        @Override
        protected String getBucketName() {
            return this.bucketName;
        }

        @Override
        protected String getBucketPassword() {
            return this.password;
        }
    }
}
