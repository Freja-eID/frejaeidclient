package com.verisec.frejaeid.client.http;

import com.verisec.frejaeid.client.beans.common.FrejaHttpErrorResponse;
import com.verisec.frejaeid.client.beans.common.FrejaHttpResponse;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.enums.HttpStatusCode;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.util.JsonService;
import com.verisec.frejaeid.client.util.RequestTemplate;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import javax.net.ssl.SSLContext;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpService implements HttpServiceApi {

    public static final Logger LOG = LoggerFactory.getLogger(HttpService.class);

    private static final int DEFAULT_TRIES_NUMBER_HTTP_POOL = 3;
    private JsonService jsonService;
    private final HttpClient httpClient;
    private static final String POST_PARAMS_DELIMITER = "&";

    private static final String VERSION_PLACEHOLDER = "%version%";
    private static final String FREJA_EID_CLIENT_VERSION_INFO = "FrejaEidClient/" + VERSION_PLACEHOLDER;
    private static final String JAVA_VM_VERSION_INFO = "(Java/" + VERSION_PLACEHOLDER + ")";
    private static String userAgentHeader;

    public HttpService(SSLContext sslContext, int connectionTimeout, int readTimeout) {

        jsonService = new JsonService();

        HttpClientBuilder httpClientBuilder = HttpClients.custom().useSystemProperties();
        if (sslContext != null) {
            httpClientBuilder.setSSLContext(sslContext);
        }
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectionTimeout).
                setConnectionRequestTimeout(connectionTimeout).setSocketTimeout(readTimeout).build();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        httpClientBuilder.setRetryHandler(new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount > DEFAULT_TRIES_NUMBER_HTTP_POOL) {
                    return false;
                }
                return exception instanceof org.apache.http.NoHttpResponseException;
            }
        });

        PoolingHttpClientConnectionManager poolingHttpConnectionManager = null;
        if (sslContext != null) {
            SSLConnectionSocketFactory sslConFactory;
            sslConFactory = new SSLConnectionSocketFactory(sslContext);
            final RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
            registryBuilder.register("http", PlainConnectionSocketFactory.getSocketFactory());
            registryBuilder.register("https", sslConFactory);
            poolingHttpConnectionManager = new PoolingHttpClientConnectionManager(registryBuilder.build());
        } else {
            poolingHttpConnectionManager = new PoolingHttpClientConnectionManager();
        }
        poolingHttpConnectionManager.setMaxTotal(20);
        poolingHttpConnectionManager.setDefaultMaxPerRoute(20);
        httpClient = httpClientBuilder.setConnectionManager(poolingHttpConnectionManager).build();
        LOG.debug("Successfully created HTTP client with SSL context, connection timeout {}ms and read timeout {}ms.", connectionTimeout, readTimeout);
        userAgentHeader = makeUserAgentHeader();
    }

    @Override
    public final <Response extends FrejaHttpResponse> Response send(String methodUrl, RequestTemplate requestTemplate, RelyingPartyRequest relyingPartyRequest, Class<Response> responseType, String relyingPartyId) throws FrejaEidClientInternalException, FrejaEidException {

        HttpResponse httpResponse = null;
        HttpPost request = null;
        HttpStatusCode httpStatusCode = null;

        try {
            request = new HttpPost(methodUrl);
            String requestBody = "";
            if (requestTemplate != null) {
                String jsonRequest = jsonService.serializeToJson(relyingPartyRequest);

                String jsonRequestB64 = Base64.encodeBase64String(jsonRequest.getBytes(StandardCharsets.UTF_8));
                requestBody = MessageFormat.format(requestTemplate.getTemplate(), jsonRequestB64);
            }

            if (relyingPartyId != null) {
                String relyingPartyIdRequest = MessageFormat.format(RequestTemplate.RELYING_PARTY_ID.getTemplate(), relyingPartyId);
                requestBody += requestTemplate != null ? POST_PARAMS_DELIMITER + relyingPartyIdRequest : relyingPartyIdRequest;
            }

            StringEntity params = new StringEntity(requestBody, StandardCharsets.UTF_8);
            request.addHeader("Content-Type", "application/json");
            request.addHeader(HttpHeaders.USER_AGENT, userAgentHeader);
            request.setEntity(params);
            httpResponse = httpClient.execute(request);
            LOG.debug("Successfully sent {}.", relyingPartyRequest.getClass());
            HttpEntity entity = httpResponse.getEntity();
            int httpStatusCodeValue = httpResponse.getStatusLine().getStatusCode();
            httpStatusCode = HttpStatusCode.getHttpStatusCode(httpStatusCodeValue);
            String responseString = "";
            if (httpStatusCode != HttpStatusCode.NO_CONTENT) {
                responseString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            }
            if (httpStatusCode == null) {
                throw new FrejaEidException(String.format("Received unsupported HTTP status code %s. Received HTTP message: %s.", httpResponse.getStatusLine().getStatusCode(), responseString));
            }
            switch (httpStatusCode) {
                case OK:
                    return jsonService.deserializeFromJson(responseString.getBytes(StandardCharsets.UTF_8), responseType);
                case NO_CONTENT:
                    return (Response) new FrejaHttpResponse() {
                    };
                case BAD_REQUEST:
                case UNPROCESSABLE_ENTITY:
                    FrejaHttpErrorResponse errorResponse = jsonService.deserializeFromJson(responseString.getBytes(StandardCharsets.UTF_8), FrejaHttpErrorResponse.class);
                    throw new FrejaEidException(errorResponse.getMessage(), errorResponse.getCode());
                default:
                    throw new FrejaEidException(String.format("HTTP code %s message: %s", httpResponse.getStatusLine().getStatusCode(), responseString));
            }
        } catch (IOException e) {
            throw new FrejaEidClientInternalException("Failed to send HTTP request.", e);
        } finally {
            if (httpResponse != null && httpStatusCode != HttpStatusCode.NO_CONTENT) {
                try {
                    httpResponse.getEntity().getContent().close();
                } catch (IOException | IllegalStateException ex) {
                    throw new FrejaEidClientInternalException("Failed to close HTTP connection.", ex);
                }
            }
        }
    }

    final String makeUserAgentHeader() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(FREJA_EID_CLIENT_VERSION_INFO.replace(VERSION_PLACEHOLDER, getLibVersion()));
        stringBuilder.append(" ");
        stringBuilder.append(JAVA_VM_VERSION_INFO.replace(VERSION_PLACEHOLDER, System.getProperty("java.version")));
        return stringBuilder.toString();
    }

    final String getLibVersion() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStreamReader is = new InputStreamReader(classLoader.getResourceAsStream("version.txt")); BufferedReader bufferedReader = new BufferedReader(is)) {
            String line = bufferedReader.readLine();
            return line != null ? line : "N/A";
        } catch (IOException e) {
            return "N/A";
        }
    }
}
