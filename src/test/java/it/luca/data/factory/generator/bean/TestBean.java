package it.luca.data.factory.generator.bean;

import it.luca.data.factory.annotation.MappedByField;
import it.luca.data.factory.annotation.RandomValue;
import lombok.Data;

@Data
public class TestBean {

    @RandomValue(useClasspathLocator = true)
    private String codCampo;

    @MappedByField(fieldName = "codCampo")
    private String nomeCampo;
}
