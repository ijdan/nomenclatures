package com.ijdan.training.nomenclatures.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ijdan.training.nomenclatures.domain.Nomenclature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;


@RestController
@RequestMapping(value = "/T1.0/nomenclatures")
public class NomenclaturesController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/{nomenclatureName}", method = RequestMethod.GET)
    String showNomenclature (
            @PathVariable(value="nomenclatureName") String nomenclatureName,

            @RequestParam(value="selectedFields", required = false) List<String> selectedFields,
            @RequestParam(value="sortField", required = false) String sortField,
            @RequestParam(value="sortSens", required = false) String sortSens,
            @RequestParam(value="paginPacket", required = false) String paginPacket,
            @RequestParam(value="offset", required = false) String offset
    ) throws IOException {

        String pathYMLFile = "nomenclatures/" + nomenclatureName + ".yaml";
        Nomenclature nomenclature = null;

        try {
            /**
             * Mapper le fichier YAML avec le POJO nomenclature
             * */
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            nomenclature = mapper.readValue(new File(getClass().getClassLoader().getResource(pathYMLFile).getFile()), Nomenclature.class);
        } catch (Exception e) {
            // throw new ResourceNotFoundException("001", e.getMessage() + " [Nomenclature <"+ nomenclatureName +"> config. not found]");
            return e.getStackTrace().toString();
        }


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

        return offset;
    }

}
