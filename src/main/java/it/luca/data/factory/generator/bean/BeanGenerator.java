package it.luca.data.factory.generator.bean;

import it.luca.data.factory.annotation.*;
import it.luca.data.factory.generator.function.RandomDateTimeSupplier;
import it.luca.data.factory.generator.function.RandomNumberSupplier;
import it.luca.data.factory.generator.function.RandomValueSupplier;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static it.luca.utils.functional.Optional.isPresent;
import static it.luca.utils.functional.Stream.anyMatch;
import static it.luca.utils.functional.Stream.filter;

public class BeanGenerator {

    /**
     * Return fields name capitalized
     * @param field instance od {@link Field}
     * @return field name capitalized
     */

    public static String fieldNameCapitalized(Field field) {

        String fieldName = field.getName();
        String firstCharacter = fieldName.substring(0, 1).toUpperCase();
        return firstCharacter.concat(fieldName.substring(1));
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
            BiPredicate<Method, Field> isSetterForField = (method, field) -> method.getName().equals("set".concat(fieldNameCapitalized(field)));
            Predicate<Field> hasSetter = field -> anyMatch(tClass.getDeclaredMethods(), method -> isSetterForField.test(method, field));
            List<Field> fieldsWithSetter = filter(tClass.getDeclaredFields(), hasSetter);

            // For each field with declared setter, invoke such setter
            for (Field field : fieldsWithSetter) {

                Method fieldSetter = tClass.getDeclaredMethod("set".concat(fieldNameCapitalized(field)), field.getType());
                fieldSetter.invoke(instance, generateSetterParameter(field, tClass));
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
     * @param tClass class of bean to be generated
     * @return object to be used for field initialization
     * @throws Exception if something fails
     */

    private static <T> Object generateSetterParameter(Field field, Class<T> tClass) throws Exception {

        Object setterObject;
        Predicate<Class<? extends Annotation>> isAnnotatedWith = aClass -> isPresent(field.getAnnotation(aClass));
        if (isAnnotatedWith.test(RandomDateTime.class)) {
            setterObject = new RandomDateTimeSupplier(field.getAnnotation(RandomDateTime.class)).apply();
        } else if (isAnnotatedWith.test(RandomNumber.class)) {
            setterObject = new RandomNumberSupplier(field.getAnnotation(RandomNumber.class)).apply();
        } else if (isAnnotatedWith.test(RandomValue.class)) {
            RandomValue annotation = field.getAnnotation(RandomValue.class);
            if (annotation.values().length > 0 && !annotation.useClasspathLocatorForValues()) {
                setterObject = new RandomValueSupplier(field.getAnnotation(RandomValue.class)).apply();
            } else {
                setterObject = getRandomValueFromClasspath(tClass, field);
            }
        } else if (isAnnotatedWith.test(RandomBean.class)) {
            setterObject = generate(field.getAnnotation(RandomBean.class).beanClass());
        } else if (isAnnotatedWith.test(RandomSequence.class)) {

            RandomSequence randomSequence = field.getAnnotation(RandomSequence.class);
            int size = new Random().nextInt(randomSequence.maxSize()) + 1;
            List<Object> sequence = new ArrayList<>();
            for (int i = 0; i < size; i ++) {
                sequence.add(generate(randomSequence.of()));
            }

            setterObject = sequence;
        } else {
            throw new MissingDataAnnotationException(field);
        }

        return setterObject;
    }

    /**
     * Given a {@link Class} and a {@link Field}, attempts to locate file "package/className/fieldName.txt",
     * pick a random value from it and use it for initializing given {@link Field}
     * @param tClass class of bean to be generated
     * @param field {@link Field} to be initialized using output value
     * @return output value for given {@link Field}
     * @throws FileNotFoundException if .txt file is not found along classPath
     */

    private static Object getRandomValueFromClasspath(Class<?> tClass, Field field) throws FileNotFoundException {

        // Retrieve possible values from a file along classPath
        String fieldName = field.getName();
        String fileName = tClass.getName().replaceAll("\\.", "/").concat("/").concat(fieldName.concat(".txt"));
        InputStream stream = BeanGenerator.class.getClassLoader().getResourceAsStream(fileName);
        if (isPresent(stream)) {
            // Extract values and choose one of them randomly
            //noinspection ConstantConditions
            List<String> values = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
            return values.get(new Random().nextInt(values.size() + 1));
        } else {
            throw new FileNotFoundException(String.format("Unable to locate file %s for retrieving values for %s", fileName, fieldName));
        }
    }
}
