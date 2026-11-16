package de.practice.zoll;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ZollanmeldungRepositoryTest {

    private Connection connection;
    private ZollanmeldungRepository repository;

    @BeforeEach
    void setUp() throws SQLException {
        // A fresh in-memory database for every test: no leftovers between tests
        connection = DriverManager.getConnection("jdbc:h2:mem:");
        try (Statement statement = connection.createStatement()) {
            statement.execute("RUNSCRIPT FROM 'classpath:schema.sql'");
            statement.execute("RUNSCRIPT FROM 'classpath:testdata.sql'");
        }
        repository = new ZollanmeldungRepository(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void openDeclarationAboveGrenzwert_isOnPruefliste() throws SQLException {
        List<Zollanmeldung> liste = repository.findPruefliste(new BigDecimal("150.00"));

        // Compare only the ids: short and readable
        List<Integer> ids = liste.stream().map(Zollanmeldung::id).toList();
        assertEquals(List.of(6, 1), ids);
    }

    @Test
    void declarationsNotOpenOrNotAboveGrenzwert_areNotOnPruefliste() throws SQLException {
        List<Integer> ids = repository.findPruefliste(new BigDecimal("150.00"))
                .stream().map(Zollanmeldung::id).toList();

        assertFalse(ids.contains(2), "ABGESCHLOSSEN darf nicht auf der Liste sein");
        assertFalse(ids.contains(3), "IN_PRUEFUNG darf nicht auf der Liste sein");
        assertFalse(ids.contains(4), "ABGESCHLOSSEN darf nicht auf der Liste sein");
        assertFalse(ids.contains(5), "Wert genau 150.00 darf nicht auf der Liste sein");
        assertFalse(ids.contains(7), "Wert unter 150.00 darf nicht auf der Liste sein");
    }
}
