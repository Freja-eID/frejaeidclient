package com.verisec.frejaeid.client.enums;

/**
 * FrejaEnvironment that can be used are:
 * <br> - <b>PRODUCTION</b> - for sending HTTP requests to production
 * environment
 * <br> - <b>TEST</b> - for sending HTTP requests to test environment
 *
 */
public enum FrejaEnvironment {

    /**
     * Production address.
     */
    PRODUCTION("https://services.prod.frejaeid.com"),
    /**
     * Test address.
     */
    TEST("https://services.test.frejaeid.com");

    private final String url;

    private FrejaEnvironment(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "FrejaEnvironment{url=" + url + '}';
    }
}
