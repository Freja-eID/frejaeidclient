package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.common.EmptyFrejaResponse;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.organisationid.cancel.CancelAddOrganisationIdRequest;
import com.verisec.frejaeid.client.client.api.OrganisationIdClientApi;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.FrejaEidErrorCode;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import com.verisec.frejaeid.client.util.MethodUrl;
import com.verisec.frejaeid.client.util.RequestTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class OrganisationIdClientCancelAddOrganisationIdTest {

    private final HttpServiceApi httpServiceMock = Mockito.mock(HttpServiceApi.class);
    private static final String REFERENCE = "reference";
    private static final String RELYING_PARTY_ID = "relyingPartyId";
    private OrganisationIdClientApi organisationIdClient;

    @Before
    public void initialiseClient() throws FrejaEidClientInternalException {
        organisationIdClient = OrganisationIdClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .build();
    }

    @Test
    public void cancelAdOrgId_relyingPartyIdNull_success() throws FrejaEidClientInternalException, FrejaEidException {
        CancelAddOrganisationIdRequest cancelAddOrganisationIdRequest =
                CancelAddOrganisationIdRequest.create(REFERENCE);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(EmptyFrejaResponse.class), (String) Mockito.isNull()))
                .thenReturn(EmptyFrejaResponse.INSTANCE);
        organisationIdClient.cancelAdd(cancelAddOrganisationIdRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.ORGANISATION_ID_CANCEL_ADD,
                                             RequestTemplate.CANCEL_ADD_ORGANISATION_ID_TEMPLATE,
                                             cancelAddOrganisationIdRequest, EmptyFrejaResponse.class, null);
    }

    @Test
    public void cancelAddOrgId_success() throws FrejaEidClientInternalException, FrejaEidException {
        CancelAddOrganisationIdRequest cancelAddOrganisationIdRequest =
                CancelAddOrganisationIdRequest.create(REFERENCE, RELYING_PARTY_ID);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(EmptyFrejaResponse.class), Mockito.anyString()))
                .thenReturn(EmptyFrejaResponse.INSTANCE);
        organisationIdClient.cancelAdd(cancelAddOrganisationIdRequest);
        Mockito.verify(httpServiceMock)
                .send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.ORGANISATION_ID_CANCEL_ADD,
                      RequestTemplate.CANCEL_ADD_ORGANISATION_ID_TEMPLATE,
                      cancelAddOrganisationIdRequest, EmptyFrejaResponse.class, RELYING_PARTY_ID);
    }

    @Test
    public void cancelAddOrgId_invalidReference_expectInvalidReferenceError()
            throws FrejaEidClientInternalException, FrejaEidException {
        CancelAddOrganisationIdRequest cancelAddOrganisationIdRequest =
                CancelAddOrganisationIdRequest.create(REFERENCE, RELYING_PARTY_ID);
        try {
            Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                              Mockito.any(RelyingPartyRequest.class),
                                              Mockito.eq(EmptyFrejaResponse.class), Mockito.anyString()))
                    .thenThrow(new FrejaEidException(FrejaEidErrorCode.INVALID_REFERENCE.getMessage(),
                                                     FrejaEidErrorCode.INVALID_REFERENCE.getCode()));
            organisationIdClient.cancelAdd(cancelAddOrganisationIdRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidException rpEx) {
            Mockito.verify(httpServiceMock)
                    .send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.ORGANISATION_ID_CANCEL_ADD,
                          RequestTemplate.CANCEL_ADD_ORGANISATION_ID_TEMPLATE, cancelAddOrganisationIdRequest,
                          EmptyFrejaResponse.class, RELYING_PARTY_ID);
            Assert.assertEquals(1100, rpEx.getErrorCode());
            Assert.assertEquals("Invalid reference (for example, nonexistent or expired).", rpEx.getLocalizedMessage());
        }
    }
}
