package cz.vutbr.fit.mongodb.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "packet_metadata")
public class PacketMetadata {

    @Id
    private String id;

    private UUID refId;

    private String srcIpAddress;

    private String dstIpAddress;

    // TODO: More fields
    // TODO: Maybe create separated class for every packet type

    public PacketMetadata() {
    }

    @PersistenceConstructor
    public PacketMetadata(String id, UUID refId, String srcIpAddress, String dstIpAddress) {
        this.id = id;
        this.refId = refId;
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

    @Override
    public String toString() {
        return "[id=" + id + ", refId=" + refId + ", srcIpAddress=" + srcIpAddress + ", dstIpAddress=" + dstIpAddress + "]";
    }

}
