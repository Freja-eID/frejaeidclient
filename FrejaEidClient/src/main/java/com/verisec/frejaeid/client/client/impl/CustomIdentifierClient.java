package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.beans.usermanagement.customidentifier.delete.DeleteCustomIdentifierRequest;
import com.verisec.frejaeid.client.beans.usermanagement.customidentifier.set.SetCustomIdentifierRequest;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.http.HttpService;
import com.verisec.frejaeid.client.http.HttpServiceApi;

import javax.net.ssl.SSLContext;

import com.verisec.frejaeid.client.client.api.CustomIdentifierClientApi;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Performs actions with custom identifier.
 */
public class CustomIdentifierClient extends BasicClient implements CustomIdentifierClientApi {

    public static final Logger LOG = LogManager.getLogger(CustomIdentifierClient.class);

    private CustomIdentifierClient(String serverCustomUrl, int pollingTimeout, HttpServiceApi httpService)
            throws FrejaEidClientInternalException {
        super(serverCustomUrl, pollingTimeout, TransactionContext.PERSONAL, httpService, null);
    }

    /**
     * CustomIdentifierClient should be initialized with keyStore parameters,
     * server certificate and type of environment.
     *
     * @param sslSettings      instance of wrapper class {@link SslSettings}
     * @param frejaEnvironment determinate which environment will be used and
     *                         can be {@linkplain FrejaEnvironment#TEST}, if you want to perform some
     *                         tests first or {@linkplain FrejaEnvironment#PRODUCTION} if you want to
     *                         send requests to production.
     * @return client builder
     * @throws FrejaEidClientInternalException if fails to initiate SSL context
     *                                         with given parameters(wrong password, wrong absolute path or unsupported
     *                                         type of client keyStore or server certificate etc.).
     */
    public static Builder create(SslSettings sslSettings, FrejaEnvironment frejaEnvironment)
            throws FrejaEidClientInternalException {
        if (sslSettings == null) {
            throw new FrejaEidClientInternalException("SslSettings cannot be null.");
        }
        return new Builder(sslSettings, frejaEnvironment);
    }

    @Override
    public void set(SetCustomIdentifierRequest setCustomIdentifierRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateSetCustomIDentifierRequest(setCustomIdentifierRequest);
        LOG.debug("Setting custom identifier for user info type {}.", setCustomIdentifierRequest.getUserInfoType());
        customIdentifierService.set(setCustomIdentifierRequest);
        LOG.debug("Successfully set custom identifier.");
    }

    @Override
    public void delete(DeleteCustomIdentifierRequest deleteCustomIdentifierRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateDeleteCustomIdentifierRequest(deleteCustomIdentifierRequest);
        LOG.debug("Deleting custom identifier {}.", deleteCustomIdentifierRequest.getCustomIdentifier());
        customIdentifierService.delete(deleteCustomIdentifierRequest);
        LOG.debug("Successfully deleted custom identifier {}.", deleteCustomIdentifierRequest.getCustomIdentifier());
    }

    public static class Builder extends GenericBuilder {

        public static final Logger LOG = LogManager.getLogger(Builder.class);

        private Builder(SslSettings sslSettings,
                        FrejaEnvironment frejaEnvironment) throws FrejaEidClientInternalException {
            super(sslSettings, frejaEnvironment);
        }

        @Override
        public CustomIdentifierClient build() throws FrejaEidClientInternalException {
            transactionContext = TransactionContext.PERSONAL;
            if (httpService == null) {
                httpService = new HttpService(sslContext, connectionTimeout, readTimeout);
            }
            if (pollingTimeout == 0) {
                pollingTimeout = DEFAULT_POLLING_TIMEOUT_IN_MILLISECONDS;
            }
            checkSetParameters();
            LOG.debug("Successfully created CustomIdentifierClient with server URL {}, polling timeout {}ms and " +
                              "transaction context {}.",
                      serverCustomUrl, pollingTimeout, transactionContext.getContext());
            return new CustomIdentifierClient(serverCustomUrl, pollingTimeout, httpService);
        }

    }

}
