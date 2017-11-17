package com.ijdan.training.nomenclatures.controllers;

import com.ijdan.training.nomenclatures.domain.*;
import com.ijdan.training.nomenclatures.repository.H2Connection;

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
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/T1.0/nomenclatures")
public class NomenclaturesController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Mapper mapper;

    @Autowired
    private PrepareResponse prepareResponse;

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
        Nomenclature nomenclature = mapper.getNomenclature(nomenclatureName);
        HashMap response = prepareResponse.get(nomenclature, httpRequest);

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