package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.covidcertificate.CovidCertificates;
import com.verisec.frejaeid.client.beans.general.*;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.covidcertificate.Vaccines;
import com.verisec.frejaeid.client.beans.sign.get.SignResult;
import com.verisec.frejaeid.client.beans.sign.get.SignResultRequest;
import com.verisec.frejaeid.client.beans.sign.get.SignResultsRequest;
import com.verisec.frejaeid.client.beans.sign.get.SignResults;
import com.verisec.frejaeid.client.client.api.SignClientApi;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.*;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
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

public class SignClientGetResultTest {

    private final HttpServiceApi httpServiceMock = Mockito.mock(HttpServiceApi.class);
    private static final String RELYING_PARTY_ID = "relyingPartyId";
    private static final String SIGN_REFERENCE = "123456789123456789";
    private static final String SIGN_DETAILS = "This is sign transaction";
    private static final String RELYING_PARTY_USER_ID = "relyingPartyUserId";
    private static final String EMAIL_ADDRESS = "test@frejaeid.com";
    private static final String PHONE_NUMBER = "+46123456789";
    private static final String ORGANISATION_ID = "orgId";
    private static OrganisationIdInfo ORGANISATION_ID_INFO;
    private static OrganisationIdInfo ORGANISATION_ID_INFO_WITH_ADDITIONAL_ATTRIBUTES;
    private static final List<AddressInfo> ADDRESSES = Arrays.asList(
            new AddressInfo(Country.SWEDEN, "city", "postCode", "address1", "address2", "address3", "1993-12-30",
                            AddressType.RESIDENTIAL, AddressSourceType.GOVERNMENT_REGISTRY));
    private static final List<Email> ALL_EMAIL_ADDRESSES = Arrays.asList(new Email(EMAIL_ADDRESS));
    private static final List<PhoneNumberInfo> ALL_PHONE_NUMBERS = Arrays.asList(new PhoneNumberInfo(PHONE_NUMBER));
    private static final Integer AGE = 35;
    private static final String PHOTO = "Base64EncodedAvatarPhoto";
    private static final DocumentInfo DOCUMENT_INFO =
            new DocumentInfo(DocumentType.PASSPORT, "123456789", Country.SWEDEN, "2050-01-01");
    private static final DocumentInfoWithPdf DOCUMENT_WITH_PDF =
            new DocumentInfoWithPdf(DocumentType.PASSPORT, "123456789", Country.SWEDEN, "2050-01-01", "Base64Pdf");
    private static final DocumentInfoWithPdf CHILDREN_DOCUMENT_WITH_PDF =
            new DocumentInfoWithPdf(DocumentType.PASSPORT, "987654321", Country.SWEDEN, "20240-01-01", "Base64Pdf");
    private static final String DOCUMENT_PHOTO = "Base64EncodedDocPhoto";
    protected static final CovidCertificates COVID_CERTIFICATES =
            new CovidCertificates(new Vaccines("covidCertificate"), null, null, true);
    private static final NetworkInfo NETWORK_INFO = new NetworkInfo("123.45.6.7");
    private static RequestedAttributes REQUESTED_ATTRIBUTES;
    private SignClientApi signClient;

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
                new RequestedAttributes(new BasicUserInfo("name", "surname"), "customIdentifier",
                                        SsnUserInfo.create(Country.SWEDEN, "ssn"), "integratorSpecificId", "1987-10-18",
                                        RELYING_PARTY_USER_ID, EMAIL_ADDRESS, ORGANISATION_ID, ADDRESSES,
                                        ALL_EMAIL_ADDRESSES, ALL_PHONE_NUMBERS, RegistrationLevel.EXTENDED,
                                        AGE, PHOTO, DOCUMENT_INFO, DOCUMENT_PHOTO,
                                        COVID_CERTIFICATES, ORGANISATION_ID_INFO, DOCUMENT_WITH_PDF,
                                        Arrays.asList(CHILDREN_DOCUMENT_WITH_PDF),
                                        NETWORK_INFO);
    }

    @Before
    public void initialiseClient() throws FrejaEidClientInternalException {
        signClient = SignClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .setTransactionContext(TransactionContext.PERSONAL).build();
    }

    @Test
    public void getSignResult_relyingPartyIdNull_expectSuccess()
            throws FrejaEidClientInternalException, FrejaEidException {
        SignResultRequest signResultsRequest = SignResultRequest.create(SIGN_REFERENCE);
        SignResult expectedResponse = new SignResult(SIGN_REFERENCE, TransactionStatus.STARTED, SIGN_DETAILS,
                                                     REQUESTED_ATTRIBUTES);
        when(httpServiceMock.send(anyString(), any(RequestTemplate.class),
                                  any(RelyingPartyRequest.class), Mockito.eq(SignResult.class),
                                  (String) Mockito.isNull())).thenReturn(expectedResponse);
        SignResult response = signClient.getResult(signResultsRequest);
        verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.SIGN_GET_RESULT,
                                     RequestTemplate.SIGN_RESULT_TEMPLATE, signResultsRequest,
                                     SignResult.class, null);
        assertEquals(SIGN_REFERENCE, response.getSignRef());
        assertEquals(TransactionStatus.STARTED, response.getStatus());
        assertEquals(SIGN_DETAILS, response.getDetails());
        assertEquals(REQUESTED_ATTRIBUTES, response.getRequestedAttributes());
    }

    @Test
    public void getSignResultPersonal_expectSuccess() throws FrejaEidClientInternalException, FrejaEidException {
        SignResultRequest signResultRequest = SignResultRequest.create(SIGN_REFERENCE, RELYING_PARTY_ID);
        SignResult expectedResponse =
                new SignResult(SIGN_REFERENCE, TransactionStatus.STARTED, SIGN_DETAILS, REQUESTED_ATTRIBUTES);
        when(httpServiceMock.send(anyString(), any(RequestTemplate.class),
                                  any(RelyingPartyRequest.class), Mockito.eq(SignResult.class),
                                  anyString())).thenReturn(expectedResponse);
        SignResult response = signClient.getResult(signResultRequest);
        verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.SIGN_GET_RESULT,
                                     RequestTemplate.SIGN_RESULT_TEMPLATE, signResultRequest,
                                     SignResult.class, RELYING_PARTY_ID);
        assertEquals(SIGN_REFERENCE, response.getSignRef());
        assertEquals(TransactionStatus.STARTED, response.getStatus());
        assertEquals(SIGN_DETAILS, response.getDetails());
        assertEquals(REQUESTED_ATTRIBUTES, response.getRequestedAttributes());
    }

    @Test
    public void getSignResultOrganisational_expectSuccess() throws FrejaEidClientInternalException, FrejaEidException {
        SignResultRequest signResultRequest = SignResultRequest.create(SIGN_REFERENCE, RELYING_PARTY_ID);
        SignResult expectedResponse =
                new SignResult(SIGN_REFERENCE, TransactionStatus.APPROVED, SIGN_DETAILS, REQUESTED_ATTRIBUTES);
        SignClientApi signClient = SignClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .setTransactionContext(TransactionContext.ORGANISATIONAL).build();
        when(httpServiceMock.send(anyString(), any(RequestTemplate.class),
                                  any(RelyingPartyRequest.class), Mockito.eq(SignResult.class),
                                  anyString())).thenReturn(expectedResponse);
        SignResult response = signClient.getResult(signResultRequest);
        verify(httpServiceMock)
                .send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.ORGANISATION_SIGN_GET_ONE_RESULT,
                      RequestTemplate.SIGN_RESULT_TEMPLATE, signResultRequest, SignResult.class, RELYING_PARTY_ID);
        assertEquals(SIGN_REFERENCE, response.getSignRef());
        assertEquals(TransactionStatus.APPROVED, response.getStatus());
        assertEquals(SIGN_DETAILS, response.getDetails());
        assertEquals(REQUESTED_ATTRIBUTES, response.getRequestedAttributes());
    }

    @Test
    public void getSignResultOrganisational_withAdditionalAttributes_expectSuccess() throws
            FrejaEidClientInternalException, FrejaEidException {
        RequestedAttributes requestedAttributes =
                new RequestedAttributes(new BasicUserInfo("name", "surname"), "customIdentifier",
                                        SsnUserInfo.create(Country.SWEDEN, "ssn"), null,
                                        "1987-10-18", RELYING_PARTY_USER_ID,
                                        EMAIL_ADDRESS, ORGANISATION_ID, ADDRESSES,
                                        ALL_EMAIL_ADDRESSES, ALL_PHONE_NUMBERS,
                                        RegistrationLevel.EXTENDED, AGE, PHOTO,
                                        DOCUMENT_INFO, DOCUMENT_PHOTO, COVID_CERTIFICATES,
                                        ORGANISATION_ID_INFO_WITH_ADDITIONAL_ATTRIBUTES, DOCUMENT_WITH_PDF,
                                        Arrays.asList(CHILDREN_DOCUMENT_WITH_PDF),
                                        NETWORK_INFO);
        SignResultRequest signResultRequest = SignResultRequest.create(SIGN_REFERENCE, RELYING_PARTY_ID);
        SignResult expectedResponse =
                new SignResult(SIGN_REFERENCE, TransactionStatus.APPROVED, SIGN_DETAILS, requestedAttributes);
        SignClientApi signClient = SignClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .setTransactionContext(TransactionContext.ORGANISATIONAL).build();
        when(httpServiceMock.send(anyString(), any(RequestTemplate.class),
                                  any(RelyingPartyRequest.class), Mockito.eq(SignResult.class),
                                  anyString())).thenReturn(expectedResponse);
        SignResult response = signClient.getResult(signResultRequest);
        verify(httpServiceMock)
                .send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.ORGANISATION_SIGN_GET_ONE_RESULT,
                      RequestTemplate.SIGN_RESULT_TEMPLATE, signResultRequest, SignResult.class, RELYING_PARTY_ID);
        assertEquals(SIGN_REFERENCE, response.getSignRef());
        assertEquals(TransactionStatus.APPROVED, response.getStatus());
        assertEquals(SIGN_DETAILS, response.getDetails());
        assertEquals(requestedAttributes, response.getRequestedAttributes());
    }

    @Test
    public void getSignResult_invalidReference_expectInvalidReferenceError()
            throws FrejaEidClientInternalException, FrejaEidException {
        SignResultRequest signResultRequest = SignResultRequest.create(SIGN_REFERENCE, RELYING_PARTY_ID);
        try {
            FrejaEidException frejaEidException = new FrejaEidException(
                    FrejaEidErrorCode.INVALID_REFERENCE.getMessage(), FrejaEidErrorCode.INVALID_REFERENCE.getCode());
            when(httpServiceMock.send(anyString(), any(RequestTemplate.class),
                                      any(RelyingPartyRequest.class), Mockito.eq(SignResult.class),
                                      anyString())).thenThrow(frejaEidException);
            signClient.getResult(signResultRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidException rpEx) {
            verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.SIGN_GET_RESULT,
                                         RequestTemplate.SIGN_RESULT_TEMPLATE, signResultRequest,
                                         SignResult.class, RELYING_PARTY_ID);
            assertEquals(1100, rpEx.getErrorCode());
            assertEquals("Invalid reference (for example, nonexistent or expired).", rpEx.getLocalizedMessage());
        }
    }

    @Test
    public void pollForResult_finalResponseRejected_success()
            throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException {
        SignResult expectedResponse = new SignResult(SIGN_REFERENCE, TransactionStatus.REJECTED, SIGN_DETAILS, null);
        when(httpServiceMock.send(anyString(), any(RequestTemplate.class),
                                  any(RelyingPartyRequest.class), Mockito.eq(SignResult.class),
                                  (String) Mockito.isNull())).thenReturn(expectedResponse);
        SignResultRequest signResultRequest = SignResultRequest.create(SIGN_REFERENCE);
        SignResult response = signClient.pollForResult(signResultRequest, 10000);
        verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.SIGN_GET_RESULT,
                                     RequestTemplate.SIGN_RESULT_TEMPLATE, signResultRequest,
                                     SignResult.class, null);
        assertEquals(TransactionStatus.REJECTED, response.getStatus());
    }

    @Test
    public void pollForResult_requestTimeout_expectTimeoutError()
            throws FrejaEidClientInternalException, FrejaEidException {
        try {
            signClient.pollForResult(SignResultRequest.create(SIGN_REFERENCE), 2);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientPollingException ex) {
            assertEquals("A timeout of 2s was reached while sending request.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void getSignResults_relyingPartyIdNull_expectSuccess()
            throws FrejaEidClientInternalException, FrejaEidException {
        SignResults expectedResponse = prepareResponse();
        when(httpServiceMock.send(anyString(), any(RequestTemplate.class),
                                  any(RelyingPartyRequest.class), Mockito.eq(SignResults.class),
                                  (String) Mockito.isNull())).thenReturn(expectedResponse);
        SignResultsRequest getSignResultsRequest = SignResultsRequest.create();
        getSignResults_success(getSignResultsRequest);
    }

    @Test
    public void getSignResults_relyingPartyIdNotNull_expectSuccess()
            throws FrejaEidClientInternalException, FrejaEidException {
        SignResults expectedResponse = prepareResponse();
        when(httpServiceMock.send(anyString(), any(RequestTemplate.class),
                                  any(RelyingPartyRequest.class), Mockito.eq(SignResults.class),
                                  anyString())).thenReturn(expectedResponse);
        SignResultsRequest signResultsRequest = SignResultsRequest.create(RELYING_PARTY_ID);
        getSignResults_success(signResultsRequest);
    }

    @Test
    public void getSignResults_expectError() throws FrejaEidClientInternalException, FrejaEidException {
        FrejaEidException frejaEidException =
                new FrejaEidException(FrejaEidErrorCode.UNKNOWN_RELYING_PARTY.getMessage(),
                                      FrejaEidErrorCode.UNKNOWN_RELYING_PARTY.getCode());
        when(httpServiceMock.send(anyString(), any(RequestTemplate.class),
                                  any(RelyingPartyRequest.class), Mockito.eq(SignResults.class),
                                  anyString()))
                .thenThrow(frejaEidException);
        SignResultsRequest signResultsRequest = SignResultsRequest.create(RELYING_PARTY_ID);
        try {
            signClient.getResults(signResultsRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidException rpEx) {
            verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.SIGN_GET_RESULTS,
                                         RequestTemplate.SIGN_RESULTS_TEMPLATE, signResultsRequest,
                                         SignResults.class, RELYING_PARTY_ID);
            assertEquals(1008, rpEx.getErrorCode());
            assertEquals("Unknown Relying party.", rpEx.getLocalizedMessage());
        }

    }

    private void getSignResults_success(SignResultsRequest signResultsRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        List<SignResult> response = signClient.getResults(signResultsRequest);
        verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.SIGN_GET_RESULTS,
                                     RequestTemplate.SIGN_RESULTS_TEMPLATE, signResultsRequest,
                                     SignResults.class, signResultsRequest.getRelyingPartyId());

        SignResult firstResponse = response.get(0);
        assertEquals(SIGN_REFERENCE, firstResponse.getSignRef());
        assertEquals(TransactionStatus.STARTED, firstResponse.getStatus());
        assertEquals(SIGN_DETAILS, firstResponse.getDetails());
        assertEquals(REQUESTED_ATTRIBUTES, firstResponse.getRequestedAttributes());

        SignResult secondResponse = response.get(1);
        assertEquals(SIGN_REFERENCE, secondResponse.getSignRef());
        assertEquals(TransactionStatus.DELIVERED_TO_MOBILE, secondResponse.getStatus());
        assertEquals("test", secondResponse.getDetails());
        Assert.assertNull(secondResponse.getRequestedAttributes());
    }

    private SignResults prepareResponse() {
        SignResult response1 =
                new SignResult(SIGN_REFERENCE, TransactionStatus.STARTED, SIGN_DETAILS, REQUESTED_ATTRIBUTES);
        SignResult response2 = new SignResult(SIGN_REFERENCE, TransactionStatus.DELIVERED_TO_MOBILE, "test", null);
        List<SignResult> responses = new ArrayList<>();
        responses.add(response1);
        responses.add(response2);
        return new SignResults(responses);
    }
}
