package com.ijdan.training.nomenclatures.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ijdan.training.nomenclatures.infrastructure.ExceptionHandling.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Mapper {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private static Nomenclature nomenclature;

    public Mapper(String nomenclatureName) {
        if (nomenclature == null || !nomenclature.getResourceName().equals(nomenclatureName)) {
            String pathYMLFile = "nomenclatures/" + nomenclatureName + ".yaml";
            LOGGER.warn("Chargement du fichier [" + pathYMLFile + "]");

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            try {
                File file = new File(getClass().getClassLoader().getResource(pathYMLFile).getFile());
                /**
                 * Mapper le fichier YAML avec le POJO nomenclature
                 * */
                nomenclature = mapper.readValue(file, Nomenclature.class);
                if (!nomenclature.isEnabled()){
                    nomenclature = null;
                }
            } catch (Exception e) {
                throw new ResourceNotFoundException("Err.00001", "!! Mapping Error with [" + nomenclatureName + "] resource !!");
            }
        }

    }

    public static Nomenclature getNomenclature() {
        return nomenclature;
    }
}
