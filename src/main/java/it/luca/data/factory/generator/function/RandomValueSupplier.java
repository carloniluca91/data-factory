package it.luca.data.factory.generator.function;

import it.luca.data.factory.annotation.RandomValue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static it.luca.utils.functional.Optional.isPresent;

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
     * @param tClass class of bean to be generated
     * @param field {@link Field} to be initialized using output value
     * @return output value for given {@link Field}
     * @throws FileNotFoundException if .txt file is not found along classPath
     */

    public <T> String apply(Class<T> tClass, Field field) throws FileNotFoundException {

        // Retrieve possible values from a file along classPath
        String fieldName = field.getName();
        String fileName = tClass.getName().replaceAll("\\.", "/").concat("/").concat(fieldName.concat(".txt"));
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        if (isPresent(stream)) {
            // Extract values and choose one of them randomly
            //noinspection ConstantConditions
            List<String> values = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
            return values.get(new Random().nextInt(values.size()));
        } else {
            throw new FileNotFoundException(String.format("Unable to locate file %s for retrieving values for %s", fileName, fieldName));
        }
    }
}
