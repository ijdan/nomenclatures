package com.ijdan.training.nomenclatures.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IFormatter {
    public String transform(HashMap<String, Object> response);
}
