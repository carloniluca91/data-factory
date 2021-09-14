package it.luca.data.factory.generator.bean;

import it.luca.data.factory.annotation.*;
import it.luca.data.factory.generator.function.*;

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

    public static <T extends Annotation> boolean isAnnotatedWith(Field field, Class<T> annotationClass) {

        return isPresent(field.getAnnotation(annotationClass));
    }

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

    protected static <T> Object generateSetterParameter(Field field, T instance) throws Exception {

        Object setterParameter;
        if (isAnnotatedWith(field, BoundedDateTime.class)) {
            setterParameter = new BoundedDateTimeSupplier(field.getAnnotation(BoundedDateTime.class)).apply();
        } else if (isAnnotatedWith(field, MappedByField.class)) {
            setterParameter = new FieldValueMapper(field.getAnnotation(MappedByField.class)).map(instance, field);
        } else if (isAnnotatedWith(field, RandomBean.class)) {
            setterParameter = generate(field.getAnnotation(RandomBean.class).beanClass());
        } else if (isAnnotatedWith(field, RandomDateTime.class)) {
            setterParameter = new RandomDateTimeSupplier(field.getAnnotation(RandomDateTime.class)).apply();
        } else if (isAnnotatedWith(field, RandomNumber.class)) {
            setterParameter = new RandomNumberSupplier(field.getAnnotation(RandomNumber.class)).apply();
        } else if (isAnnotatedWith(field, RandomSequence.class)) {
            RandomSequence randomSequence = field.getAnnotation(RandomSequence.class);
            int size = new Random().nextInt(randomSequence.maxSize()) + 1;
            List<Object> sequence = new ArrayList<>();
            for (int i = 0; i < size; i ++) {
                sequence.add(generate(randomSequence.of()));
            }

            setterParameter = sequence;
        } else if (isAnnotatedWith(field, RandomValue.class)) {
            //noinspection unchecked
            Class<T> tClass = (Class<T>) instance.getClass();
            RandomValueSupplier supplier = new RandomValueSupplier(field.getAnnotation(RandomValue.class));
            setterParameter = supplier.mustUseClasspathLocator() ?
                    supplier.apply(field, tClass):
                    supplier.apply();
        } else setterParameter = null;

        return maybeSetToNull(field, setterParameter);
    }

    protected static Object maybeSetToNull(Field field, Object setterParameter) {

        // Maybe set to null
        if (isPresent(setterParameter) && isAnnotatedWith(field, Nullable.class)) {
            Nullable nullable = field.getAnnotation(Nullable.class);
            return Math.random() < nullable.probability() ? null : setterParameter;
        } else return setterParameter;
    }
}
