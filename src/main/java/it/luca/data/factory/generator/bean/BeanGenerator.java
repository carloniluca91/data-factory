package it.luca.data.factory.generator.bean;

import it.luca.data.factory.annotation.*;
import it.luca.data.factory.generator.function.FieldValueMapper;
import it.luca.data.factory.generator.function.RandomDateTimeSupplier;
import it.luca.data.factory.generator.function.RandomNumberSupplier;
import it.luca.data.factory.generator.function.RandomValueSupplier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static it.luca.data.factory.generator.utils.ReflectionUtils.setterNameFor;
import static it.luca.utils.functional.Optional.isPresent;
import static it.luca.utils.functional.Stream.anyMatch;
import static it.luca.utils.functional.Stream.filter;

public class BeanGenerator {

    /**
     * Create a random instance of T
     * @param tClass class of T
     * @param <T> instance type
     * @return instance of T with fields initialized randomly
     * @throws Exception if instance initialization fails
     */

    public static <T> T generate(Class<T> tClass) throws Exception {

        // Find a no-arg constructor
        Optional<Constructor<?>> optionalConstructor = Arrays
                .stream(tClass.getDeclaredConstructors())
                .filter(x -> x.getParameterCount() == 0)
                .findFirst();

        if (optionalConstructor.isPresent()) {

            // Create empty instance of T
            @SuppressWarnings("unchecked")
            T instance = (T) optionalConstructor.get().newInstance();
            BiPredicate<Method, Field> isSetterForField = (method, field) -> method.getName().equals(setterNameFor(field));
            Predicate<Field> hasSetter = field -> anyMatch(tClass.getDeclaredMethods(), method -> isSetterForField.test(method, field));
            List<Field> fieldsWithSetter = filter(tClass.getDeclaredFields(), hasSetter);

            // For each field with declared setter, invoke such setter
            for (Field field : fieldsWithSetter) {

                Method fieldSetter = tClass.getDeclaredMethod(setterNameFor(field), field.getType());
                fieldSetter.invoke(instance, generateSetterParameter(field, instance));
            }

            // return non-empty instance of T
            return instance;
        } else {
            throw new MissingNoArgConstructorException(tClass);
        }
    }

    /**
     * Initializes a field of the instance
     * @param field instance of {@link Field}
     * @param instance generated bean
     * @param <T> type of generated bean
     * @return object to be used for field initialization
     * @throws Exception if something fails
     */

    private static <T> Object generateSetterParameter(Field field, T instance) throws Exception {

        Object setterObject;
        //noinspection unchecked
        Class<T> tClass = (Class<T>) instance.getClass();
        Predicate<Class<? extends Annotation>> isAnnotatedWith = aClass -> isPresent(field.getAnnotation(aClass));
        if (isAnnotatedWith.test(MappedByField.class)) {
            setterObject = new FieldValueMapper(field.getAnnotation(MappedByField.class)).map(instance, field);
        } else if (isAnnotatedWith.test(RandomBean.class)) {
            setterObject = generate(field.getAnnotation(RandomBean.class).beanClass());
        } else if (isAnnotatedWith.test(RandomDateTime.class)) {
            setterObject = new RandomDateTimeSupplier(field.getAnnotation(RandomDateTime.class)).apply();
        } else if (isAnnotatedWith.test(RandomNumber.class)) {
            setterObject = new RandomNumberSupplier(field.getAnnotation(RandomNumber.class)).apply();
        } else if (isAnnotatedWith.test(RandomSequence.class)) {
            RandomSequence randomSequence = field.getAnnotation(RandomSequence.class);
            int size = new Random().nextInt(randomSequence.maxSize()) + 1;
            List<Object> sequence = new ArrayList<>();
            for (int i = 0; i < size; i ++) {
                sequence.add(generate(randomSequence.of()));
            }

            setterObject = sequence;
        } else if (isAnnotatedWith.test(RandomValue.class)) {
            RandomValueSupplier supplier = new RandomValueSupplier(field.getAnnotation(RandomValue.class));
            setterObject = supplier.mustUseClasspathLocator() ?
                    supplier.apply(field, tClass):
                    supplier.apply();
        } else setterObject = null;

        return setterObject;
    }
}
