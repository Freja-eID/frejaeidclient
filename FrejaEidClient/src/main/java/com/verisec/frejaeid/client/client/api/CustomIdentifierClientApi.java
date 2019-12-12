package com.verisec.frejaeid.client.client.api;

import com.verisec.frejaeid.client.beans.usermanagement.customidentifier.delete.DeleteCustomIdentifierRequest;
import com.verisec.frejaeid.client.beans.usermanagement.customidentifier.set.SetCustomIdentifierRequest;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;

/**
 * Performs actions with custom identifier.
 *
 */
public interface CustomIdentifierClientApi {

    /**
     * Sets a custom identifier for a specific user. The existing user
     * information for that user in the Freja eID service must be passed as a
     * parameter in request.
     *
     * @param setCustomIdentifierRequest instance of
     * {@linkplain SetCustomIdentifierRequest} with as said before, existing
     * user information and custom identifier to be set.
     *
     * @throws FrejaEidClientInternalException if internal validation of request
     * fails.
     * @throws FrejaEidException if server returns an error.
     */
    public void set(SetCustomIdentifierRequest setCustomIdentifierRequest) throws FrejaEidClientInternalException, FrejaEidException;

    /**
     * Deletes a custom identifier for a specific user.
     *
     * @param deleteCustomIdentifierRequest contains custom identifier to be
     * deleted for the end user. Must exist within the requesting relying party
     * system inside the Freja eID service.
     * @throws FrejaEidClientInternalException if internal validation of request
     * fails.
     * @throws FrejaEidException if server returns an error.
     */
    public void delete(DeleteCustomIdentifierRequest deleteCustomIdentifierRequest) throws FrejaEidClientInternalException, FrejaEidException;

}
