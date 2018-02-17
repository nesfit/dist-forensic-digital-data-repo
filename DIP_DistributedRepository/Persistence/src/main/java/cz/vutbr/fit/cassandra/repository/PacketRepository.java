package cz.vutbr.fit.cassandra.repository;

import cz.vutbr.fit.cassandra.entity.Packet;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PacketRepository extends CrudRepository<Packet, UUID>, InsertAsync {

    @Query("Select * from pcap where id=?0")
    public Packet findByPacketId(UUID id);

}
