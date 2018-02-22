package cz.vutbr.fit.mongodb.entity;

import cz.vutbr.fit.DatabaseType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "packet_metadata")
public class PacketMetadata {

    @Id
    private String id;

    private UUID refId;

    private DatabaseType databaseType;

    private String srcIpAddress;

    private String dstIpAddress;

    // TODO: More fields
    // TODO: Maybe create separated class for every packet type

    public PacketMetadata() {
    }

    @PersistenceConstructor
    public PacketMetadata(UUID refId, DatabaseType databaseType, String srcIpAddress, String dstIpAddress) {
        this.refId = refId;
        this.databaseType = databaseType;
        this.srcIpAddress = srcIpAddress;
        this.dstIpAddress = dstIpAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UUID getRefId() {
        return refId;
    }

    public void setRefId(UUID refId) {
        this.refId = refId;
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public String getSrcIpAddress() {
        return srcIpAddress;
    }

    public void setSrcIpAddress(String srcIpAddress) {
        this.srcIpAddress = srcIpAddress;
    }

    public String getDstIpAddress() {
        return dstIpAddress;
    }

    public void setDstIpAddress(String dstIpAddress) {
        this.dstIpAddress = dstIpAddress;
    }

    public static class Builder {

        private PacketMetadata packetMetadata;

        public Builder() {
            packetMetadata = new PacketMetadata();
        }

        public Builder refId(UUID refId) {
            this.packetMetadata.setRefId(refId);
            return this;
        }

        public Builder databaseType(DatabaseType databaseType) {
            this.packetMetadata.setDatabaseType(databaseType);
            return this;
        }

        public Builder srcIpAddress(String srcIpAddress) {
            this.packetMetadata.setSrcIpAddress(srcIpAddress);
            return this;
        }

        public Builder dstIpAddress(String dstIpAddress) {
            this.packetMetadata.setDstIpAddress(dstIpAddress);
            return this;
        }

        public PacketMetadata build() {
            return this.packetMetadata;
        }

    }

    @Override
    public String toString() {
        return "["
                + "id=" + id + ", refId=" + refId + ", databaseType=" + databaseType
                + ", srcIpAddress=" + srcIpAddress + ", dstIpAddress=" + dstIpAddress
                + "]";
    }

}
