package com.verisec.frejaeid.client.enums;

public enum HttpStatusCode {
    OK(200),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    NOT_AUTHORIZED(401),
    NOT_FOUND(404),
    GONE(410),
    UNPROCESSABLE_ENTITY(422),
    INTERNAL_SERVER_ERROR(500),
    SERVICE_UNAVAILABLE(503);

    private final int code;

    private HttpStatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static HttpStatusCode getHttpStatusCode(int code) {
        for (HttpStatusCode val : HttpStatusCode.values()) {
            if (val.getCode() == code) {
                return val;
            }
        }
        return null;
    }

}
