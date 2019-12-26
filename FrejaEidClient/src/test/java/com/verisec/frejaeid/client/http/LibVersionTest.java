package com.verisec.frejaeid.client.http;

import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import org.junit.Assert;
import org.junit.Test;

public class LibVersionTest {

    @Test
    public void getLibVersionTest() throws FrejaEidClientInternalException {
        HttpService httpService = new HttpService(null, 0, 0);
        String libVersion = httpService.getLibVersion();
        Assert.assertNotNull(libVersion);
        Assert.assertNotEquals("", libVersion);
        Assert.assertNotEquals("${project.version}", libVersion);
    }

}
