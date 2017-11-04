package com.ijdan.training.nomenclatures.domain;

import java.util.ArrayList;
import java.util.Map;

public class Response {
    private String elementsReturn;
    private String total;
    private ArrayList pays = new ArrayList<Map>();

    public String getElementsReturn() {
        return elementsReturn;
    }

    public void setElementsReturn(String elementsReturn) {
        this.elementsReturn = elementsReturn;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ArrayList getPays() {
        return pays;
    }

    public void setPays(ArrayList pays) {
        this.pays = pays;
    }
}
