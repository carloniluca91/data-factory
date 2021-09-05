package it.luca.data.factory.generator.bean;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BeanGeneratorTest {

    @Test
    void fieldNameCapitalized() {

        String FIELD_NAME = "tsInsert";
        Field field = mock(Field.class);
        when(field.getName()).thenReturn(FIELD_NAME);
        assertEquals("TsInsert", BeanGenerator.fieldNameCapitalized(field));
    }

    @Test
    void generate() {

        assertThrows(MissingNoArgConstructorException.class, () -> BeanGenerator.generate(AllArgsConstructorBean.class));
        assertThrows(MissingDataAnnotationException.class, () -> BeanGenerator.generate(FieldWithNoAnnotationBean.class));
    }
}