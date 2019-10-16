package com.neotys.AdunoVisecaProtocol.datamodel;

import com.bsiag.scout.shared.proxy.http.HttpProxyHandlerResponse;

import java.util.Optional;

public class NeoLoadSoapResponse {
    public String status;
    public String type;
    public HttpProxyHandlerResponse data;
    public String origin;
    public Optional<NeoLoadExceptionResponse> exception;


    public NeoLoadSoapResponse(HttpProxyHandlerResponse data, String origin,String status) {
        this.data = data;
        this.origin = origin;
        this.status=status;
        if(data.getData() != null)
            this.type=data.getData().getClass().getSimpleName();
        else
            this.type="";

        if(data.getException()!=null)
            this.exception=Optional.ofNullable(new NeoLoadExceptionResponse(data.getException()));
        else
            this.exception=Optional.empty();


    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HttpProxyHandlerResponse getData() {
        return data;
    }

    public void setData(HttpProxyHandlerResponse data) {
        this.data = data;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Optional<NeoLoadExceptionResponse> getException() {
        return exception;
    }

    public void setException(Optional<NeoLoadExceptionResponse> exception) {
        this.exception = exception;
    }
}

