#!/usr/bin/env bash

# Switch Java JDK
#export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
#eval "$(docker-machine env default)"

#SPRING_CONFIG_NAME=prod,local,config ./gradlew clean build
./gradlew clean build

DOCKER_IMAGE_NAME=quizzer
DOCKER_TAG=latest

# docker login -u loxal
docker build --tag=loxal/$DOCKER_IMAGE_NAME:$DOCKER_TAG .
docker push loxal/$DOCKER_IMAGE_NAME:$DOCKER_TAG
docker rm -f $DOCKER_IMAGE_NAME
docker run -d -p 82:8200 -e VAULT_TOKEN=$VAULT_TOKEN --name $DOCKER_IMAGE_NAME loxal/$DOCKER_IMAGE_NAME:$DOCKER_TAG

docker rmi $(docker images -f "dangling=true" -q) # cleanup, GC for dangling images
