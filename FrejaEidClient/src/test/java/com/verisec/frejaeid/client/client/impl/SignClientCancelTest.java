package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.common.EmptyFrejaResponse;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.sign.cancel.CancelSignRequest;
import com.verisec.frejaeid.client.client.api.SignClientApi;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.FrejaEidErrorCode;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import com.verisec.frejaeid.client.util.MethodUrl;
import com.verisec.frejaeid.client.util.RequestTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SignClientCancelTest {

    private final HttpServiceApi httpServiceMock = Mockito.mock(HttpServiceApi.class);
    private static final String REFERENCE = "reference";
    private static final String RELYING_PARTY_ID = "relyingPartyId";
    private  SignClientApi signClient;

    @Before
    public void initialiseClient() throws FrejaEidClientInternalException {
        signClient = SignClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .setTransactionContext(TransactionContext.PERSONAL).build();
    }

    @Test
    public void cancelSign_relyingPartyIdNull_success() throws FrejaEidClientInternalException, FrejaEidException {
        CancelSignRequest cancelSignRequest = CancelSignRequest.create(REFERENCE);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(EmptyFrejaResponse.class), (String) Mockito.isNull()))
                .thenReturn(EmptyFrejaResponse.INSTANCE);
        signClient.cancel(cancelSignRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.SIGN_CANCEL,
                                             RequestTemplate.CANCEL_SIGN_TEMPLATE, cancelSignRequest,
                                             EmptyFrejaResponse.class, null);
    }

    @Test
    public void cancelSignPersonal_success() throws FrejaEidClientInternalException, FrejaEidException {
        CancelSignRequest cancelSignRequest = CancelSignRequest.create(REFERENCE, RELYING_PARTY_ID);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(EmptyFrejaResponse.class), Mockito.anyString()))
                .thenReturn(EmptyFrejaResponse.INSTANCE);
        signClient.cancel(cancelSignRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.SIGN_CANCEL,
                                             RequestTemplate.CANCEL_SIGN_TEMPLATE, cancelSignRequest,
                                             EmptyFrejaResponse.class, RELYING_PARTY_ID);
    }

    @Test
    public void cancelSignOrganisational_success() throws FrejaEidClientInternalException, FrejaEidException {
        SignClientApi signClient = SignClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .setTransactionContext(TransactionContext.ORGANISATIONAL).build();
        CancelSignRequest cancelSignRequest = CancelSignRequest.create(REFERENCE, RELYING_PARTY_ID);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(EmptyFrejaResponse.class), Mockito.anyString()))
                .thenReturn(EmptyFrejaResponse.INSTANCE);
        signClient.cancel(cancelSignRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.ORGANISATION_SIGN_CANCEL,
                                             RequestTemplate.CANCEL_SIGN_TEMPLATE, cancelSignRequest,
                                             EmptyFrejaResponse.class, RELYING_PARTY_ID);
    }

    @Test
    public void cancelSign_invalidReference_expectErrorInvalidReference()
            throws FrejaEidClientInternalException, FrejaEidException {
        CancelSignRequest cancelSignRequest = CancelSignRequest.create("123");
        try {
            FrejaEidException frejaEidException = new FrejaEidException(
                    FrejaEidErrorCode.INVALID_REFERENCE.getMessage(), FrejaEidErrorCode.INVALID_REFERENCE.getCode());
            Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                              Mockito.any(RelyingPartyRequest.class),
                                              Mockito.eq(EmptyFrejaResponse.class), (String) Mockito.isNull()))
                    .thenThrow(frejaEidException);

            signClient.cancel(cancelSignRequest);
            Assert.fail();
        } catch (FrejaEidException rpEx) {
            Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.SIGN_CANCEL,
                                                 RequestTemplate.CANCEL_SIGN_TEMPLATE, cancelSignRequest,
                                                 EmptyFrejaResponse.class, null);
            Assert.assertEquals(1100, rpEx.getErrorCode());
            Assert.assertEquals("Invalid reference (for example, nonexistent or expired).", rpEx.getLocalizedMessage());
        }
    }

}
