package it.luca.data.factory.generator.bean;

import it.luca.data.factory.annotation.RandomDateTime;
import it.luca.data.factory.generator.function.Now;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FieldWithNoAnnotationBean {

    @RandomDateTime(supplier = Now.class)
    private LocalDateTime dateTime;

    private Integer id;
}
