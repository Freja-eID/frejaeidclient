package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.beans.organisationid.cancel.CancelAddOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.delete.DeleteOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.get.OrganisationIdResult;
import com.verisec.frejaeid.client.beans.organisationid.get.OrganisationIdResultRequest;
import com.verisec.frejaeid.client.beans.organisationid.init.InitiateAddOrganisationIdRequest;
import com.verisec.frejaeid.client.client.api.OrganisationIdClientApi;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.http.HttpService;
import com.verisec.frejaeid.client.http.HttpServiceApi;

import javax.net.ssl.SSLContext;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientPollingException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;

/**
 * Performs actions with organisation eID.
 *
 */
public class OrganisationIdClient extends BasicClient implements OrganisationIdClientApi {

    private OrganisationIdClient(String serverCustomUrl, int pollingTimeoutInMillseconds, HttpServiceApi httpService) throws FrejaEidClientInternalException {
        super(serverCustomUrl, pollingTimeoutInMillseconds, TransactionContext.ORGANISATIONAL, httpService);
    }

    /**
     * OrganisationId should be initialized with keyStore parameters, server
     * certificate and type of environment.
     *
     * @param sslSettings instance of wrapper class {@link SslSettings}
     * @param frejaEnvironment determines which {@linkplain FrejaEnvironment}
     * will be used
     *
     * @return client builder
     * @throws FrejaEidClientInternalException if fails to initiate SSL context
     * with given parameters(wrong password, wrong absolute path or unsupported
     * type of client keyStore or server certificate etc.).
     */
    public static Builder create(SslSettings sslSettings, FrejaEnvironment frejaEnvironment) throws FrejaEidClientInternalException {
        if (sslSettings.getSslContext() == null) {
            return new Builder(sslSettings.getKeystorePath(), sslSettings.getKeystorePass(), sslSettings.getServerCertificatePath(), frejaEnvironment);
        }
        return new Builder(sslSettings.getSslContext(), frejaEnvironment);
    }

    @Override
    public String initiateAdd(InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateInitAddOrganisationIdRequest(initiateAddOrganisationIdRequest);
        return organisationIdService.initiateAdd(initiateAddOrganisationIdRequest).getOrgIdRef();
    }

    @Override
    public OrganisationIdResult getResult(OrganisationIdResultRequest getOneOrganisationIdResultRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateResultRequest(getOneOrganisationIdResultRequest);
        return organisationIdService.getResult(getOneOrganisationIdResultRequest);
    }

    @Override
    public OrganisationIdResult pollForResult(OrganisationIdResultRequest getOneOrganisationIdResultRequest, int maxWaitingTimeInSec) throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException {
        requestValidationService.validateResultRequest(getOneOrganisationIdResultRequest);
        return organisationIdService.pollForResult(getOneOrganisationIdResultRequest, maxWaitingTimeInSec);
    }

    @Override
    public void cancelAdd(CancelAddOrganisationIdRequest cancelAddOrganisationIdRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateCancelRequest(cancelAddOrganisationIdRequest);
        organisationIdService.cancelAdd(cancelAddOrganisationIdRequest);
    }

    @Override
    public void delete(DeleteOrganisationIdRequest deleteOrganisationIdRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateDeleteOrganisationIdRequest(deleteOrganisationIdRequest);
        organisationIdService.delete(deleteOrganisationIdRequest);
    }

    public static class Builder extends GenericBuilder {

        private Builder(SSLContext sslContext, FrejaEnvironment frejaEnvironment) throws FrejaEidClientInternalException {
            super(sslContext, frejaEnvironment);
        }

        private Builder(String keystorePath, String keystorePass, String certificatePath, FrejaEnvironment frejaEnvironment) throws FrejaEidClientInternalException {
            super(keystorePath, keystorePass, certificatePath, frejaEnvironment);
        }

        @Override
        public OrganisationIdClient build() throws FrejaEidClientInternalException {
            transactionContext = TransactionContext.ORGANISATIONAL;
            checkSetParametars();
            if (httpService == null) {
                httpService = new HttpService(sslContext, connectionTimeout, readTimeout);
            }
            return new OrganisationIdClient(serverCustomUrl, pollingTimeout, httpService);
        }

    }

}
