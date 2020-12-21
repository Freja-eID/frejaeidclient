package com.verisec.frejaeid.client.service;

import com.verisec.frejaeid.client.beans.authentication.init.InitiateAuthenticationRequest;
import com.verisec.frejaeid.client.beans.common.CancelRequest;
import com.verisec.frejaeid.client.beans.common.ResultRequest;
import com.verisec.frejaeid.client.beans.common.ResultsRequest;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.organisationid.delete.DeleteOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.getall.GetAllOrganisationIdUsersRequest;
import com.verisec.frejaeid.client.beans.organisationid.init.InitiateAddOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.sign.init.InitiateSignRequest;
import com.verisec.frejaeid.client.beans.usermanagement.customidentifier.delete.DeleteCustomIdentifierRequest;
import com.verisec.frejaeid.client.beans.usermanagement.customidentifier.set.SetCustomIdentifierRequest;
import com.verisec.frejaeid.client.enums.AttributeToReturn;
import com.verisec.frejaeid.client.enums.MinRegistrationLevel;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.enums.UserInfoType;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class RequestValidationService {

    public void validateInitAuthRequest(InitiateAuthenticationRequest initiateAuthenticationRequest,
                                        TransactionContext transactionContext)
            throws FrejaEidClientInternalException {
        validateRequest(initiateAuthenticationRequest);
        validateUserInfoTypeAndUserInfo(initiateAuthenticationRequest.getUserInfoType(),
                                        initiateAuthenticationRequest.getUserInfo(), transactionContext);
        validateRequestedAttributes(initiateAuthenticationRequest.getAttributesToReturn());
        validateRegistrationState(initiateAuthenticationRequest.getMinRegistrationLevel(), transactionContext);
        validateRelyingPartyIdIsEmpty(initiateAuthenticationRequest.getRelyingPartyId());
    }

    public <T extends ResultRequest> void validateResultRequest(T getOneResultRequest)
            throws FrejaEidClientInternalException {
        validateRequest(getOneResultRequest);
        validateReference(getOneResultRequest.getReference());
        validateRelyingPartyIdIsEmpty(getOneResultRequest.getRelyingPartyId());
    }

    public <T extends ResultsRequest> void validateResultsRequest(T getResultsRequest)
            throws FrejaEidClientInternalException {
        validateRequest(getResultsRequest);
        validateRelyingPartyIdIsEmpty(getResultsRequest.getRelyingPartyId());
    }

    public <T extends CancelRequest> void validateCancelRequest(T cancelRequest)
            throws FrejaEidClientInternalException {
        validateRequest(cancelRequest);
        validateReference(cancelRequest.getReference());
        validateRelyingPartyIdIsEmpty(cancelRequest.getRelyingPartyId());
    }

    public void validateInitSignRequest(InitiateSignRequest initiateSignRequest, TransactionContext transactionContext)
            throws FrejaEidClientInternalException {
        validateRequest(initiateSignRequest);
        validateUserInfoTypeAndUserInfo(initiateSignRequest.getUserInfoType(), initiateSignRequest.getUserInfo(),
                                        transactionContext);
        validateRequestedAttributes(initiateSignRequest.getAttributesToReturn());
        if (initiateSignRequest.getTitle() != null && initiateSignRequest.getTitle().isEmpty()) {
            throw new FrejaEidClientInternalException("Title cannot be empty.");
        }
        if (initiateSignRequest.getDataToSign() == null || initiateSignRequest.getDataToSign().getText() == null
                || initiateSignRequest.getDataToSign().getText().isEmpty()) {
            throw new FrejaEidClientInternalException("DataToSign cannot be null or empty.");
        }
        if (initiateSignRequest.getPushNotification() != null) {
            if (StringUtils.isBlank(initiateSignRequest.getPushNotification().getText())
                    || StringUtils.isBlank(initiateSignRequest.getPushNotification().getTitle())) {
                throw new FrejaEidClientInternalException("PushNotification title or text cannot be null or empty.");
            }
        }
        validateRegistrationState(initiateSignRequest.getMinRegistrationLevel(), transactionContext);
        validateRelyingPartyIdIsEmpty(initiateSignRequest.getRelyingPartyId());
    }

    public void validateSetCustomIDentifierRequest(SetCustomIdentifierRequest setCustomIdentifierRequest)
            throws FrejaEidClientInternalException {
        validateRequest(setCustomIdentifierRequest);
        validateUserInfoTypeAndUserInfo(setCustomIdentifierRequest.getUserInfoType(),
                                        setCustomIdentifierRequest.getUserInfo(), TransactionContext.PERSONAL);
        validateIdentifier(setCustomIdentifierRequest.getCustomIdentifier());
        validateRelyingPartyIdIsEmpty(setCustomIdentifierRequest.getRelyingPartyId());
    }

    public void validateDeleteCustomIdentifierRequest(DeleteCustomIdentifierRequest deleteCustomIdentifierRequest)
            throws FrejaEidClientInternalException {
        validateRequest(deleteCustomIdentifierRequest);
        validateIdentifier(deleteCustomIdentifierRequest.getCustomIdentifier());
        validateRelyingPartyIdIsEmpty(deleteCustomIdentifierRequest.getRelyingPartyId());
    }

    public void validateInitAddOrganisationIdRequest(InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest)
            throws FrejaEidClientInternalException {
        validateRequest(initiateAddOrganisationIdRequest);
        validateUserInfoTypeAndUserInfo(initiateAddOrganisationIdRequest.getUserInfoType(),
                                        initiateAddOrganisationIdRequest.getUserInfo(),
                                        TransactionContext.ORGANISATIONAL);
        validateOrganisationIdTitle(initiateAddOrganisationIdRequest.getOrganisationId().getTitle());
        validateIdentifierName(initiateAddOrganisationIdRequest.getOrganisationId().getIdentifierName());
        validateIdentifier(initiateAddOrganisationIdRequest.getOrganisationId().getIdentifier());
        validateRegistrationState(initiateAddOrganisationIdRequest.getMinRegistrationLevel(),
                                  TransactionContext.ORGANISATIONAL);
        validateRelyingPartyIdIsEmpty(initiateAddOrganisationIdRequest.getRelyingPartyId());
    }

    public void validateDeleteOrganisationIdRequest(DeleteOrganisationIdRequest deleteOrganisationIdRequest)
            throws FrejaEidClientInternalException {
        validateRequest(deleteOrganisationIdRequest);
        validateIdentifier(deleteOrganisationIdRequest.getIdentifier());
        validateRelyingPartyIdIsEmpty(deleteOrganisationIdRequest.getRelyingPartyId());
    }

    public void validateGetAllOrganisationIdUsersRequest(
            GetAllOrganisationIdUsersRequest getAllOrganisationIdUsersRequest)
            throws FrejaEidClientInternalException {
        validateRequest(getAllOrganisationIdUsersRequest);
        validateRelyingPartyIdIsEmpty(getAllOrganisationIdUsersRequest.getRelyingPartyId());
    }

    private void validateRequest(RelyingPartyRequest relyingPartyRequest) throws FrejaEidClientInternalException {
        if (relyingPartyRequest == null) {
            throw new FrejaEidClientInternalException("Request cannot be null value.");
        }
    }

    private void validateRelyingPartyIdIsEmpty(String relyingPartyId) throws FrejaEidClientInternalException {
        if (relyingPartyId != null) {
            if (StringUtils.isEmpty(relyingPartyId)) {
                throw new FrejaEidClientInternalException("RelyingPartyId cannot be empty.");
            }
        }
    }

    private void validateUserInfoTypeAndUserInfo(UserInfoType userInfoType, String userInfo,
                                                 TransactionContext transactionContext)
            throws FrejaEidClientInternalException {
        if (userInfoType == null) {
            throw new FrejaEidClientInternalException("UserInfoType cannot be null.");
        }
        if (StringUtils.isBlank(userInfo)) {
            throw new FrejaEidClientInternalException("UserInfo cannot be null or empty.");
        }
        if (transactionContext == TransactionContext.PERSONAL && userInfoType.equals(UserInfoType.ORG_ID)) {
            throw new FrejaEidClientInternalException("UserInfoType ORG ID cannot be used in personal context.");
        }
    }

    private void validateRequestedAttributes(Set<AttributeToReturn> requestedAttributes)
            throws FrejaEidClientInternalException {
        if (requestedAttributes != null && requestedAttributes.isEmpty()) {
            throw new FrejaEidClientInternalException("RequestedAttributes cannot be empty.");
        }
    }

    private void validateRegistrationState(MinRegistrationLevel minRegistrationLevel,
                                           TransactionContext transactionContext)
            throws FrejaEidClientInternalException {
        if (minRegistrationLevel == null && TransactionContext.PERSONAL == transactionContext) {
            throw new FrejaEidClientInternalException("RegistrationState cannot be null.");
        }
    }

    private void validateReference(String reference) throws FrejaEidClientInternalException {
        if (StringUtils.isBlank(reference)) {
            throw new FrejaEidClientInternalException("Reference cannot be null or empty.");
        }
    }

    private void validateOrganisationIdTitle(String organisationIdTitle) throws FrejaEidClientInternalException {
        if (StringUtils.isBlank(organisationIdTitle)) {
            throw new FrejaEidClientInternalException("OrganisationIdTitle cannot be null or empty.");
        }
    }

    private void validateIdentifierName(String identifierName) throws FrejaEidClientInternalException {
        if (StringUtils.isBlank(identifierName)) {
            throw new FrejaEidClientInternalException("IdentifierName cannot be null or empty.");
        }
    }

    private void validateIdentifier(String identifier) throws FrejaEidClientInternalException {
        if (StringUtils.isBlank(identifier)) {
            throw new FrejaEidClientInternalException("Identifier cannot be null or empty.");
        }
    }

}
