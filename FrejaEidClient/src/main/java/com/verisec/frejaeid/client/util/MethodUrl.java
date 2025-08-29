package com.verisec.frejaeid.client.util;

public enum MethodUrl {

    AUTHENTICATION_INIT("/authentication/1.0/initAuthentication"),
    AUTHENTICATION_GET_RESULT("/authentication/1.0/getOneResult"),
    AUTHENTICATION_GET_RESULTS("/authentication/1.0/getResults"),
    AUTHENTICATION_CANCEL("/authentication/1.0/cancel"),
    ORGANISATION_AUTHENTICATION_INIT("/organisation/authentication/1.0/init"),
    ORGANISATION_AUTHENTICATION_GET_ONE_RESULT("/organisation/authentication/1.0/getOneResult"),
    ORGANISATION_AUTHENTICATION_GET_RESULTS("/organisation/authentication/1.0/getResults"),
    ORGANISATION_AUTHENTICATION_CANCEL("/organisation/authentication/1.0/cancel"),
    SIGN_INIT("/sign/1.0/initSignature"),
    SIGN_GET_RESULT("/sign/1.0/getOneResult"),
    SIGN_GET_RESULTS("/sign/1.0/getResults"),
    SIGN_CANCEL("/sign/1.0/cancel"),
    ORGANISATION_SIGN_INIT("/organisation/sign/1.0/init"),
    ORGANISATION_SIGN_GET_ONE_RESULT("/organisation/sign/1.0/getOneResult"),
    ORGANISATION_SIGN_GET_RESULTS("/organisation/sign/1.0/getResults"),
    ORGANISATION_SIGN_CANCEL("/organisation/sign/1.0/cancel"),
    ORGANISATION_ID_INIT_ADD("/organisation/management/orgId/1.0/initAdd"),
    ORGANISATION_ID_GET_RESULT("/organisation/management/orgId/1.0/getOneResult"),
    ORGANISATION_ID_DELETE("/organisation/management/orgId/1.0/delete"),
    ORGANISATION_ID_CANCEL_ADD("/organisation/management/orgId/1.0/cancelAdd"),
    ORGANISATION_ID_GET_ALL_USERS("/organisation/management/orgId/1.0/users/getAll"),
    ORGANISATION_ID_GET_ALL_USERS_V2("/organisation/management/orgId/1.0/users/getAllV2"),
    ORGANISATION_ID_UPDATE("/organisation/management/orgId/1.0/update"),
    CUSTOM_IDENTIFIER_SET("/user/manage/1.0/setCustomIdentifier"),
    CUSTOM_IDENTIFIER_DELETE("/user/manage/1.0/deleteCustomIdentifier"),
    QR_CODE_GENERATE("/qrcode/generate"),
    CUSTODIANSHIP_GET_USER_STATUS("/custodianship/user/1.0/getCustodianshipStatus");

    private final String url;

    private MethodUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }

}
