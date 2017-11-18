package com.ijdan.training.nomenclatures.response;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonResponse implements IFormatter {
    public String transform(HashMap<String, Object> response) {
        return JSONObject.toJSONString(response);
    }
}
