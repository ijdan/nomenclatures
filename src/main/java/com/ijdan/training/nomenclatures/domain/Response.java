package com.ijdan.training.nomenclatures.domain;

import com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling.ResourceNotFoundException;
import com.ijdan.training.nomenclatures.repository.DatabasesProperties;
import com.ijdan.training.nomenclatures.repository.H2Connection;
import com.ijdan.training.nomenclatures.repository.H2Repository;
import com.ijdan.training.nomenclatures.repository.IRepository;
import com.ijdan.training.nomenclatures.response.CsvResponse;
import com.ijdan.training.nomenclatures.response.IFormatter;
import com.ijdan.training.nomenclatures.response.JsonResponse;
import com.ijdan.training.nomenclatures.response.XmlResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class Response {
    private static final Logger LOGGER = LoggerFactory.getLogger(Response.class);
    private static List<String> clausesPart = new ArrayList<>();

    private Nomenclature nomenclatureConfig;

    @Autowired
    private H2Connection h2connection;

    @Autowired
    DatabasesProperties databasesProperties;

    public Response(DatabasesProperties databasesProperties) {
        this.databasesProperties = databasesProperties;
    }

    public List<Map> getCollection(Nomenclature nomenclatureConfig, Request request){
        this.nomenclatureConfig = nomenclatureConfig;

        try {
            h2connection.createStatement(nomenclatureConfig.getCache());
        }catch (SQLException e){
            throw new ResourceNotFoundException("Err.00003", "!! Nomenclature inexistante !!");
        }

        IRepository repository;
        repository = new H2Repository(databasesProperties); //Selon les cas. A voir plus tard.

        ResultSet rs = repository.findAllItems(nomenclatureConfig, request);
        if (rs != null){
            try {
                List<Map> items = new ArrayList<>();
                while (rs.next()) {
                    Map item = new HashMap<String, String>();
                    for (String sf : request.getSelectedFields().keySet()){
                        //resource attribute <> value
                        item.put(request.getSelectedFields().get(sf), rs.getString(sf));
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

    public List<Map> getItem (Nomenclature nomenclatureConfig, String id, Request request) {
        this.nomenclatureConfig = nomenclatureConfig;

        IRepository repository;
        repository = new H2Repository(databasesProperties); //Selon les cas. A voir plus tard.

        ResultSet rs = repository.findItemById(nomenclatureConfig, id, request);
        if (rs != null){
            try {
                List<Map> items = new ArrayList<>();
                while (rs.next()) {
                    Map item = new HashMap<String, String>();
                    for (String sf : request.getSelectedFields().keySet()){
                        //resource attribute <> value
                        item.put(request.getSelectedFields().get(sf), rs.getString(sf));
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

    public ResponseEntity<String> getAdaptedContentType(HashMap<String, Object> response,  HttpServletRequest httpRequest){
        HttpHeaders httpHeaders= new HttpHeaders();
        IFormatter output;

        switch (httpRequest.getHeader("accept")){
            case MediaType.APPLICATION_XML_VALUE:
                httpHeaders.setContentType(MediaType.APPLICATION_XML);
                output = new XmlResponse();
                break;

            case MediaType.TEXT_PLAIN_VALUE:
            case "text/csv":
                httpHeaders.setContentType(MediaType.TEXT_PLAIN);
                output = new CsvResponse();
                break;

            default:
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                output = new JsonResponse();
        }

        return new ResponseEntity<>(output.transform(response), httpHeaders, HttpStatus.OK);
    }

    public int getQueryTotal(Nomenclature nomenclatureConfig){
        IRepository repository;
        repository = new H2Repository(databasesProperties); //Selon les cas. A voir plus tard.

        return repository.count(nomenclatureConfig);
    }

}