package com.verisec.frejaeid.client.util;

import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import org.apache.commons.codec.binary.Base64;
import java.nio.charset.StandardCharsets;

public class UserInfoUtil {

    public static String convertSsnUserInfo(SsnUserInfo ssnUserInfo) throws FrejaEidClientInternalException {
        JsonService jsonService = new JsonService();
        try {
            String jsonSsnUserInfo = jsonService.serializeToJson(ssnUserInfo);
            return Base64.encodeBase64String(jsonSsnUserInfo.getBytes(StandardCharsets.UTF_8));
        } catch (FrejaEidClientInternalException ex) {
            throw new FrejaEidClientInternalException("Failed to serialize user info.", ex);
        }
    }
}
