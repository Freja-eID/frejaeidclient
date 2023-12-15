package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.general.OrganisationIdAttribute;
import com.verisec.frejaeid.client.beans.general.UpdateOrganisationIdStatus;
import com.verisec.frejaeid.client.beans.organisationid.update.UpdateOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.update.UpdateOrganisationIdResponse;
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

import java.util.Arrays;
import java.util.List;

public class OrganisationIdUpdateTest {

    private static final String IDENTIFIER = "identifier";
    private static final List<OrganisationIdAttribute> ADDITIONAL_ATTRIBUTES =
            Arrays.asList(OrganisationIdAttribute.create("key", "friendly name", "value"),
                          OrganisationIdAttribute.create("attribute_id", "attribute name", "attribute value"));
    private static final String RELYING_PARTY_ID = "relyingPartyId";

    private final HttpServiceApi httpServiceMock = Mockito.mock(HttpServiceApi.class);
    private OrganisationIdClientApi organisationIdClient;

    @Before
    public void initialiseClient() throws FrejaEidClientInternalException {
        organisationIdClient = OrganisationIdClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .build();
    }

    @Test
    public void updateOrgId_withoutRelyingPartyId_success() throws FrejaEidClientInternalException, FrejaEidException {
        UpdateOrganisationIdRequest updateOrganisationIdRequest =
                UpdateOrganisationIdRequest.create(IDENTIFIER, ADDITIONAL_ATTRIBUTES);
        UpdateOrganisationIdResponse expectedResponse = new UpdateOrganisationIdResponse(new UpdateOrganisationIdStatus(0, 2, 0));
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(UpdateOrganisationIdResponse.class), (String) Mockito.isNull()))
                .thenReturn(expectedResponse);
        UpdateOrganisationIdResponse receivedResponse = organisationIdClient.update(updateOrganisationIdRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.ORGANISATION_ID_UPDATE,
                                             RequestTemplate.UPDATE_ORGANISATION_ID_TEMPLATE,
                                             updateOrganisationIdRequest, UpdateOrganisationIdResponse.class, null);
        Assert.assertEquals(expectedResponse, receivedResponse);
    }

    @Test
    public void updateOrgId_withRelyingPartyId_success() throws FrejaEidClientInternalException, FrejaEidException {
        UpdateOrganisationIdRequest updateOrganisationIdRequest =
                UpdateOrganisationIdRequest.create(IDENTIFIER, ADDITIONAL_ATTRIBUTES, RELYING_PARTY_ID);
        UpdateOrganisationIdResponse expectedResponse = new UpdateOrganisationIdResponse(new UpdateOrganisationIdStatus(1, 0, 1));
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(UpdateOrganisationIdResponse.class), Mockito.eq(RELYING_PARTY_ID)))
                .thenReturn(expectedResponse);
        UpdateOrganisationIdResponse receivedResponse = organisationIdClient.update(updateOrganisationIdRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.ORGANISATION_ID_UPDATE,
                                             RequestTemplate.UPDATE_ORGANISATION_ID_TEMPLATE, updateOrganisationIdRequest,
                                             UpdateOrganisationIdResponse.class, RELYING_PARTY_ID);
        Assert.assertEquals(expectedResponse, receivedResponse);
    }

    @Test
    public void updateOrgId_nonExistentCustomIdentifier() throws FrejaEidClientInternalException, FrejaEidException {
        UpdateOrganisationIdRequest updateOrganisationIdRequest =
                UpdateOrganisationIdRequest.create(IDENTIFIER, ADDITIONAL_ATTRIBUTES);
        FrejaEidException expectedException = new FrejaEidException(
                FrejaEidErrorCode.ORGANISATION_ID_IDENTIFIER_DOES_NOT_EXIST.getMessage(),
                FrejaEidErrorCode.ORGANISATION_ID_IDENTIFIER_DOES_NOT_EXIST.getCode());
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(UpdateOrganisationIdResponse.class), (String) Mockito.isNull()))
                .thenThrow(expectedException);
        try {
            organisationIdClient.update(updateOrganisationIdRequest);
            Assert.fail("Test unexpectedly passed.");
        } catch (FrejaEidException fex) {
            Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.ORGANISATION_ID_UPDATE,
                                                 RequestTemplate.UPDATE_ORGANISATION_ID_TEMPLATE, updateOrganisationIdRequest,
                                                 UpdateOrganisationIdResponse.class, null);
            Assert.assertEquals(expectedException, fex);
        }
    }
}
