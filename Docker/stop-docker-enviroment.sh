#!/bin/sh

cd Environment/cassandra-custom
docker-compose down
cd ../..

cd Environment/kafka
docker-compose down
cd ../..

#docker container stop cassandra
#docker container stop kafka
#docker container stop zookeeper
#docker container stop hadoop

#cd Environment
#docker-compose down
#cd ..
