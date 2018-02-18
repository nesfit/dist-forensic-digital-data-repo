package cz.vutbr.fit.database.factory;

import communication.command.DataType;
import cz.vutbr.fit.database.interfaces.ILoad;
import cz.vutbr.fit.database.interfaces.IStore;

public interface DatabaseAbstractFactory {

    public IStore getStore(DataType dataType);

    public ILoad getLoad(DataType dataType);

}
