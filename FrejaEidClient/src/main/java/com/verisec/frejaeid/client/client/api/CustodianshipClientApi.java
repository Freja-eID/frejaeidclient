package com.verisec.frejaeid.client.client.api;

import com.verisec.frejaeid.client.beans.custodianship.get.GetUserCustodianshipStatusRequest;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;

public interface CustodianshipClientApi {
    /**
     * Gets the custodianship status for a specific user.
     *
     * @param getUserCustodianshipStatusRequest instance of
     *                                   {@linkplain GetUserCustodianshipStatusRequest} with the relying party ID.
     *                                          and the users CountryCode and CRN as parameters.
     * @return user custodianship status (UNKNOWN or USER_HAS_CUSTODIAN)
     *
     * @throws FrejaEidClientInternalException if internal validation of request
     *                                         fails.
     * @throws FrejaEidException               if server returns an error.
     */
    public String getUserCustodianshipStatus(GetUserCustodianshipStatusRequest getUserCustodianshipStatusRequest)
            throws FrejaEidClientInternalException, FrejaEidException;
}
