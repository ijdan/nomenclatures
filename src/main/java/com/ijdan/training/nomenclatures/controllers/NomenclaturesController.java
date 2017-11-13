package com.ijdan.training.nomenclatures.controllers;

import com.ijdan.training.nomenclatures.domain.Response;
import com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling.ResourceNotFoundException;
import com.ijdan.training.nomenclatures.repository.H2Connection;

import com.ijdan.training.nomenclatures.domain.Mapper;
import com.ijdan.training.nomenclatures.domain.Nomenclature;
import com.ijdan.training.nomenclatures.domain.PrepareRequest;
import com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling.InternalErrorException;
import com.ijdan.training.nomenclatures.repository.DatabasesProperties;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/T1.0/nomenclatures")
public class NomenclaturesController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private H2Connection h2connection;

    @Autowired
    public void setH2connection(H2Connection h2connection) {
        this.h2connection = h2connection;
    }

    @RequestMapping(
            value = "/{nomenclatureName}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<String> showNomenclatures (
            @PathVariable(value="nomenclatureName") String nomenclatureName,

            @RequestParam(value="selectedFields", required = false) List<String> selectedFields,
            @RequestParam(value="sortField", required = false) String sortField,
            @RequestParam(value="sortSens", required = false) String sortSens,
            @RequestParam(value="paginPacket", required = false) String paginPacket,
            @RequestParam(value="offset", required = false) String offset,

            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        LOGGER.warn(h2connection.getDatabasesProperties().getH2().toString() );
        /**
         * Construction d'un objet Nomenclature
         * */
        HashMap response = new HashMap<String, Object>();
        Nomenclature nomenclature = new Mapper(nomenclatureName).getNomenclature();
        if (nomenclature != null){
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


            PrepareRequest prepareRequest = new PrepareRequest(nomenclature);
            try {
                h2connection.setCache(nomenclature.getCache());
                ResultSet rs = h2connection.executeQuery(prepareRequest.getCallRequest(selectedFields, sortField, sortSens, paginPacket, offset));
                List<Map> values = nomenclature.getListMap(rs, selectedFields);
                response.put(nomenclature.getResourceName(), values);


                if (nomenclature.getSummary().isEnabled()){
                    response.put(nomenclature.getSummary().getNbElementsAttributeName(), String.valueOf(values.size()));

                    rs = h2connection.executeQuery( prepareRequest.getTotalRequest() );
                    if (rs.next()) {
                        response.put(nomenclature.getSummary().getTotalAttributeName(), rs.getString("TOTAL"));
                    }
                }
            }catch (SQLException e){
                throw new InternalErrorException("Err.00002", "!! Error data base access !!");
            }

        }else {
            throw new ResourceNotFoundException("Err.00003", "!! Nomenclature inexistante !!");
        }

        String mediaType;
        if (httpRequest.getContentType() == null){
            mediaType = MediaType.APPLICATION_JSON_VALUE;
        }else {
            mediaType = httpRequest.getContentType();
        }

        Response r = new Response(response);

        HttpHeaders headers = new HttpHeaders();
        final HttpHeaders httpHeaders= new HttpHeaders();
        switch (mediaType){
            case MediaType.APPLICATION_XML_VALUE:
                httpHeaders.setContentType(MediaType.APPLICATION_XML);
                return new ResponseEntity<String>(r.getXMLResponse(), httpHeaders, HttpStatus.OK);

            case MediaType.TEXT_PLAIN_VALUE:
                httpHeaders.setContentType(MediaType.TEXT_PLAIN);
                return new ResponseEntity<String>(r.getCSVResponse(), httpHeaders, HttpStatus.OK);

            default:
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                return new ResponseEntity<String>(JSONObject.toJSONString(response), httpHeaders, HttpStatus.OK);
        }

    }

}