package com.verisec.frejaeid.client.service;

import com.verisec.frejaeid.client.enums.TransactionStatus;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import com.verisec.frejaeid.client.util.MethodUrl;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicService {

    protected HttpServiceApi httpService;
    protected String serverAddress;
    private final String resourceServiceUrl;
    private static final String BIND_USER_TRANSACTION_URL_PREFIX = "frejaeid://bindUserToTransaction"
            + "?transactionReference=";
    private static final String QR_CODE_PARAMETER_NAME = "qrcodedata";

    public BasicService(String serverAddress, HttpServiceApi httpService, String resourceServiceUrl) {
        this.httpService = httpService;
        this.serverAddress = serverAddress;
        this.resourceServiceUrl = resourceServiceUrl;
    }

    protected String getUrl(String serverAddress, MethodUrl url) {
        return serverAddress + url.toString();
    }

    protected boolean isFinalStatus(TransactionStatus status) {
        return (status == TransactionStatus.CANCELED || status == TransactionStatus.RP_CANCELED
                || status == TransactionStatus.EXPIRED || status == TransactionStatus.APPROVED
                || status == TransactionStatus.REJECTED);
    }

    public byte[] generateQRCode(String reference) throws FrejaEidClientInternalException,
            FrejaEidException, IOException {
        String encodedReference = URLEncoder.encode(reference, StandardCharsets.UTF_8.toString());
        String bindTransactionToUserUrl = BIND_USER_TRANSACTION_URL_PREFIX + encodedReference;
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put(QR_CODE_PARAMETER_NAME, bindTransactionToUserUrl);
        return httpService.httpGet(getUrl(resourceServiceUrl, MethodUrl.QR_CODE_GENERATE), parameterMap);
    }

}
