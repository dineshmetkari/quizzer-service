/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.dto;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import java.util.Objects;

@Table("uptime")
public class Uptime {
    @PrimaryKeyColumn(name = "endpoint", forceQuote = true, ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String endpoint;

    private Integer intervalInSeconds;

    public Uptime(String endpoint, Integer intervalInSeconds) {
        this.endpoint = endpoint;
        this.intervalInSeconds = intervalInSeconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Uptime uptime = (Uptime) o;
        return Objects.equals(endpoint, uptime.endpoint) &&
                Objects.equals(intervalInSeconds, uptime.intervalInSeconds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endpoint, intervalInSeconds);
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
