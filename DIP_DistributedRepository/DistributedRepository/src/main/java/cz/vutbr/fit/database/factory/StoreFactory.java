package cz.vutbr.fit.database.factory;

import communication.command.DataType;
import cz.vutbr.fit.database.impl.PacketStore;
import cz.vutbr.fit.database.interfaces.ILoad;
import cz.vutbr.fit.database.interfaces.IStore;

public class StoreFactory implements DatabaseAbstractFactory {

    @Override
    public IStore getStore(DataType dataType) {
        switch (dataType) {
            case PCAP:
                return new PacketStore();
            default:
                throw new IllegalArgumentException("Unknown DataType");
        }
    }

    @Override
    public ILoad getLoad(DataType dataType) {
        return null;
    }

}
