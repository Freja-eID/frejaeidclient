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
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class OrganisationIdClientGetResultTest {

    private final HttpServiceApi httpServiceMock = Mockito.mock(HttpServiceApi.class);
    private static final String RELYING_PARTY_ID = "relyingPartyId";
    private static final String REFERENCE = "123456789123456789";
    private static final String DETAILS = "This is sign transaction";

    @Test
    public void getOrganisationIdResult_relyingPartyIdNull_expectSuccess()
            throws FrejaEidClientInternalException, FrejaEidException {
        OrganisationIdResultRequest organisationIdResultRequest = OrganisationIdResultRequest.create(REFERENCE);
        OrganisationIdResult expectedResponse = new OrganisationIdResult(REFERENCE, TransactionStatus.STARTED, DETAILS);
        OrganisationIdClientApi organisationIdClient =
                OrganisationIdClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                        .setHttpService(httpServiceMock)
                        .build();
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(OrganisationIdResult.class), (String) Mockito.isNull()))
                .thenReturn(expectedResponse);
        OrganisationIdResult response = organisationIdClient.getResult(organisationIdResultRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.ORGANISATION_ID_GET_RESULT,
                                             RequestTemplate.ORGANISATION_ID_RESULT_TEMPLATE,
                                             organisationIdResultRequest, OrganisationIdResult.class, null);
        Assert.assertEquals(REFERENCE, response.getOrgIdRef());
        Assert.assertEquals(TransactionStatus.STARTED, response.getStatus());
        Assert.assertEquals(DETAILS, response.getDetails());
    }

    @Test
    public void getOrganisationIdResult_expectSuccess() throws FrejaEidClientInternalException, FrejaEidException {
        OrganisationIdResultRequest organisationIdResultRequest =
                OrganisationIdResultRequest.create(REFERENCE, RELYING_PARTY_ID);
        OrganisationIdResult expectedResponse = new OrganisationIdResult(REFERENCE, TransactionStatus.STARTED, DETAILS);
        OrganisationIdClientApi organisationIdClient =
                OrganisationIdClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                        .setHttpService(httpServiceMock)
                        .build();
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(OrganisationIdResult.class), Mockito.anyString()))
                .thenReturn(expectedResponse);
        OrganisationIdResult response = organisationIdClient.getResult(organisationIdResultRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.ORGANISATION_ID_GET_RESULT,
                                             RequestTemplate.ORGANISATION_ID_RESULT_TEMPLATE,
                                             organisationIdResultRequest, OrganisationIdResult.class, RELYING_PARTY_ID);
        Assert.assertEquals(REFERENCE, response.getOrgIdRef());
        Assert.assertEquals(TransactionStatus.STARTED, response.getStatus());
        Assert.assertEquals(DETAILS, response.getDetails());
    }

    @Test
    public void getOrganisationIdResult_invalidReference_expectInvalidReferenceError()
            throws FrejaEidClientInternalException, FrejaEidException {
        OrganisationIdResultRequest organisationIdResultRequest =
                OrganisationIdResultRequest.create(REFERENCE, RELYING_PARTY_ID);
        try {
            OrganisationIdClientApi organisationIdClient =
                    OrganisationIdClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                            .setHttpService(httpServiceMock)
                            .build();
            FrejaEidException frejaEidException =
                    new FrejaEidException(FrejaEidErrorCode.INVALID_REFERENCE.getMessage(),
                                          FrejaEidErrorCode.INVALID_REFERENCE.getCode());
            Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                              Mockito.any(RelyingPartyRequest.class),
                                              Mockito.eq(OrganisationIdResult.class), Mockito.anyString()))
                    .thenThrow(frejaEidException);
            organisationIdClient.getResult(organisationIdResultRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidException rpEx) {
            Mockito.verify(httpServiceMock)
                    .send(FrejaEnvironment.TEST.getUrl() + MethodUrl.ORGANISATION_ID_GET_RESULT,
                          RequestTemplate.ORGANISATION_ID_RESULT_TEMPLATE, organisationIdResultRequest,
                          OrganisationIdResult.class, RELYING_PARTY_ID);
            Assert.assertEquals(1100, rpEx.getErrorCode());
            Assert.assertEquals("Invalid reference (for example, nonexistent or expired).", rpEx.getLocalizedMessage());
        }
    }

    @Test
    public void pollForResult_finalResponseRejected_success()
            throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException {
        OrganisationIdResult expectedResponse =
                new OrganisationIdResult(REFERENCE, TransactionStatus.REJECTED, DETAILS);
        OrganisationIdClientApi organisationIdClient =
                OrganisationIdClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                        .setHttpService(httpServiceMock)
                        .build();
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(OrganisationIdResult.class), (String) Mockito.isNull()))
                .thenReturn(expectedResponse);
        OrganisationIdResultRequest organisationIdResultRequest = OrganisationIdResultRequest.create(REFERENCE);
        OrganisationIdResult response = organisationIdClient.pollForResult(organisationIdResultRequest, 10000);
        Mockito.verify(httpServiceMock)
                .send(FrejaEnvironment.TEST.getUrl() + MethodUrl.ORGANISATION_ID_GET_RESULT,
                      RequestTemplate.ORGANISATION_ID_RESULT_TEMPLATE, organisationIdResultRequest,
                      OrganisationIdResult.class, null);
        Assert.assertEquals(TransactionStatus.REJECTED, response.getStatus());
    }

    @Test
    public void getFinalOrganisationIdResponse_requestTimeout_expectTimeoutError()
            throws FrejaEidException, FrejaEidClientInternalException {
        try {
            OrganisationIdClientApi organisationIdClient =
                    OrganisationIdClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                            .setHttpService(httpServiceMock)
                            .build();
            organisationIdClient.pollForResult(OrganisationIdResultRequest.create(REFERENCE), 2);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientPollingException ex) {
            Assert.assertEquals("A timeout of 2s was reached while sending request.", ex.getLocalizedMessage());
        }
    }
}
