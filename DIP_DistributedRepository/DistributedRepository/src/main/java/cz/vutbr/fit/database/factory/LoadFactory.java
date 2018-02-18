package cz.vutbr.fit.database.factory;

import communication.command.DataType;
import cz.vutbr.fit.database.interfaces.ILoad;
import cz.vutbr.fit.database.interfaces.IStore;

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
