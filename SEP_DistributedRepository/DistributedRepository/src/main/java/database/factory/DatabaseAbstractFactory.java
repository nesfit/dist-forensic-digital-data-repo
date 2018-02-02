package database.factory;

import communication.command.DataType;
import database.interfaces.ILoad;
import database.interfaces.IStore;

public interface DatabaseAbstractFactory {

    public IStore getStore(DataType dataType);

    public ILoad getLoad(DataType dataType);

}
