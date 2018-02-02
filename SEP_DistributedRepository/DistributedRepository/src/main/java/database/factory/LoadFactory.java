package database.factory;

import communication.command.DataType;
import database.interfaces.ILoad;
import database.interfaces.IStore;

public class LoadFactory implements DatabaseAbstractFactory {

    @Override
    public IStore getStore(DataType dataType) {
        return null;
    }

    @Override
    public ILoad getLoad(DataType dataType) {
        return null;
    }

}
