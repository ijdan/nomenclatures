package com.ijdan.training.nomenclatures.domain;

import com.ijdan.training.nomenclatures.util.HashMaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Prepare {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    List<String> fields;
    String sort = "";
    String sens = "ASC";
    String offset = "0";
    String item = "";

    StringBuilder query = new StringBuilder();

    public Prepare(List<String> _fields, String _sort, String _sens, String _offset, String _item) {
        this.setFields(_fields);
        this.setSort(_sort);
        this.setSens(_sens);
        this.setOffset(_offset);
        this.setItem(_item);
    }

    public String prepareQuery(Structure nomenclature) {
        HashMaps hashmaps = new HashMaps();

        HashMap nomenclatureOutPut = nomenclature.getOutput();
        List<String> output = hashmaps.hashMapsIntersectWithList(nomenclatureOutPut, this.getFields());
        if (output.size() == 0) {
            output = hashmaps.getHashMapValues(nomenclatureOutPut);
        }

        query.append("SELECT " + String.join(", ", output) + " ");
        query.append("FROM nomenclatures." + nomenclature.getTable() + " ");

        if (!this.getItem().isEmpty()){
            query.append("WHERE " + nomenclature.getId() + " = '" + this.getItem() + "' " );
        }
        if (!this.getSort().isEmpty() &&
                hashmaps.getHashMapKeys(nomenclatureOutPut).contains(this.getSort()) &&
                nomenclature.getSort() != null &&
                nomenclature.getSort().getEnable().equals("true")
                ) {

            LOGGER.info("Tri possible sur {" + this.getSort() + "}" + hashmaps.getKeyFromValue(nomenclatureOutPut, this.getSort()));
            if (hashmaps.getKeyFromValue(nomenclatureOutPut, this.getSort()) != null) {
                query.append("ORDER BY " + hashmaps.getKeyFromValue(nomenclatureOutPut, this.getSort()) + " " + this.getSens() + " ");
            }
        }

        return query.toString();
    }


    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public String getSens() {
        return sens;
    }

    public void setSens(String _sens) {
        if (!_sens.equals("DESC")) {
            _sens = "ASC";
        }
        this.sens = _sens;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String _sort) {
        this.sort = _sort;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String _offset) {
        if (!_offset.isEmpty()) {
            this.offset = String.valueOf(Integer.parseInt(_offset));
        }
    }
    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

}
