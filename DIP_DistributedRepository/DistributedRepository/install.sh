#!/bin/sh

docker run -it --rm --name repository -v "$PWD":/usr/src/app -v "$HOME/.m2":/root/.m2 -w "/usr/src/app/" martinfit/maven:3.5.2-jdk-9-slim mvn clean install