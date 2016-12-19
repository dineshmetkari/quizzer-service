#!/usr/bin/env bash

# Switch Java JDK
#export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
#eval "$(docker-machine env default)"

SPRING_CONFIG_NAME=prod,local,config ./gradlew clean build

DOCKER_IMAGE_NAME=quizzer

# docker login -u loxal
docker build --tag=loxal/$DOCKER_IMAGE_NAME:v1 .
docker push loxal/$DOCKER_IMAGE_NAME:v1
docker rm -f $DOCKER_IMAGE_NAME
docker run -d -p 82:8200 --name $DOCKER_IMAGE_NAME loxal/$DOCKER_IMAGE_NAME:v1

docker rmi $(docker images -f "dangling=true" -q) # cleanup, GC for dangling images
