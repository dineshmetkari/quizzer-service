#!/usr/bin/env bash

execute() {
    SPRING_CONFIG_NAME=application,local ./gradlew clean test \
        --no-rebuild \
        --build-cache \
        --continuous \
        --parallel \
        --no-scan \
#        -Ddebug \
#        --debug
}
execute
