package com.ijdan.training.nomenclatures.controllers;

import com.ijdan.training.nomenclatures.domain.*;
import com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling.ResourceNotFoundException;
import com.ijdan.training.nomenclatures.repository.H2Connection;

import com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling.InternalErrorException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/T1.0/nomenclatures")
public class NomenclaturesController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private H2Connection h2connection;

/*
    private PrepareResponse prepareResponse;
    @Autowired
    public void setPrepareResponse(PrepareResponse prepareResponse) {
        this.prepareResponse = prepareResponse;
    }

    private Mapper mapper;
    @Autowired
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }
*/
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
        /**
         * Construction d'un objet Nomenclature
         * */
/*
        Nomenclature nomenclature = mapper.getNomenclature(nomenclatureName);
        LOGGER.warn(h2connection.getDatabasesProperties().getH2().toString());

        HashMap response = new HashMap<String, Object>();
        response = prepareResponse.selectAll(selectedFields, sortField, sortSens, paginPacket, offset);
        if (nomenclature != null){


            try {
                h2connection.setCache( nomenclature.getCache() );
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
*/
        final HttpHeaders httpHeaders= new HttpHeaders();
        return new ResponseEntity<String>(JSONObject.toString("alpha", "beta"), httpHeaders, HttpStatus.OK);
    }

}