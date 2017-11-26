package service.database.storage;

import communication.command.Operation;
import service.database.storage.store.ISave;

public class FactorySelect<T extends ISave> {

    public AbstractFactory<T> getFactory(Operation operation) {
        switch (operation) {
            case STORE:
                return new StoreFactory<T>();
            default:
                throw new IllegalArgumentException("Unknown Operation");
        }
    }

}
