package it.luca.data.factory.generator.function;

public interface DataSupplier<T> {

    T apply() throws Exception;
}

