package cz.vutbr.fit.communication.command;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class DataSource {

    private DataSourceStorage dataSourceStorage;
    private String uri;
    private boolean removeAfterUse;

    public DataSource() {

    }

    public DataSource(DataSourceStorage dataSourceStorage, String uri) {
        this(dataSourceStorage, uri, false);
    }

    public DataSource(DataSourceStorage dataSourceStorage, String uri, boolean removeAfterUse) {
        this.dataSourceStorage = dataSourceStorage;
        this.uri = uri;
        this.removeAfterUse = removeAfterUse;
    }

    public DataSourceStorage getDataSourceStorage() {
        return dataSourceStorage;
    }

    public void setDataSourceStorage(DataSourceStorage dataSourceStorage) {
        this.dataSourceStorage = dataSourceStorage;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean getRemoveAfterUse() {
        return removeAfterUse;
    }

    public void setRemoveAfterUse(boolean removeAfterUse) {
        this.removeAfterUse = removeAfterUse;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
