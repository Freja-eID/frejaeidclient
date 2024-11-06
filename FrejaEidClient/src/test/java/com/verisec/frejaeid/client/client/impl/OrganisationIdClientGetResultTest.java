package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.organisationid.get.OrganisationIdResult;
import com.verisec.frejaeid.client.beans.organisationid.get.OrganisationIdResultRequest;
import com.verisec.frejaeid.client.client.api.OrganisationIdClientApi;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.FrejaEidErrorCode;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.TransactionStatus;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientPollingException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import com.verisec.frejaeid.client.util.MethodUrl;
import com.verisec.frejaeid.client.util.RequestTemplate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class OrganisationIdClientGetResultTest {

    private final HttpServiceApi httpServiceMock = Mockito.mock(HttpServiceApi.class);
    private static final String RELYING_PARTY_ID = "relyingPartyId";
    private static final String REFERENCE = "123456789123456789";
    private static final String DETAILS = "This is sign transaction";
    private static final String FREJA_COOKIE = "frejaCookie";
    private OrganisationIdClientApi organisationIdClient;

    @Before
    public void initialiseClient() throws FrejaEidClientInternalException {
        organisationIdClient = OrganisationIdClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .build();
    }

    @Test
    public void getOrganisationIdResult_relyingPartyIdNull_expectSuccess()
            throws FrejaEidClientInternalException, FrejaEidException {
        OrganisationIdResultRequest organisationIdResultRequest = OrganisationIdResultRequest.create(REFERENCE);
        OrganisationIdResult expectedResponse = new OrganisationIdResult(REFERENCE, TransactionStatus.STARTED, DETAILS,
                                                                         FREJA_COOKIE);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(OrganisationIdResult.class), (String) Mockito.isNull()))
                .thenReturn(expectedResponse);
        OrganisationIdResult response = organisationIdClient.getResult(organisationIdResultRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl()
                + MethodUrl.ORGANISATION_ID_GET_RESULT,
                                             RequestTemplate.ORGANISATION_ID_RESULT_TEMPLATE,
                                             organisationIdResultRequest, OrganisationIdResult.class, null);
        assertEquals(REFERENCE, response.getOrgIdRef());
        assertEquals(TransactionStatus.STARTED, response.getStatus());
        assertEquals(DETAILS, response.getDetails());
        assertEquals(FREJA_COOKIE, response.getFrejaCookie());
    }

    @Test
    public void getOrganisationIdResult_expectSuccess() throws FrejaEidClientInternalException, FrejaEidException {
        OrganisationIdResultRequest organisationIdResultRequest =
                OrganisationIdResultRequest.create(REFERENCE, RELYING_PARTY_ID);
        OrganisationIdResult expectedResponse = new OrganisationIdResult(REFERENCE, TransactionStatus.STARTED, DETAILS,
                                                                         FREJA_COOKIE);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(OrganisationIdResult.class), Mockito.anyString()))
                .thenReturn(expectedResponse);
        OrganisationIdResult response = organisationIdClient.getResult(organisationIdResultRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl()
                + MethodUrl.ORGANISATION_ID_GET_RESULT,
                                             RequestTemplate.ORGANISATION_ID_RESULT_TEMPLATE,
                                             organisationIdResultRequest, OrganisationIdResult.class, RELYING_PARTY_ID);
        assertEquals(REFERENCE, response.getOrgIdRef());
        assertEquals(TransactionStatus.STARTED, response.getStatus());
        assertEquals(DETAILS, response.getDetails());
        assertEquals(FREJA_COOKIE, response.getFrejaCookie());
    }

    @Test
    public void getOrganisationIdResult_invalidReference_expectInvalidReferenceError()
            throws FrejaEidClientInternalException, FrejaEidException {
        OrganisationIdResultRequest organisationIdResultRequest =
                OrganisationIdResultRequest.create(REFERENCE, RELYING_PARTY_ID);
        try {
            FrejaEidException frejaEidException =
                    new FrejaEidException(FrejaEidErrorCode.INVALID_REFERENCE.getMessage(),
                                          FrejaEidErrorCode.INVALID_REFERENCE.getCode());
            Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                              Mockito.any(RelyingPartyRequest.class),
                                              Mockito.eq(OrganisationIdResult.class), Mockito.anyString()))
                    .thenThrow(frejaEidException);
            organisationIdClient.getResult(organisationIdResultRequest);
            fail("Test should throw exception!");
        } catch (FrejaEidException rpEx) {
            Mockito.verify(httpServiceMock)
                    .send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.ORGANISATION_ID_GET_RESULT,
                          RequestTemplate.ORGANISATION_ID_RESULT_TEMPLATE, organisationIdResultRequest,
                          OrganisationIdResult.class, RELYING_PARTY_ID);
            assertEquals(1100, rpEx.getErrorCode());
            assertEquals("Invalid reference (for example, nonexistent or expired).", rpEx.getLocalizedMessage());
        }
    }

    @Test
    public void pollForResult_finalResponseRejected_success()
            throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException {
        OrganisationIdResult expectedResponse =
                new OrganisationIdResult(REFERENCE, TransactionStatus.REJECTED, DETAILS, null);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(OrganisationIdResult.class), (String) Mockito.isNull()))
                .thenReturn(expectedResponse);
        OrganisationIdResultRequest organisationIdResultRequest = OrganisationIdResultRequest.create(REFERENCE);
        OrganisationIdResult response = organisationIdClient.pollForResult(organisationIdResultRequest, 10000);
        Mockito.verify(httpServiceMock)
                .send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.ORGANISATION_ID_GET_RESULT,
                      RequestTemplate.ORGANISATION_ID_RESULT_TEMPLATE, organisationIdResultRequest,
                      OrganisationIdResult.class, null);
        assertEquals(TransactionStatus.REJECTED, response.getStatus());
    }

    @Test
    public void getFinalOrganisationIdResponse_requestTimeout_expectTimeoutError()
            throws FrejaEidException, FrejaEidClientInternalException {
        try {
            organisationIdClient.pollForResult(OrganisationIdResultRequest.create(REFERENCE), 2);
            fail("Test should throw exception!");
        } catch (FrejaEidClientPollingException ex) {
            assertEquals("A timeout of 2s was reached while sending request.", ex.getLocalizedMessage());
        }
    }
}
