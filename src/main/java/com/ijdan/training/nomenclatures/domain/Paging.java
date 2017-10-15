package com.ijdan.training.nomenclatures.domain;

public class Paging {
    private String enabled;
    private String packet;

    public Paging() {
    }

    public String isEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getPacket() {
        return packet;
    }

    public void setPacket(String packet) {
        this.packet = packet;
    }
}
