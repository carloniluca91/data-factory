package it.luca.data.factory.generator.function;

import it.luca.data.factory.annotation.RandomNumber;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RandomNumberSupplierTest {

    private final int MIN = 0;
    private final int MAX = 10;
    private final Function<Class<? extends Number>, RandomNumber> function = c -> new RandomNumber() {

        @Override
        public Class<? extends Annotation> annotationType() {
            return RandomNumber.class;
        }

        @Override
        public int min() {
            return MIN;
        }

        @Override
        public int max() {
            return MAX;
        }

        @Override
        public Class<? extends Number> as() {
            return c;
        }
    };

    @Test
    void apply() {

        Stream.of(Integer.class, Double.class, Float.class).forEach(c -> {

            RandomNumber randomNumberAnnotation = function.apply(c);
            Supplier<Number> numberSupplier = () -> new RandomNumberSupplier(randomNumberAnnotation).apply();
            assertDoesNotThrow(numberSupplier::get);
            Number randomNumber = numberSupplier.get();
            assertEquals(c, randomNumber.getClass());
        });
    }
}