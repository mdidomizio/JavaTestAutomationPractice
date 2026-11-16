-- Test data for REQ-04. Synthetic data only, never real company data.
-- Columns: id, warennummer, ursprungsland, zollwert, status, eingangsdatum

-- Klasse: offen, Wert usber der Grenze (150.00) -> auf der Liste
INSERT INTO zollanmeldung VALUES (1, '84713000', 'CN', 1000.00, 'OFFEN', DATE '2026-11-02');

  -- Klasse: abgeschlossen, Wert niedrieger der Grenze (150.00) -> NICHT auf der Liste (REQ-04: strikt größer)
INSERT INTO zollanmeldung VALUES (2, '84713001', 'CN', 19.00, 'ABGESCHLOSSEN', DATE '2026-11-01');

-- Klasse: Pruefung, Wert oben der Grenze (150.00) -> NICHT auf der Liste (REQ-04: nur offen )
INSERT INTO zollanmeldung VALUES (3, '84713002', 'CN', 500.00, 'IN_PRUEFUNG', DATE '2026-10-20');

-- Klasse: abgeschlossen, Wert oben der Grenze (150.00) -> NICHT auf der Liste (REQ-04: nur offen)
INSERT INTO zollanmeldung VALUES (4, '84713003', 'CN', 800.00, 'ABGESCHLOSSEN', DATE '2026-10-10');

-- Klasse: offen, Wert genau an der Grenze (150.00) -> NICHT auf der Liste (REQ-04: strikt größer)
INSERT INTO zollanmeldung VALUES (5, '84713004', 'CN', 150.00, 'OFFEN', DATE '2026-10-05');

-- Klasse: offen, Wert uber der Grenze (150.00) -> auf der Liste
INSERT INTO zollanmeldung VALUES (6, '84713005', 'CN', 150.01, 'OFFEN', DATE '2026-10-01');

-- Klasse: offen, Wert unter der Grenze (150.00) -> NICHT auf der Liste  (REQ-04: strikt größer)
INSERT INTO zollanmeldung VALUES (7, '84713006', 'CN', 149.99, 'OFFEN', DATE '2026-09-25');




