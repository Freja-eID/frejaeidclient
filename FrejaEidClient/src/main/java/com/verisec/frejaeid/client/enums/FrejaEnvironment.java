package com.verisec.frejaeid.client.enums;

/**
 * FrejaEnvironment that can be used are:
 * <br> - <b>PRODUCTION</b> - for sending HTTP requests to production
 * environment
 * <br> - <b>TEST</b> - for sending HTTP requests to test environment
 */
public enum FrejaEnvironment {

    /**
     * Production address.
     */
    PRODUCTION("https://services.prod.frejaeid.com", "https://resources.prod.frejaeid.com"),
    /**
     * Test address.
     */
    TEST("https://services.test.frejaeid.com", "https://resources.test.frejaeid.com");

    private final String serviceUrl;
    private final String resourceUrl;

    private FrejaEnvironment(String serviceUrl, String resourceUrl) {
        this.serviceUrl = serviceUrl;
        this.resourceUrl = resourceUrl;
    }

    public String getServiceUrl() { return serviceUrl; }

    public String getResourceUrl() { return resourceUrl; }

    @Override
    public String toString() {
        return "FrejaEnvironment{" +
                "serviceUrl='" + serviceUrl + '\'' +
                ", resourceUrl='" + resourceUrl + '\'' +
                '}';
    }
}
