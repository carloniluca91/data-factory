package it.luca.data.factory.generator.function;

import it.luca.data.factory.annotation.RandomDateTime;

import java.time.LocalDateTime;

public class RandomDateTimeSupplier extends DataAnnotationSupplier<RandomDateTime, LocalDateTime> {

    public RandomDateTimeSupplier(RandomDateTime annotation) {
        super(annotation);
    }

    @Override
    public LocalDateTime apply() throws Exception {
        return annotation.supplier().newInstance().apply();
    }
}
