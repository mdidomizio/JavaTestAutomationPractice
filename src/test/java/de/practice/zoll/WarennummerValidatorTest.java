package de.practice.zoll;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class WarennummerValidatorTest {
    private final WarennummerValidator validator = new WarennummerValidator();

    // 1. wrong length
    @ParameterizedTest
    @ValueSource(strings = {"8471300", "847130001"})
    void wrongLength_isInvalid(String input) {
        assertFalse(validator.isValid(input));
    }

    // 2. null and ""
    @ParameterizedTest
    @NullAndEmptySource
    void nullOrEmpty_isInvalid(String input) {
        assertFalse(validator.isValid(input));
    }

    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @CsvSource({
            "84713000, true", // correct
            "8471A000, false", // letter
            "847 1300, false", // space
            "٨٤٧١٣٠٠٠, false" // arabic
    })
    void checkWarennummer(String input, boolean expected) {
        assertEquals(expected, validator.isValid(input));
    }
}
