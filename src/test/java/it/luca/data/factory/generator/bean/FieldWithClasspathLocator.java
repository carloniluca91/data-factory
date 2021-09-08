package it.luca.data.factory.generator.bean;

import it.luca.data.factory.annotation.RandomValue;
import lombok.Data;

@Data
public class FieldWithClasspathLocator {

    @RandomValue(useClasspathLocatorForValues = true)
    private String codCampo;
}
