#!/bin/sh

echo "Starting Cassandra..."
docker-compose up -d
#docker run \
#	-it -d \
#	--rm \
#	-p 7199:7199 -p 9042:9042 -p 9160:9160 \
#	--name cassandra \
#	-v "$PWD/database-data":/var/lib/cassandra \
#	cassandra
