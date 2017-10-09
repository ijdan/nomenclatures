package com.ijdan.training.nomenclatures.domain;

public class Paging {
    private String enable;
    private String packet;

    public Paging() {
    }

    public String getPacket() {
        return packet;
    }

    public void setPacket(String packet) {
        this.packet = packet;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

}
