package cz.vutbr.fit.communication;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.UUID;

public class KafkaResponse {

    private UUID id;
    private String responseTopic;
    private ResponseCode responseCode;
    private String status;
    private String detailMessage;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getResponseTopic() {
        return responseTopic;
    }

    public void setResponseTopic(String responseTopic) {
        this.responseTopic = responseTopic;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public static class Builder {

        private KafkaResponse response;

        public Builder() {
            response = new KafkaResponse();
        }

        public Builder id(UUID id) {
            response.setId(id);
            return this;
        }

        public Builder responseTopic(String responseTopic) {
            response.setResponseTopic(responseTopic);
            return this;
        }

        public Builder responseCode(ResponseCode responseCode) {
            response.setResponseCode(responseCode);
            return this;
        }

        public Builder status(String status) {
            response.setStatus(status);
            return this;
        }

        public Builder detailMessage(String detailMessage) {
            response.setDetailMessage(detailMessage);
            return this;
        }

        public KafkaResponse build() {
            return response;
        }

    }

}
