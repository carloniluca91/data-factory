package it.luca.data.factory.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Utils {

    /**
     * Capitalize a given string
     * @param string input string
     * @return input string capitalized
     */

    public static String capitalize(String string) {

        String firstCharacter = string.substring(0, 1).toUpperCase();
        return firstCharacter.concat(string.substring(1));
    }

    /**
     * Load a file from classPath and return all of its lines
     * @param fileName name of file
     * @return {@link List} of strings
     */

    public static List<String> getLinesOfFile(String fileName) {

        InputStream stream = Objects.requireNonNull(
                Utils.class.getClassLoader().getResourceAsStream(fileName),
                String.format("Unable to locate file %s along classPath", fileName));

        return new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.toList());
    }
}
