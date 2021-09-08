package it.luca.data.factory.generator.bean;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BeanGeneratorTest {

    @Test
    void generate() {

        assertThrows(MissingNoArgConstructorException.class, () -> BeanGenerator.generate(AllArgsConstructorBean.class));
        assertDoesNotThrow(() -> {

            TestBean bean = BeanGenerator.generate(TestBean.class);
            assertNotNull(bean.getCodCampo());
            assertNotNull(bean.getNomeCampo());
        });
    }
}