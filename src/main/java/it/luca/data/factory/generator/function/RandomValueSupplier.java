package it.luca.data.factory.generator.function;

import it.luca.data.factory.annotation.RandomValue;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import static it.luca.data.factory.generator.utils.ReflectionUtils.getFileNameForFieldOfClass;
import static it.luca.data.factory.utils.Utils.getLinesOfFile;

public class RandomValueSupplier extends DataAnnotationSupplier<RandomValue, String> {

    public RandomValueSupplier(RandomValue annotation) {
        super(annotation);
    }

    @Override
    public String apply() {

        String[] values = annotation.values();
        return values[new Random().nextInt(values.length)];
    }

    /**
     * Return whether classpath locator should be used
     * @return true if the annotation contains no values and useClasspathLocator flag is set, false otherwise
     */

    public boolean mustUseClasspathLocator() {

        return (annotation.values().length == 0 && annotation.useClasspathLocator());
    }

    /**
     * Given a {@link Class} and a {@link Field}, attempts to locate file "package/className/fieldName.txt",
     * pick a random value from it and use it for initializing given {@link Field}
     * @param field {@link Field} to be initialized using output value
     * @param tClass class of bean to be generated
     * @return output value for given {@link Field}
     * @throws FileNotFoundException if .txt file is not found along classPath
     */

    public <T> String apply(Field field, Class<T> tClass) throws FileNotFoundException {

        // Retrieve possible values from a file along classPath
        String fileName = getFileNameForFieldOfClass(field, tClass);
        List<String> values = getLinesOfFile(fileName);
        return values.get(new Random().nextInt(values.size()));
    }
}
