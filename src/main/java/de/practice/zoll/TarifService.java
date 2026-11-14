package de.practice.zoll;

import java.math.BigDecimal;

/**
 * External tariff service (in real life: a separate system the calculation calls over the network).
 * Returns the duty rate in percent, e.g. 4.5 means 4.5 %.
 * Throws TarifServiceNichtErreichbarException when the service is down.
 */
public interface TarifService {
    BigDecimal getZollsatz(String warennummer, String ursprungsland);
}
