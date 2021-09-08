package it.luca.data.factory.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void capitalize() {

        String INPUT = "hello";
        assertEquals("Hello", Utils.capitalize(INPUT));
    }

}