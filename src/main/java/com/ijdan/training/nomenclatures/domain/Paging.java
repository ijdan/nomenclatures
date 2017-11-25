package com.ijdan.training.nomenclatures.domain;

public class Paging {
    private String enabled;
    private String packet;

    public Paging() {
    }

    public Boolean isEnabled() {
        return this.getEnabled().equals("1");
    }
    private String getEnabled() {
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

    /**
     * Fixe le nombre d'éléments par paquet
     * */
    public String getPagingPacket (String paginPacket){
        String packet;
        try{
            packet = Integer.valueOf(paginPacket).toString();
        }catch (NumberFormatException e){
            packet = this.getPacket();
        }

        if(packet.equals("0")){
            packet = this.getPacket();
        }
        return packet;
    }
}
