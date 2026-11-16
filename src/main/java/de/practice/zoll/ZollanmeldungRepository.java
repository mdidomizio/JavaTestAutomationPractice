package de.practice.zoll;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads customs declarations from the database.
 *
 * Requirement REQ-04 (Prüfliste for customs officers):
 *   findPruefliste(grenzwert) returns every declaration that
 *   - has the status 'OFFEN' (other statuses: 'IN_PRUEFUNG', 'ABGESCHLOSSEN'), and
 *   - has a Zollwert strictly greater than the given grenzwert.
 *   The list is sorted by eingangsdatum, OLDEST first, so the longest-waiting
 *   declarations are checked first.
 */
public class ZollanmeldungRepository {

    private final Connection connection;

    public ZollanmeldungRepository(Connection connection) {
        this.connection = connection;
    }

    public List<Zollanmeldung> findPruefliste(BigDecimal grenzwert) throws SQLException {
        String sql = "SELECT id, warennummer, ursprungsland, zollwert, status, eingangsdatum "
                + "FROM zollanmeldung "
                + "WHERE status = 'OFFEN' AND zollwert > ? "
                + "ORDER BY eingangsdatum ASC";

        List<Zollanmeldung> result = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, grenzwert);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(new Zollanmeldung(
                            rs.getInt("id"),
                            rs.getString("warennummer"),
                            rs.getString("ursprungsland"),
                            rs.getBigDecimal("zollwert"),
                            rs.getString("status"),
                            rs.getObject("eingangsdatum", LocalDate.class)));
                }
            }
        }
        return result;
    }
}
