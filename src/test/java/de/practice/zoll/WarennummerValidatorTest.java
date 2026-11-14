package de.practice.zoll;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WarennummerValidatorTest {

    private final WarennummerValidator validator = new WarennummerValidator();

    // Example test: read this one first, then write your own below.
    @Test
    void exactlyEightDigits_isValid() {
        assertTrue(validator.isValid("84713000"));
    }

    // TODO 1: 7 digits must be invalid (boundary just below 8)
    @Test
    void exactlySevenDigits_isInvalid() {
        assertFalse(validator.isValid("8471300"));
    }

    // TODO 2: 9 digits must be invalid (boundary just above 8)

    @Test
    void exactlyNineDigits_isInvalid() {
        assertFalse(validator.isValid("847130000"));
    }

    // TODO 3: letters inside the number must be invalid, e.g. "8471A000"

    @Test
    void lettersAndNumbersDigits_isInvalid() {
        assertFalse(validator.isValid("8471A000"));
    }

    // TODO 4: null must be invalid
    @Test
    void null_isInvalid() {
        assertFalse(validator.isValid(null));
    }

    // TODO 5: empty text "" must be invalid

    @Test
    void empty_isInvalid() {assertFalse(validator.isValid("")); }

    // TODO 6 (think like a tester): which other input should you check?

    @Test
    void space_isInvalid() {assertFalse(validator.isValid("8471 3000")); }
    @Test
    void arabicDigits_isInvalid() {
        assertFalse(validator.isValid("٨٤٧١٣٠٠٠"));
    }
}
