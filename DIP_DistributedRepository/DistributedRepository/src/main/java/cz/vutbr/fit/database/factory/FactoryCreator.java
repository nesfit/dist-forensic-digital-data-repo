package cz.vutbr.fit.database.factory;

import communication.command.Operation;

public class FactoryCreator {

    public static DatabaseAbstractFactory getFactory(Operation operation) {

        switch (operation) {
            case STORE:
                return new StoreFactory();
            case LOAD:
                return new LoadFactory();
            default:
                throw new IllegalArgumentException("Unknown Operation");
        }

    }

}
