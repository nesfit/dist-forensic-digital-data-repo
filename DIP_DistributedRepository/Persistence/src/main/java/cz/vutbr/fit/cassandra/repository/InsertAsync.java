package cz.vutbr.fit.cassandra.repository;

import cz.vutbr.fit.cassandra.entity.Packet;

public interface InsertAsync {

    public void insertAsync(Packet packet);

}
