/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.dto;

public class Uptime {
    private String endpoint;
    private Integer intervalInSeconds;

    public Uptime(String endpoint, Integer intervalInSeconds) {
        this.endpoint = endpoint;
        this.intervalInSeconds = intervalInSeconds;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Integer getIntervalInSeconds() {
        return intervalInSeconds;
    }

    public void setIntervalInSeconds(Integer intervalInSeconds) {
        this.intervalInSeconds = intervalInSeconds;
    }
}
