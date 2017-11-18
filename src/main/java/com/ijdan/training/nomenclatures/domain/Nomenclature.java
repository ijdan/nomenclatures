package com.ijdan.training.nomenclatures.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Nomenclature {
    private String resourceName;
    private String enbled;
    private String dbTable;
    private String pk;
    private Map<String, String> output = new HashMap<>();
    private Paging paging;
    private Sort sort;
    private String enabledFieldsSelection;
    private List<Clause> clause;
    private Cache cache;
    private Summary summary;
    private List<String> produces;

    public Nomenclature() {}

    public String getResourceName() {
        return resourceName;
    }
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getEnbled() {
        return enbled;
    }

    public void setEnbled(String enbled) {
        this.enbled = enbled;
    }
    public boolean isEnabled(){
        return this.getEnbled().equals("1");
    }

    public String getDbTable() {
        return dbTable;
    }
    public void setDbTable(String dbTable) {
        this.dbTable = dbTable;
    }

    public String getPk() {
        return pk;
    }
    public void setPk(String pk) {
        this.pk = pk;
    }

    public Map<String, String> getOutput() {
        return output;
    }
    public void setOutput(Map<String, String> output) {
        this.output = output;
    }

    public Paging getPaging() {
        return paging;
    }
    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public Sort getSort() {
        return sort;
    }
    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public String getEnabledFieldsSelection() {
        return enabledFieldsSelection;
    }
    public void setEnabledFieldsSelection(String enabledFieldsSelection) {
        this.enabledFieldsSelection = enabledFieldsSelection;
    }

    public List<Clause> getClause() {
        return clause;
    }
    public void setClause(List<Clause> clause) {
        this.clause = clause;
    }

    public List<String> getProduces() {
        return produces;
    }

    public void setProduces(List<String> produces) {
        this.produces = produces;
    }

    /**
     * Fixe les attributs de la ressource à restituer
     * */
    public List<String> getOutputKeys (List<String> selectedFields){
        List<String> fields = new ArrayList<String>();
        List<String> outputKeys = new ArrayList<String>();

        for (Map.Entry<String, String> key : this.getOutput().entrySet()) {
            fields.add( key.getKey() );

            if (selectedFields != null && selectedFields.contains(key.getKey())) {
                outputKeys.add( key.getKey() );
            }
        }
        if (outputKeys.size() > 0){
            return outputKeys;
        }else {
            return fields;
        }
    }

    /**
     * Fixe l'attibut sur lequel appliquer le tri
     * */
    public String getSortField (String sortFields){
        if ( sortFields !=  null && !sortFields.isEmpty() && this.getSort().getFields().contains(sortFields) ){
            return sortFields;
        }else {
            return this.getSort().getFields().get(0).toString();
        }
    }

    /**
     * Fixe le sens du tri
     * */
    public String getSortSens (String sortSens){
        if ( sortSens !=  null && !sortSens.isEmpty() && this.getSort().getSens().contains(sortSens) ){
            return sortSens;
        }else {
            return this.getSort().getSens().get(0).toString();
        }

    }

    /**
     * Fixe le nombre d'éléments par paquet
     * */
    public String getPagingPacket (String paginPacket){
        String packet;
        try{
            packet = Integer.valueOf(paginPacket).toString();
        }catch (NumberFormatException e){
            packet = this.getPaging().getPacket();
        }

        if(packet.equals("0")){
            packet = this.getPaging().getPacket();
        }
        return packet;
    }

    /**
     * Fixe le positionnement du paquet à demander
     * */
    public String getOffset(String offset) {
        try{
            return Integer.valueOf(offset).toString();
        }catch (NumberFormatException e){
            return "0";
        }
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public List<Map> getListMap (ResultSet rs, List<String> selectedFields) throws SQLException {
        List<Map> items = new ArrayList<>();

        int length = selectedFields.size();
        int cpt = 0;
        while (rs.next()) {
            cpt ++;
            Map item = new HashMap<String, String>();
            for (int i = 0; i < length; i++) {
                item.put(selectedFields.get(i), rs.getString(selectedFields.get(i)));
            }
            items.add(item);
        }

        return items;

    }


}