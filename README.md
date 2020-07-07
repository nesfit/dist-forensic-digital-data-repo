# Distributed Forensic Digital Data Repository

(c) 2018-2020 Marek Rychly (rychly@fit.vutbr.cz) and Martin Josefik (xjosef00@stud.fit.vutbr.cz)

Distributed storage for digital forensic data with data/metadata repository, API for queries and incoming/outgoing data, indexing, plug-in system for yet unsupported data-types, etc.

## Applications

Building by [Apache Maven](https://maven.apache.org/) in subdirectories (Maven projects) in [apps directory](./apps).
The components should be built in the following order:

1.	[Communication](./apps/Communication): a communication bus based on [Apache Kafka](https://kafka.apache.org/)
2.	[Persistence](./apps/Persistence): a persistent storage based on [Apache Cassandra](https://cassandra.apache.org/) for data and [MongoDB](https://www.mongodb.com/) for metadata
3.	[DistributedRepository](./apps/DistributedRepository): the repository server
4.	[ProducerDemo](./apps/ProducerDemo): a demo of the repository client (a producer of PCAP data)

## Docker

The repository system and its components can run in [Docker](https://www.docker.com/) as described in [docker directory](./docker).

The individual components can run in the Docker by `run.sh` script in their root (project) directories.

## Documentation and Experiments

The documentation can be found in [docs directory](./docs).

There is also a set of experiments to measure the performance on PCAP storing/retrieving to/from the repository, see [experiments directory](./experiments).

## Acknowledgements

*This work was supported by the Ministry of the Interior of the Czech Republic as a part of the project Integrated platform for analysis of digital data from security incidents VI20172020062.*
