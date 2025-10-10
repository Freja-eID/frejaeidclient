package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.custodianship.get.GetUserCustodianshipStatusRequest;
import com.verisec.frejaeid.client.beans.custodianship.get.GetUserCustodianshipStatusResponse;
import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.client.api.CustodianshipClientApi;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.http.HttpService;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLContext;

public class CustodianshipClient extends BasicClient implements CustodianshipClientApi {
    public static final Logger LOG = LogManager.getLogger(CustodianshipClient.class);

    private CustodianshipClient(String serverCustomUrl, int pollingTimeout, HttpServiceApi httpService)
            throws FrejaEidClientInternalException {
        super(serverCustomUrl, pollingTimeout, TransactionContext.PERSONAL, httpService, null);
    }

    public static CustodianshipClient.Builder create(SslSettings sslSettings, FrejaEnvironment frejaEnvironment)
            throws FrejaEidClientInternalException {
        if (sslSettings == null) {
            throw new FrejaEidClientInternalException("SslSettings cannot be null.");
        }
        return new CustodianshipClient.Builder(sslSettings, frejaEnvironment);
    }

    @Override
    public String getUserCustodianshipStatus(GetUserCustodianshipStatusRequest getUserCustodianshipStatusRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateGetUserCustodianshipStatusRequest(getUserCustodianshipStatusRequest);
        LOG.debug("Getting custodianship status for user.");
        GetUserCustodianshipStatusResponse getUserCustodianshipStatusResponse =
                custodianshipService.getUserCustodianshipStatus(getUserCustodianshipStatusRequest);
        LOG.debug("Successfully got user custodianship status.");
        return getUserCustodianshipStatusResponse.getCustodianshipStatus();
    }

    public static class Builder extends GenericBuilder {

        public static final Logger LOG = LogManager.getLogger(CustodianshipClient.Builder.class);

        private Builder(SslSettings sslSettings,
                        FrejaEnvironment frejaEnvironment) throws FrejaEidClientInternalException {
            super(sslSettings, frejaEnvironment);
        }

        @Override
        public CustodianshipClient build() throws FrejaEidClientInternalException {
            transactionContext = TransactionContext.PERSONAL;
            if (httpService == null) {
                httpService = new HttpService(sslContext, connectionTimeout, readTimeout);
            }
            if (pollingTimeout == 0) {
                pollingTimeout = DEFAULT_POLLING_TIMEOUT_IN_MILLISECONDS;
            }
            checkSetParameters();
            LOG.debug("Successfully created CustodianshipClient with server URL {}, polling timeout {}ms and " +
                              "transaction context {}.",
                      serverCustomUrl, pollingTimeout, transactionContext.getContext());
            return new CustodianshipClient(serverCustomUrl, pollingTimeout, httpService);
        }

    }
}
