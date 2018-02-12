#!/bin/sh

cd Environment

docker-compose pull

cd maven-jdk-9-slim-custom
docker build -t martinfit/maven:3.5.2-jdk-9-slim .
cd ..

cd ..