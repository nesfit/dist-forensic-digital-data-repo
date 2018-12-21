package cz.vutbr.fit.persistence.cassandra.repository;

import cz.vutbr.fit.persistence.cassandra.entity.CassandraPacket;

@FunctionalInterface
public interface OnSuccessCallback {

    public void onSuccess(CassandraPacket cassandraPacket);

}
