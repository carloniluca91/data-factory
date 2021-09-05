package it.luca.data.factory.generator.function;

import lombok.AllArgsConstructor;

import java.lang.annotation.Annotation;

@AllArgsConstructor
public abstract class DataAnnotationSupplier<T extends Annotation, R> implements DataSupplier<R> {

    protected final T annotation;
}
