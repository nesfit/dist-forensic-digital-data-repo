#!/bin/sh

HADOOP_IP=172.16.254.33
HDFS_PORT=9000
SPRING_HADOOP_FS_URI=hdfs://$HADOOP_IP:$HDFS_PORT

KAFKA_IP=172.16.254.34
KAFKA_PORT=9092
SPRING_KAFKA_BOOTSTRAP_SERVERS=$KAFKA_IP:$KAFKA_PORT
SPRING_KAFKA_PRODUCER_BOOTSTRAP_SERVERS=$KAFKA_IP:$KAFKA_PORT

DATA_DIR=$PWD/../../PCAP_Input

docker run \
	-it \
	--rm \
	--name producer-demo \
	--network environment_default \
	-v "$PWD":/usr/src/app \
	-v "$DATA_DIR":/usr/src/app/data \
	-v "$HOME/.m2":/root/.m2 \
	-w "/usr/src/app/" \
	maven:3.5.2-jdk-9-slim \
		java \
			-Dspring.hadoop.fs-uri=$SPRING_HADOOP_FS_URI \
			-Dspring.kafka.bootstrap-servers=$SPRING_KAFKA_BOOTSTRAP_SERVERS \
			-Dspring.kafka.producer.bootstrap-servers=$SPRING_KAFKA_PRODUCER_BOOTSTRAP_SERVERS \
			-cp producer-demo-1.0-SNAPSHOT.jar \
			-jar target/producer-demo-1.0.jar data