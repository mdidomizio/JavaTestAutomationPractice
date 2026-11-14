package de.practice.zoll;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbgabenRechnerTest {

    @Mock
    private TarifService tarifService;   // the fake

    @Test
    void normalCase_calculatesZoll() {
        // given: the fake answers 4.5 % for this goods number and country
        when(tarifService.getZollsatz("84713000", "CN")).thenReturn(new BigDecimal("4.5"));
        AbgabenRechner rechner = new AbgabenRechner(tarifService);

        // when
        BigDecimal zoll = rechner.berechneZoll("84713000", "CN", new BigDecimal("1000.00"));

        // then
        assertEquals(new BigDecimal("45.00"), zoll);
        verify(tarifService).getZollsatz("84713000", "CN");   // called exactly once
    }

    @Test
    void exactHalfCent_roundsUp() {
        // a) Zoll = Zollwert x Zollsatz / 100, rounded commercially to 2 decimals (0.005 rounds up).
        // 151.00 x 4.5 / 100 = 6.795 -> commercial rounding -> 6.80

        when(tarifService.getZollsatz("84713000", "CN")).thenReturn(new BigDecimal("4.5"));
        AbgabenRechner rechner = new AbgabenRechner(tarifService);

        BigDecimal zoll = rechner.berechneZoll("84713000", "CN", new BigDecimal("151.00"));

        assertEquals(new BigDecimal("6.80"), zoll);
        verify(tarifService).getZollsatz("84713000", "CN");
    }

    @Test
    void zollwertExactly150_isDutyFree() {
        // b) 150.00 is still within the Freigrenze -> 0.00, no service call
        // given
        AbgabenRechner rechner = new AbgabenRechner(tarifService);

        //when
        BigDecimal zoll = rechner.berechneZoll("84713000", "CN", new BigDecimal("150.00"));

        //then
        assertEquals(new BigDecimal("0.00"), zoll);
        verifyNoInteractions(tarifService);
    }

    @Test
    void zollwertLessThen150_isDutyFree() {
        // given
        AbgabenRechner rechner = new AbgabenRechner(tarifService);

        //when
        BigDecimal zoll = rechner.berechneZoll("84713000", "CN", new BigDecimal("149.50"));

        //then
        assertEquals(new BigDecimal("0.00"), zoll);
        verifyNoInteractions(tarifService);
    }

    @Test
    void zollwertMoreThen150_isDutyFree() {
        // given
        when(tarifService.getZollsatz("84713000", "CN")).thenReturn(new BigDecimal("4.5"));
        AbgabenRechner rechner = new AbgabenRechner(tarifService);

        BigDecimal zoll = rechner.berechneZoll("84713000", "CN", new BigDecimal("150.01"));

        assertEquals(new BigDecimal("6.75"), zoll);
        verify(tarifService).getZollsatz("84713000", "CN");
    }

    @Test
    void negativeZollwert_throwsException() {
        // c) negative Zollwert -> IllegalArgumentException, no service call
        // given
        AbgabenRechner rechner = new AbgabenRechner(tarifService);

        //when / then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> rechner.berechneZoll("84713000", "CN", new BigDecimal("-0.01")));
        assertEquals("Zollwert darf nicht negativ sein", e.getMessage());

        verifyNoInteractions(tarifService);
    }

    @Test
        void tarifServiceNotReachable_throwsException() {
        // d) service down -> AbgabenBerechnungException, never 0.00
        // given: the service is down
        when(tarifService.getZollsatz("84713000", "CN"))
                .thenThrow(new TarifServiceNichtErreichbarException("Tarifservice nicht erreichbar"));
        AbgabenRechner rechner = new AbgabenRechner(tarifService);

        assertThrows(AbgabenBerechnungException.class,
                () -> rechner.berechneZoll("84713000", "CN", new BigDecimal("1000.00")));
    }
}
