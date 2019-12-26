package com.verisec.frejaeid.client.service;

import com.verisec.frejaeid.client.beans.authentication.cancel.CancelAuthenticationRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResultsRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResultRequest;
import com.verisec.frejaeid.client.beans.authentication.init.InitiateAuthenticationRequest;
import com.verisec.frejaeid.client.beans.general.OrganisationId;
import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.beans.organisationid.cancel.CancelAddOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.delete.DeleteOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.get.OrganisationIdResultRequest;
import com.verisec.frejaeid.client.beans.organisationid.init.InitiateAddOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.sign.cancel.CancelSignRequest;
import com.verisec.frejaeid.client.beans.sign.get.SignResultRequest;
import com.verisec.frejaeid.client.beans.sign.get.SignResultsRequest;
import com.verisec.frejaeid.client.beans.sign.init.DataToSign;
import com.verisec.frejaeid.client.beans.sign.init.InitiateSignRequest;
import com.verisec.frejaeid.client.beans.sign.init.PushNotification;
import com.verisec.frejaeid.client.beans.usermanagement.customidentifier.delete.DeleteCustomIdentifierRequest;
import com.verisec.frejaeid.client.beans.usermanagement.customidentifier.set.SetCustomIdentifierRequest;
import com.verisec.frejaeid.client.client.api.AuthenticationClientApi;
import com.verisec.frejaeid.client.client.api.CustomIdentifierClientApi;
import com.verisec.frejaeid.client.client.api.OrganisationIdClientApi;
import com.verisec.frejaeid.client.client.api.SignClientApi;
import com.verisec.frejaeid.client.client.impl.AuthenticationClient;
import com.verisec.frejaeid.client.client.impl.CustomIdentifierClient;
import com.verisec.frejaeid.client.client.impl.OrganisationIdClient;
import com.verisec.frejaeid.client.client.impl.SignClient;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientPollingException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RequestValidationServiceTest {

    private static final String ORGANISATION_ID_TITLE = "OrgananisationId title";
    private static final String IDENTIFIER_NAME = "Identifier name";
    private static final String IDENTIFIER = "identifier";
    private static final String TEXT = "text";
    private static final String TITLE = "title";
    private static final String EMAIL = "email";
    private static final String PHONE_NUM = "phoneNum";
    private static final String REFERENCE = "reference";

    private static AuthenticationClientApi authenticationClient;
    private static SignClientApi signClient;
    private static CustomIdentifierClientApi customIdentifierClient;
    private static OrganisationIdClientApi organisationIdClient;

    @BeforeClass
    public static void initialization() throws FrejaEidClientInternalException {
        authenticationClient = AuthenticationClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .setTestModeCustomUrl("test").setTransactionContext(TransactionContext.PERSONAL).build();
        signClient = SignClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .setTestModeCustomUrl("test").setTransactionContext(TransactionContext.PERSONAL).build();
        customIdentifierClient = CustomIdentifierClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .setTestModeCustomUrl("test").build();
        organisationIdClient = OrganisationIdClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .setTestModeCustomUrl("test").build();
    }

    @Test
    public void cancelRequest_missingReference_expectError() throws FrejaEidException {
        try {
            authenticationClient.cancel(CancelAuthenticationRequest.create(""));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Reference cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void cancelRequest_referenceNull_expectError() throws FrejaEidException {
        try {
            authenticationClient.cancel(CancelAuthenticationRequest.create(null));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Reference cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void cancelRequest_relyingPartyIdEmpty_expectError() throws FrejaEidException {
        try {
            authenticationClient.cancel(CancelAuthenticationRequest.create(REFERENCE, ""));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("RelyingPartyId cannot be empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void cancelRequest_requestNull_expectError() throws FrejaEidException {
        try {
            authenticationClient.cancel(null);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Request cannot be null value.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getResult_missingReference_expectError() throws FrejaEidException {
        try {
            authenticationClient.getResult(AuthenticationResultRequest.create(""));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Reference cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getResult_referenceNull_expectError() throws FrejaEidException {
        try {
            authenticationClient.getResult(AuthenticationResultRequest.create(null));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Reference cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getResult_requestNull_expectError() throws FrejaEidException {
        try {
            authenticationClient.getResult(null);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Request cannot be null value.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getResult_relyingPartyIdEmpty_expectError() throws FrejaEidException {
        try {
            authenticationClient.getResult(AuthenticationResultRequest.create(REFERENCE, ""));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("RelyingPartyId cannot be empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getResults_requestNull_expectError() throws FrejaEidException {
        try {
            authenticationClient.getResults(null);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Request cannot be null value.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getResults_relyingPartyIdEmpty_expectError() throws FrejaEidException {
        try {
            authenticationClient.getResults(AuthenticationResultsRequest.create(""));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("RelyingPartyId cannot be empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAuth_requestNull() throws FrejaEidException {
        try {
            authenticationClient.initiate(null);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Request cannot be null value.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAuth_relyingPartyIdEmpty() throws FrejaEidException {
        try {
            authenticationClient.initiate(InitiateAuthenticationRequest.createCustom().setEmail(EMAIL).setRelyingPartyId("").build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("RelyingPartyId cannot be empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAuth_userInfoEmpty() throws FrejaEidException {
        try {
            authenticationClient.initiate(InitiateAuthenticationRequest.createCustom().setEmail("").build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("UserInfo cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAuth_userInfoNull() throws FrejaEidException {
        try {
            authenticationClient.initiate(InitiateAuthenticationRequest.createCustom().setEmail(null).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("UserInfo cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAuth_requestedAttributesEmpty() throws FrejaEidException {
        try {
            authenticationClient.initiate(InitiateAuthenticationRequest.createCustom().setEmail(EMAIL).setAttributesToReturn().build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("RequestedAttributes cannot be empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAuth_perosnal_setOrgId() throws FrejaEidException {
        try {
            authenticationClient.initiate(InitiateAuthenticationRequest.createCustom().setOrganisationId(IDENTIFIER).setAttributesToReturn().build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("UserInfoType ORG ID cannot be used in personal context.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void setCustomIdentifier_nullCustomIdentifier_expectError() throws FrejaEidException {
        try {
            customIdentifierClient.set(SetCustomIdentifierRequest.createDefaultWithEmail(EMAIL, null));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Identifier cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void setCustomIdentifier_emptyCustomIdentifier_expectError() throws FrejaEidException {
        try {
            customIdentifierClient.set(SetCustomIdentifierRequest.createDefaultWithEmail(EMAIL, ""));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Identifier cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void setCustomIdentifier_nullUserInfo_expectError() throws FrejaEidException {
        try {
            customIdentifierClient.set(SetCustomIdentifierRequest.createDefaultWithEmail(null, IDENTIFIER));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("UserInfo cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void setCustomIdentifier_emptyUserInfo_expectError() throws FrejaEidException {
        try {
            customIdentifierClient.set(SetCustomIdentifierRequest.createDefaultWithEmail("", IDENTIFIER));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("UserInfo cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void setCustomIdentifier_emptyRelyingPartyId_expectError() throws FrejaEidException {
        try {
            customIdentifierClient.set(SetCustomIdentifierRequest.createCustom().setPhoneNumberAndCustomIdentifier(PHONE_NUM, IDENTIFIER).setRelyingPartyId("").build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("RelyingPartyId cannot be empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void deleteCustomIdentifier_emptyCustomIdentifier_expectError() throws FrejaEidException {
        try {
            customIdentifierClient.delete(DeleteCustomIdentifierRequest.create(""));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Identifier cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void deleteCustomIdentifier_nullCustomIdentifier_expectError() throws FrejaEidException {
        try {
            customIdentifierClient.delete(DeleteCustomIdentifierRequest.create(null));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Identifier cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void deleteCustomIdentifier_emptyRelyingPartyId_expectError() throws FrejaEidException {
        try {
            customIdentifierClient.delete(DeleteCustomIdentifierRequest.create(IDENTIFIER, ""));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("RelyingPartyId cannot be empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void cancelSignRequest_missingReference_expectError() throws FrejaEidException {
        try {
            signClient.cancel(CancelSignRequest.create(""));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Reference cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void cancelSignRequest_referenceNull_expectError() throws FrejaEidException {
        try {
            signClient.cancel(CancelSignRequest.create(null));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Reference cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void cancelSignRequest_relyingPartyIdEmpty_expectError() throws FrejaEidException {
        try {
            signClient.cancel(CancelSignRequest.create(REFERENCE, ""));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("RelyingPartyId cannot be empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void cancelSignRequest_requestNull_expectError() throws FrejaEidException {
        try {
            signClient.cancel(null);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Request cannot be null value.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getSignResult_missingReference_expectError() throws FrejaEidException {
        try {
            signClient.getResult(SignResultRequest.create(""));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Reference cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getSignResult_referenceNull_expectError() throws FrejaEidException {
        try {
            signClient.getResult(SignResultRequest.create(null));;
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Reference cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getSignResult_requestNull_expectError() throws FrejaEidException {
        try {
            signClient.getResult(null);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Request cannot be null value.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getSignResult_relyingPartyIdEmpty_expectError() throws FrejaEidException {
        try {
            signClient.getResult(SignResultRequest.create(REFERENCE, ""));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("RelyingPartyId cannot be empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getSignResults_requestNull_expectError() throws FrejaEidException, FrejaEidClientPollingException {
        try {
            signClient.getResults(null);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Request cannot be null value.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getSignResults_relyingPartyIdEmpty_expectError() throws FrejaEidException, FrejaEidClientPollingException {
        try {
            signClient.getResults(SignResultsRequest.create(""));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("RelyingPartyId cannot be empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_requestNull() throws FrejaEidException {
        try {
            signClient.initiate(null);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Request cannot be null value.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_relyingPartyIdEmpty() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL).setDataToSign(DataToSign.create(TEXT)).setRelyingPartyId("").build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("RelyingPartyId cannot be empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_userInfoEmpty() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail("").build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("UserInfo cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_userInfoNull() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(null).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("UserInfo cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_requestedAttributesEmpty() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL).setAttributesToReturn().build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("RequestedAttributes cannot be empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_dataToSignNull() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL).setDataToSign(null).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("DataToSign cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_dataToSignTextNull() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL).setDataToSign(DataToSign.create(null)).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("DataToSign cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_dataToSignTextEmpty() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL).setDataToSign(DataToSign.create("")).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("DataToSign cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_pushNotificationTextEmpty() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL).setDataToSign(DataToSign.create(TEXT)).setPushNotification(PushNotification.create(TITLE, "")).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("PushNotification title or text cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_pushNotificationTextNull() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL).setDataToSign(DataToSign.create(TEXT)).setPushNotification(PushNotification.create(TITLE, null)).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("PushNotification title or text cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_pushNotificationTitleEmpty() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL).setDataToSign(DataToSign.create(TEXT)).setPushNotification(PushNotification.create("", TEXT)).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("PushNotification title or text cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_pushNotificationTitleNull() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL).setDataToSign(DataToSign.create(TEXT)).setPushNotification(PushNotification.create(null, TEXT)).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("PushNotification title or text cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_emptyTitle() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL).setDataToSign(DataToSign.create(TEXT)).setTitle("").build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Title cannot be empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAddOrgId_requestNull() throws FrejaEidException {
        try {
            organisationIdClient.initiateAdd(null);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Request cannot be null value.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAddOrgId_relyingPartyIdEmpty() throws FrejaEidException {
        try {
            organisationIdClient.initiateAdd(InitiateAddOrganisationIdRequest.createCustom().setEmailAndOrganisationId(EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER)).setRelyingPartyId("").build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("RelyingPartyId cannot be empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAddOrgId_userInfoEmpty() throws FrejaEidException {
        try {
            organisationIdClient.initiateAdd(InitiateAddOrganisationIdRequest.createCustom().setEmailAndOrganisationId("", OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER)).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("UserInfo cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAddOrgId_userInfoNull() throws FrejaEidException {
        try {
            organisationIdClient.initiateAdd(InitiateAddOrganisationIdRequest.createCustom().setEmailAndOrganisationId(null, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER)).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("UserInfo cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAddOrgId_organisationIdTitleNull() throws FrejaEidException {
        try {
            organisationIdClient.initiateAdd(InitiateAddOrganisationIdRequest.createCustom().setEmailAndOrganisationId(EMAIL, OrganisationId.create(null, IDENTIFIER_NAME, IDENTIFIER)).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("OrganisationIdTitle cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAddOrgId_organisationIdEmpty() throws FrejaEidException {
        try {
            organisationIdClient.initiateAdd(InitiateAddOrganisationIdRequest.createCustom().setEmailAndOrganisationId(EMAIL, OrganisationId.create("", IDENTIFIER_NAME, IDENTIFIER)).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("OrganisationIdTitle cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAddOrgId_idnetifierNameNull() throws FrejaEidException {
        try {
            organisationIdClient.initiateAdd(InitiateAddOrganisationIdRequest.createCustom().setEmailAndOrganisationId(EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE, null, IDENTIFIER)).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("IdentifierName cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAddOrgId_idnetifierNameEmpty() throws FrejaEidException {
        try {
            organisationIdClient.initiateAdd(InitiateAddOrganisationIdRequest.createCustom().setEmailAndOrganisationId(EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE, "", IDENTIFIER)).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("IdentifierName cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAddOrgId_idnetifierNull() throws FrejaEidException {
        try {
            organisationIdClient.initiateAdd(InitiateAddOrganisationIdRequest.createCustom().setEmailAndOrganisationId(EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, null)).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Identifier cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAddOrgId_idnetifierEmpty() throws FrejaEidException {
        try {
            organisationIdClient.initiateAdd(InitiateAddOrganisationIdRequest.createCustom().setEmailAndOrganisationId(EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, "")).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Identifier cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void cancelAddOrgIdRequest_missingReference_expectError() throws FrejaEidException {
        try {
            organisationIdClient.cancelAdd(CancelAddOrganisationIdRequest.create(""));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Reference cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void cancelAddOrgIdRequest_referenceNull_expectError() throws FrejaEidException {
        try {
            organisationIdClient.cancelAdd(CancelAddOrganisationIdRequest.create(null));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Reference cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void cancelAddOrgIdRequest_relyingPartyIdEmpty_expectError() throws FrejaEidException {
        try {
            organisationIdClient.cancelAdd(CancelAddOrganisationIdRequest.create(REFERENCE, ""));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("RelyingPartyId cannot be empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void cancelAddOrgIdRequest_requestNull_expectError() throws FrejaEidException {
        try {
            organisationIdClient.cancelAdd(null);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Request cannot be null value.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getOrgIdResult_missingReference_expectError() throws FrejaEidException {
        try {
            organisationIdClient.getResult(OrganisationIdResultRequest.create(""));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Reference cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getOrgIdResult_referenceNull_expectError() throws FrejaEidException {
        try {
            organisationIdClient.getResult(OrganisationIdResultRequest.create(null));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Reference cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getOrgIdResult_requestNull_expectError() throws FrejaEidException {
        try {
            organisationIdClient.getResult(null);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Request cannot be null value.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getOrgIdResult_relyingPartyIdEmpty_expectError() throws FrejaEidException {
        try {
            organisationIdClient.getResult(OrganisationIdResultRequest.create(REFERENCE, ""));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("RelyingPartyId cannot be empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void deleteOrgId_emptyIdentifier_expectError() throws FrejaEidException {
        try {
            organisationIdClient.delete(DeleteOrganisationIdRequest.create(""));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Identifier cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void deleteOrgId_nullIdentifier_expectError() throws FrejaEidException {
        try {
            organisationIdClient.delete(DeleteOrganisationIdRequest.create(null));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Identifier cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void deleteOrgId_emptyRelyingPartyId_expectError() throws FrejaEidException {
        try {
            organisationIdClient.delete(DeleteOrganisationIdRequest.create(IDENTIFIER, ""));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("RelyingPartyId cannot be empty.", ex.getLocalizedMessage());
        }
    }

}
