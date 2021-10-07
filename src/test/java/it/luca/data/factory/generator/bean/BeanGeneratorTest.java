package it.luca.data.factory.generator.bean;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BeanGeneratorTest {

    @Test
    void generateListOf() throws Exception {

        int SIZE = 10;
        List<TestBean> testBeans = BeanGenerator.generateListOf(SIZE, TestBean.class);
        assertEquals(SIZE, testBeans.size());
        testBeans.forEach(b -> {

            assertNotNull(b.getCodCampo());
            assertNotNull(b.getFlag());
            assertNotNull(b.getNomeCampo());
            assertNotNull(b.getInteger());
            assertNotNull(b.getDataInvio());
            assertNotNull(b.getDataElaborazione());
        });
    }

    @Test
    void generateSingleton() {

        assertDoesNotThrow(() -> BeanGenerator.generateSingleTon(TestBean.class));
    }
}