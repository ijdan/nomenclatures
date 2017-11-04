package com.ijdan.training.nomenclatures.controllers;

import com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling.ResourceNotFoundException;
import com.ijdan.training.nomenclatures.repository.H2Connection;

import com.ijdan.training.nomenclatures.domain.Mapper;
import com.ijdan.training.nomenclatures.domain.Nomenclature;
import com.ijdan.training.nomenclatures.domain.PrepareRequest;
import com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling.InternalErrorException;
import com.ijdan.training.nomenclatures.repository.DatabasesProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/T1.0/nomenclatures")
public class NomenclaturesController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private DatabasesProperties databasesProperties;
    @Autowired
    public void setDatabasesProperties(DatabasesProperties databasesProperties) {
        this.databasesProperties = databasesProperties;
    }

    @RequestMapping(
            value = "/{nomenclatureName}",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    Map<String, Object> showNomenclature (
            @PathVariable(value="nomenclatureName") String nomenclatureName,

            @RequestParam(value="selectedFields", required = false) List<String> selectedFields,
            @RequestParam(value="sortField", required = false) String sortField,
            @RequestParam(value="sortSens", required = false) String sortSens,
            @RequestParam(value="paginPacket", required = false) String paginPacket,
            @RequestParam(value="offset", required = false) String offset
    ) {
        LOGGER.warn(databasesProperties.getH2().toString() );
        /**
         * Construction d'un objet Nomenclature
         * */
        Map toReturn = new HashMap<String, Object>();
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
                H2Connection h2c = new H2Connection(nomenclature.getCache(), this.databasesProperties);
                ResultSet rs = h2c.executeQuery(prepareRequest.getCallRequest(selectedFields, sortField, sortSens, paginPacket, offset));
                toReturn.put(nomenclature.getResourceName(), nomenclature.getListMap(rs, selectedFields));


                if (nomenclature.getSummary().isEnabled()){
                    toReturn.put(nomenclature.getSummary().getNbElementsAttributeName(), toReturn.size());

                    rs = h2c.executeQuery( prepareRequest.getTotalRequest() );
                    if (rs.next()) {
                        toReturn.put(nomenclature.getSummary().getTotalAttributeName(), rs.getString("TOTAL"));
                    }
                }
            }catch (SQLException e){
                throw new InternalErrorException("Err.00002", "!! Error data base access !!");
            }

        }else {
            throw new ResourceNotFoundException("Err.00003", "!! Nomenclature inexistante !!");
        }
        return toReturn;

    }

}