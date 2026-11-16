-- Test data for REQ-04. Synthetic data only, never real company data.
-- Columns: id, warennummer, ursprungsland, zollwert, status, eingangsdatum

INSERT INTO zollanmeldung VALUES (1, '84713000', 'CN', 1000.00, 'OFFEN', DATE '2026-11-02');

-- TODO: add more rows here. Every equivalence class of REQ-04 needs at least one row.
