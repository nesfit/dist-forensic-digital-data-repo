#!/bin/sh

docker run -it --rm --name communication -v "$HOME/.m2":/root/.m2 -v "$PWD":/usr/src/app -w "/usr/src/app/" martinfit/maven:3.5.2-jdk-9-slim mvn clean install