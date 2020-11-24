package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.common.EmptyFrejaResponse;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.organisationid.delete.DeleteOrganisationIdRequest;
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
import org.junit.Test;
import org.mockito.Mockito;

public class OrganisationIdClientDeleteOrganisationIdTest {

    private final HttpServiceApi httpServiceMock = Mockito.mock(HttpServiceApi.class);
    private static final String IDENTIFIER = "vealmar";
    private static final String RELYING_PARTY_ID = "verisec_integrator";

    @Test
    public void deleteOrganisationId_success() throws FrejaEidClientInternalException, FrejaEidException {
        OrganisationIdClientApi organisationIdClient =
                OrganisationIdClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                        .setHttpService(httpServiceMock)
                        .build();
        DeleteOrganisationIdRequest deleteOrganisationIdRequest = DeleteOrganisationIdRequest.create(IDENTIFIER);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(EmptyFrejaResponse.class), (String) Mockito.isNull()))
                .thenReturn(EmptyFrejaResponse.INSTANCE);
        organisationIdClient.delete(deleteOrganisationIdRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.ORGANISATION_ID_DELETE,
                                             RequestTemplate.DELETE_ORGANINSATION_ID_TEMPLATE,
                                             deleteOrganisationIdRequest, EmptyFrejaResponse.class, null);
    }

    @Test
    public void deleteOrganisationId_relyingPartyIdFromRequest_expectedSuccess()
            throws FrejaEidClientInternalException, FrejaEidException {
        OrganisationIdClientApi organisationIdClient =
                OrganisationIdClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                        .setHttpService(httpServiceMock)
                        .build();
        DeleteOrganisationIdRequest deleteOrganisationIdRequest =
                DeleteOrganisationIdRequest.create(IDENTIFIER, RELYING_PARTY_ID);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(EmptyFrejaResponse.class), Mockito.anyString()))
                .thenReturn(EmptyFrejaResponse.INSTANCE);
        organisationIdClient.delete(deleteOrganisationIdRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.ORGANISATION_ID_DELETE,
                                             RequestTemplate.DELETE_ORGANINSATION_ID_TEMPLATE,
                                             deleteOrganisationIdRequest, EmptyFrejaResponse.class, RELYING_PARTY_ID);
    }

    @Test
    public void deleteOrganisationId_nonexistentCustomIdentifier_expectNoUserForCustomIdentifierError()
            throws FrejaEidClientInternalException, FrejaEidException {
        DeleteOrganisationIdRequest deleteOrganisationIdRequest = DeleteOrganisationIdRequest.create(IDENTIFIER);
        try {
            OrganisationIdClientApi organisationIdClient =
                    OrganisationIdClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                            .setHttpService(httpServiceMock)
                            .build();
            FrejaEidException frejaEidException = new FrejaEidException(
                    FrejaEidErrorCode.USER_MANAGEMENT_CUSTOM_IDENTIFIER_DOES_NOT_EXIST.getMessage(),
                    FrejaEidErrorCode.USER_MANAGEMENT_CUSTOM_IDENTIFIER_DOES_NOT_EXIST.getCode());
            Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                              Mockito.any(RelyingPartyRequest.class),
                                              Mockito.eq(EmptyFrejaResponse.class), (String) Mockito.isNull()))
                    .thenThrow(frejaEidException);
            organisationIdClient.delete(deleteOrganisationIdRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidException rpEx) {
            Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.ORGANISATION_ID_DELETE,
                                                 RequestTemplate.DELETE_ORGANINSATION_ID_TEMPLATE,
                                                 deleteOrganisationIdRequest, EmptyFrejaResponse.class, null);
            Assert.assertEquals(5001, rpEx.getErrorCode());
            Assert.assertEquals("There is no user for given custom identifier.", rpEx.getLocalizedMessage());
        }
    }
}
