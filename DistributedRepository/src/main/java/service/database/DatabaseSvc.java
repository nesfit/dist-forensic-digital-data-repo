package service.database;

import communication.command.DataType;
import communication.command.Operation;
import service.database.storage.AbstractFactory;
import service.database.storage.FactorySelect;
import service.database.storage.store.ISave;
import service.database.storage.store.IStorage;

public class DatabaseSvc<T extends ISave> {

    private FactorySelect<T> factorySelect = new FactorySelect<>();

    public void store(Operation operation, DataType dataType, T value) {
        AbstractFactory<T> abstractFactory = factorySelect.getFactory(operation);
        IStorage<T> storage = abstractFactory.getStorage(dataType);
        storage.store(value);
    }

    public T load() {
        return null;
    }

}
