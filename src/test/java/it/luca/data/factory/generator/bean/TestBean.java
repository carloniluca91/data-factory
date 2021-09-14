package it.luca.data.factory.generator.bean;

import it.luca.data.factory.annotation.*;
import it.luca.data.factory.generator.function.Now;
import it.luca.data.factory.generator.function.PlusOrMinusTime;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
public class TestBean {

    @RandomValue(useClasspathLocator = true)
    private String codCampo;

    @MappedByField(fieldName = "codCampo")
    private String nomeCampo;

    @RandomNumber(min = 0, max = 11, as = Integer.class)
    private Integer integer;

    @RandomDateTime(supplier = Now.class)
    private LocalDateTime dataInvio;

    @BoundedDateTime(
            upper = @BoundedDateTime.Bound(signum = PlusOrMinusTime.Signum.PLUS, amount = 1, unit = ChronoUnit.HOURS))
    private LocalDateTime dataElaborazione;
}
