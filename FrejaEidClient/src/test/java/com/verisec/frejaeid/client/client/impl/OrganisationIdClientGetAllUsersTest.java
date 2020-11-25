package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.general.OrganisationId;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.beans.organisationid.getall.GetAllOrganisationIdUsersRequest;
import com.verisec.frejaeid.client.beans.organisationid.getall.GetAllOrganisationIdUsersResponse;
import com.verisec.frejaeid.client.beans.general.OrganisationIdUserInfo;
import com.verisec.frejaeid.client.client.api.OrganisationIdClientApi;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.Country;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.RegistrationState;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import com.verisec.frejaeid.client.util.MethodUrl;
import com.verisec.frejaeid.client.util.RequestTemplate;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class OrganisationIdClientGetAllUsersTest {

    private final HttpServiceApi httpServiceMock = Mockito.mock(HttpServiceApi.class);
    private final OrganisationIdClientApi organisationIdClient;

    private static final OrganisationId ORGANISATION_ID =
            OrganisationId.create("Title", "Identifier name", "Identifier");
    private static final SsnUserInfo SSN = SsnUserInfo.create(Country.SWEDEN, "301219938787");
    private static final String RELYING_PARTY_ID = "relyingPartyId";
    private final OrganisationIdUserInfo organisationIdUserInfo =
            new OrganisationIdUserInfo(ORGANISATION_ID, SSN, RegistrationState.EXTENDED);
    private final GetAllOrganisationIdUsersResponse expectedResponse =
            new GetAllOrganisationIdUsersResponse(Arrays.asList(organisationIdUserInfo));


    public OrganisationIdClientGetAllUsersTest() throws FrejaEidClientInternalException {
        organisationIdClient = OrganisationIdClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .build();
    }

    @Test
    public void getAllOrganisationIdUsers_withoutRelyingPartyId_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        GetAllOrganisationIdUsersRequest request = GetAllOrganisationIdUsersRequest.create();
        Mockito.when(httpServiceMock.send(Mockito.anyString(), (RequestTemplate) Mockito.isNull(),
                                          Mockito.any(GetAllOrganisationIdUsersRequest.class),
                                          Mockito.eq(GetAllOrganisationIdUsersResponse.class),
                                          (String) Mockito.isNull())).thenReturn(expectedResponse);
        List<OrganisationIdUserInfo> actualListOfOrganisationIdUserInfos = organisationIdClient.getAllUsers(request);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.ORGANISATION_ID_GET_ALL_USERS,
                                             null, request, GetAllOrganisationIdUsersResponse.class, null);
        Assert.assertEquals(expectedResponse.getUserInfos(), actualListOfOrganisationIdUserInfos);
    }

    @Test
    public void getAllOrganisationIdUsers_withRelyingPartyId_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        GetAllOrganisationIdUsersRequest request = GetAllOrganisationIdUsersRequest.create(RELYING_PARTY_ID);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), (RequestTemplate) Mockito.isNull(),
                                          Mockito.any(GetAllOrganisationIdUsersRequest.class),
                                          Mockito.eq(GetAllOrganisationIdUsersResponse.class), Mockito.anyString()))
                .thenReturn(expectedResponse);
        List<OrganisationIdUserInfo> actualListOfOrgansiationIdUserInfos = organisationIdClient.getAllUsers(request);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.ORGANISATION_ID_GET_ALL_USERS,
                                             null, request, GetAllOrganisationIdUsersResponse.class, RELYING_PARTY_ID);
        Assert.assertEquals(expectedResponse.getUserInfos(), actualListOfOrgansiationIdUserInfos);
    }
}
