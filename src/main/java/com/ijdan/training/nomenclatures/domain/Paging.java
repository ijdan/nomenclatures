package com.ijdan.training.nomenclatures.domain;

public class Paging {
    private String enabled;
    private String packet;

    public Paging() {
    }

    public Boolean isEnabled() {
        return this.getEnabled().equals("1");
    }
    public String getEnabled() {
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
