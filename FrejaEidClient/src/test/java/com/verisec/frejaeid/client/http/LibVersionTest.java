package com.verisec.frejaeid.client.http;

import org.junit.Assert;
import org.junit.Test;

public class LibVersionTest {

    @Test
    public void getLibVersionTest() {
        HttpService httpService = new HttpService(null, 0, 0);
        String libVersion = httpService.getLibVersion();
        Assert.assertNotNull(libVersion);
        Assert.assertNotEquals("", libVersion);
        Assert.assertNotEquals("${project.version}", libVersion);
    }

}
