package cz.vutbr.fit.persistence.cassandra.repository;

import cz.vutbr.fit.persistence.cassandra.entity.CassandraPacket;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PacketRepository extends ReactiveCassandraRepository<CassandraPacket, UUID>, AsyncOperations {

}
