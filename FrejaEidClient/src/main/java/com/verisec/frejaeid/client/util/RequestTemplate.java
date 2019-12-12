package com.verisec.frejaeid.client.util;

public enum RequestTemplate {

    INIT_AUTHENTICATION("initAuthRequest={0}"),
    AUTHENTICATION_RESULT_TEMPLATE("getOneAuthResultRequest={0}"),
    AUTHENTICATION_RESULTS_TEMPLATE("getAuthResultsRequest={0}"),
    CANCEL_AUTHENTICATION_TEMPLATE("cancelAuthRequest={0}"),
    INIT_SIGN_TEMPLATE("initSignRequest={0}"),
    SIGN_RESULT_TEMPLATE("getOneSignResultRequest={0}"),
    SIGN_RESULTS_TEMPLATE("getSignResultsRequest={0}"),
    CANCEL_SIGN_TEMPLATE("cancelSignRequest={0}"),
    INIT_ADD_ORGANISATION_ID_TEMPLATE("initAddOrganisationIdRequest={0}"),
    ORGANISATION_ID_RESULT_TEMPLATE("getOneOrganisationIdResultRequest={0}"),
    DELETE_ORGANINSATION_ID_TEMPLATE("deleteOrganisationIdRequest={0}"),
    CANCEL_ADD_ORGANISATION_ID_TEMPLATE("cancelAddOrganisationIdRequest={0}"),
    RELYING_PARTY_ID("relyingPartyId={0}"),
    SET_CUSTOM_IDENITIFIER_TEMPLATE("setCustomIdentifierRequest={0}"),
    DELETE_CUSTOM_IDENTIFIER_TEMPLATE("deleteCustomIdentifierRequest={0}");

    private final String template;

    private RequestTemplate(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

}
