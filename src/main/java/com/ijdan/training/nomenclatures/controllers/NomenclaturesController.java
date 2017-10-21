package com.ijdan.training.nomenclatures.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ijdan.training.nomenclatures.domain.Nomenclature;
import com.ijdan.training.nomenclatures.domain.PrepareRequest;
import com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling.InternalErrorException;
import com.ijdan.training.nomenclatures.repository.H2Connection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.sql.*;
import java.util.List;

@RestController
@RequestMapping(value = "/T1.0/nomenclatures")
public class NomenclaturesController {
    private Nomenclature nomenclature;
    private static String LAST_NOMENCLATURE = "";


    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/{nomenclatureName}", method = RequestMethod.GET, produces = "application/json")
    JSONObject showNomenclature (
            @PathVariable(value="nomenclatureName") String nomenclatureName,

            @RequestParam(value="selectedFields", required = false) List<String> selectedFields,
            @RequestParam(value="sortField", required = false) String sortField,
            @RequestParam(value="sortSens", required = false) String sortSens,
            @RequestParam(value="paginPacket", required = false) String paginPacket,
            @RequestParam(value="offset", required = false) String offset
    ) {

        String pathYMLFile = "nomenclatures/" + nomenclatureName + ".yaml";
        LOGGER.warn("nomenclatures/" + nomenclatureName + ".yaml");

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File file;
        try {
            file = new File( getClass().getClassLoader().getResource(pathYMLFile).getFile() );
            /**
             * Mapper le fichier YAML avec le POJO nomenclature
             * */
            nomenclature = mapper.readValue(file, Nomenclature.class);
        }catch (Exception e) {
            throw new InternalErrorException("Err.00001", "!! Mapping Error with ["+ nomenclatureName +"] resource !!");
        }

          //  if (!LAST_NOMENCLATURE.equals(nomenclatureName)) {

        /**
         * Validation des RequestParam
         * */
        //Validation des attributs souhaités en sorite
        selectedFields = nomenclature.getOutputKeys(selectedFields);

        //Validation l'attribut sur lequel le tri est permis
        sortField = nomenclature.getSortField (sortField);

        //Validation du sens du tri
        sortSens = nomenclature.getSortSens (sortSens);

        //Validation du nombre d'éléments par appels souhaité par l'appelant
        paginPacket = nomenclature.getPagingPacket (paginPacket);

        //Validation de l'offset : le positionnement du paquet à retourner
        offset = nomenclature.getOffset (offset);

        JSONObject JSonO = new JSONObject();
        PrepareRequest prepareRequest = new PrepareRequest(nomenclature);
        try {
            H2Connection h2c = new H2Connection(nomenclature.getCache());
            ResultSet rs = h2c.executeQuery(prepareRequest.getCallRequest(selectedFields, sortField, sortSens, paginPacket, offset));
            JSONArray JSonA = nomenclature.toJSONArray(rs, selectedFields);

            if (nomenclature.getSummary().isEnabled()){
                JSonO.put(nomenclature.getSummary().getNbElementsAttributeName(), JSonA.size());

                rs = h2c.executeQuery( prepareRequest.getTotalRequest() );
                if (rs.next()) {
                    JSonO.put(nomenclature.getSummary().getTotalAttributeName(), rs.getString("TOTAL"));
                }
            }
            JSonO.put(nomenclature.getResourceName(), JSonA);

        }catch (SQLException e){
            throw new InternalErrorException("Err.00002", "!! Error data base access !!");
        }

        return JSonO;
    }

}