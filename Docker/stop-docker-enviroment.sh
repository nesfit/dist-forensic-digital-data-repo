#!/bin/sh

docker container stop cassandra
docker container stop kafka
docker container stop zookeeper
#docker container stop hadoop

#cd Environment
#docker-compose down
#cd ..
