#!/bin/sh

cd Environment

	docker-compose pull mongodb hadoop kafka zookeeper

	cd cassandra-custom
	docker build -t martinfit/cassandra:latest .
	cd ..

	cd maven-jdk-9-slim-custom
	docker build -t martinfit/maven:3.5.2-jdk-9-slim .
	cd ..

cd ..
