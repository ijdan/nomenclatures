package com.ijdan.training.nomenclatures.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "nomenclatures")
public class Nomenclature {

    private Structure nomenclature;

    public Nomenclature() {
    }

    public void setNomenclature(Structure nomenclature) {
        this.nomenclature = nomenclature;
    }


    public Structure getNomenclatures() {
        return this.nomenclature;
    }
}