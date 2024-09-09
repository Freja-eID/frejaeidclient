package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResultsRequest;
import com.verisec.frejaeid.client.beans.general.*;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResults;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResultRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResult;
import com.verisec.frejaeid.client.beans.covidcertificate.CovidCertificates;
import com.verisec.frejaeid.client.beans.covidcertificate.Vaccines;
import com.verisec.frejaeid.client.client.api.AuthenticationClientApi;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.*;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientPollingException;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import com.verisec.frejaeid.client.util.MethodUrl;
import com.verisec.frejaeid.client.util.RequestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthenticationClientGetResultTest {

    private final HttpServiceApi httpServiceMock = Mockito.mock(HttpServiceApi.class);

    private static final SsnUserInfo SSN = SsnUserInfo.create(Country.SWEDEN, "199207295578");
    private static final String REFERENCE = "123456789012345678";
    private static final String RELYING_PARTY_ID = "relyingPartyId";
    private static final BasicUserInfo BASIC_USER_INFO = new BasicUserInfo("John", "Fante");
    private static final String CUSTOM_IDENTIFIER = "vejofan";
    private static final String DETAILS = "Ask the dust";
    private static final String RELYING_PARTY_USER_ID = "relyingPartyUserId";
    private static final String DATE_OF_BIRTH = "1987-10-18";
    private static final String EMAIL_ADDRESS = "test@frejaeid.com";
    private static final String PHONE_NUMBER = "+46123456789";
    private static final String ORGANISATION_ID = "vealrad";
    private static OrganisationIdInfo ORGANISATION_ID_INFO;
    private static OrganisationIdInfo ORGANISATION_ID_INFO_WITH_ADDITIONAL_ATTRIBUTES;
    private static final List<AddressInfo> ADDRESSES = Arrays.asList(
            new AddressInfo(Country.SWEDEN, "city", "postCode", "address1", "address2", "address3", "1993-12-30",
                            AddressType.RESIDENTIAL, AddressSourceType.GOVERNMENT_REGISTRY));
    private static final List<Email> ALL_EMAIL_ADDRESSES = Arrays.asList(new Email(EMAIL_ADDRESS));
    private static final List<PhoneNumberInfo> ALL_PHONE_NUMBERS = Arrays.asList(new PhoneNumberInfo(PHONE_NUMBER));
    private static final Integer AGE = 35;
    private static final String PHOTO = "https://image-hashId/test";
    private static final DocumentInfo DOCUMENT_INFO =
            new DocumentInfo(DocumentType.PASSPORT, "123456789", Country.SWEDEN, "2050-01-01");
    private static final DocumentInfoWithPdf DOCUMENT_WITH_PDF =
            new DocumentInfoWithPdf(DocumentType.PASSPORT, "123456789", Country.SWEDEN, "2050-01-01", "Base64Pdf");
    private static final DocumentInfoWithPdf CHILDREN_DOCUMENT_WITH_PDF =
            new DocumentInfoWithPdf(DocumentType.PASSPORT, "987654321", Country.SWEDEN, "20240-01-01", "Base64Pdf");
    private static final String DOCUMENT_PHOTO = "Base64EncodedDocPhoto";
    private static final CovidCertificates COVID_CERTIFICATES =
            new CovidCertificates(new Vaccines("covidCertificate"), null, null, true);
    private static final NetworkInfo NETWORK_INFO = new NetworkInfo("123.45.6.7");
    private static RequestedAttributes REQUESTED_ATTRIBUTES;
    private AuthenticationClientApi authenticationClient;

    @BeforeClass
    public static void initTestData() {
        Map<String, String> organisationIdIssuerNames = new HashMap<>();
        organisationIdIssuerNames.put("EN", "Org ID issuer");
        organisationIdIssuerNames.put("SV", "Org ID issuer Swedish");
        ORGANISATION_ID_INFO =
                new OrganisationIdInfo("org_id", organisationIdIssuerNames, "org_id_issuer", null);
        OrganisationIdAttribute additionalOrgIdAttribute =
                OrganisationIdAttribute.create("key", "friendly name", "value");
        ORGANISATION_ID_INFO_WITH_ADDITIONAL_ATTRIBUTES =
                new OrganisationIdInfo("org_id_with_attributes", organisationIdIssuerNames, "org_id_issuer",
                                       Arrays.asList(additionalOrgIdAttribute));
        REQUESTED_ATTRIBUTES =
                new RequestedAttributes(BASIC_USER_INFO, CUSTOM_IDENTIFIER, SSN, null, DATE_OF_BIRTH,
                                        RELYING_PARTY_USER_ID, EMAIL_ADDRESS, ORGANISATION_ID, ADDRESSES,
                                        ALL_EMAIL_ADDRESSES, ALL_PHONE_NUMBERS, RegistrationLevel.EXTENDED,
                                        AGE, PHOTO, DOCUMENT_INFO, DOCUMENT_PHOTO,
                                        COVID_CERTIFICATES, ORGANISATION_ID_INFO,
                                        DOCUMENT_WITH_PDF, Arrays.asList(CHILDREN_DOCUMENT_WITH_PDF),
                                        NETWORK_INFO);
    }

    @Before
    public void initialiseClient() throws FrejaEidClientInternalException {
        authenticationClient = AuthenticationClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .setTransactionContext(TransactionContext.PERSONAL).build();
    }

    @Test
    public void getAuthenticationResult_relyingPartyIdNull_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        AuthenticationResultRequest authenticationResultRequest = AuthenticationResultRequest.create(REFERENCE);
        when(httpServiceMock.send(anyString(), any(RequestTemplate.class),
                                  any(RelyingPartyRequest.class),
                                  Mockito.eq(AuthenticationResult.class), (String) Mockito.isNull()))
                .thenReturn(new AuthenticationResult(REFERENCE, TransactionStatus.STARTED, DETAILS,
                                                     REQUESTED_ATTRIBUTES));
        AuthenticationResult response = authenticationClient.getResult(authenticationResultRequest);
        verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.AUTHENTICATION_GET_RESULT,
                                     RequestTemplate.AUTHENTICATION_RESULT_TEMPLATE,
                                     authenticationResultRequest, AuthenticationResult.class, null);
        assertEquals(REFERENCE, response.getAuthRef());
        assertEquals(TransactionStatus.STARTED, response.getStatus());
        assertEquals(DETAILS, response.getDetails());
        assertEquals(REQUESTED_ATTRIBUTES, response.getRequestedAttributes());
    }

    @Test
    public void getAuthenticationResultPersonal_success() throws FrejaEidClientInternalException, FrejaEidException {
        AuthenticationResultRequest authenticationResultRequest =
                AuthenticationResultRequest.create(REFERENCE, RELYING_PARTY_ID);
        when(httpServiceMock.send(anyString(), any(RequestTemplate.class),
                                  any(RelyingPartyRequest.class),
                                  Mockito.eq(AuthenticationResult.class), anyString()))
                .thenReturn(new AuthenticationResult(REFERENCE, TransactionStatus.STARTED, DETAILS,
                                                     REQUESTED_ATTRIBUTES));
        AuthenticationResult response = authenticationClient.getResult(authenticationResultRequest);
        verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.AUTHENTICATION_GET_RESULT,
                                     RequestTemplate.AUTHENTICATION_RESULT_TEMPLATE,
                                     authenticationResultRequest, AuthenticationResult.class, RELYING_PARTY_ID);
        assertEquals(REFERENCE, response.getAuthRef());
        assertEquals(TransactionStatus.STARTED, response.getStatus());
        assertEquals(DETAILS, response.getDetails());
        assertEquals(REQUESTED_ATTRIBUTES, response.getRequestedAttributes());
    }

    @Test
    public void getAuthenticationResultOrganisational_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        AuthenticationClientApi authenticationClient =
                AuthenticationClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                        .setHttpService(httpServiceMock)
                        .setTransactionContext(TransactionContext.ORGANISATIONAL).build();
        AuthenticationResultRequest authenticationResultRequest =
                AuthenticationResultRequest.create(REFERENCE, RELYING_PARTY_ID);
        when(httpServiceMock.send(anyString(), any(RequestTemplate.class),
                                  any(RelyingPartyRequest.class),
                                  Mockito.eq(AuthenticationResult.class), anyString()))
                .thenReturn(new AuthenticationResult(REFERENCE, TransactionStatus.APPROVED, DETAILS,
                                                     REQUESTED_ATTRIBUTES));
        AuthenticationResult response = authenticationClient.getResult(authenticationResultRequest);
        verify(httpServiceMock)
                .send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.ORGANISATION_AUTHENTICATION_GET_ONE_RESULT,
                      RequestTemplate.AUTHENTICATION_RESULT_TEMPLATE, authenticationResultRequest,
                      AuthenticationResult.class, RELYING_PARTY_ID);
        assertEquals(REFERENCE, response.getAuthRef());
        assertEquals(TransactionStatus.APPROVED, response.getStatus());
        assertEquals(DETAILS, response.getDetails());
        assertEquals(REQUESTED_ATTRIBUTES, response.getRequestedAttributes());
    }

    @Test
    public void getAuthenticationResultOrganisational_withAdditionalAttributes_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        RequestedAttributes requestedAttributes =
                new RequestedAttributes(BASIC_USER_INFO, CUSTOM_IDENTIFIER, SSN, null,
                                        DATE_OF_BIRTH, RELYING_PARTY_USER_ID,
                                        EMAIL_ADDRESS, ORGANISATION_ID, ADDRESSES,
                                        ALL_EMAIL_ADDRESSES, ALL_PHONE_NUMBERS,
                                        RegistrationLevel.EXTENDED, AGE, PHOTO,
                                        DOCUMENT_INFO, DOCUMENT_PHOTO, COVID_CERTIFICATES,
                                        ORGANISATION_ID_INFO_WITH_ADDITIONAL_ATTRIBUTES, DOCUMENT_WITH_PDF,
                                        Arrays.asList(CHILDREN_DOCUMENT_WITH_PDF),
                                        NETWORK_INFO);
        AuthenticationClientApi authenticationClient =
                AuthenticationClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                        .setHttpService(httpServiceMock)
                        .setTransactionContext(TransactionContext.ORGANISATIONAL).build();
        AuthenticationResultRequest authenticationResultRequest =
                AuthenticationResultRequest.create(REFERENCE, RELYING_PARTY_ID);
        when(httpServiceMock.send(anyString(), any(RequestTemplate.class),
                                  any(RelyingPartyRequest.class),
                                  Mockito.eq(AuthenticationResult.class), anyString()))
                .thenReturn(new AuthenticationResult(REFERENCE, TransactionStatus.APPROVED, DETAILS,
                                                     requestedAttributes));
        AuthenticationResult response = authenticationClient.getResult(authenticationResultRequest);
        verify(httpServiceMock)
                .send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.ORGANISATION_AUTHENTICATION_GET_ONE_RESULT,
                      RequestTemplate.AUTHENTICATION_RESULT_TEMPLATE, authenticationResultRequest,
                      AuthenticationResult.class, RELYING_PARTY_ID);
        assertEquals(REFERENCE, response.getAuthRef());
        assertEquals(TransactionStatus.APPROVED, response.getStatus());
        assertEquals(DETAILS, response.getDetails());
        assertEquals(requestedAttributes, response.getRequestedAttributes());
    }

    @Test
    public void getAuthenticationResult_invalidReference_expectInvalidReferenceError()
            throws FrejaEidClientInternalException, FrejaEidException {
        AuthenticationResultRequest getOneAuthenticationResultRequest =
                AuthenticationResultRequest.create(REFERENCE, RELYING_PARTY_ID);
        try {
            when(httpServiceMock.send(anyString(), any(RequestTemplate.class),
                                      any(RelyingPartyRequest.class),
                                      Mockito.eq(AuthenticationResult.class), anyString()))
                    .thenThrow(new FrejaEidException(FrejaEidErrorCode.INVALID_REFERENCE.getMessage(),
                                                     FrejaEidErrorCode.INVALID_REFERENCE.getCode()));
            authenticationClient.getResult(getOneAuthenticationResultRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidException rpEx) {
            verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.AUTHENTICATION_GET_RESULT,
                                         RequestTemplate.AUTHENTICATION_RESULT_TEMPLATE,
                                         getOneAuthenticationResultRequest, AuthenticationResult.class,
                                         RELYING_PARTY_ID);
            assertEquals(1100, rpEx.getErrorCode());
            assertEquals("Invalid reference (for example, nonexistent or expired).", rpEx.getLocalizedMessage());
        }
    }

    @Test
    public void pollForResult_relyingPartyIdNull_finalResponseRejected_success()
            throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException {
        AuthenticationResult expectedResponse =
                new AuthenticationResult(DETAILS, TransactionStatus.REJECTED, DETAILS, null);
        AuthenticationResultRequest authenticationResultRequest = AuthenticationResultRequest.create(REFERENCE);
        when(httpServiceMock.send(anyString(), any(RequestTemplate.class),
                                  any(RelyingPartyRequest.class),
                                  Mockito.eq(AuthenticationResult.class), (String) Mockito.isNull()))
                .thenReturn(expectedResponse);
        AuthenticationResult response = authenticationClient.pollForResult(authenticationResultRequest, 10000);
        verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.AUTHENTICATION_GET_RESULT,
                                     RequestTemplate.AUTHENTICATION_RESULT_TEMPLATE,
                                     authenticationResultRequest, AuthenticationResult.class, null);
        assertEquals(TransactionStatus.REJECTED, response.getStatus());
    }

    @Test
    public void pollForResult_relyingPartyIdNotNull_finalResponseRejected_success()
            throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException {
        AuthenticationResult expectedResponse =
                new AuthenticationResult(DETAILS, TransactionStatus.REJECTED, DETAILS, null);
        AuthenticationResultRequest authenticationResultRequest =
                AuthenticationResultRequest.create(REFERENCE, RELYING_PARTY_ID);
        when(httpServiceMock.send(anyString(), any(RequestTemplate.class),
                                  any(RelyingPartyRequest.class),
                                  Mockito.eq(AuthenticationResult.class), anyString()))
                .thenReturn(expectedResponse);
        AuthenticationResult response = authenticationClient
                .pollForResult(AuthenticationResultRequest.create(REFERENCE, RELYING_PARTY_ID), 10000);
        verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.AUTHENTICATION_GET_RESULT,
                                     RequestTemplate.AUTHENTICATION_RESULT_TEMPLATE,
                                     authenticationResultRequest, AuthenticationResult.class, RELYING_PARTY_ID);
        assertEquals(TransactionStatus.REJECTED, response.getStatus());
    }

    @Test
    public void pollForResult_requestTimeout_expectTimeoutError()
            throws FrejaEidClientInternalException, FrejaEidException {
        try {
            authenticationClient.pollForResult(AuthenticationResultRequest.create(REFERENCE), 2);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientPollingException ex) {
            assertEquals("A timeout of 2s was reached while sending request.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getAuthenticationResults_relyingPartyIdNull_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        AuthenticationResults expectedResponse = prepareResponse();
        AuthenticationResultsRequest authenticationResultsRequest = AuthenticationResultsRequest.create();
        when(httpServiceMock.send(anyString(), any(RequestTemplate.class),
                                  any(RelyingPartyRequest.class),
                                  Mockito.eq(AuthenticationResults.class), (String) Mockito.isNull()))
                .thenReturn(expectedResponse);
        getAuthenticationResults_success(authenticationResultsRequest);
    }

    @Test
    public void getAuthenticationResults_relyingPartyIdNotNull_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        AuthenticationResults expectedResponse = prepareResponse();
        AuthenticationResultsRequest authenticationResultsRequest =
                AuthenticationResultsRequest.create(RELYING_PARTY_ID);
        when(httpServiceMock.send(anyString(), any(RequestTemplate.class),
                                  any(RelyingPartyRequest.class),
                                  Mockito.eq(AuthenticationResults.class),
                                  anyString()))
                .thenReturn(expectedResponse);
        getAuthenticationResults_success(authenticationResultsRequest);
    }

    private void getAuthenticationResults_success(AuthenticationResultsRequest getAuthenticationResultsRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        List<AuthenticationResult> response = authenticationClient.getResults(getAuthenticationResultsRequest);
        verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.AUTHENTICATION_GET_RESULTS,
                                     RequestTemplate.AUTHENTICATION_RESULTS_TEMPLATE,
                                     getAuthenticationResultsRequest, AuthenticationResults.class,
                                     getAuthenticationResultsRequest.getRelyingPartyId());
        AuthenticationResult first = response.get(0);
        assertEquals(REFERENCE, first.getAuthRef());
        assertEquals(TransactionStatus.STARTED, first.getStatus());
        assertEquals(DETAILS, first.getDetails());
        assertEquals(REQUESTED_ATTRIBUTES, first.getRequestedAttributes());

        AuthenticationResult second = response.get(1);
        assertEquals(REFERENCE, second.getAuthRef());
        assertEquals(TransactionStatus.DELIVERED_TO_MOBILE, second.getStatus());
        assertEquals("test", second.getDetails());
        assertEquals("test", second.getRequestedAttributes().getCustomIdentifier());
    }

    private AuthenticationResults prepareResponse() {
        RequestedAttributes attributes1 =
                new RequestedAttributes(BASIC_USER_INFO, CUSTOM_IDENTIFIER, SSN, null, DATE_OF_BIRTH,
                                        RELYING_PARTY_USER_ID, EMAIL_ADDRESS, ORGANISATION_ID, ADDRESSES,
                                        ALL_EMAIL_ADDRESSES, ALL_PHONE_NUMBERS, RegistrationLevel.EXTENDED, AGE,
                                        PHOTO, DOCUMENT_INFO, DOCUMENT_PHOTO, COVID_CERTIFICATES,
                                        ORGANISATION_ID_INFO, DOCUMENT_WITH_PDF,
                                        Arrays.asList(CHILDREN_DOCUMENT_WITH_PDF),
                                        NETWORK_INFO);
        AuthenticationResult firstResponse =
                new AuthenticationResult(REFERENCE, TransactionStatus.STARTED, DETAILS, attributes1);
        RequestedAttributes attributes2 =
                new RequestedAttributes(null, "test", null, null, null, null, null, null, null, null,
                                        null, null, null, null, null, null, null, null, null, null, null);
        AuthenticationResult secondResponse =
                new AuthenticationResult(REFERENCE, TransactionStatus.DELIVERED_TO_MOBILE, "test", attributes2);
        List<AuthenticationResult> responses = new ArrayList<>();
        responses.add(firstResponse);
        responses.add(secondResponse);
        return new AuthenticationResults(responses);
    }
}
