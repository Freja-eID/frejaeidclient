package com.verisec.frejaeid.client.service;

import com.verisec.frejaeid.client.beans.authentication.cancel.CancelAuthenticationRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResultRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResultsRequest;
import com.verisec.frejaeid.client.beans.authentication.init.InitiateAuthenticationRequest;
import com.verisec.frejaeid.client.beans.custodianship.get.GetUserCustodianshipStatusRequest;
import com.verisec.frejaeid.client.beans.general.OrganisationId;
import com.verisec.frejaeid.client.beans.general.OrganisationIdAttribute;
import com.verisec.frejaeid.client.beans.organisationid.cancel.CancelAddOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.delete.DeleteOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.get.OrganisationIdResultRequest;
import com.verisec.frejaeid.client.beans.organisationid.getall.GetAllOrganisationIdUsersRequest;
import com.verisec.frejaeid.client.beans.organisationid.init.InitiateAddOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.update.UpdateOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.sign.cancel.CancelSignRequest;
import com.verisec.frejaeid.client.beans.sign.get.SignResultRequest;
import com.verisec.frejaeid.client.beans.sign.get.SignResultsRequest;
import com.verisec.frejaeid.client.beans.sign.init.DataToSign;
import com.verisec.frejaeid.client.beans.sign.init.InitiateSignRequest;
import com.verisec.frejaeid.client.beans.sign.init.PushNotification;
import com.verisec.frejaeid.client.beans.usermanagement.customidentifier.delete.DeleteCustomIdentifierRequest;
import com.verisec.frejaeid.client.beans.usermanagement.customidentifier.set.SetCustomIdentifierRequest;
import com.verisec.frejaeid.client.client.api.*;
import com.verisec.frejaeid.client.client.impl.*;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.*;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class RequestValidationServiceTest {

    private static final String ORGANISATION_ID_TITLE = "OrganisationId title";
    private static final String IDENTIFIER_NAME = "Identifier name";
    private static final String IDENTIFIER = "identifier";
    private static final String TEXT = "text";
    private static final String TITLE = "title";
    private static final String EMAIL = "email";
    private static final String PHONE_NUM = "phoneNum";
    private static final String REFERENCE = "reference";
    private static final String ORG_ID_ISSUER = "orgIdIssuer";
    private static final String RELYING_PARTY_ID = "relyingPartyId";
    private static final String USER_COUNTRY_ID_AND_CRN = "SE12345678";
    private static final List<OrganisationIdAttribute> ADDITIONAL_ATTRIBUTES =
            Arrays.asList(OrganisationIdAttribute.create("key", "friendly name", "value"),
                          OrganisationIdAttribute.create("attribute_id", "attribute name", "attribute value"));

    private static AuthenticationClientApi authenticationClient;
    private static SignClientApi signClient;
    private static CustomIdentifierClientApi customIdentifierClient;
    private static OrganisationIdClientApi organisationIdClient;
    private static CustodianshipClientApi custodianshipClient;

    @BeforeClass
    public static void initialization() throws FrejaEidClientInternalException {
        authenticationClient = AuthenticationClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setTestModeServerCustomUrl("test").setTransactionContext(TransactionContext.PERSONAL).build();
        signClient = SignClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setTestModeServerCustomUrl("test").setTransactionContext(TransactionContext.PERSONAL).build();
        customIdentifierClient = CustomIdentifierClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setTestModeServerCustomUrl("test").build();
        organisationIdClient = OrganisationIdClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setTestModeServerCustomUrl("test").build();
        custodianshipClient = CustodianshipClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setTestModeServerCustomUrl("test").setTransactionContext(TransactionContext.PERSONAL).build();
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
            authenticationClient.initiate(InitiateAuthenticationRequest.createCustom().setEmail(EMAIL)
                                                  .setRelyingPartyId("").build());
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
            authenticationClient.initiate(InitiateAuthenticationRequest.createCustom()
                                                  .setEmail(EMAIL).setAttributesToReturn().build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("RequestedAttributes cannot be empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAuth_perosnal_setOrgId() throws FrejaEidException {
        try {
            authenticationClient.initiate(InitiateAuthenticationRequest.createCustom()
                                                  .setOrganisationId(IDENTIFIER).setAttributesToReturn().build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("UserInfoType ORG ID cannot be used in personal context.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAuth_orgIdIssuerWrongValue() throws FrejaEidException {
        try {
            authenticationClient.initiate(InitiateAuthenticationRequest.createCustom().setEmail(EMAIL)
                                                  .setOrgIdIssuer(ORG_ID_ISSUER).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("OrgIdIssuer unsupported value. OrgIdIssuer must be null/empty or <ANY>",
                                ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAuth_userConfirmationMethod_wrongMinRegLevel() throws FrejaEidException {
        try {
            authenticationClient.initiate(
                    InitiateAuthenticationRequest.createCustom().setEmail(EMAIL)
                            .setMinRegistrationLevel(MinRegistrationLevel.BASIC)
                            .setUserConfirmationMethod(UserConfirmationMethod.DEFAULT_AND_FACE)
                            .build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("For the chosen userConfirmationMethod you must set "
                                        + "minRegistrationLevel to at least EXTENDED", ex.getLocalizedMessage());
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
            customIdentifierClient.set(SetCustomIdentifierRequest.createCustom()
                                               .setPhoneNumberAndCustomIdentifier(PHONE_NUM, IDENTIFIER)
                                               .setRelyingPartyId("").build());
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
            signClient.getResult(SignResultRequest.create(null));
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
    public void getSignResults_requestNull_expectError() throws FrejaEidException {
        try {
            signClient.getResults(null);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Request cannot be null value.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getSignResults_relyingPartyIdEmpty_expectError()
            throws FrejaEidException {
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
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL)
                                        .setDataToSign(DataToSign.create(TEXT)).setRelyingPartyId("").build());
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
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL)
                                        .setDataToSign(DataToSign.create(null)).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("DataToSign cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_dataToSignTextEmpty() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL)
                                        .setDataToSign(DataToSign.create("")).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("DataToSign cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_pushNotificationTextEmpty() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL)
                                        .setDataToSign(DataToSign.create(TEXT))
                                        .setPushNotification(PushNotification.create(TITLE, "")).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("PushNotification title or text cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_pushNotificationTextNull() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL)
                                        .setDataToSign(DataToSign.create(TEXT))
                                        .setPushNotification(PushNotification.create(TITLE, null)).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("PushNotification title or text cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_pushNotificationTitleEmpty() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL)
                                        .setDataToSign(DataToSign.create(TEXT))
                                        .setPushNotification(PushNotification.create("", TEXT)).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("PushNotification title or text cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_pushNotificationTitleNull() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL)
                                        .setDataToSign(DataToSign.create(TEXT))
                                        .setPushNotification(PushNotification.create(null, TEXT)).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("PushNotification title or text cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_emptyTitle() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL)
                                        .setDataToSign(DataToSign.create(TEXT)).setTitle("").build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Title cannot be empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_advancedSignatureTypeBasicRegistration_expectFail() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL)
                                        .setDataToSign(DataToSign.create(TEXT), SignatureType.XML_MINAMEDDELANDEN)
                                        .setMinRegistrationLevel(MinRegistrationLevel.BASIC).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Advanced signature type request requires registration levels above BASIC.",
                                ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_signatureTypeAndDataTypeMismatch_expectFail() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL)
                                        .setDataToSign(DataToSign.create(TEXT), SignatureType.EXTENDED)
                                        .setMinRegistrationLevel(MinRegistrationLevel.PLUS).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("DataToSignType and SignatureType mismatch.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initSign_advancedSignatureTypeBadRequestedAttributes_expectFail() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL)
                                        .setDataToSign(DataToSign.create(TEXT), SignatureType.XML_MINAMEDDELANDEN)
                                        .setMinRegistrationLevel(MinRegistrationLevel.PLUS).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Sign transaction with an advanced signature type requires SSN and BasicUserInfo in its RequestedAttributes.",
                                ex.getLocalizedMessage());
        }
    }


    @Test
    public void initSign_orgIdIssuerWrongValue() throws FrejaEidException {
        try {
            signClient.initiate(InitiateSignRequest.createCustom().setEmail(EMAIL)
                                        .setDataToSign(DataToSign.create(TEXT))
                                        .setOrgIdIssuer(ORG_ID_ISSUER).build());
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("OrgIdIssuer unsupported value. OrgIdIssuer must be null/empty or <ANY>",
                                ex.getLocalizedMessage());
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
            InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest =
                    InitiateAddOrganisationIdRequest.createCustom()
                            .setEmailAndOrganisationId(
                                    EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER))
                            .setRelyingPartyId("").build();
            organisationIdClient.initiateAdd(initiateAddOrganisationIdRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("RelyingPartyId cannot be empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAddOrgId_userInfoEmpty() throws FrejaEidException {
        try {
            InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest =
                    InitiateAddOrganisationIdRequest.createCustom()
                            .setEmailAndOrganisationId(
                                    "", OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER))
                            .build();
            organisationIdClient.initiateAdd(initiateAddOrganisationIdRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("UserInfo cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAddOrgId_userInfoNull() throws FrejaEidException {
        try {
            InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest =
                    InitiateAddOrganisationIdRequest.createCustom()
                            .setEmailAndOrganisationId(
                                    null, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER))
                            .build();
            organisationIdClient.initiateAdd(initiateAddOrganisationIdRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("UserInfo cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAddOrgId_organisationIdTitleNull() throws FrejaEidException {
        try {
            InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest =
                    InitiateAddOrganisationIdRequest.createCustom()
                            .setEmailAndOrganisationId(EMAIL, OrganisationId.create(null, IDENTIFIER_NAME, IDENTIFIER))
                            .build();
            organisationIdClient.initiateAdd(initiateAddOrganisationIdRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("OrganisationIdTitle cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAddOrgId_organisationIdEmpty() throws FrejaEidException {
        try {
            InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest =
                    InitiateAddOrganisationIdRequest.createCustom()
                            .setEmailAndOrganisationId(EMAIL, OrganisationId.create("", IDENTIFIER_NAME, IDENTIFIER))
                            .build();
            organisationIdClient.initiateAdd(initiateAddOrganisationIdRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("OrganisationIdTitle cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAddOrgId_identifierNameNull() throws FrejaEidException {
        try {
            InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest =
                    InitiateAddOrganisationIdRequest.createCustom()
                            .setEmailAndOrganisationId(
                                    EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE, null, IDENTIFIER))
                            .build();
            organisationIdClient.initiateAdd(initiateAddOrganisationIdRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("IdentifierName cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAddOrgId_identifierNameEmpty() throws FrejaEidException {
        try {
            InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest =
                    InitiateAddOrganisationIdRequest.createCustom()
                            .setEmailAndOrganisationId(
                                    EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE, "", IDENTIFIER))
                            .build();
            organisationIdClient.initiateAdd(initiateAddOrganisationIdRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("IdentifierName cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAddOrgId_identifierNull() throws FrejaEidException {
        try {
            InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest =
                    InitiateAddOrganisationIdRequest.createCustom()
                            .setEmailAndOrganisationId(
                                    EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, null))
                            .build();
            organisationIdClient.initiateAdd(initiateAddOrganisationIdRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Identifier cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void initAddOrgId_identifierEmpty() throws FrejaEidException {
        try {
            InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest =
                    InitiateAddOrganisationIdRequest.createCustom()
                            .setEmailAndOrganisationId(
                                    EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, ""))
                            .build();
            organisationIdClient.initiateAdd(initiateAddOrganisationIdRequest);
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

    @Test
    public void getAllOrgIdUsers_emptyRelyingPartyId_expectError() throws FrejaEidException {
        try {
            organisationIdClient.getAllUsers(GetAllOrganisationIdUsersRequest.create(""));
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("RelyingPartyId cannot be empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getCustodianshipStatus_emptyRelyingPartyId_expectError() throws FrejaEidException {
        try {
            GetUserCustodianshipStatusRequest getUserCustodianshipStatusRequest =
                    GetUserCustodianshipStatusRequest.create(USER_COUNTRY_ID_AND_CRN, "");
            custodianshipClient.getUserCustodianshipStatus(getUserCustodianshipStatusRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("RelyingPartyId cannot be empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getCustodianshipStatus_emptyCountryIdAndCrn_expectError() throws FrejaEidException {
        try {
            GetUserCustodianshipStatusRequest getUserCustodianshipStatusRequest =
                    new GetUserCustodianshipStatusRequest(null, RELYING_PARTY_ID);
            custodianshipClient.getUserCustodianshipStatus(getUserCustodianshipStatusRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Invalid user country ID and CRN. Parameter missing or country code different than SE.",
                                ex.getLocalizedMessage());
        }
    }

    @Test
    public void updateOrganisationIdRequest_emptyIdentifier() {
        try {
            UpdateOrganisationIdRequest updateOrganisationIdRequest =
                    UpdateOrganisationIdRequest.create("", ADDITIONAL_ATTRIBUTES);
            organisationIdClient.update(updateOrganisationIdRequest);
            Assert.fail("Test unexpectedly passed.");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Identifier cannot be null or empty.", ex.getLocalizedMessage());
        } catch (FrejaEidException e) {
            Assert.fail("Unexpected error occurred.");
        }
    }

    @Test
    public void updateOrganisationIdRequest_emptyRelyingPartyId() {
        try {
            UpdateOrganisationIdRequest updateOrganisationIdRequest =
                    UpdateOrganisationIdRequest.create(IDENTIFIER, ADDITIONAL_ATTRIBUTES, "");
            organisationIdClient.update(updateOrganisationIdRequest);
            Assert.fail("Test unexpectedly passed.");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("RelyingPartyId cannot be empty.", ex.getLocalizedMessage());
        } catch (FrejaEidException e) {
            Assert.fail("Unexpected error occurred.");
        }
    }

}
