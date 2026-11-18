package de.practice.zoll;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @WireMockTest starts a fake HTTP server on a free port before each test
 * and resets it afterwards. wm.getHttpBaseUrl() is its address, e.g. http://localhost:51234
 */
@WireMockTest
class HttpTarifServiceTest {

    @Test
    void answer200_returnsZollsatz(WireMockRuntimeInfo wm) {
        // given: the fake tariff system answers with JSON
        stubFor(get(urlPathEqualTo("/tarife/84713000"))
                .willReturn(okJson("{\"warennummer\":\"84713000\",\"land\":\"CN\",\"zollsatz\":4.5}")));
        HttpTarifService service = new HttpTarifService(wm.getHttpBaseUrl());

        // when
        BigDecimal zollsatz = service.getZollsatz("84713000", "CN");

        // then
        assertEquals(new BigDecimal("4.5"), zollsatz);
    }

    @Test
    void request_containsAcceptHeader(WireMockRuntimeInfo wm) {
        // given: the fake tariff system answers with JSON
        stubFor(get(urlPathEqualTo("/tarife/84713000"))
                .willReturn(okJson("{\"warennummer\":\"84713000\",\"land\":\"CN\",\"zollsatz\":4.5}")));
        HttpTarifService service = new HttpTarifService(wm.getHttpBaseUrl());

        //when
        service.getZollsatz("84713000", "CN");

        // then
        verify(getRequestedFor(urlPathEqualTo("/tarife/84713000"))
                .withHeader("Accept", equalTo("application/json"))
        );
    }

    @Test
    void request_containsParameters(WireMockRuntimeInfo wm) {
        // given: the fake tariff system answers with JSON
        stubFor(get(urlPathEqualTo("/tarife/84713000"))
                .willReturn(okJson("{\"warennummer\":\"84713000\",\"land\":\"CN\",\"zollsatz\":4.5}")));
        HttpTarifService service = new HttpTarifService(wm.getHttpBaseUrl());

        //when
        service.getZollsatz("84713000", "CN");

        // then
        verify(getRequestedFor(urlPathEqualTo("/tarife/84713000"))
                .withQueryParam("land", equalTo("CN"))
        );
    }

    @Test
    void checksServerError503(WireMockRuntimeInfo wm) {
        // given: the tariff system answers 503 (service unavailable)
        stubFor(get(urlPathEqualTo("/tarife/84713000"))
                .willReturn(aResponse().withStatus(503)));
        HttpTarifService service = new HttpTarifService(wm.getHttpBaseUrl());

        // when/then
        assertThrows(TarifServiceNichtErreichbarException.class,
                () -> service.getZollsatz("84713000", "CN"));
    }

    @ParameterizedTest
    @ValueSource(ints = {500, 502, 503, 599})
    void serverError_throwNichtErreichbar(int status, WireMockRuntimeInfo wm) {
        stubFor(get(urlPathEqualTo("/tarife/84713000"))
                .willReturn(aResponse().withStatus(status)));
        HttpTarifService service = new HttpTarifService(wm.getHttpBaseUrl());

        assertThrows(TarifServiceNichtErreichbarException.class,
                () -> service.getZollsatz("84713000", "CN"));
    }

    @Test
    void slowAnswer_throwNichtErreichbar(WireMockRuntimeInfo wm) {
        // given: the tariff system answers only after 3 seconds (limit: 2 s)
        stubFor(get(urlPathEqualTo("/tarife/84713000"))
                .willReturn(okJson("{\"zollsatz\":4.5}").withFixedDelay(3000)));
        HttpTarifService service = new HttpTarifService(wm.getHttpBaseUrl());

        assertThrows(TarifServiceNichtErreichbarException.class,
                () -> service.getZollsatz("84713000", "CN"));
    }

    @Test
    void answerWithinLimit_returnsZollsatz(WireMockRuntimeInfo wm){
        // given: the tariff system answers after 1 seconds (within the 2s limit)
        stubFor(get(urlPathEqualTo("/tarife/84713000"))
                .willReturn(okJson("{\"zollsatz\":4.5}").withFixedDelay(1000)));
        HttpTarifService service = new HttpTarifService(wm.getHttpBaseUrl());

        //when/then
        assertEquals(new BigDecimal("4.5"), service.getZollsatz("84713000", "CN"));
    }
}
