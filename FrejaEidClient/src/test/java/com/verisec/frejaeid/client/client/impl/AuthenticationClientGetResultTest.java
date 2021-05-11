package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResultsRequest;
import com.verisec.frejaeid.client.beans.general.*;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResults;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResultRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResult;
import com.verisec.frejaeid.client.beans.greencertificate.GreenCertificates;
import com.verisec.frejaeid.client.beans.greencertificate.Vaccine;
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
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
    private static final List<AddressInfo> ADDRESSES = Arrays.asList(
            new AddressInfo(Country.SWEDEN, "city", "postCode", "address1", "address2", "address3", "1993-12-30",
                            AddressType.RESIDENTIAL, AddressSourceType.GOVERNMENT_REGISTRY));
    private static final List<Email> ALL_EMAIL_ADDRESSES = Arrays.asList(new Email(EMAIL_ADDRESS));
    private static final List<PhoneNumberInfo> ALL_PHONE_NUMBERS = Arrays.asList(new PhoneNumberInfo(PHONE_NUMBER));
    private static final Integer AGE = 35;
    private static final GreenCertificates GREEN_CERTIFICATES =
            new GreenCertificates(new Vaccine("greenCertificate"), null, null, true);
    private static final RequestedAttributes REQUESTED_ATTRIBUTES =
            new RequestedAttributes(BASIC_USER_INFO, CUSTOM_IDENTIFIER, SSN, null, DATE_OF_BIRTH,
                    RELYING_PARTY_USER_ID, EMAIL_ADDRESS, ORGANISATION_ID, ADDRESSES,
                    ALL_EMAIL_ADDRESSES, ALL_PHONE_NUMBERS, RegistrationLevel.EXTENDED, AGE, GREEN_CERTIFICATES);
    private AuthenticationClientApi authenticationClient;

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
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(AuthenticationResult.class), (String) Mockito.isNull()))
                .thenReturn(new AuthenticationResult(REFERENCE, TransactionStatus.STARTED, DETAILS,
                                                     REQUESTED_ATTRIBUTES));
        AuthenticationResult response = authenticationClient.getResult(authenticationResultRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.AUTHENTICATION_GET_RESULT,
                                             RequestTemplate.AUTHENTICATION_RESULT_TEMPLATE,
                                             authenticationResultRequest, AuthenticationResult.class, null);
        Assert.assertEquals(REFERENCE, response.getAuthRef());
        Assert.assertEquals(TransactionStatus.STARTED, response.getStatus());
        Assert.assertEquals(DETAILS, response.getDetails());
        Assert.assertEquals(REQUESTED_ATTRIBUTES, response.getRequestedAttributes());
    }

    @Test
    public void getAuthenticationResultPersonal_success() throws FrejaEidClientInternalException, FrejaEidException {
        AuthenticationResultRequest authenticationResultRequest =
                AuthenticationResultRequest.create(REFERENCE, RELYING_PARTY_ID);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(AuthenticationResult.class), Mockito.anyString()))
                .thenReturn(new AuthenticationResult(REFERENCE, TransactionStatus.STARTED, DETAILS,
                                                     REQUESTED_ATTRIBUTES));
        AuthenticationResult response = authenticationClient.getResult(authenticationResultRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.AUTHENTICATION_GET_RESULT,
                                             RequestTemplate.AUTHENTICATION_RESULT_TEMPLATE,
                                             authenticationResultRequest, AuthenticationResult.class, RELYING_PARTY_ID);
        Assert.assertEquals(REFERENCE, response.getAuthRef());
        Assert.assertEquals(TransactionStatus.STARTED, response.getStatus());
        Assert.assertEquals(DETAILS, response.getDetails());
        Assert.assertEquals(REQUESTED_ATTRIBUTES, response.getRequestedAttributes());
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
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(AuthenticationResult.class), Mockito.anyString()))
                .thenReturn(new AuthenticationResult(REFERENCE, TransactionStatus.STARTED, DETAILS,
                                                     REQUESTED_ATTRIBUTES));
        AuthenticationResult response = authenticationClient.getResult(authenticationResultRequest);
        Mockito.verify(httpServiceMock)
                .send(FrejaEnvironment.TEST.getUrl() + MethodUrl.ORGANISATION_AUTHENTICATION_GET_ONE_RESULT,
                      RequestTemplate.AUTHENTICATION_RESULT_TEMPLATE, authenticationResultRequest,
                      AuthenticationResult.class, RELYING_PARTY_ID);
        Assert.assertEquals(REFERENCE, response.getAuthRef());
        Assert.assertEquals(TransactionStatus.STARTED, response.getStatus());
        Assert.assertEquals(DETAILS, response.getDetails());
        Assert.assertEquals(REQUESTED_ATTRIBUTES, response.getRequestedAttributes());
    }

    @Test
    public void getAuthenticationResult_invalidReference_expectInvalidReferenceError()
            throws FrejaEidClientInternalException, FrejaEidException {
        AuthenticationResultRequest getOneAuthenticationResultRequest =
                AuthenticationResultRequest.create(REFERENCE, RELYING_PARTY_ID);
        try {
            Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                              Mockito.any(RelyingPartyRequest.class),
                                              Mockito.eq(AuthenticationResult.class), Mockito.anyString()))
                    .thenThrow(new FrejaEidException(FrejaEidErrorCode.INVALID_REFERENCE.getMessage(),
                                                     FrejaEidErrorCode.INVALID_REFERENCE.getCode()));
            authenticationClient.getResult(getOneAuthenticationResultRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidException rpEx) {
            Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.AUTHENTICATION_GET_RESULT,
                                                 RequestTemplate.AUTHENTICATION_RESULT_TEMPLATE,
                                                 getOneAuthenticationResultRequest, AuthenticationResult.class,
                                                 RELYING_PARTY_ID);
            Assert.assertEquals(1100, rpEx.getErrorCode());
            Assert.assertEquals("Invalid reference (for example, nonexistent or expired).", rpEx.getLocalizedMessage());
        }
    }

    @Test
    public void pollForResult_relyingPartyIdNull_finalResponseRejected_success()
            throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException {
        AuthenticationResult expectedResponse =
                new AuthenticationResult(DETAILS, TransactionStatus.REJECTED, DETAILS, null);
        AuthenticationResultRequest authenticationResultRequest = AuthenticationResultRequest.create(REFERENCE);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(AuthenticationResult.class), (String) Mockito.isNull()))
                .thenReturn(expectedResponse);
        AuthenticationResult response = authenticationClient.pollForResult(authenticationResultRequest, 10000);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.AUTHENTICATION_GET_RESULT,
                                             RequestTemplate.AUTHENTICATION_RESULT_TEMPLATE,
                                             authenticationResultRequest, AuthenticationResult.class, null);
        Assert.assertEquals(TransactionStatus.REJECTED, response.getStatus());
    }

    @Test
    public void pollForResult_relyingPartyIdNotNull_finalResponseRejected_success()
            throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException {
        AuthenticationResult expectedResponse =
                new AuthenticationResult(DETAILS, TransactionStatus.REJECTED, DETAILS, null);
        AuthenticationResultRequest authenticationResultRequest =
                AuthenticationResultRequest.create(REFERENCE, RELYING_PARTY_ID);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(AuthenticationResult.class), Mockito.anyString()))
                .thenReturn(expectedResponse);
        AuthenticationResult response = authenticationClient
                .pollForResult(AuthenticationResultRequest.create(REFERENCE, RELYING_PARTY_ID), 10000);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.AUTHENTICATION_GET_RESULT,
                                             RequestTemplate.AUTHENTICATION_RESULT_TEMPLATE,
                                             authenticationResultRequest, AuthenticationResult.class, RELYING_PARTY_ID);
        Assert.assertEquals(TransactionStatus.REJECTED, response.getStatus());
    }

    @Test
    public void pollForResult_requestTimeout_expectTimeoutError()
            throws FrejaEidClientInternalException, FrejaEidException {
        try {
            authenticationClient.pollForResult(AuthenticationResultRequest.create(REFERENCE), 2);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientPollingException ex) {
            Assert.assertEquals("A timeout of 2s was reached while sending request.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getAuthenticationResults_relyingPartyIdNull_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        AuthenticationResults expectedResponse = prepareResponse();
        AuthenticationResultsRequest authenticationResultsRequest = AuthenticationResultsRequest.create();
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
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
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(AuthenticationResults.class),
                                          Mockito.anyString()))
                .thenReturn(expectedResponse);
        getAuthenticationResults_success(authenticationResultsRequest);
    }

    private void getAuthenticationResults_success(AuthenticationResultsRequest getAuthenticationResultsRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        List<AuthenticationResult> response = authenticationClient.getResults(getAuthenticationResultsRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.AUTHENTICATION_GET_RESULTS,
                                             RequestTemplate.AUTHENTICATION_RESULTS_TEMPLATE,
                                             getAuthenticationResultsRequest, AuthenticationResults.class,
                                             getAuthenticationResultsRequest.getRelyingPartyId());
        AuthenticationResult first = response.get(0);
        Assert.assertEquals(REFERENCE, first.getAuthRef());
        Assert.assertEquals(TransactionStatus.STARTED, first.getStatus());
        Assert.assertEquals(DETAILS, first.getDetails());
        Assert.assertEquals(REQUESTED_ATTRIBUTES, first.getRequestedAttributes());

        AuthenticationResult second = response.get(1);
        Assert.assertEquals(REFERENCE, second.getAuthRef());
        Assert.assertEquals(TransactionStatus.DELIVERED_TO_MOBILE, second.getStatus());
        Assert.assertEquals("test", second.getDetails());
        Assert.assertEquals("test", second.getRequestedAttributes().getCustomIdentifier());
    }

    private AuthenticationResults prepareResponse() {
        RequestedAttributes attributes1 =
                new RequestedAttributes(BASIC_USER_INFO, CUSTOM_IDENTIFIER, SSN, null, DATE_OF_BIRTH,
                                        RELYING_PARTY_USER_ID, EMAIL_ADDRESS, ORGANISATION_ID, ADDRESSES,
                                        ALL_EMAIL_ADDRESSES, ALL_PHONE_NUMBERS, RegistrationLevel.EXTENDED, AGE,
                        GREEN_CERTIFICATES);
        AuthenticationResult firstResponse =
                new AuthenticationResult(REFERENCE, TransactionStatus.STARTED, DETAILS, attributes1);
        RequestedAttributes attributes2 =
                new RequestedAttributes(null, "test", null, null, null, null, null, null, null, null, null, null, null,
                                        null);
        AuthenticationResult secondResponse =
                new AuthenticationResult(REFERENCE, TransactionStatus.DELIVERED_TO_MOBILE, "test", attributes2);
        List<AuthenticationResult> responses = new ArrayList<>();
        responses.add(firstResponse);
        responses.add(secondResponse);
        return new AuthenticationResults(responses);
    }
}
