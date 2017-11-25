#!/bin/sh

echo "Starting Kafka..."
docker-compose up -d
#docker run \
#	-it -d \
#	--rm \
#	-p 2181:2181 -p 9092:9092 \
#	--env ADVERTISED_HOST=`docker-machine ip \`docker-machine active\`` \
#	--env ADVERTISED_PORT=9092 \
#	--name kafka \
#	harisekhon/kafka:latest
