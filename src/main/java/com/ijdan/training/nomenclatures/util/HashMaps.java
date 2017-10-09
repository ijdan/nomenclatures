package com.ijdan.training.nomenclatures.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashMaps {

    public HashMaps() {
    }

    public List hashMapsIntersectWithList (HashMap<String, String> hashmap, List<String> list){
        //Conserve les élements de la list quand il est le trouve dans les values de hashmap
        List intersect = new ArrayList<String>();
        for (Map.Entry<String, String> entry : hashmap.entrySet()) {
            if (list.contains(entry.getKey())){
                intersect.add(entry.getValue());
            }
        }
        return intersect;
    }

    public List<String> getHashMapKeys (HashMap<String, String> hashmap){
        List<String> keys = new ArrayList<String>();
        for (Map.Entry<String, String> entry : hashmap.entrySet()) {
            keys.add(entry.getKey());
        }
        return keys;
    }

    public List<String> getHashMapValues (HashMap<String, String> hashmap){
        List<String> values = new ArrayList<String>();
        for (Map.Entry<String, String> entry : hashmap.entrySet()) {
            values.add(entry.getValue());
        }
        return values;
    }

    public String getKeyFromValue (HashMap<String, String> hashmap, String _value){
        for(Map.Entry<String, String> entry : hashmap.entrySet()) {
            if (_value.equals(entry.getValue())){
                return entry.getKey();
            }
        }
        return null;
    }

    public String getValueFromKey (HashMap<String, String> hashmap, String _key){
        for(Map.Entry<String, String> entry : hashmap.entrySet()) {
            if (_key.equals(entry.getKey())){
                return entry.getValue();
            }
        }
        return null;
    }
}
