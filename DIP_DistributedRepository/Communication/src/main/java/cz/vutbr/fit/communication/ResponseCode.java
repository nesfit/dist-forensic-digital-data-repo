package cz.vutbr.fit.communication;

public enum ResponseCode {

    OK(200),
    BAD_REQUEST(400),
    UNSUPPORTED_MEDIA_TYPE(415),
    INTERNAL_SERVER_ERROR(500);

    private int code;

    ResponseCode(int code) {
        this.code = code;
    }

    public String toString() {
        return String.valueOf(code);
    }

}
