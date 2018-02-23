package cz.vutbr.fit.cassandra.repository;

import cz.vutbr.fit.cassandra.entity.CassandraPacket;

public interface InsertAsync {

    public void insertAsync(CassandraPacket cassandraPacket);

}
