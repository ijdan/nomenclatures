package com.ijdan.training.nomenclatures.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private HashMap<String, Object> response;

    public Response(HashMap<String, Object> response) {
        this.response = response;
    }

    public HashMap<String, Object> getResponse() {
        return response;
    }

    public void setResponse(HashMap<String, Object> response) {
        this.response = response;
    }

    public String getXMLResponse (){
        String type;
        List<String> resp = new ArrayList<>();
        resp.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        List<Map> elements;
        for (String key : this.getResponse().keySet()) {
            type = this.getResponse().get(key).getClass().getSimpleName();
            if (type.equals("String")){
                //Le sommaire
                resp.add("<"+key+">"+ this.getResponse().get(key) +"</"+key+">");

            }else {
                //Les éléments
                elements = (ArrayList)this.getResponse().get(key);
                resp.add("<elements>");
                for(Map element : elements){
                    resp.add("<element>");
                    for (Object k : element.keySet()) {
                        resp.add("<"+k+">"+ element.get(k) +"</"+k+">");
                    }
                    resp.add("</element>");
                }
                resp.add("</elements>");
            }
        }

        return String.join("", resp);
    }

    public String getCSVResponse(){
        String type;
        List<String> resp = new ArrayList<>();
        List<String> head = new ArrayList<>();

        List<Map> elements;
        for (String key : this.getResponse().keySet()) {
            type = this.getResponse().get(key).getClass().getSimpleName();
            if (type.equals("String")){
                //Le sommaire
                head.add("<"+key+">"+ this.getResponse().get(key) +"</"+key+">");

            }else {
                //Les éléments
                elements = (ArrayList)this.getResponse().get(key);
                resp.add("<elements>");
                for(Map element : elements){
                    resp.add("<element>");
                    for (Object k : element.keySet()) {
                        resp.add("<"+k+">"+ element.get(k) +"</"+k+">");
                    }
                    resp.add("</element>");
                }
                resp.add("</elements>");
            }
        }

        return String.join("", resp);
    }

}
