# Distributed Forensic Digital Data Repository: Installation Guide

(c) 2018-2020 Marek Rychly (rychly@fit.vutbr.cz) and Martin Josefik (xjosef00@stud.fit.vutbr.cz)

## Requirements

*	installed [Docker](https://docs.docker.com/get-docker/)

## Infrastructure

Pull and build required Docker images and start the Docker containers infrastructure based on the images:

~~~
cd dist-forensic-digital-data-repo/docker/Environment
docker-compose pull
docker-compose build
docker-compose up
~~~

## Application

Build and install the application modules:

~~~sh
cd dist-forensic-digital-data-repo/app
for I in Communication Persistence DistributedRepository ProducerDemo; do
  cd ${I}
  ./install.sh || break
  cd -
done
~~~

Run the application the Distributed Repository:

~~~sh
cd dist-forensic-digital-data-repo/app/DistributedRepository
./run.sh
~~~

## Test

Run the Producer Demo to test the Distributed Repository:

~~~sh
cd dist-forensic-digital-data-repo/app/ProducerDemo
./run.sh
~~~

## Acknowledgements

*This work was supported by the Ministry of the Interior of the Czech Republic as a part of the project Integrated platform for analysis of digital data from security incidents VI20172020062.*
