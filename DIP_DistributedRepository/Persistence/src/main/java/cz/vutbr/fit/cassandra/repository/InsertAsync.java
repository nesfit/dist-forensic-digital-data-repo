package cz.vutbr.fit.cassandra.repository;

import com.datastax.driver.core.ResultSetFuture;
import cz.vutbr.fit.cassandra.entity.CassandraPacket;

public interface InsertAsync {

    public ResultSetFuture insertAsync(CassandraPacket cassandraPacket);

}
