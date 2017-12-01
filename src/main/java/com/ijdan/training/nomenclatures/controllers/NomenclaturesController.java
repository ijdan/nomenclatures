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
    private static final Logger LOGGER = LoggerFactory.getLogger(NomenclaturesController.class);

    @Autowired
    private Mapper mapper;

    @Autowired
    private Response response;

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
        HashMap rsps = new HashMap();

        Nomenclature nomenclatureConfig = mapper.getNomenclature(nomenclatureName);
        Request request = new Request(nomenclatureConfig, selectedFields, sortField, sortSens, paginPacket, offset);

        List<Map>  items = response.getCollection(nomenclatureConfig, request);

        if (nomenclatureConfig.getSummary().isEnabled()){
            rsps.put(nomenclatureConfig.getSummary().getNbElementsAttributeName(), Integer.valueOf(items.size()));

            int total = response.getQueryTotal(nomenclatureConfig);
            rsps.put(nomenclatureConfig.getSummary().getTotalAttributeName(), Integer.valueOf(total));
            //Enrichissement par le sommaire
        }
        rsps.put(nomenclatureConfig.getResourceName(), items);

        return response.getAdaptedContentType(rsps, httpRequest);
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
        HashMap rsps = new HashMap();

        Nomenclature nomenclatureConfig = mapper.getNomenclature(nomenclatureName);
        Request request = new Request(nomenclatureConfig, selectedFields, "", "", "", "");

        List<Map>  items = response.getItem(nomenclatureConfig, id, request);
        rsps.put(nomenclatureConfig.getResourceName(), items);

        return response.getAdaptedContentType(rsps, httpRequest);
    }
}