package com.verisec.frejaeid.client.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserAgentHeaderTest {

    @Test
    public void makeUserAgentHeaderTest() {
        HttpService httpService = new HttpService(null, 0, 0);
        String userAgentHeader = httpService.makeUserAgentHeader();
        Assertions.assertTrue(userAgentHeader.contains("FrejaEidClient/"));
        Assertions.assertTrue(userAgentHeader.contains("Java/"));
        Assertions.assertFalse(userAgentHeader.contains("%version%"));
    }

}
