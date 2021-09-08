package it.luca.data.factory.generator.bean;

public interface CustomGenerator<T> {

    default T generate(Class<T> tClass) throws Exception {

        T instance = BeanGenerator.generate(tClass);
        return setNonRandomAttributes(instance);
    }

    default T setNonRandomAttributes(T instance) {

        return instance;
    }
}
