package cz.vutbr.fit.communication;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public enum MetadataOperation {

    //  Comparison Query Operators
    //  See https://docs.mongodb.com/manual/reference/operator/query-comparison/
    // There is not any eq method inside org.springframework.data.mongodb.core.query.Criteria.Criteria !
    EQ("is", false),
    GT("gt", false),
    GTE("gte", false),
    IN("in", true),
    LT("lt", false),
    LTE("lte", false),
    NE("ne", false),
    NIN("nin", true);

    private String operationAsString;
    private boolean arrayRequired;

    MetadataOperation(String operationAsString, boolean arrayRequired) {
        this.operationAsString = operationAsString;
        this.arrayRequired = arrayRequired;
    }

    public String getOperationAsString() {

        return operationAsString;
    }

    public void setOperationAsString(String operationAsString) {
        this.operationAsString = operationAsString;
    }

    public boolean isArrayRequired() {
        return arrayRequired;
    }

    public void setArrayRequired(boolean arrayRequired) {
        this.arrayRequired = arrayRequired;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
