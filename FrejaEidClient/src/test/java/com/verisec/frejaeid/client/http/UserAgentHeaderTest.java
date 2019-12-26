package com.verisec.frejaeid.client.http;

import org.junit.Assert;
import org.junit.Test;

public class UserAgentHeaderTest {

    @Test
    public void makeUserAgentHeaderTest() {
        HttpService httpService = new HttpService(null, 0, 0);
        String userAgentHeader = httpService.makeUserAgentHeader();
        Assert.assertTrue(userAgentHeader.contains("FrejaEidClient/"));
        Assert.assertTrue(userAgentHeader.contains("Java/"));
        Assert.assertFalse(userAgentHeader.contains("%version%"));
    }

}
