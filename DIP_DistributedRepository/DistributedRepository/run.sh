#!/bin/sh

HADOOP_IP=`docker inspect --format '{{ .NetworkSettings.IPAddress }}' hadoop`
HDFS_PORT=9000
SPRING_HADOOP_FS_URI=hdfs://$HADOOP_IP:$HDFS_PORT

docker run \
	-it \
	--rm \
	--name distributed-repository \
	-v "$PWD":/usr/src/app \
	-v "$HOME/.m2":/root/.m2 \
	-w "/usr/src/app/" \
	martinfit/maven:3.5.2-jdk-9-slim \
		java \
			-Dspring.hadoop.fs-uri=$SPRING_HADOOP_FS_URI \
			-cp distributed-repository-1.0-SNAPSHOT.jar \
			-jar target/distributed-repository-1.0.jar