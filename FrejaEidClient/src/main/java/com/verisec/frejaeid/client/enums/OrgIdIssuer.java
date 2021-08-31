package com.verisec.frejaeid.client.enums;

public enum OrgIdIssuer {

    ANY ("ANY");

    private final String name;

    private OrgIdIssuer(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    @Override
    public String toString() { return "OrgIdIssuer{" + "name='" + name + '\'' + '}'; }
}
