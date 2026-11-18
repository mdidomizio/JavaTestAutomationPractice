package de.practice.zoll;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Real TarifService: asks a remote tariff system over HTTP.
 *
 * Requirement REQ-05 (interface to the tariff system):
 *   a) Request: GET {baseUrl}/tarife/{warennummer}?land={ursprungsland}
 *      with the header "Accept: application/json".
 *   b) Answer 200 with JSON like {"warennummer":"84713000","land":"CN","zollsatz":4.5}
 *      -> return the value of "zollsatz".
 *   c) Any server error (status 500 to 599)
 *      -> throw TarifServiceNichtErreichbarException.
 *   d) No answer within 2 seconds (timeout)
 *      -> throw TarifServiceNichtErreichbarException.
 *      The calculation must never hang waiting for the tariff system.
 */
public class HttpTarifService implements TarifService {

    private static final Pattern ZOLLSATZ = Pattern.compile("\"zollsatz\"\\s*:\\s*([0-9.]+)");

    private final String baseUrl;
    private final HttpClient client = HttpClient.newHttpClient();

    public HttpTarifService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public BigDecimal getZollsatz(String warennummer, String ursprungsland) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/tarife/" + warennummer + "?country=" + ursprungsland))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new TarifServiceNichtErreichbarException("Keine Verbindung zum Tarifsystem: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TarifServiceNichtErreichbarException("Anfrage unterbrochen");
        }

        if (response.statusCode() == 500) {
            throw new TarifServiceNichtErreichbarException("Serverfehler " + response.statusCode());
        }

        Matcher matcher = ZOLLSATZ.matcher(response.body());
        if (!matcher.find()) {
            throw new IllegalStateException("Kein Zollsatz in der Antwort: " + response.body());
        }
        return new BigDecimal(matcher.group(1));
    }
}
