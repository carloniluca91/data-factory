package it.luca.data.factory.generator.function;

import it.luca.data.factory.annotation.RandomValue;

import java.util.Random;

public class RandomValueSupplier extends DataAnnotationSupplier<RandomValue, String> {

    public RandomValueSupplier(RandomValue annotation) {
        super(annotation);
    }

    @Override
    public String apply() {

        String[] values = annotation.values();
        return values[new Random().nextInt(values.length)];
    }
}
