package de.practice.zoll;

import java.math.BigDecimal;
import java.time.LocalDate;

/** One customs declaration as stored in the database. */
public record Zollanmeldung(
        int id,
        String warennummer,
        String ursprungsland,
        BigDecimal zollwert,
        String status,
        LocalDate eingangsdatum) {
}
