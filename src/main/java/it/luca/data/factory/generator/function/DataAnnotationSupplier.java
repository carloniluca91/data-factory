package it.luca.data.factory.generator.function;

import lombok.AllArgsConstructor;

import java.lang.annotation.Annotation;

/**
 * Abstract class for generating a random object depending on a custom annotation
 * @param <T> annotation's type
 * @param <R> return data type
 */

@AllArgsConstructor
public abstract class DataAnnotationSupplier<T extends Annotation, R> implements DataSupplier<R> {

    protected final T annotation;
}
