#!/bin/sh

VM_IP=192.168.99.100

HADOOP_IP=`docker inspect --format '{{ .NetworkSettings.IPAddress }}' hadoop`
HDFS_PORT=9000
SPRING_HADOOP_FS_URI=hdfs://$HADOOP_IP:$HDFS_PORT

KAFKA_PORT=9092
SPRING_KAFKA_BOOTSTRAP_SERVERS=$VM_IP:$KAFKA_PORT
SPRING_KAFKA_PRODUCER_BOOTSTRAP_SERVERS=$VM_IP:$KAFKA_PORT

docker run \
	-it \
	--rm \
	--name producer-demo \
	-v "$PWD":/usr/src/app \
	-v "$HOME/.m2":/root/.m2 \
	-w "/usr/src/app/" \
	martinfit/maven:3.5.2-jdk-9-slim \
		java \
			-Dspring.hadoop.fs-uri=$SPRING_HADOOP_FS_URI \
			-Dspring.kafka.bootstrap-servers=$SPRING_KAFKA_BOOTSTRAP_SERVERS \
			-Dspring.kafka.producer.bootstrap-servers=$SPRING_KAFKA_PRODUCER_BOOTSTRAP_SERVERS \
			-cp producer-demo-1.0-SNAPSHOT.jar \
			-jar target/producer-demo-1.0.jar ../../PCAP_Input