package it.luca.data.factory.generator.function;

import it.luca.data.factory.annotation.RandomNumber;

public class RandomNumberSupplier extends DataAnnotationSupplier<RandomNumber, Number> {

    public RandomNumberSupplier(RandomNumber annotation) {
        super(annotation);
    }

    @Override
    public Number apply() {

        int delta = annotation.max() - annotation.min();
        double randomNumber = annotation.min() + (delta * Math.random());
        if (annotation.as().equals(Integer.class)) {
            return (int) randomNumber;
        } else if (annotation.as().equals(Float.class)) {
            return (float) randomNumber;
        } else {
            return randomNumber;
        }
    }
}
