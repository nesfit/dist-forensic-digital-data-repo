#!/bin/sh

cd Environment/cassandra-custom
docker-compose up -d
cd ../..

cd Environment/hadoop
docker-compose up -d
cd ../..

cd Environment/kafka
docker-compose up -d
cd ../..

cd Environment/mongodb
docker-compose up -d
cd ../..

#cd Environment

#for i in ./* ; do
#  if [ -d "$i" ]; then
#	cd $i
#	docker-compose up -d
#	cd ..
#  fi
#done

#cd ..
