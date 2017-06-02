#!/usr/bin/env bash

execute() {
    SPRING_CONFIG_NAME=application,local ./gradlew clean test \
        --parallel \
        --no-scan \
#        --continuous \
#        --no-rebuild \
#        --build-cache \
#        -Ddebug \
#        --debug
}
execute
