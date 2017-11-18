package com.ijdan.training.nomenclatures.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class Mapper {
    private final static Logger LOGGER = LoggerFactory.getLogger(Mapper.class);
    private static Nomenclature nomenclature;

    public Nomenclature getNomenclature(String nomenclatureName) {
        if (nomenclature == null || !nomenclature.getResourceName().equals(nomenclatureName)) {
            String pathYMLFile = "nomenclatures/" + nomenclatureName + ".yaml";
            LOGGER.info("Chargement du fichier [" + pathYMLFile + "]");

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            try {
                //noinspection ConstantConditions
                File file = new File(getClass().getClassLoader().getResource(pathYMLFile).getFile());
                // Mapper le fichier YAML avec le POJO nomenclature
                nomenclature = mapper.readValue(file, Nomenclature.class);
                if (!nomenclature.isEnabled()){
                    nomenclature = null;
                }

            } catch (Exception e) {
                throw new ResourceNotFoundException("Err.00001", "!! Mapping Error with [" + nomenclatureName + "] resource !!");
            }
        }

        return nomenclature;
    }

}
