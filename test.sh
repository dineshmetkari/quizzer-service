#!/usr/bin/env bash

execute() {
    SPRING_CONFIG_NAME=application,local ./gradlew clean test \
        --parallel \
        --no-scan \
        --no-rebuild \
        --info \
#        --continuous \
#        --build-cache \
#        -Ddebug \
#        --debug
}
execute
