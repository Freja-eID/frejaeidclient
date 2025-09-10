package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.general.OrganisationIdUserInfo;
import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.beans.organisationid.cancel.CancelAddOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.delete.DeleteOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.get.OrganisationIdResult;
import com.verisec.frejaeid.client.beans.organisationid.get.OrganisationIdResultRequest;
import com.verisec.frejaeid.client.beans.organisationid.getall.GetAllOrganisationIdUsersRequest;
import com.verisec.frejaeid.client.beans.organisationid.init.InitiateAddOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.init.InitiateAddOrganisationIdResponse;
import com.verisec.frejaeid.client.beans.organisationid.update.UpdateOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.update.UpdateOrganisationIdResponse;
import com.verisec.frejaeid.client.client.api.OrganisationIdClientApi;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.http.HttpService;
import com.verisec.frejaeid.client.http.HttpServiceApi;

import javax.net.ssl.SSLContext;

import com.verisec.frejaeid.client.exceptions.FrejaEidClientPollingException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Performs actions with organisation eID.
 */
public class OrganisationIdClient extends BasicClient implements OrganisationIdClientApi {

    public static final Logger LOG = LogManager.getLogger(OrganisationIdClient.class);
    private static final long DEFAULT_EXPIRY_TIME_IN_MILLIS = TimeUnit.DAYS.toMillis(7);
    private static final int DEFAULT_POLLING_TIMEOUT_IN_MILLISECONDS = 60000;

    private OrganisationIdClient(String serverCustomUrl, int pollingTimeoutInMillseconds, HttpServiceApi httpService)
            throws FrejaEidClientInternalException {
        super(serverCustomUrl, pollingTimeoutInMillseconds, TransactionContext.ORGANISATIONAL, httpService, null);
    }

