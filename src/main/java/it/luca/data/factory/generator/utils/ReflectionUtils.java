package it.luca.data.factory.generator.utils;

import java.lang.reflect.Field;

import static it.luca.data.factory.utils.Utils.capitalize;

public class ReflectionUtils {

    public static final String GET = "get";
    public static final String SET = "set";

    /**
     * Return the classPath location directory for given class
     * @param tClass given {@link Class}
     * @param <T> type of given class
     * @return name of given class, to be interpreted as a classPath directory
     */

    public static <T> String getClasspathForClass(Class<T> tClass) {

        return String.join("/", tClass.getName().split("\\."));
    }

    public static <T> String getFileNameForFieldOfClass(Field field, Class<T> tClass) {

        return getClasspathForClass(tClass)
                .concat("/")
                .concat(field.getName())
                .concat(".txt");
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
     * Return the standard name of a setter for given field
     * @param field given {@link Field}
     * @return setter method name (i.e. "setFieldName")
     */

    public static String setterNameFor(Field field) {

        return SET.concat(capitalize(field.getName()));
    }
}
