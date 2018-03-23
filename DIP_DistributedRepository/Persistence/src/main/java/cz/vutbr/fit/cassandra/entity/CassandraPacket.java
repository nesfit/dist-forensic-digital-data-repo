package cz.vutbr.fit.cassandra.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.persistence.Entity;
import java.nio.ByteBuffer;
import java.util.UUID;

@Table("packet")
@Entity
public class CassandraPacket {

    @PrimaryKey
    @Id
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

        private CassandraPacket cassandraPacket;

        public Builder() {
            cassandraPacket = new CassandraPacket();
        }

        public Builder id(UUID id) {
            this.cassandraPacket.setId(id);
            return this;
        }

        public Builder packet(ByteBuffer packet) {
            this.cassandraPacket.setPacket(packet);
            return this;
        }

        public CassandraPacket build() {
            return this.cassandraPacket;
        }

    }

    @Override
    public String toString() {
        return "[id=" + id + ", cassandraPacket=" + packet.array() + "]";
    }

}
