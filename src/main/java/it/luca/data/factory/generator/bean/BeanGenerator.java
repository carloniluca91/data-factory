package it.luca.data.factory.generator.bean;

import it.luca.data.factory.annotation.*;
import it.luca.data.factory.generator.function.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

import static it.luca.data.factory.generator.utils.ReflectionUtils.*;
import static it.luca.data.factory.utils.Utils.getLinesOfFile;
import static it.luca.utils.functional.Optional.isPresent;
import static it.luca.utils.functional.Stream.filter;

public class BeanGenerator {

    /**
     * Create a single instance of T
     * @param tClass {@link Class} of instance to generate
     * @param <T> type of instance to generate
     * @return instance of T
     * @throws Exception if singleton initializaiton fails
     */

    public static <T> T generateSingleTon(Class<T> tClass) throws Exception {

        return generateListOf(1, tClass).get(0);
    }

    /**
     * Create a list of instances of T
     * @param tClass {@link Class} of T
     * @param <T> instance type
     * @return list of instances of T with fields initialized randomly
     * @throws Exception if instance initialization fails
     */

    @SuppressWarnings("unchecked")
    public static <T> List<T> generateListOf(int size, Class<T> tClass) throws Exception {

        // Find a no-arg constructor
        Optional<Constructor<?>> optionalConstructor = Arrays
                .stream(tClass.getDeclaredConstructors())
                .filter(x -> x.getParameterCount() == 0)
                .findFirst();

        if (optionalConstructor.isPresent()) {

            // Create empty instance of T
            List<T> sampleList = new ArrayList<>();
            Constructor<T> tConstructor = (Constructor<T>) optionalConstructor.get();
            for (int i = 0; i < size; i++) {
                sampleList.add(tConstructor.newInstance());
            }

            // handle field annotated with RandomValue that use classPath locator in a different way,
            // in order to avoid continuous file opening, reading and closing
            List<Field> fieldsWithSetter = filter(tClass.getDeclaredFields(), x -> hasSetter(x, tClass));
            Predicate<Field> shouldUseClassPathLocator = f -> isAnnotatedWith(f, RandomValue.class)
                    && f.getAnnotation(RandomValue.class).useClasspathLocator();
            List<Field> fieldsThatShouldUseClassPathLocator = filter(fieldsWithSetter, shouldUseClassPathLocator);
            if (!fieldsThatShouldUseClassPathLocator.isEmpty()) {
                initFieldsThatShouldUseClassPathLocator(sampleList, tClass, fieldsThatShouldUseClassPathLocator);
            }

            // For each remaining field with declared setter, invoke such setter
            List<Field> fieldsWithSetterSecondSet = filter(fieldsWithSetter, shouldUseClassPathLocator.negate());
            for (Field field : fieldsWithSetterSecondSet) {
                Method fieldSetter = getSetterForField(field, tClass);
                for (T sample: sampleList) {
                    fieldSetter.invoke(sample, generateSetterParameter(field, sample));
                }
            }
            return sampleList;
        } else {
            throw new MissingNoArgConstructorException(tClass);
        }
    }

    protected static <T> void initFieldsThatShouldUseClassPathLocator(List<T> sampleList, Class<T> tClass, List<Field> annotatedFields)
            throws Exception {

        Map<Field, SetterAndValues> fieldSetterAndValuesMap = new HashMap<>();
        for (Field f: annotatedFields) {

            // Retrieve field setter and possible values along classPath
            Method fieldSetter = getSetterForField(f, tClass);
            List<String> values = getLinesOfFile(getClassPathFileLocation(f, tClass));
            fieldSetterAndValuesMap.put(f, new SetterAndValues(fieldSetter, values));
        }

        // For each sample, initialize such fields
        for (T sample : sampleList) {
            for (Map.Entry<Field, SetterAndValues> entry : fieldSetterAndValuesMap.entrySet()) {
                Method setter = entry.getValue().getSetter();
                String setterValue = entry.getValue().getValue();
                setter.invoke(sample, setterValue);
            }
        }
    }

    /**
     * Initializes a field of the instance
     * @param field given {@link Field}
     * @param instance instance of T
     * @param <T> type of input instance
     * @return object to be used for field's setter invocation
     * @throws Exception if something fails
     */

    protected static <T> Object generateSetterParameter(Field field, T instance) throws Exception {

        Object setterParameter;
        if (isAnnotatedWith(field, BoundedDateTime.class)) {
            setterParameter = new BoundedDateTimeSupplier(field.getAnnotation(BoundedDateTime.class)).apply();
        } else if (isAnnotatedWith(field, MappedByField.class)) {
            setterParameter = new FieldValueMapper(field.getAnnotation(MappedByField.class)).map(instance, field);
        } else if (isAnnotatedWith(field, RandomBean.class)) {
            setterParameter = generateSingleTon(field.getAnnotation(RandomBean.class).beanClass());
        } else if (isAnnotatedWith(field, RandomDateTime.class)) {
            setterParameter = new RandomDateTimeSupplier(field.getAnnotation(RandomDateTime.class)).apply();
        } else if (isAnnotatedWith(field, RandomNumber.class)) {
            setterParameter = new RandomNumberSupplier(field.getAnnotation(RandomNumber.class)).apply();
        } else if (isAnnotatedWith(field, RandomSequence.class)) {
            RandomSequence randomSequence = field.getAnnotation(RandomSequence.class);
            int size = new Random().nextInt(randomSequence.maxSize()) + 1;
            setterParameter = generateListOf(size, randomSequence.of());
        } else if (isAnnotatedWith(field, RandomValue.class) && !field.getAnnotation(RandomValue.class).useClasspathLocator()) {
            setterParameter = new RandomValueSupplier(field.getAnnotation(RandomValue.class)).apply();
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
