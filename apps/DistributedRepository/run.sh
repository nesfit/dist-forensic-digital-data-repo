#!/bin/sh

SPRING_DATA_CASSANDRA_CONTACT_POINTS=172.16.254.31

HADOOP_IP=172.16.254.33
HDFS_PORT=9000
SPRING_HADOOP_FS_URI=hdfs://$HADOOP_IP:$HDFS_PORT

KAFKA_IP=172.16.254.34
KAFKA_PORT=9092
SPRING_KAFKA_BOOTSTRAP_SERVERS=$KAFKA_IP:$KAFKA_PORT
SPRING_KAFKA_PRODUCER_BOOTSTRAP_SERVERS=$KAFKA_IP:$KAFKA_PORT

SPRING_DATA_MONGODB_HOST=172.16.254.32

exec docker run \
	-it \
	--rm \
	--name distributed-repository \
	--network environment_default \
	-v "$PWD":/usr/src/app \
	-v "$HOME/.m2":/root/.m2 \
	-w "/usr/src/app/" \
	maven:3.5.2-jdk-9-slim \
		java \
			-Dspring.data.cassandra.contact-points=$SPRING_DATA_CASSANDRA_CONTACT_POINTS \
			-Dspring.hadoop.fs-uri=$SPRING_HADOOP_FS_URI \
			-Dspring.kafka.bootstrap-servers=$SPRING_KAFKA_BOOTSTRAP_SERVERS \
			-Dspring.kafka.producer.bootstrap-servers=$SPRING_KAFKA_PRODUCER_BOOTSTRAP_SERVERS \
			-Dspring.data.mongodb.host=$SPRING_DATA_MONGODB_HOST \
			-cp distributed-repository-1.0-SNAPSHOT.jar \
			-jar target/distributed-repository-1.0.jar
