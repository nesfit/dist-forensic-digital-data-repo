package cz.vutbr.fit.cassandra.repository;

import cz.vutbr.fit.cassandra.entity.CassandraPacket;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import java.util.UUID;

public interface PacketRepository extends ReactiveCassandraRepository<CassandraPacket, UUID>, AsyncOperations {

}
