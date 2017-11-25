#!/bin/sh

cd Environment/cassandra-official
./run.sh
cd ../..

cd Environment/kafka
./run.sh
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
