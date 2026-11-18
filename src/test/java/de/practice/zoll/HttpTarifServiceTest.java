package de.practice.zoll;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
