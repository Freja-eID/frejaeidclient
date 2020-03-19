package com.verisec.frejaeid.client.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.general.AddressInfo;
import com.verisec.frejaeid.client.beans.general.BasicUserInfo;
import com.verisec.frejaeid.client.beans.general.RequestedAttributes;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.enums.AddressSourceType;
import com.verisec.frejaeid.client.enums.AddressType;
import com.verisec.frejaeid.client.enums.Country;
import com.verisec.frejaeid.client.util.JsonService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.junit.After;
import org.junit.Assert;

public abstract class CommonHttpTest {

    protected static final String REFERENCE = "123456789012345678";
    protected static final String EMAIL = "eid.demo.verisec@gmail.com";
    protected static final String SSN = "123455697887";
    protected static final String RELYING_PARTY_ID = "relying_party_id";
    protected static final SsnUserInfo SSN_USER_INFO = SsnUserInfo.create(Country.SWEDEN, SSN);
    protected static final BasicUserInfo BASIC_USER_INFO = new BasicUserInfo("John", "Fante");
    protected static final String CUSTOM_IDENTIFIER = "vejofan";
    protected static final String DETAILS = "Ask the dust";
    protected static final String RELYING_PARTY_USER_ID = "relyingPartyUserId";
    protected static final String DATE_OF_BIRTH = "1987-10-18";
    protected static final String EMAIL_ADDRESS = "test@frejaeid.com";
    protected static final String ORGANISATION_ID = "vealrad";
    protected static final List<AddressInfo> ADDRESSES = Arrays.asList(AddressInfo.create(Country.SWEDEN, "city", "postCode", "address1", "address2", "address3", "1993-12-30", AddressType.RESIDENTIAL, AddressSourceType.GOVERNMENT_REGISTRY));
    protected static final RequestedAttributes REQUESTED_ATTRIBUTES = new RequestedAttributes(BASIC_USER_INFO, CUSTOM_IDENTIFIER, SSN_USER_INFO, null, DATE_OF_BIRTH, RELYING_PARTY_USER_ID, EMAIL_ADDRESS, ORGANISATION_ID, ADDRESSES);
    protected static final String POST_PARAMS_DELIMITER = "&";
    protected static final String KEY_VALUE_DELIMITER = "=";
    protected static final int MOCK_SERVICE_PORT = 30665;
    private HttpServer server;
    protected static JsonService jsonService;

    protected void startMockServer(final RelyingPartyRequest expectedRequest, final int statusCodeToReturn, final String responseToReturn) throws IOException {
        server = HttpServer.create(new InetSocketAddress(MOCK_SERVICE_PORT), 0);
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange t) throws IOException {
                String requestData = null;
                try (InputStreamReader isr = new InputStreamReader(t.getRequestBody())) {
                    BufferedReader reader = new BufferedReader(isr);
                    requestData = reader.readLine();
                    Assert.assertNotNull(requestData);

                    String[] postParams = requestData.split(POST_PARAMS_DELIMITER);
                    String RequestParam = postParams[0].split(KEY_VALUE_DELIMITER, 2)[1];
                    if (postParams.length == 2) {
                        String relyingPartyIdParam = postParams[1].split(KEY_VALUE_DELIMITER, 2)[1];
                        Assert.assertEquals(RELYING_PARTY_ID, relyingPartyIdParam);
                    }
                    String jsonReceivedRequest = new String(Base64.decodeBase64(RequestParam), StandardCharsets.UTF_8);
                    String jsonExpectedRequest = jsonService.serializeToJson(expectedRequest);
                    Assert.assertEquals(jsonExpectedRequest, jsonReceivedRequest);
                    RelyingPartyRequest receivedRequest = jsonService.deserializeFromJson(Base64.decodeBase64(RequestParam), expectedRequest.getClass());
                    Assert.assertEquals(expectedRequest, receivedRequest);
                } catch (Exception ex) {
                    Assert.fail(ex.getMessage());
                }

                t.sendResponseHeaders(statusCodeToReturn, responseToReturn.length());
                try (OutputStream os = t.getResponseBody()) {
                    os.write(responseToReturn.getBytes());
                }
            }
        });
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    @After
    public void stopServer() throws InterruptedException {
        stopMockServer();
        Thread.sleep(1000);
    }

    private void stopMockServer() {
        if (server != null) {
            server.stop(1);
        }
    }

}
