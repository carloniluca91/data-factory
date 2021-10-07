package it.luca.data.factory.generator.function;

import it.luca.data.factory.annotation.MappedByField;
import lombok.AllArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.luca.data.factory.generator.utils.ReflectionUtils.getClassPathFileLocation;
import static it.luca.data.factory.generator.utils.ReflectionUtils.getterNameFor;
import static it.luca.data.factory.utils.Utils.getLinesOfFile;
import static it.luca.utils.functional.Optional.isPresent;

@AllArgsConstructor
public class FieldValueMapper {

    private final MappedByField annotation;

    public <T> String map(T instance, Field field) throws Exception {

        // Retrieve value of mapping field
        String mappingFieldName = annotation.fieldName();
        Method mappingFieldGetter = instance.getClass().getDeclaredMethod(getterNameFor(mappingFieldName));
        Object mappingFieldValueRaw = mappingFieldGetter.invoke(instance);
        if (isPresent(mappingFieldValueRaw)) {

            // Build a map whose keys are all of possible values of mapping field
            Map<String, String> valueMap = new HashMap<>();
            List<String> mappedFieldsFileLines = getLinesOfFile(getClassPathFileLocation(field, instance.getClass()));
            mappedFieldsFileLines.forEach(s -> {

                List<String> entries = Arrays.asList(s.split(" -> "));
                valueMap.put(entries.get(0), entries.get(1));
            });

            return valueMap.get((String) mappingFieldValueRaw);
        } else throw new IllegalAccessException(String.format("Value of mapping field %s has not been initialized yet", mappingFieldName));
    }
}
