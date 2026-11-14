package de.practice.zoll;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class LaendercodeValidatorTest {
    private final LaendercodeValidator validator = new LaendercodeValidator();
    // check length = 2 (different length)
    @ParameterizedTest
    @ValueSource(strings = {"D", "DEE"})
    void wrongLength_isInvalid(String input) { assertFalse(validator.isValid(input)); }

    // check is upper case / lowercase / digits / spaces
    //check from A_Z /
    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @CsvSource({
            "DE, true",
            "De, false", // one lowercase
            "de, false", // two lowercase
            "' DE', false",// one space + length 3
            "'DE ', false",// one space + length 3
            "' DE', false",// one space + length 3
            "' D', false", // one space + length 2
            "'D ', false", // one space + length
            "'  ', false", // one space + length
            "D1, false", // one digit
            "DK, false" // out of countries list
    })
    void checkLaendercode(String input, boolean expected) {
        assertEquals(expected, validator.isValid(input));
    }

    // check null and empty
    @ParameterizedTest
    @NullAndEmptySource
    void nullOrEmpty_isInvalid(String input) { assertFalse(validator.isValid(input)); }

}


