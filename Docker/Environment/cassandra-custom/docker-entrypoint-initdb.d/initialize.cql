
/* KeySpace for structured data, can contain more tables. */
CREATE KEYSPACE IF NOT EXISTS structured_data WITH replication = { 'class':'SimpleStrategy', 'replication_factor':1 };
USE structured_data;

/* Table for packets of PCAP files, column packet contains one packet from PCAP. */
CREATE TABLE IF NOT EXISTS packet (
	id timeuuid PRIMARY KEY,
	packet blob
);