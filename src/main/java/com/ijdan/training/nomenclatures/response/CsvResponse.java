package com.ijdan.training.nomenclatures.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvResponse implements IFormatter {
    public String transform(HashMap<String, Object> response){
        String type;
        List<String> r;
        List<String> resp = new ArrayList<>();

        List<Map> elements;
        for (String key : response.keySet()) {
            type = response.get(key).getClass().getSimpleName();
            if (type.equals("String")){
                //Le sommaire
                resp.add('"' + response.get(key).toString().replace('"', '\"') + '"' + "\n");

            }else {
                //Les éléments
                elements = (ArrayList)response.get(key);
                for(Map element : elements){
                    r = new ArrayList<>();
                    for (Object k : element.keySet()) {
                        r.add('"'+ element.get(k).toString().replace('"', '\"') + '"');
                    }
                    resp.add( String.join(";", r) );
                    resp.add("\n");
                }
            }
        }

        return String.join("", resp);
    }
}
