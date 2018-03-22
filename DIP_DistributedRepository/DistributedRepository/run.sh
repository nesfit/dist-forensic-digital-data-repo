#!/bin/sh

VM_IP=192.168.99.100
SPRING_DATA_CASSANDRA_CONTACT_POINTS=$VM_IP

HADOOP_IP=`docker inspect --format '{{ .NetworkSettings.IPAddress }}' hadoop`
HDFS_PORT=9000
SPRING_HADOOP_FS_URI=hdfs://$HADOOP_IP:$HDFS_PORT

KAFKA_PORT=9092
SPRING_KAFKA_BOOTSTRAP_SERVERS=$VM_IP:$KAFKA_PORT
SPRING_KAFKA_PRODUCER_BOOTSTRAP_SERVERS=$VM_IP:$KAFKA_PORT

SPRING_DATA_MONGODB_HOST=$VM_IP

docker run \
	-it \
	--rm \
	--name distributed-repository \
	-v "$PWD":/usr/src/app \
	-v "$HOME/.m2":/root/.m2 \
	-w "/usr/src/app/" \
	martinfit/maven:3.5.2-jdk-9-slim \
		java \
			-Dspring.data.cassandra.contact-points=$SPRING_DATA_CASSANDRA_CONTACT_POINTS \
			-Dspring.hadoop.fs-uri=$SPRING_HADOOP_FS_URI \
			-Dspring.kafka.bootstrap-servers=$SPRING_KAFKA_BOOTSTRAP_SERVERS \
			-Dspring.kafka.producer.bootstrap-servers=$SPRING_KAFKA_PRODUCER_BOOTSTRAP_SERVERS \
			-Dspring.data.mongodb.host=$SPRING_DATA_MONGODB_HOST \
			-cp distributed-repository-1.0-SNAPSHOT.jar \
			-jar target/distributed-repository-1.0.jar