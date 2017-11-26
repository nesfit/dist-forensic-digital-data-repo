package service.database.storage;

import communication.command.DataType;
import service.database.storage.store.ISave;
import service.database.storage.store.IStorage;

public interface AbstractFactory<T extends ISave> {

    public IStorage<T> getStorage(DataType dataType);

}
