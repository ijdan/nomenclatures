package com.ijdan.training.nomenclatures.repository;

import com.ijdan.training.nomenclatures.domain.Nomenclature;
import com.ijdan.training.nomenclatures.domain.Request;


import java.sql.ResultSet;
import java.sql.Statement;

public interface IRepository {
    static String query = "select ? from ? where ? ? ?";

    public ResultSet findAllItems(Nomenclature nomenclatureConfig, Request request);
    public int count(Nomenclature nomenclatureConfig);
    public ResultSet findItemById(Nomenclature nomenclatureConfig, String id, Request request);

    public Statement connexion ();

    public ResultSet execution (String query);

}
