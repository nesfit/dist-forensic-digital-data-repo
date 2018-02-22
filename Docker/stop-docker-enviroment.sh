#!/bin/sh

cd Environment/cassandra-custom
docker-compose down
cd ../..

cd Environment/mongodb
docker-compose down
cd ../..

cd Environment/kafka
docker-compose down
cd ../..
