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

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(List.of(1), ids);
    }
}
