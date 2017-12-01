package com.ijdan.training.nomenclatures.response;

import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlResponse implements IFormatter {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(XmlResponse.class);

    public String transform(HashMap<String, Object> response) {
        final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(XmlResponse.class);
        String type;
        List<String> resp = new ArrayList<>();
        resp.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        List<Map> elements;
        for (String key : response.keySet()) {
            type = response.get(key).getClass().getSimpleName();
            LOGGER.warn("Type >>" + type);
            if (type.equals("Integer")){
                //Le sommaire
                resp.add("<"+key+">"+ response.get(key) +"</"+key+">");

            }else {
                //Les éléments
                LOGGER.warn(">>><" + response.get(key).toString());
                elements = (ArrayList)response.get(key);
                resp.add("<items>");
                for(Map element : elements){
                    resp.add("<item>");
                    for (Object k : element.keySet()) {
                        resp.add("<"+k+">"+ element.get(k).toString() +"</"+k+">");
                    }
                    resp.add("</item>");
                }
                resp.add("</items>");
            }
        }

        return String.join("", resp);

    }

}
