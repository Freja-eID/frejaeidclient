package com.verisec.frejaeid.client.service;

import com.verisec.frejaeid.client.enums.TransactionStatus;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import com.verisec.frejaeid.client.util.MethodUrl;

public class BasicService {

    protected HttpServiceApi httpService;
    protected String serverAddress;

    public BasicService(String serverAddress, HttpServiceApi httpService) throws FrejaEidClientInternalException {
        this.httpService = httpService;
        this.serverAddress = serverAddress;
    }

    protected String getUrl(String serverAddress, MethodUrl url) {
        return serverAddress + url.toString();
    }

    protected boolean isFinalStatus(TransactionStatus status) {
        return (status == TransactionStatus.CANCELED || status == TransactionStatus.RP_CANCELED || status == TransactionStatus.EXPIRED || status == TransactionStatus.APPROVED || status == TransactionStatus.REJECTED);
    }

}
