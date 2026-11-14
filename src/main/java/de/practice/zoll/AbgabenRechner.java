package de.practice.zoll;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calculates the customs duty (Zoll) for one item of a declaration.
 *
 * Requirement REQ-03:
 *   a) Zoll = Zollwert x Zollsatz / 100, rounded commercially to 2 decimals (0.005 rounds up).
 *   b) If the Zollwert is 150.00 EUR or less, the Zoll is 0.00
 *      and the TarifService must NOT be called.
 *   c) A negative Zollwert is invalid: throw IllegalArgumentException,
 *      the TarifService must NOT be called.
 *   d) If the TarifService is not reachable, throw AbgabenBerechnungException.
 *      Never return 0.00 in this case, because the declaration would then pass without duty.
 */
public class AbgabenRechner {

    private static final BigDecimal FREIGRENZE = new BigDecimal("150.00");

    private final TarifService tarifService;

    public AbgabenRechner(TarifService tarifService) {
        this.tarifService = tarifService;
    }

    public BigDecimal berechneZoll(String warennummer, String ursprungsland, BigDecimal zollwert) {
        if (zollwert.signum() < 0) {
            throw new IllegalArgumentException("Zollwert darf nicht negativ sein");
        }
        if (zollwert.compareTo(FREIGRENZE) <= 0 ) {
            return new BigDecimal("0.00");
        }
        try {
            BigDecimal zollsatz = tarifService.getZollsatz(warennummer, ursprungsland);
            return zollwert.multiply(zollsatz)
                    .divide(new BigDecimal("100"))
                    .setScale(2, RoundingMode.HALF_UP);
        } catch (TarifServiceNichtErreichbarException e) {
            throw new AbgabenBerechnungException("TarifService nicht erreichbar", e);
        }
    }
}
