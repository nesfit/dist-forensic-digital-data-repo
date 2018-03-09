package cz.vutbr.fit.cassandra.repository;

import cz.vutbr.fit.cassandra.entity.CassandraPacket;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import java.util.UUID;

public interface PacketRepository extends ReactiveCassandraRepository<CassandraPacket, UUID>, InsertAsync {

    @Query("Select * from packet where id=?0")
    public CassandraPacket findByPacketId(UUID id);

    // @Modifying
    // @Query(nativeQuery = true, value = "INSERT INTO packet (id, packet) VALUES(:id, :packet)")
    // @Deprecated
    // public void insertAsync(@Param("id") UUID id, @Param("packet") ByteBuffer packet);

}
