#!/bin/sh

docker run -it --rm --name producer-app -v "$PWD":/usr/src/app -v "$HOME/.m2":/root/.m2 -w "/usr/src/app/" martinfit/maven:3.5.2-jdk-9-slim java -cp producer-app-1.0-SNAPSHOT.jar -jar target/producer-app-1.0.jar target/classes/PCAP