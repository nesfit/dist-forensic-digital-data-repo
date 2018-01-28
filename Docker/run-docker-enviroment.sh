#!/bin/sh

cd Environment/cassandra-custom
docker-compose up -d --build
cd ../..

cd Environment/kafka
docker-compose up -d
cd ../..

#cd Environment

#for i in ./* ; do
#  if [ -d "$i" ]; then
#	cd $i
#	./run.sh
#	cd ..
#  fi
#done

#cd ..
