package it.luca.data.factory.generator.utils;

import it.luca.data.factory.generator.bean.TestBean;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static it.luca.data.factory.utils.Utils.capitalize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReflectionUtilsTest {

    private static final String FIELD_NAME = "field";
    private static final Field field = mock(Field.class);
    private final Class<TestBean> clazz = TestBean.class;
    private final String[] CLASS_PATH_PIECES = TestBean.class.getName().split("\\.");

    @BeforeAll
    public static void init() {

        when(field.getName()).thenReturn(FIELD_NAME);
    }

    @Test
    void getClasspathForClass() {

        String EXPECTED = String.join("/", CLASS_PATH_PIECES);
        assertEquals(EXPECTED, ReflectionUtils.getClasspathForClass(clazz));
    }

    @Test
    void getFileNameForFieldOfClass() {

        String EXPECTED = String.join("/", CLASS_PATH_PIECES)
                .concat("/")
                .concat(FIELD_NAME)
                .concat(".txt");

        assertEquals(EXPECTED, ReflectionUtils.getFileNameForFieldOfClass(field, clazz));
    }

    @Test
    void getterNameFor() {

        assertEquals("get".concat(capitalize(FIELD_NAME)), ReflectionUtils.getterNameFor(FIELD_NAME));
    }

    @Test
    void setterNameFor() {

        assertEquals("set".concat(capitalize(FIELD_NAME)), ReflectionUtils.setterNameFor(field));
    }

}