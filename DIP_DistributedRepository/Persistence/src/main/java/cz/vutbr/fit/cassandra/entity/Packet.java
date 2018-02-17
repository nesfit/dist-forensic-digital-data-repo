package cz.vutbr.fit.cassandra.entity;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.persistence.Entity;
import java.nio.ByteBuffer;
import java.util.UUID;

// TODO: Change table name to packet
@Table("pcap")
@Entity
public class Packet {

    @PrimaryKey
    private UUID id;

    private ByteBuffer packet;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ByteBuffer getPacket() {
        return packet;
    }

    public void setPacket(ByteBuffer packet) {
        this.packet = packet;
    }

    @Override
    public String toString() {
        return "[id=" + id + ", packet=" + new String(packet.array()) + "]";
    }

}
