package it.luca.data.factory.generator.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

@Getter
@AllArgsConstructor
public class SetterAndValues {

    private final Method setter;
    private final List<String> values;

    public String getValue() {

        return values.get(new Random().nextInt(values.size()));
    }
}
