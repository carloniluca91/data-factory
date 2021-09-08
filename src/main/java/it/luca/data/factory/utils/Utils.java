package it.luca.data.factory.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static it.luca.utils.functional.Optional.isPresent;

public class Utils {

    /**
     * Capitalize a given {@link String}
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
     * @throws FileNotFoundException if given fileName is not found
     */

    public static List<String> getLinesOfFile(String fileName) throws FileNotFoundException {

        InputStream stream = Utils.class.getClassLoader().getResourceAsStream(fileName);
        if (isPresent(stream)) {
            //noinspection ConstantConditions
            return new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.toList());
        } else {
            throw new FileNotFoundException(String.format("Unable to locate file %s along classPath", fileName));
        }
    }
}
