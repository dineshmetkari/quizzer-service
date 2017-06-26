#!/usr/bin/env bash

standalone_start() {
    SPRING_CONFIG_NAME=local,application ./gradlew clean bootRun \
        --no-rebuild \
        --build-cache \
        --continuous \
        --parallel \
        --no-scan \
#        -Ddebug \
#        --debug
}
standalone_start
