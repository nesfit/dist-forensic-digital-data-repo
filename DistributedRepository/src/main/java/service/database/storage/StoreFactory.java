package service.database.storage;

import communication.command.DataType;
import service.database.storage.store.CassandraStorage;
import service.database.storage.store.ISave;
import service.database.storage.store.IStorage;

public class StoreFactory<T extends ISave> implements AbstractFactory<T> {

    @Override
    public IStorage<T> getStorage(DataType dataType) {
        switch (dataType) {
            case PCAP:
                return new CassandraStorage<T>();
            default:
                throw new IllegalArgumentException("Unknown DataType");
        }
    }

}
