package com.ijdan.training.nomenclatures.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {
    private HashMap<String, String> selectedFields;
    private String sortField;
    private String sortSens;
    private String paginPacket;
    private String offset;

    private Nomenclature nomenclatureConfig;

    public Request(Nomenclature nomenclatureConfig, List<String> selectedFields, String sortField, String sortSens, String paginPacket, String offset) {
        this.nomenclatureConfig = nomenclatureConfig;
        /**
         * Validation des RequestParam
         * Evite les éventuelles injections SQL
         * */
        //Validation des attributs souhaités en sorite
        this.setSelectedFields(selectedFields);
        //Validation l'attribut sur lequel le tri est permis
        this.setSortField(nomenclatureConfig.getSort().getSortField (sortField));
        //Validation du sens du tri
        this.setSortSens(nomenclatureConfig.getSort().getSortSens (sortSens));
        //Validation du nombre d'éléments par appels souhaité par l'appelant
        this.setPagingPacket (paginPacket);
        //Validation de l'offset : le positionnement du paquet à retourner
        this.setOffset (offset);
    }

    public HashMap<String, String> getSelectedFields() {
        return selectedFields;
    }

    public void setSelectedFields(List<String> selectedFields) {
        List<String> fields = new ArrayList<>();
        List<String> outputKeys = new ArrayList<>();

        for (Map.Entry<String, String> key : nomenclatureConfig.getOutput().entrySet()) {
            fields.add( key.getKey() );

            if (selectedFields != null && selectedFields.contains(key.getKey())) {
                outputKeys.add( key.getKey() );
            }
        }
        if (outputKeys.size() > 0){
            selectedFields = outputKeys;
        }else {
            selectedFields = fields;
        }
        HashMap selectedColumn = new HashMap<String, String>();
        for(String sf : selectedFields){
            selectedColumn.put(this.nomenclatureConfig.getOutput().get(sf), sf);
        }

        this.selectedFields = selectedColumn;
    }

    public void setPagingPacket (String paginPacket){
        String packet;
        try{
            packet = Integer.valueOf(paginPacket).toString();
        }catch (NumberFormatException e){
            packet = nomenclatureConfig.getPaging().getPacket();

        }
        if(packet.equals("0")){
            packet = nomenclatureConfig.getPaging().getPacket();
        }
        this.paginPacket = packet;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortSens() {
        return sortSens;
    }

    public void setSortSens(String sortSens) {
        this.sortSens = sortSens;
    }

    public String getPaginPacket() {
        return paginPacket;
    }

    public void setPaginPacket(String paginPacket) {
        this.paginPacket = paginPacket;
    }

    public void setOffset(String offset) {
        try{
            this.offset = Integer.valueOf(offset).toString();
        }catch (NumberFormatException e){
            this.offset = "0";
        }
    }

    public String getOffset() {
        return offset;
    }
}
