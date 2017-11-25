package com.ijdan.training.nomenclatures.repository;

import com.ijdan.training.nomenclatures.domain.Cache;
import com.ijdan.training.nomenclatures.domain.Clause;
import com.ijdan.training.nomenclatures.domain.Nomenclature;
import com.ijdan.training.nomenclatures.domain.PrepareResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class H2Repository implements IRepository  {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrepareResponse.class);

    private String query = "select %s from %s where %s order by %s %s %s";
    private String total = "select count(*) as total from %s where %s";

    @Autowired
    private DatabasesProperties databasesProperties;
    public H2Repository(DatabasesProperties databasesProperties) {
        this.databasesProperties = databasesProperties;
    }

    @Override
    public List<Map> findAllItems(Nomenclature nomenclatureConfig, HashMap<String, String> selectedFields, String sortField, String sortSens, String paginPacket, String offset) {
        String limit = "";
        if(nomenclatureConfig.getPaging().isEnabled()){
            limit = "limit " + offset + ", " + paginPacket;
        }

        List<String> where = new ArrayList<>();
        List<Clause> clauses = nomenclatureConfig.getClause();
        if (clauses.size() > 0){
            for (Clause c : clauses) {
                where.add(c.getName() + " IN ('"+ String.join("', '", c.getValues()) +"') ");
            }
        }else {
            where.add("1=1");
        }
        String req = String.format(query,
                                    String.join(", ", selectedFields.keySet()),
                                    nomenclatureConfig.getDbTable(),
                                    String.join(" and ", where),
                                    selectedFields.get(sortField),
                                    sortSens,
                                    limit);
        LOGGER.warn(req);
        ResultSet rs = this.execution(req);
        if (rs != null){
            try {
                List<Map> items = new ArrayList<>();
                while (rs.next()) {
                    Map item = new HashMap<String, String>();
                    for (String sf : selectedFields.keySet()){
                        //resource attribute <> value
                        item.put(selectedFields.get(sf), rs.getString(sf));
                    }
                    items.add(item);
                }
                return items;

            } catch (SQLException e) {
                LOGGER.error("Aucune donnée trouvée : ?", e.getMessage());
            }
        }
        return null;


    }

    @Override
    public List<Map> count(Nomenclature nomenclatureConfig) {
        String req = String.format(total, nomenclatureConfig.getDbTable(), "1=1");
        return null;
    }

    @Override
    public Statement connexion() {
        EmbeddedDatabase db;
        LOGGER.info("Utilisation de la base H2 sans raffraîchissement");
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .build();
        try {
            return db
                    .getConnection(databasesProperties.getH2().getUsername(), databasesProperties.getH2().getPassword())
                    .createStatement();

        } catch (SQLException e) {
            LOGGER.error("Error de connexion à la base de données: ?", e.getMessage());
            return null;
        }


    }

    @Override
    public ResultSet execution(String query) {
        try {
            return this
                    .connexion()
                    .executeQuery(query);
        } catch (SQLException e) {
            LOGGER.error("Error d'exécution de la requete : " + query, e.getMessage());
            return null;
        }

    }
}
