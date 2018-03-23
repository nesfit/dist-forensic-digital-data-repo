package cz.vutbr.fit.cassandra.repository;

import com.datastax.driver.core.ResultSetFuture;
import cz.vutbr.fit.cassandra.entity.CassandraPacket;

import java.util.UUID;

public interface AsyncOperations {

    public ResultSetFuture insertAsync(CassandraPacket cassandraPacket);

    public ResultSetFuture selectAsync(UUID id);

}
