package com.ijdan.training.nomenclatures.repository;

import com.ijdan.training.nomenclatures.domain.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class H2Connection {
    private static EmbeddedDatabase db = null;
    private static Statement stmt = null;
    static String FILE_DROP_DB = "/db/sql/drop-db.sql";
    static String FILE_CREATE_DB = "/db/sql/create-db.sql";
    static String FILE_INSERT_DATA = "/db/sql/insert-data.sql";
    static String USER_NAME = "sa";
    static String PASSWORD = "";
    static boolean LOADED = false;
    static long LOADED_TIME = 0;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private String username;

    private DatabasesProperties databasesProperties = new DatabasesProperties();


    public H2Connection(Cache cache) throws SQLException {
        LOGGER.warn("username : " + databasesProperties.getH2().toString() );


        long currentTimeMillis = System.currentTimeMillis();
        int numberOfSecondsPassed = (int) ((currentTimeMillis - LOADED_TIME)/1000);


        if (cache.isEnabled() && numberOfSecondsPassed > Integer.parseInt(cache.getExpiration()) )
        {
            LOGGER.warn("Chargement des données depuis la source maître : raffraîchissement de la H2");
            LOADED_TIME = currentTimeMillis;
            db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                    .addScript(FILE_DROP_DB)
                    .addScript(FILE_CREATE_DB)
                    .addScript(FILE_INSERT_DATA)
                    .build();
        }else {
            LOGGER.warn("Utilisation de la base H2 sans raffraîchissement");
            db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                    .build();

        }

        Connection con = db.getConnection(USER_NAME, PASSWORD);
        stmt = con.createStatement();
    }


    public ResultSet executeQuery (String sql) throws SQLException {
        return getStmt().executeQuery(sql);
    }

    public static Statement getStmt() {
        return stmt;
    }

    public static void setStmt(Statement stmt) {
        H2Connection.stmt = stmt;
    }
}
