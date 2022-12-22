package com.verisec.frejaeid.client.enums;

/**
 * FrejaResourceEnvironment that can be used are:
 * <br> - <b>PRODUCTION</b> - for sending HTTP requests to production
 * environment
 * <br> - <b>TEST</b> - for sending HTTP requests to test environment
 */
public enum FrejaResourceEnvironment {
    /**
     * Production address.
     */
    PRODUCTION("https://resources.prod.frejaeid.com"),
    /**
     * Test address.
     */
    TEST("https://resources.test.frejaeid.com");

    private final String url;

    private FrejaResourceEnvironment(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "FrejaResourceEnvironment{url=" + url + '}';
    }
}
