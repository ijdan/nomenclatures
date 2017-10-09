package com.ijdan.training.nomenclatures.controllers;

import com.datastax.driver.core.*;
import com.ijdan.training.nomenclatures.domain.ErrorMessage;
import com.ijdan.training.nomenclatures.domain.Nomenclature;
import com.ijdan.training.nomenclatures.domain.Prepare;
import com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling.BadRequestException;
import com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling.ResourceNotFoundException;
import com.ijdan.training.nomenclatures.infrastructure.PropertiesCassandra;
import com.ijdan.training.nomenclatures.util.HashMaps;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@RestController
@RequestMapping(value = "/v0.1/nomenclatures")

public class NomenclatureController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private PropertiesCassandra properties = new PropertiesCassandra();

    /**
     * Construction de la Collection Nomenclatures
     * */
    @RequestMapping(value = "/{name}")
    public @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(   code = HttpURLConnection.HTTP_BAD_REQUEST,
                    message = "Mauvaise requête",
                    response = ErrorMessage.class),
            @ApiResponse(   code = HttpURLConnection.HTTP_NOT_FOUND,
                    message = "Nomenclature non existante",
                    response = ErrorMessage.class),
            @ApiResponse(   code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                    message = "Erreur interne",
                    response = ErrorMessage.class),
        }
    )

    @ApiOperation(  value = "Consulter les valeurs d'une nomenclature",
                    produces = "application/json",
                    httpMethod = "GET",
                    nickname = "getNomenclatureCollection",
                    tags = "LIST")
    ResponseEntity showNomenclature (
            @ApiParam(value = "Nom de la nomenclature", required = true) @PathVariable String name,
            @ApiParam(value = "Données souhaitées",name = "fields") @RequestParam(defaultValue = "", required = false) List<String> fields,
            @ApiParam(value = "Donnée triée", name = "sort") @RequestParam(defaultValue = "", required = false) String sort,
            @ApiParam(value = "Sens tri", allowableValues = "ASC, DESC", name = "sens") @RequestParam(defaultValue = "ASC", required = false) String sens,
            @ApiParam(value = "Paquet", name = "offset") @RequestParam(defaultValue = "0", required = false) String offset
            ) {
        //Initiliser la sortie
        HashMap jsonMap = new HashMap();

        //Localiser le fichier de paramètrage
        String file_name = "nomenclatures/" + name + ".yaml";
        File file;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            file = new File(classLoader.getResource(file_name).getFile());
        }catch (Exception e) {
            throw new ResourceNotFoundException("001", "Nomenclature not found");
        }

        if (!file.exists() || file.isDirectory()) {
            throw new ResourceNotFoundException("002", "Nomenclature not found");
        }

        //Mapper le paramètrage
        Nomenclature nomenclature;
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            nomenclature = mapper.readValue(file, Nomenclature.class);
        }catch (Exception e) {
            throw new BadRequestException("003", "Erreur interne");
        }

        LOGGER.info("Start prepare Query...");
        Prepare prepare = new Prepare(fields, sort, sens, offset, "");
        String query = prepare.prepareQuery(nomenclature.getNomenclatures());
        LOGGER.info("Préparation de l'exécution de la requête : "+ query);

        Session session;
        ResultSet results;
        try {
            session = Cluster.builder().addContactPoint(properties.getIP()).build().connect();
            results = session.execute(query);
        }catch (Exception e) {
            LOGGER.info("Préparation de l'exécution de la requête : "+ query);
            throw new BadRequestException("004", "Erreur interne");
        }

        LOGGER.info("Préparation de la sortie du résultat");

        HashMap hmap = new HashMap();
        HashMaps hashmaps = new HashMaps();
        ArrayList arrayList = new ArrayList();
        for (Row currentElement : results.all()) {
            int col = currentElement.getColumnDefinitions().size();
            hmap = new HashMap();
            for(int i = 0; i < col; i++){
                String field = currentElement.getColumnDefinitions().getName(i);
                hmap.put(hashmaps.getKeyFromValue(nomenclature.getNomenclatures().getOutput(), field), currentElement.getObject(field).toString());
            }
            arrayList.add(hmap);
        }

        return new ResponseEntity(arrayList, HttpStatus.OK);
    }

    /**
     * Construction de l'itème Nomenclatures
     * */
    @RequestMapping(value = "/{name}/{id}")
    public @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(   code = HttpURLConnection.HTTP_BAD_REQUEST,
                    message = "Mauvaise requête",
                    response = ErrorMessage.class),
            @ApiResponse(   code = HttpURLConnection.HTTP_NOT_FOUND,
                    message = "Itèmenon existant",
                    response = ErrorMessage.class),
            @ApiResponse(   code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                    message = "Erreur interne",
                    response = ErrorMessage.class),
    }
    )

    @ApiOperation(  value = "Consulter les attributs d'une nomenclature",
            produces = "application/json",
            httpMethod = "GET",
            nickname = "getNomenclatureItem",
            tags = "ITEM")
    ResponseEntity showNomenclature (
            @ApiParam(value = "Nom de la nomenclature", required = true) @PathVariable String name,
            @ApiParam(value = "Id l'itème", required = true) @PathVariable String id,
            @ApiParam(value = "Données souhaitées",name = "fields") @RequestParam(defaultValue = "", required = false) List<String> fields
    ) {

        //Initiliser la sortie
        HashMap jsonMap = new HashMap();

        //Localiser le fichier de paramètrage
        String file_name = "nomenclatures/" + name + ".yaml";
        File file;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            file = new File(classLoader.getResource(file_name).getFile());
        }catch (Exception e) {
            throw new ResourceNotFoundException("001", "Nomenclature not found");
        }

        if (!file.exists() || file.isDirectory()) {
            throw new ResourceNotFoundException("002", "Nomenclature not found");
        }

        //Mapper le paramètrage
        Nomenclature nomenclature;
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            nomenclature = mapper.readValue(file, Nomenclature.class);
        }catch (Exception e) {
            throw new BadRequestException("003", "Erreur interne");
        }

        LOGGER.info("Start prepare Query...");
        Prepare prepare = new Prepare(fields, "", "", "", id);
        String query = prepare.prepareQuery(nomenclature.getNomenclatures());
        LOGGER.info("Préparation de l'exécution de la requête : "+ query);

        Session session;
        ResultSet results;
        try {
            session = Cluster.builder().addContactPoint(properties.getIP()).build().connect();
            results = session.execute(query);
        }catch (Exception e) {
            throw new BadRequestException("004", "Erreur interne");
        }

        LOGGER.info("Préparation de la sortie du résultat");

        HashMap hmap = new HashMap();
        HashMaps hashmaps = new HashMaps();
        ArrayList arrayList = new ArrayList();
        for (Row currentElement : results.all()) {
            int col = currentElement.getColumnDefinitions().size();
            hmap = new HashMap();
            for(int i = 0; i < col; i++){
                String field = currentElement.getColumnDefinitions().getName(i);
                hmap.put(hashmaps.getKeyFromValue(nomenclature.getNomenclatures().getOutput(), field), currentElement.getObject(field).toString());
            }
            arrayList.add(hmap);
        }

        return new ResponseEntity(arrayList, HttpStatus.OK);
    }

}