package cz.vutbr.fit.mongodb.entity;

import cz.vutbr.fit.DatabaseType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "packet_metadata")
public class PacketMetadata {

    @Id
    private String id;
    private UUID refId;
    private DatabaseType databaseType;

    private short ethernetTypeValue;    // EtherType (value, name)
    private String ethernetTypeName;
    private String srcLinkLayerAddress;
    private String dstLinkLayerAddress;

    private byte ipProtocolValue;       // IpNumber (value, name)
    private String ipProtocolName;
    private byte ipVersionValue;        // IpVersion (value, name)
    private String ipVersionName;
    private String srcIpAddress;
    private String dstIpAddress;

    private short srcPortNum;
    private short dstPortNum;
    private String srcPortStr;
    private String dstPortStr;

    // TODO: More fields

    public PacketMetadata() {
    }

    /*@PersistenceConstructor
    public PacketMetadata(UUID refId, DatabaseType databaseType, String srcIpAddress, String dstIpAddress) {
        this.refId = refId;
        this.databaseType = databaseType;
        this.srcIpAddress = srcIpAddress;
        this.dstIpAddress = dstIpAddress;
    }*/

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

    public short getEthernetTypeValue() {
        return ethernetTypeValue;
    }

    public void setEthernetTypeValue(short ethernetTypeValue) {
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

    public byte getIpProtocolValue() {
        return ipProtocolValue;
    }

    public void setIpProtocolValue(byte ipProtocolValue) {
        this.ipProtocolValue = ipProtocolValue;
    }

    public String getIpProtocolName() {
        return ipProtocolName;
    }

    public void setIpProtocolName(String ipProtocolName) {
        this.ipProtocolName = ipProtocolName;
    }

    public byte getIpVersionValue() {
        return ipVersionValue;
    }

    public void setIpVersionValue(byte ipVersionValue) {
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

    public short getSrcPortNum() {
        return srcPortNum;
    }

    public void setSrcPortNum(short srcPortNum) {
        this.srcPortNum = srcPortNum;
    }

    public short getDstPortNum() {
        return dstPortNum;
    }

    public void setDstPortNum(short dstPortNum) {
        this.dstPortNum = dstPortNum;
    }

    public String getSrcPortStr() {
        return srcPortStr;
    }

    public void setSrcPortStr(String srcPortStr) {
        this.srcPortStr = srcPortStr;
    }

    public String getDstPortStr() {
        return dstPortStr;
    }

    public void setDstPortStr(String dstPortStr) {
        this.dstPortStr = dstPortStr;
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

        public Builder ethernetTypeValue(short ethernetTypeValue) {
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

        public Builder ipProtocolValue(byte ipProtocolValue) {
            this.packetMetadata.setIpProtocolValue(ipProtocolValue);
            return this;
        }

        public Builder ipProtocolName(String ipProtocolName) {
            this.packetMetadata.setIpProtocolName(ipProtocolName);
            return this;
        }

        public Builder ipVersionValue(byte ipVersionValue) {
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

        public Builder srcPortNum(short srcPortNum) {
            this.packetMetadata.setSrcPortNum(srcPortNum);
            return this;
        }

        public Builder srcPortStr(String srcPortStr) {
            this.packetMetadata.setSrcPortStr(srcPortStr);
            return this;
        }

        public Builder dstPortNum(short dstPortNum) {
            this.packetMetadata.setDstPortNum(dstPortNum);
            return this;
        }

        public Builder dstPortStr(String dstPortStr) {
            this.packetMetadata.setDstPortStr(dstPortStr);
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
