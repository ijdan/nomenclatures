package com.ijdan.training.nomenclatures.controllers;

import com.ijdan.training.nomenclatures.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/T1.0/nomenclatures")
public class NomenclaturesController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Mapper mapper;

    @Autowired
    private PrepareResponse prepareResponse;

    /***
     * endpoint collection
     * Restitue tous itèmes d'une collection
     * Possibilité de sélectionner les données souhaitées en retour
     * De préciser la donner à trier le sens du tri
     * De paginer et de positionner le curseur de la récupération
     */
    @RequestMapping(
            value = "/{nomenclatureName}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<String> collecttion (
            @PathVariable(value="nomenclatureName") String nomenclatureName,

            @RequestParam(value="selectedFields", required = false) List<String> selectedFields,
            @RequestParam(value="sortField", required = false) String sortField,
            @RequestParam(value="sortSens", required = false) String sortSens,
            @RequestParam(value="paginPacket", required = false) String paginPacket,
            @RequestParam(value="offset", required = false) String offset,

            HttpServletRequest httpRequest
    ) {
        HashMap response = new HashMap();

        Nomenclature nomenclatureConfig = mapper.getNomenclature(nomenclatureName);
        List<Map>  items = prepareResponse.getCollection(nomenclatureConfig, selectedFields, sortField, sortSens, paginPacket, offset);
        response.put(nomenclatureConfig.getResourceName(), items);

        if (nomenclatureConfig.getSummary().isEnabled()){
            //Enrichissement par le sommaire
        }

        return prepareResponse.getAdaptedContentType(response, httpRequest);
    }


    /***
     * endpoint item
     * Récupère les données d'un itème donné
     * avec possibilité de sélectionner les données souhaitées pour l'itème demandé
     */
    @RequestMapping(
            value = "/{nomenclatureName}/{id}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<String> item (
            @PathVariable(value="nomenclatureName") String nomenclatureName,
            @PathVariable(value="id") String id,
            @RequestParam(value="selectedFields", required = false) List<String> selectedFields,

            HttpServletRequest httpRequest
    ){
        Nomenclature nomenclatureConfig = mapper.getNomenclature(nomenclatureName);
        HashMap response = prepareResponse.getItem(nomenclatureConfig, id, selectedFields);

        return prepareResponse.getAdaptedContentType(response, httpRequest);
    }
}