    /**
     * OrganisationId should be initialized with keyStore parameters, server
     * certificate and type of environment.
     *
     * @param sslSettings      instance of wrapper class {@link SslSettings}
     * @param frejaEnvironment determines which {@linkplain FrejaEnvironment}
     *                         will be used
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
        if (sslSettings.getSslContext() == null) {
            return new Builder(sslSettings.getKeystorePath(), sslSettings.getKeystorePass(),
                               sslSettings.getServerCertificatePath(), frejaEnvironment);
        }
        return new Builder(sslSettings.getSslContext(), frejaEnvironment);
    }

    @Override
    public InitiateAddOrganisationIdResponse initiateAdd(InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateInitAddOrganisationIdRequest(initiateAddOrganisationIdRequest);
        LOG.debug("Initiating adding organisation ID with user info type {}, minimum registration level of user {} " +
                          "and expiry time {} min.", initiateAddOrganisationIdRequest.getUserInfoType(),
                  initiateAddOrganisationIdRequest.getMinRegistrationLevel().getState(),
                  initiateAddOrganisationIdRequest.getExpiry() == null ? DEFAULT_EXPIRY_TIME_IN_MILLIS :
                          initiateAddOrganisationIdRequest.getExpiry());
        InitiateAddOrganisationIdResponse response = organisationIdService.initiateAdd(initiateAddOrganisationIdRequest);
        LOG.debug("Received add organisation ID transaction reference {}.", response.getOrgIdRef());
        return response;
    }

    @Override
    public OrganisationIdResult getResult(OrganisationIdResultRequest getOneOrganisationIdResultRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateResultRequest(getOneOrganisationIdResultRequest);
        LOG.debug("Getting result for add organisation ID transaction reference {}.",
                  getOneOrganisationIdResultRequest.getOrgIdRef());
        OrganisationIdResult organisationIdResult = organisationIdService.getResult(getOneOrganisationIdResultRequest);
        LOG.debug("Received {} status for adding organisation ID transaction reference {}.",
                  organisationIdResult.getStatus(), organisationIdResult.getOrgIdRef());
        return organisationIdResult;
    }

    @Override
    public OrganisationIdResult pollForResult(OrganisationIdResultRequest getOneOrganisationIdResultRequest,
                                              int maxWaitingTimeInSec)
            throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException {
        requestValidationService.validateResultRequest(getOneOrganisationIdResultRequest);
        LOG.debug("Polling {}s for result for adding organisation ID transaction reference {}.", maxWaitingTimeInSec,
                  getOneOrganisationIdResultRequest.getOrgIdRef());
        OrganisationIdResult organisationIdResult =
                organisationIdService.pollForResult(getOneOrganisationIdResultRequest, maxWaitingTimeInSec);
        LOG.debug("Received {} status for adding organisation ID transaction reference {}, after polling for result.",
                  organisationIdResult.getStatus(), organisationIdResult.getOrgIdRef());
        return organisationIdResult;
    }

    @Override
    public void cancelAdd(CancelAddOrganisationIdRequest cancelAddOrganisationIdRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateCancelRequest(cancelAddOrganisationIdRequest);
        LOG.debug("Canceling add organisation ID transaction with reference {}.",
                  cancelAddOrganisationIdRequest.getOrgIdRef());
        organisationIdService.cancelAdd(cancelAddOrganisationIdRequest);
        LOG.debug("Successfully canceled adding organisation ID transaction with reference {}.",
                  cancelAddOrganisationIdRequest.getOrgIdRef());
    }

    @Override
    public void delete(DeleteOrganisationIdRequest deleteOrganisationIdRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateDeleteOrganisationIdRequest(deleteOrganisationIdRequest);
        LOG.debug("Deleting organisation ID identifier {}.", deleteOrganisationIdRequest.getIdentifier());
        organisationIdService.delete(deleteOrganisationIdRequest);
        LOG.debug("Successfully deleted organisation ID identifier {}.", deleteOrganisationIdRequest.getIdentifier());
    }

    @Override
    public List<OrganisationIdUserInfo> getAllUsers(GetAllOrganisationIdUsersRequest getAllOrganisationIdUsersRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateGetAllOrganisationIdUsersRequest(getAllOrganisationIdUsersRequest);
        LOG.debug("Getting information about users with organisation ID.");
        List<OrganisationIdUserInfo> organisationIdUserInfos =
                organisationIdService.getAllUsers(getAllOrganisationIdUsersRequest).getUserInfos();
        LOG.debug("Successfully got information about users with organisation ID.");
        return organisationIdUserInfos;
    }

    @Override
    public List<OrganisationIdUserInfo> getAllUsersV1_1(GetAllOrganisationIdUsersRequest getAllOrganisationIdUsersRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateGetAllOrganisationIdUsersRequest(getAllOrganisationIdUsersRequest);
        LOG.debug("Getting information about all users with organisation ID.");
        List<OrganisationIdUserInfo> organisationIdUserInfos =
                organisationIdService.getAllUsersV1_1(getAllOrganisationIdUsersRequest).getUserInfos();
        LOG.debug("Successfully got information about all users with organisation ID.");
        return organisationIdUserInfos;
    }

    @Override
    public UpdateOrganisationIdResponse update(UpdateOrganisationIdRequest updateOrganisationIdRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateUpdateOrganisationIdRequest(updateOrganisationIdRequest);
        LOG.debug("Updating organisation ID with identifier {}. ", updateOrganisationIdRequest.getIdentifier());
        UpdateOrganisationIdResponse response = organisationIdService.update(updateOrganisationIdRequest);
        LOG.debug("Successfully updated organisation ID with identifier {}.", updateOrganisationIdRequest.getIdentifier());
        return response;
    }

    public static class Builder extends GenericBuilder {

        public static final Logger LOG = LogManager.getLogger(Builder.class);

        private Builder(SSLContext sslContext, FrejaEnvironment frejaEnvironment) {
            super(sslContext, frejaEnvironment);
        }

        private Builder(String keystorePath, String keystorePass, String certificatePath,
                        FrejaEnvironment frejaEnvironment)
                throws FrejaEidClientInternalException {
            super(keystorePath, keystorePass, certificatePath, frejaEnvironment);
        }

        /**
         * Polling timeout is time between two polls for final results.
         *
         * @param pollingTimeout in milliseconds on client side. Default value is
         *                       {@value #DEFAULT_POLLING_TIMEOUT_IN_MILLISECONDS} milliseconds.
         * @return builder
         */
        @Override
        public Builder setPollingTimeout(int pollingTimeout) {
            return (Builder) super.setPollingTimeout(pollingTimeout);
        }

        @Override
        public OrganisationIdClient build() throws FrejaEidClientInternalException {
            transactionContext = TransactionContext.ORGANISATIONAL;
            if (httpService == null) {
                httpService = new HttpService(sslContext, connectionTimeout, readTimeout);
            }
            if (pollingTimeout == 0) {
                pollingTimeout = DEFAULT_POLLING_TIMEOUT_IN_MILLISECONDS;
            }
            checkSetParameters();
            LOG.debug("Successfully created OrganisationIdClient with server URL {}, polling timeout {}ms and " +
                              "transaction context {}.",
                      serverCustomUrl, pollingTimeout, transactionContext.getContext());
            return new OrganisationIdClient(serverCustomUrl, pollingTimeout, httpService);
        }

    }
}
