package database.factory;

import communication.command.DataType;
import database.impl.PacketStore;
import database.interfaces.ILoad;
import database.interfaces.IStore;

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
