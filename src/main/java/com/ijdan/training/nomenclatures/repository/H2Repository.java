package com.ijdan.training.nomenclatures.repository;

import com.ijdan.training.nomenclatures.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class H2Repository implements IRepository  {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2Repository.class);

    private String query = "select %s from %s where %s order by %s %s %s";
    private String total = "select count(*) as total from %s where %s";

    @Autowired
    private DatabasesProperties databasesProperties;
    public H2Repository(DatabasesProperties databasesProperties) {
        this.databasesProperties = databasesProperties;
    }

    @Override
    public ResultSet findAllItems(Nomenclature nomenclatureConfig, Request request) {
        String limit = "";
        if(nomenclatureConfig.getPaging().isEnabled()){
            limit = "limit " + request.getOffset() + ", " + request.getPaginPacket();
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
                                    String.join(", ", request.getSelectedFields().keySet()),
                                    nomenclatureConfig.getDbTable(),
                                    String.join(" and ", where),
                                    request.getSelectedFields().get(request.getSortField()),
                                    request.getSortSens(),
                                    limit);
        LOGGER.warn(req);
        return this.execution(req);
    }

    @Override
    public int count(Nomenclature nomenclatureConfig) {
        String req = String.format(total, nomenclatureConfig.getDbTable(), "1=1");
        ResultSet rs = this.execution(req);
        if (rs != null) {
            try {
                if (rs.next()) {
                    return Integer.valueOf(rs.getString("total"));
                }
            } catch (SQLException e) {
                LOGGER.error("Aucune donnée trouvée : ?", e.getMessage());
            }
        }
        return 0;
    }

    @Override
    public ResultSet findItemById(Nomenclature nomenclatureConfig, String id, Request request) {
        List<String> where = new ArrayList<>();
        where.add(nomenclatureConfig.getPk() + " = " + id);

        List<Clause> clauses = nomenclatureConfig.getClause();
        if (clauses.size() > 0){
            for (Clause c : clauses) {
                where.add(c.getName() + " IN ('"+ String.join("', '", c.getValues()) +"') ");
            }
        }

        String req = String.format(query,
                String.join(", ", request.getSelectedFields().keySet()),
                nomenclatureConfig.getDbTable(),
                String.join(" and ", where),
                nomenclatureConfig.getPk(),
                request.getSortSens(),
                ""
                );
        LOGGER.warn(req);
        return this.execution(req);
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
            return connexion().executeQuery(query);
        } catch (SQLException e) {
            LOGGER.error("Error d'exécution de la requete : {} / Message : {}", query, e.getMessage());
            return null;
        }

    }
}
