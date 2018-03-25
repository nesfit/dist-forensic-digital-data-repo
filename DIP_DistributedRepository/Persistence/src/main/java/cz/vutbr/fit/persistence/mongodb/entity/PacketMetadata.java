package cz.vutbr.fit.persistence.mongodb.entity;

import cz.vutbr.fit.persistence.DatabaseType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Document(collection = "packet_metadata")
public class PacketMetadata {

    @Id
    private String id;                  // ID in MongoDB
    private UUID refId;                 // ID in Cassandra
    private String uri;                 // ID or path to data in HDFS
    private DatabaseType databaseType;  // Cassandra, HDFS, etc

    private Instant timestamp;
    private int originalLength;

    private String ethernetTypeValue;   // HEX string, for IPv4: 0x0800, for IPv6: 0x86dd, etc
    private String ethernetTypeName;    // IPv4, IPv6, ARP, PPP, MPLS, etc
    private String srcLinkLayerAddress; // In format: xx:xx:xx:xx:xx:xx
    private String dstLinkLayerAddress; // In format: xx:xx:xx:xx:xx:xx

    private String ipProtocolValue;     // For ICMPv4: 4, for IGMP: 2, for TCP: 6, for UDP: 17, etc
    private String ipProtocolName;      // ICMPv4, IGMP, Stream, TCP, IGP, etc
    private String ipVersionValue;      // For IPv4: 4, for ST: 5, for IPv6: 6, etc
    private String ipVersionName;       // IPv4, ST, IPv6, etc
    private String srcIpAddress;        // e.g. IPv4 192.168.1.147, IPv6 fe80:0:0:0:40ab:f9d1:bd06:214c
    private String dstIpAddress;        // e.g. IPv4 192.168.1.147, IPv6 fe80:0:0:0:40ab:f9d1:bd06:214c

    private String srcPort;
    private String dstPort;

    // TODO: More fields

    public PacketMetadata() {
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public int getOriginalLength() {
        return originalLength;
    }

    public void setOriginalLength(int originalLength) {
        this.originalLength = originalLength;
    }

    public String getEthernetTypeValue() {
        return ethernetTypeValue;
    }

    public void setEthernetTypeValue(String ethernetTypeValue) {
        this.ethernetTypeValue = ethernetTypeValue;
    }

    public String getEthernetTypeName() {
        return ethernetTypeName;
    }

    public void setEthernetTypeName(String ethernetTypeName) {
        this.ethernetTypeName = ethernetTypeName;
    }

    public String getSrcLinkLayerAddress() {
        return srcLinkLayerAddress;
    }

    public void setSrcLinkLayerAddress(String srcLinkLayerAddress) {
        this.srcLinkLayerAddress = srcLinkLayerAddress;
    }

    public String getDstLinkLayerAddress() {
        return dstLinkLayerAddress;
    }

    public void setDstLinkLayerAddress(String dstLinkLayerAddress) {
        this.dstLinkLayerAddress = dstLinkLayerAddress;
    }

    public String getIpProtocolValue() {
        return ipProtocolValue;
    }

    public void setIpProtocolValue(String ipProtocolValue) {
        this.ipProtocolValue = ipProtocolValue;
    }

    public String getIpProtocolName() {
        return ipProtocolName;
    }

    public void setIpProtocolName(String ipProtocolName) {
        this.ipProtocolName = ipProtocolName;
    }

    public String getIpVersionValue() {
        return ipVersionValue;
    }

    public void setIpVersionValue(String ipVersionValue) {
        this.ipVersionValue = ipVersionValue;
    }

    public String getIpVersionName() {
        return ipVersionName;
    }

    public void setIpVersionName(String ipVersionName) {
        this.ipVersionName = ipVersionName;
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

    public String getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(String srcPort) {
        this.srcPort = srcPort;
    }

    public String getDstPort() {
        return dstPort;
    }

    public void setDstPort(String dstPort) {
        this.dstPort = dstPort;
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

        public Builder uri(String uri) {
            this.packetMetadata.setUri(uri);
            return this;
        }

        public Builder databaseType(DatabaseType databaseType) {
            this.packetMetadata.setDatabaseType(databaseType);
            return this;
        }

        public Builder timestamp(Instant timestamp) {
            this.packetMetadata.setTimestamp(timestamp);
            return this;
        }

        public Builder originalLength(int originalLength) {
            this.packetMetadata.setOriginalLength(originalLength);
            return this;
        }

        public Builder ethernetTypeValue(String ethernetTypeValue) {
            this.packetMetadata.setEthernetTypeValue(ethernetTypeValue);
            return this;
        }

        public Builder ethernetTypeName(String ethernetTypeName) {
            this.packetMetadata.setEthernetTypeName(ethernetTypeName);
            return this;
        }

        public Builder srcLinkLayerAddress(String srcLinkLayerAddress) {
            this.packetMetadata.setSrcLinkLayerAddress(srcLinkLayerAddress);
            return this;
        }

        public Builder dstLinkLayerAddress(String dstLinkLayerAddress) {
            this.packetMetadata.setDstLinkLayerAddress(dstLinkLayerAddress);
            return this;
        }

        public Builder ipProtocolValue(String ipProtocolValue) {
            this.packetMetadata.setIpProtocolValue(ipProtocolValue);
            return this;
        }

        public Builder ipProtocolName(String ipProtocolName) {
            this.packetMetadata.setIpProtocolName(ipProtocolName);
            return this;
        }

        public Builder ipVersionValue(String ipVersionValue) {
            this.packetMetadata.setIpVersionValue(ipVersionValue);
            return this;
        }

        public Builder ipVersionName(String ipVersionName) {
            this.packetMetadata.setIpVersionName(ipVersionName);
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

        public Builder srcPort(String srcPort) {
            this.packetMetadata.setSrcPort(srcPort);
            return this;
        }

        public Builder dstPort(String dstPort) {
            this.packetMetadata.setDstPort(dstPort);
            return this;
        }

        public PacketMetadata build() {
            return this.packetMetadata;
        }

    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
