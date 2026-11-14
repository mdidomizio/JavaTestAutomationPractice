package de.practice.zoll;

import java.util.Set;

/**
 * Checks the country code (Ländercode) of origin on a customs declaration.
 *
 * Requirement REQ-02:
 *   A Ländercode is valid only if it is exactly 2 uppercase letters (A-Z)
 *   AND it is in the list of supported countries.
 *   null, empty text, lowercase letters, spaces, digits or any other length are invalid.
 */
public class LaendercodeValidator {

    private static final Set<String> SUPPORTED = Set.of("DE", "FR", "IT", "CH", "GB", "US", "CN", "TR");

    public boolean isValid(String code) {
        if (code == null || code.isEmpty()) {
            return false;
        }
        return SUPPORTED.contains(code);
    }
}
