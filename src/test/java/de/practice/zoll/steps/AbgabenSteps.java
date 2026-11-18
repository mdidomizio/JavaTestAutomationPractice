package de.practice.zoll.steps;

import de.practice.zoll.AbgabenBerechnungException;
import de.practice.zoll.AbgabenRechner;
import de.practice.zoll.TarifService;
import de.practice.zoll.TarifServiceNichtErreichbarException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Step definitions: the Java code behind each sentence in abgaben.feature.
 * Cucumber matches a sentence to a method by its text. Given/When/Then only
 * group them, so the German keywords Angenommen/Wenn/Dann/Und all work.
 *
 * Instead of Mockito, this uses a small hand-written fake TarifService (a lambda),
 * which also counts how often it was called.
 */
public class AbgabenSteps {

    private String warennummer = "84713000";
    private String ursprungsland = "CN";
    private TarifService tarifService;
    private int tarifServiceAufrufe = 0;
    private BigDecimal ergebnis;
    private Exception fehler;

    @Given("der Zollsatz für Warennummer {string} aus {string} beträgt {word} Prozent")
    public void derZollsatzBetraegt(String warennummer, String ursprungsland, String zollsatz) {
        this.warennummer = warennummer;
        this.ursprungsland = ursprungsland;
        BigDecimal satz = zahl(zollsatz);
        tarifService = (w, l) -> {
            tarifServiceAufrufe++;
            return satz;
        };
    }

    @Given("der Tarifservice ist nicht erreichbar")
    public void derTarifserviceIstNichtErreichbar() {
        tarifService = (w, l) -> {
            tarifServiceAufrufe++;
            throw new TarifServiceNichtErreichbarException("Timeout");
        };
    }

    @When("der Zoll für einen Zollwert von {word} EUR berechnet wird")
    public void derZollWirdBerechnet(String zollwert) {
        try {
            ergebnis = new AbgabenRechner(tarifService).berechneZoll(warennummer, ursprungsland, zahl(zollwert));
        } catch (Exception e) {
            fehler = e;
        }
    }

    @Then("beträgt der Zoll {word} EUR")
    public void betraegtDerZoll(String erwarteterZoll) {
        assertNull(fehler, "Es wurde ein Fehler geworfen: " + fehler);
        assertEquals(zahl(erwarteterZoll), ergebnis);
    }

    @Then("wird ein Berechnungsfehler gemeldet")
    public void wirdEinBerechnungsfehlerGemeldet() {
        assertInstanceOf(AbgabenBerechnungException.class, fehler);
    }

    @Then("wird der Zollwert als ungültig abgelehnt")
    public void wirdDerZollwertAlsUngueltigAbgelehnt() {
        assertInstanceOf(IllegalArgumentException.class, fehler);
    }

    @Then("der Tarifservice wird nicht abgefragt")
    public void derTarifserviceWirdNichtAbgefragt() {
        assertEquals(0, tarifServiceAufrufe);
    }

    /** Lets the feature file use German decimals: "4,5" becomes 4.5 */
    private static BigDecimal zahl(String text) {
        return new BigDecimal(text.replace(",", "."));
    }
}
