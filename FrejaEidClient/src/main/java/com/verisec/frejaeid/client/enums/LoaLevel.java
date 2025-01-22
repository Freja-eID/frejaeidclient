package com.verisec.frejaeid.client.enums;

public enum LoaLevel {
    LOA1("LOA1"),
    LOA2("LOA2"),
    LOA3("LOA3"),
    LOA3NR("LOA3-NR");

    private final String level;

    LoaLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }
}
