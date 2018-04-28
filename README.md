# Master-Thesis

Docker prostredi
--------

Nachazi se v adresari `Docker`.

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
    * Docker Toolbox: IP virtualniho stroje (`192.168.99.100`)
	
* Pristup do beziciho kontejneru
    * Cassandra<br>
		Otevreni CSLSH<br>
		`$ docker run -it --link cassandra:cassandra --rm martinfit/cassandra:3 cqlsh cassandra`
	
    * MongoDB<br>
		Otevreni Mongo shell<br>
		`$ docker run -it --link mongodb:mongo --rm mongo:3.4 mongo 192.168.99.100:27017`
	
* Kafka
    * Kafka fronty jsou vytvoreny automaticky, pripadne je lze vytvorit nasledujicim prikazem:<br>
		`$ docker exec kafka kafka-topics.sh --create --zookeeper 192.168.99.100:2181 --replication-factor 1 --partitions 1 --topic input_topic`
    * Vypsani topicu<br>
		`$ docker exec kafka opt/kafka/bin/kafka-topics.sh --describe --zookeeper 192.168.99.100:2181`
	
* Hadoop/HDFS
    * Aplikace potrebuji pristoupit primo do kontejneru dle adresy kontejneru
	  (typicky adresa beziciho kontejneru vypada nasledovne: `172.17.0.4`),
	  pod Windows je potreba pridat zaznam do smerovaci tabulky pres prikazovou radku jako admin:<br>
		`route add 172.17.0.0/16 192.168.99.100`<br>
		`route add 172.18.0.0/16 192.168.99.100`<br>
	  atd., z duvodu pritomnosti virtualniho stroje, pod Linuxem neni potreba

Instalace systemu distribuovaneho uloziste
--------

System se nachazi se v adresari `DIP_DistributedRepository`.

* Implementace se sklada ze ctyr Maven modulu:
    * Communication: rozhrani pro komunikaci
    * Persistence: obsluha Cassandry a MongoDB
    * DistributedRepository: system distribuovaneho repositare, po spusteni bezi nepretrzite
    * ProducerDemo: klientska aplikace
	
* Prvni je nutne nainstalovat moduly Communication a Persistence, ostatni 2 aplikace jsou na nich zavisle.
    * Instalace probiha v tomto poradi:<br>
		`cd Communication`<br>
			`./install.sh`<br>
		`cd Persistence`<br>
			`./install.sh`<br>
		`cd DistributedRepository`<br>
			`./install.sh`<br>
		`cd ProducerDemo`<br>
			`./install.sh`<br>
	
* Jedna se o projekty vytvorene ve vyvojovem prostredi IntelliJ IDEA. Pro IDEA existuje plugin Docker,
kde lze videt po pripojeni obrazy a bezici kontejnery, zaroven lze sledovat jejich nastaveni a logy.

Spusteni systemu distribuovaneho uloziste
--------

Oba dva moduly `DistributedRepository` a `ProducerDemo` maji ve svych adresarich skript `run.sh`.
Skript vzdy zjisti adresu kontejneru, ve kterem bezi HDFS, a preda tuto adresu jako aplikacni promennou.
Jsou take nastaveny aplikacni promenne IP adres pro ostatni technologie (jako IP adresa virtualniho stroje `192.168.99.100`).

* Spusteni<br>
	`cd DistributedRepository`<br>
		`./run.sh`<br>
	`cd ProducerDemo`<br>
		`./run.sh`<br>

Vstupni data - PCAP soubory
--------

Vstupni data se nachazi v adresari `PCAP_Input`.

Technicka zprava diplomove prace
--------

Nachazi se v adresari `Text` spolecne s latexovymi zdrojovymi kody, a pouzitymi obrazky a schematy. Technicka zprava je PDF dokument `projekt.pdf`.

Poznamka
--------
System byl vyvijen v prostredi Docker pod Windows, a tak vsechny skripty pro Docker obrazy a kontejnery,
a take spousteci skripty jsou nastaveny tak, aby slo vysledny system spustit rovnez v prostredi Docker pod Windows.
Pro spusteni pod Linuxem je potreba skripty upravit, prepsat IP adresy z `192.168.99.100` na `localhost` atd.

