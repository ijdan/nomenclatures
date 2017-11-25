package com.ijdan.training.nomenclatures.repository;

import com.ijdan.training.nomenclatures.domain.Cache;
import com.ijdan.training.nomenclatures.domain.Nomenclature;
import com.ijdan.training.nomenclatures.repository.DatabasesProperties;


import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IRepository {
    static String query = "select ? from ? where ? ? ?";

    public List<Map> findAllItems(Nomenclature nomenclatureConfig, HashMap<String, String> selectedColumn, String sortField, String sortSens, String paginPacket, String offset);

    public List<Map> count(Nomenclature nomenclatureConfig);

    public Statement connexion ();

    public ResultSet execution (String query);

}
