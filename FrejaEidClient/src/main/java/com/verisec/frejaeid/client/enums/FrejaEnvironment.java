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
    private final String resourceServiceUrl;

    private FrejaEnvironment(String serviceUrl, String resourceServiceUrl) {
        this.serviceUrl = serviceUrl;
        this.resourceServiceUrl = resourceServiceUrl;
    }

    public String getServiceUrl() { return serviceUrl; }

    public String getResourceServiceUrl() { return resourceServiceUrl; }

    @Override
    public String toString() {
        return "FrejaEnvironment{" +
                "serviceUrl='" + serviceUrl + '\'' +
                ", resourceServiceUrl='" + resourceServiceUrl + '\'' +
                '}';
    }
}
