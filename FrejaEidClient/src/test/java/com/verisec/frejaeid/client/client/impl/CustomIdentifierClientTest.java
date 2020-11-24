package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.common.EmptyFrejaResponse;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.beans.usermanagement.customidentifier.delete.DeleteCustomIdentifierRequest;
import com.verisec.frejaeid.client.beans.usermanagement.customidentifier.set.SetCustomIdentifierRequest;
import com.verisec.frejaeid.client.client.api.CustomIdentifierClientApi;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.Country;
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

import java.util.Arrays;
import java.util.List;

public class CustomIdentifierClientTest {

    private final HttpServiceApi httpServiceMock = Mockito.mock(HttpServiceApi.class);

    private static final String EMAIL = "test@verisec.se";
    private static final String SSN = "199207295578";
    private static final String CUSTOM_IDENTIFIER = "vealmar";
    private static final String RELYING_PARTY_ID = "verisec_integrator";
    private static CustomIdentifierClientApi customIdentifierClient;

    @Test
    public void setCustomIdentifier_success() throws FrejaEidClientInternalException, FrejaEidException {
        customIdentifierClient = CustomIdentifierClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .build();
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(EmptyFrejaResponse.class), (String) Mockito.isNull()))
                .thenReturn(EmptyFrejaResponse.INSTANCE);
        SetCustomIdentifierRequest setCustomIdentifierRequestDefaultEmail =
                SetCustomIdentifierRequest.createDefaultWithEmail(EMAIL, CUSTOM_IDENTIFIER);
        customIdentifierClient.set(setCustomIdentifierRequestDefaultEmail);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.CUSTOM_IDENTIFIER_SET,
                                             RequestTemplate.SET_CUSTOM_IDENITIFIER_TEMPLATE,
                                             setCustomIdentifierRequestDefaultEmail, EmptyFrejaResponse.class, null);
        SetCustomIdentifierRequest setCustomIdentifierRequestDefaultSsn =
                SetCustomIdentifierRequest.createDefaultWithSsn(SsnUserInfo.create(Country.NORWAY, SSN),
                                                                CUSTOM_IDENTIFIER);
        customIdentifierClient.set(setCustomIdentifierRequestDefaultSsn);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.CUSTOM_IDENTIFIER_SET,
                                             RequestTemplate.SET_CUSTOM_IDENITIFIER_TEMPLATE,
                                             setCustomIdentifierRequestDefaultSsn, EmptyFrejaResponse.class, null);
    }

    @Test
    public void setCustomIdentifier_relyingPartyIdFromRequest_expectedSuccess()
            throws FrejaEidClientInternalException, FrejaEidException {
        customIdentifierClient = CustomIdentifierClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .build();
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(EmptyFrejaResponse.class), Mockito.anyString()))
                .thenReturn(EmptyFrejaResponse.INSTANCE);
        SetCustomIdentifierRequest setCustomIdentifierRequestEmail =
                SetCustomIdentifierRequest.createCustom().setEmailAndCustomIdentifier(EMAIL, CUSTOM_IDENTIFIER)
                        .setRelyingPartyId(RELYING_PARTY_ID).build();
        SetCustomIdentifierRequest setCustomIdentifierRequestSsn =
                SetCustomIdentifierRequest.createCustom()
                        .setSsnAndCustomIdentifier(SsnUserInfo.create(Country.NORWAY, SSN), CUSTOM_IDENTIFIER)
                        .setRelyingPartyId(RELYING_PARTY_ID).build();
        SetCustomIdentifierRequest setCustomIdentifierRequestPhoneNum =
                SetCustomIdentifierRequest.createCustom().setPhoneNumberAndCustomIdentifier(EMAIL, CUSTOM_IDENTIFIER)
                        .setRelyingPartyId(RELYING_PARTY_ID).build();
        List<SetCustomIdentifierRequest> requests = Arrays.asList(setCustomIdentifierRequestEmail,
                                                                  setCustomIdentifierRequestSsn,
                                                                  setCustomIdentifierRequestPhoneNum);
        for (SetCustomIdentifierRequest request : requests) {
            customIdentifierClient.set(request);
            Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.CUSTOM_IDENTIFIER_SET,
                                                 RequestTemplate.SET_CUSTOM_IDENITIFIER_TEMPLATE, request,
                                                 EmptyFrejaResponse.class, RELYING_PARTY_ID);
        }

    }

    @Test
    public void setCustomIdentifier_customIdentifierAlreadySetForUser_expectCustomIdentifierAlreadySetError()
            throws FrejaEidClientInternalException, FrejaEidException {
        SetCustomIdentifierRequest setCustomIdentifierRequest =
                SetCustomIdentifierRequest.createDefaultWithEmail(EMAIL, CUSTOM_IDENTIFIER);
        try {
            customIdentifierClient =
                    CustomIdentifierClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                            .setHttpService(httpServiceMock)
                            .build();
            FrejaEidException frejaEidException = new FrejaEidException(
                    FrejaEidErrorCode.USER_MANAGEMENT_CUSTOM_IDENTIFIER_ALREADY_EXISTS.getMessage(),
                    FrejaEidErrorCode.USER_MANAGEMENT_CUSTOM_IDENTIFIER_ALREADY_EXISTS.getCode());
            Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                              Mockito.any(RelyingPartyRequest.class),
                                              Mockito.eq(EmptyFrejaResponse.class), (String) Mockito.isNull()))
                    .thenThrow(frejaEidException);
            customIdentifierClient.set(setCustomIdentifierRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidException rpEx) {
            Mockito.verify(httpServiceMock)
                    .send(FrejaEnvironment.TEST.getUrl() + MethodUrl.CUSTOM_IDENTIFIER_SET,
                          RequestTemplate.SET_CUSTOM_IDENITIFIER_TEMPLATE, setCustomIdentifierRequest,
                          EmptyFrejaResponse.class, null);
            Assert.assertEquals(5002, rpEx.getErrorCode());
            Assert.assertEquals("You have already used this custom identifier.", rpEx.getLocalizedMessage());
        }
    }

    @Test
    public void deleteCustomIdentifier_success() throws FrejaEidClientInternalException, FrejaEidException {
        customIdentifierClient = CustomIdentifierClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .build();
        DeleteCustomIdentifierRequest deleteCustomIdentifierRequest =
                DeleteCustomIdentifierRequest.create(CUSTOM_IDENTIFIER);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(EmptyFrejaResponse.class), (String) Mockito.isNull()))
                .thenReturn(EmptyFrejaResponse.INSTANCE);
        customIdentifierClient.delete(deleteCustomIdentifierRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.CUSTOM_IDENTIFIER_DELETE,
                                             RequestTemplate.DELETE_CUSTOM_IDENTIFIER_TEMPLATE,
                                             deleteCustomIdentifierRequest, EmptyFrejaResponse.class, null);
    }

    @Test
    public void deleteCustomIdentifier_relyingPartyIdFromRequest_expectedSuccess()
            throws FrejaEidClientInternalException, FrejaEidException {
        customIdentifierClient = CustomIdentifierClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .build();
        DeleteCustomIdentifierRequest deleteCustomIdentifierRequest =
                DeleteCustomIdentifierRequest.create(CUSTOM_IDENTIFIER, RELYING_PARTY_ID);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(EmptyFrejaResponse.class), Mockito.anyString()))
                .thenReturn(EmptyFrejaResponse.INSTANCE);
        customIdentifierClient.delete(deleteCustomIdentifierRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.CUSTOM_IDENTIFIER_DELETE,
                                             RequestTemplate.DELETE_CUSTOM_IDENTIFIER_TEMPLATE,
                                             deleteCustomIdentifierRequest, EmptyFrejaResponse.class, RELYING_PARTY_ID);
    }

    @Test
    public void deleteCustomIdentifier_nonexistentCustomIdentifier_expectNoUserForCustomIdentifierError()
            throws FrejaEidClientInternalException, FrejaEidException {
        DeleteCustomIdentifierRequest deleteCustomIdentifierRequest =
                DeleteCustomIdentifierRequest.create(CUSTOM_IDENTIFIER);
        try {
            customIdentifierClient =
                    CustomIdentifierClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                            .setHttpService(httpServiceMock)
                            .build();
            FrejaEidException frejaEidException = new FrejaEidException(
                    FrejaEidErrorCode.USER_MANAGEMENT_CUSTOM_IDENTIFIER_DOES_NOT_EXIST.getMessage(),
                    FrejaEidErrorCode.USER_MANAGEMENT_CUSTOM_IDENTIFIER_DOES_NOT_EXIST.getCode());
            Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                              Mockito.any(RelyingPartyRequest.class),
                                              Mockito.eq(EmptyFrejaResponse.class), (String) Mockito.isNull()))
                    .thenThrow(frejaEidException);
            customIdentifierClient.delete(deleteCustomIdentifierRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidException rpEx) {
            Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.CUSTOM_IDENTIFIER_DELETE,
                                                 RequestTemplate.DELETE_CUSTOM_IDENTIFIER_TEMPLATE,
                                                 deleteCustomIdentifierRequest, EmptyFrejaResponse.class, null);
            Assert.assertEquals("Invalid error", 5001, rpEx.getErrorCode());
            Assert.assertEquals("Invalid error", "There is no user for given custom identifier.",
                                rpEx.getLocalizedMessage());
        }
    }

}
