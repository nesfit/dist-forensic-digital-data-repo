package cz.vutbr.fit.mongodb.repository;

import cz.vutbr.fit.mongodb.entity.PacketMetadata;

public interface InsertAsync {

    public void insertAsync(PacketMetadata packetMetadata);

}
