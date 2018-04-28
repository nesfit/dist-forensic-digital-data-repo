# Master-Thesis

Docker prostredi nachazejici se v adresari `Docker`
--------

* Pozadavky
    * Mit nainstalovany Docker (Docker Toolbox a Oracle VM VirtualBox pod Windows),
    * Aktualni konfigurace v `Environment/docker-compose.yml` je pro prostredi
	  Docker pod Windows, pod Linuxem bude potreba mirne upravit konfiguraci
	  (IP adresu virtualniho stroje zamenit za localhost apod.)
    * Pod Windows: spustit Docker daemona napr. pomoci Docker Quickstart Terminal

* Vytvoreni virtualniho stroje
    * Virtualni stroj by mel byt vytvoren pri instalaci Docker Toolbox, ale
	  lze jej i znovu vytvorit spustenim skriptu `docker-machine-recreate.sh`
	  (pozor na parametry stroje - lze nastavit velikost RAM, pocet CPU jader, velikost disku)

* Stazeni technologii v prostredi Docker
    * Spusteni skriptu `install-docker-enviroment.sh`, ktery provede stazeni obrazu
	  technologii z DockerHub: Cassandra, Kafka, ZooKeeper, MongoDB, Hadoop

* Spusteni technologii v prostredi Docker
    * Spusteni skriptu `run-docker-enviroment.sh`, spusti vsechny vyse zminene technologie

* Ukonceni behu technologii v prostredi Docker
    * Spusteni skriptu `stop-docker-enviroment.sh`
	
* Statistiky bezicich kontejneru (vyuziti RAM, CPU, ...) lze sledovat v prehledne tabulce pomoci prikazu
    * `$ docker stats --format "table {{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.NetIO}}\t{{.BlockIO}}"`

* V zavislosti na pouziti Docker nebo Docker Toolbox se pouzivaji ruzne IP adresy:
    * Docker: localhost
    * Docker Toolbox: IP virtualniho stroje (192.168.99.100)
	
* Pristup do beziciho kontejneru
    * Cassandra
      * Otevreni CSLSH
         * `$ docker run -it --link cassandra:cassandra --rm martinfit/cassandra:3 cqlsh cassandra`
	
    * MongoDB
      * Otevreni Mongo shell
         * `$ docker run -it --link mongodb:mongo --rm mongo:3.4 mongo 192.168.99.100:27017`
	
* Kafka
    * Kafka fronty jsou vytvoreny automaticky, pripadne je lze vytvorit nasledujicim prikazem:
		`$ docker exec kafka kafka-topics.sh --create --zookeeper 192.168.99.100:2181 --replication-factor 1 --partitions 1 --topic input_topic`
    * Vypsani topicu
		`$ docker exec kafka opt/kafka/bin/kafka-topics.sh --describe --zookeeper 192.168.99.100:2181`
	
* Hadoop/HDFS
    * Aplikace potrebuji pristoupit primo do kontejneru dle adresy kontejneru
	  (typicky adresa beziciho kontejneru vypada nasledovne: `172.17.0.4`),
	  pod Windows je potreba pridat zaznam do smerovaci tabulky pres prikazovou radku jako admin:<br>
		`route add 172.17.0.0/16 192.168.99.100`<br>
		`route add 172.18.0.0/16 192.168.99.100`<br>
	  atd., z duvodu pritomnosti virtualniho stroje, pod Linuxem neni potreba

System distribuovaneho uloziste
--------

* Implementace se sklada ze ctyr Maven modulu:
    * Communication: rozhrani pro komunikaci
    * Persistence: obsluha Cassandry a MongoDB
    * DistributedRepository: system distribuovaneho repositare, po spusteni bezi nepretrzite
    * ProducerDemo: klientska aplikace
	
* Prvni je nutne nainstalovat moduly Communication a Persistence, ostatni 2 aplikace jsou na nich zavisle.
    * Instalace probiha v tomto poradi:
		`cd Communication`<br>
			`./install.sh`<br>
		`cd Persistence`<br>
			`./install.sh`<br>
		`cd DistributedRepository`<br>
			`./install.sh`<br>
		`cd ProducerDemo`<br>
			`./install.sh`<br>
	
* Jedna se o projekty vytvorene ve vyvojovem prostredi IntelliJ IDEA.
Pro IDEA existuje plugin Docker, kde lze videt po pripojeni obrazy
a bezici kontejnery, zaroven lze sledovat jejich nastaveni a logy.
