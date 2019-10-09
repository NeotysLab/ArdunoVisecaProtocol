package com.neotys.AdunoVisecaProtocol.datamodel;

import com.bsiag.scout.shared.proxy.http.HttpProxyHandlerRequest;

public class NeoloadSoapRequest {
    public String operation;
    public String serviceInterfaceClassName;
    public String version;
    public String language;
    public String format;
    public HttpProxyHandlerRequest data;
    public String origin;

    public NeoloadSoapRequest(HttpProxyHandlerRequest data, String origin) {
        this.data = data;
        this.operation=data.getOperation();
        this.serviceInterfaceClassName=data.getServiceInterfaceClassName();
        this.version=data.getVersion();
        this.language=data.getNlsLocale().toString();
        this.format=data.getLocale().toString();
        this.origin = origin;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getServiceInterfaceClassName() {
        return serviceInterfaceClassName;
    }

    public void setServiceInterfaceClassName(String serviceInterfaceClassName) {
        this.serviceInterfaceClassName = serviceInterfaceClassName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public HttpProxyHandlerRequest getData() {
        return data;
    }

    public void setData(HttpProxyHandlerRequest data) {
        this.data = data;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
