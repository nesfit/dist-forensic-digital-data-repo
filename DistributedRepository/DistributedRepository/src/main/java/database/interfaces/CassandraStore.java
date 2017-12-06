package database.interfaces;

import common.properties.Properties;
import common.properties.PropertyConstants;

public abstract class CassandraStore {

    protected String address = Properties.getInstance().loadProperty(PropertyConstants.IP);
    ;
    protected String keyspace = Properties.getInstance().loadProperty(PropertyConstants.KEYSPACE);

}
