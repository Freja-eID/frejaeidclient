package com.verisec.frejaeid.client.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LibVersionTest {

    @Test
    public void getLibVersionTest() {
        HttpService httpService = new HttpService(null, 0, 0);
        String libVersion = httpService.getLibVersion();
        Assertions.assertNotNull(libVersion);
        Assertions.assertNotEquals("", libVersion);
        Assertions.assertNotEquals("${project.version}", libVersion);
    }

}
