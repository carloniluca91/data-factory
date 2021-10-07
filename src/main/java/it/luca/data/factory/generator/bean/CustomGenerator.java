package it.luca.data.factory.generator.bean;

import java.util.List;

import static it.luca.utils.functional.Stream.map;

/**
 * Interface to extend in order to create custom instances of T
 * @param <T> instance's type
 */

public interface CustomGenerator<T> {

    /**
     * Generate a collection of instances of T
     * @param tClass instance's class
     * @return collection of instances of T
     * @throws Exception if collection creation fails
     */

    default List<T> generateListOf(int size, Class<T> tClass) throws Exception {

        return map(BeanGenerator.generateListOf(size, tClass), this::setNonRandomAttributes);
    }

    /**
     * Generate a singleton of type T
     * @param tClass instance's class
     * @return instance of T
     * @throws Exception if singleton creation fails
     */

    default T generateSingleTon(Class<T> tClass) throws Exception {

        return setNonRandomAttributes(BeanGenerator.generateSingleTon(tClass));
    }

    /**
     * Initialize deterministic attributes of given instance of type T
     * @param instance instance of type T
     * @return input instance
     */

    T setNonRandomAttributes(T instance);
}
