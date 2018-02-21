package cz.vutbr.fit.cassandra.entity;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.persistence.Entity;
import java.nio.ByteBuffer;
import java.util.UUID;

@Table("packet")
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

    public static class Builder {

        private Packet packet;

        public Builder() {
            packet = new Packet();
        }

        public Builder id(UUID id) {
            this.packet.setId(id);
            return this;
        }

        public Builder packet(ByteBuffer packet) {
            this.packet.setPacket(packet);
            return this;
        }

        public Packet build() {
            return this.packet;
        }

    }

    @Override
    public String toString() {
        return "[id=" + id + ", packet=" + new String(packet.array()) + "]";
    }

}
