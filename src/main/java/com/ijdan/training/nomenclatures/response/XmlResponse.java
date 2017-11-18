package com.ijdan.training.nomenclatures.response;

import com.ijdan.training.nomenclatures.domain.PrepareResponse;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class XmlResponse implements IFormatter {

    public String transform(HashMap<String, Object> response) {
        final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(XmlResponse.class);
        String type;
        List<String> resp = new ArrayList<>();
        resp.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        List<Map> elements;
        for (String key : response.keySet()) {
            type = response.get(key).getClass().getSimpleName();
            if (type.equals("String")){
                //Le sommaire
                resp.add("<"+key+">"+ response.get(key) +"</"+key+">");

            }else {
                //Les éléments
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
