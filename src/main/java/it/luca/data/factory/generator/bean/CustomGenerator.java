package it.luca.data.factory.generator.bean;

/**
 * Interface to extend in order to create a custom instance of T
 * @param <T> instance's type
 */

public interface CustomGenerator<T> {

    /**
     * Generate an instance of T
     * @param tClass instance's class
     * @return instance of T
     * @throws Exception if instance creation fails
     */

    default T generate(Class<T> tClass) throws Exception {

        T instance = BeanGenerator.generate(tClass);
        return setNonRandomAttributes(instance);
    }

    /**
     * Initialize deterministic attributes of given instance of type T
     * @param instance instance of type T
     * @return input instance
     */

    T setNonRandomAttributes(T instance);
}
