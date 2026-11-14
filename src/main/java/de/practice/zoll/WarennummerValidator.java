package de.practice.zoll;

/**
 * Checks the goods number (Warennummer) on a customs declaration.
 * <p>
 * Requirement REQ-01:
 * A Warennummer is valid only if it consists of exactly 8 digits (0-9).
 * null, empty text, letters, spaces or any other length are invalid.
 * <p>
 * Careful: this class may contain a bug. Your tests should find it.
 */
public class WarennummerValidator {

    public boolean isValid(String warennummer) {
        if (warennummer == null) {
            return false;
        }
        if (warennummer.length() != 8) {
            return false;
        }
        for (char c : warennummer.toCharArray()) {
            if (c < '0' || c > '9' ) {
                return false;
            }
        }
        return true;
    }
}
