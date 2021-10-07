package it.luca.data.factory.generator.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static it.luca.data.factory.utils.Utils.capitalize;
import static it.luca.utils.functional.Optional.isPresent;
import static it.luca.utils.functional.Stream.anyMatch;

public class ReflectionUtils {

    public static final String GET = "get";
    public static final String SET = "set";

    /**
     * Get location along classPath for a .txt file named according to given field and class name
     * @param field given {@link Field}
     * @param tClass given {@link Class}
     * @return 'package/y/x.txt' for a field named 'x' of a class whose fully qualified name is 'package.y'
     */

    public static String getClassPathFileLocation(Field field, Class<?> tClass) {

        return String.join("/", tClass.getName().split("\\."))
                .concat("/")
                .concat(field.getName())
                .concat(".txt");
    }

    /**
     * Get setter method for given field (if defined)
     * @param field {@link Field}
     * @param tClass {@link Class}
     * @return {@link Method} representing field setter
     * @throws NoSuchMethodException if setter method is not found
     */

    public static Method getSetterForField(Field field, Class<?> tClass) throws NoSuchMethodException {

        return tClass.getDeclaredMethod(setterNameFor(field), field.getType());
    }

    /**
     * Return the standard name of a getter for given field's name
     * @param fieldName name of the field
     * @return getter method name (i.e. "getFieldName")
     */

    public static String getterNameFor(String fieldName) {

        return GET.concat(capitalize(fieldName));
    }

    /**
     * Evaluates if given field has a declared setter
     * @param field given {@link Field}
     * @param tClass {@link Class} definition
     * @return true if a setter is declared, false otherwise
     */

    public static boolean hasSetter(Field field, Class<?> tClass) {

        return anyMatch(tClass.getDeclaredMethods(), method -> method.getName().equals(setterNameFor(field)));
    }

    /**
     * Evaluates if given field is annotated with an annotation
     * @param field given {@link Field}
     * @param annotationClass {@link Class} of annotation to check
     * @param <T> type of annotation
     * @return true if given field is annotated with given annotation class, false otherwise
     */

    public static <T extends Annotation> boolean isAnnotatedWith(Field field, Class<T> annotationClass) {

        return isPresent(field.getAnnotation(annotationClass));
    }

    /**
     * Return the standard name of a setter for given field
     * @param field given {@link Field}
     * @return setter method name (i.e. "setFieldName")
     */

    public static String setterNameFor(Field field) {

        return SET.concat(capitalize(field.getName()));
    }
}
