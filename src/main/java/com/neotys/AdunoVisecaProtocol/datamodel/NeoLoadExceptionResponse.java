package com.neotys.AdunoVisecaProtocol.datamodel;

public class NeoLoadExceptionResponse {
    public String type;
    public String message;

    public NeoLoadExceptionResponse(Throwable t) {
        this.type = t.getClass().getSimpleName();
        this.message = t.getMessage();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
