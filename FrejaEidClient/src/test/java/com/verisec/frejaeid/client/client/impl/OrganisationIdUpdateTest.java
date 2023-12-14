package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.common.EmptyFrejaResponse;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.general.OrganisationIdAttribute;
import com.verisec.frejaeid.client.beans.general.UpdateStatus;
import com.verisec.frejaeid.client.beans.organisationid.delete.DeleteOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.update.UpdateOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.update.UpdateOrganisationIdResponse;
import com.verisec.frejaeid.client.client.api.OrganisationIdClientApi;
import com.verisec.frejaeid.client.client.util.TestUtil;
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

    private final HttpServiceApi httpServiceMock = Mockito.mock(HttpServiceApi.class);
    private OrganisationIdClientApi organisationIdClient;

    @Before
    public void initialiseClient() throws FrejaEidClientInternalException {
        organisationIdClient = OrganisationIdClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .build();
    }

    @Test
    public void updateOrgId_success() throws FrejaEidClientInternalException, FrejaEidException {
        UpdateOrganisationIdRequest updateOrganisationIdRequest =
                UpdateOrganisationIdRequest.create(IDENTIFIER, ADDITIONAL_ATTRIBUTES);
        UpdateOrganisationIdResponse expectedResponse = new UpdateOrganisationIdResponse(new UpdateStatus(0, 2, 0));
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
}
