DROP TABLE IF EXISTS zollanmeldung;

CREATE TABLE zollanmeldung (
    id             INT PRIMARY KEY,
    warennummer    VARCHAR(8)     NOT NULL,
    ursprungsland  CHAR(2)        NOT NULL,
    zollwert       DECIMAL(10, 2) NOT NULL,
    status         VARCHAR(20)    NOT NULL,
    eingangsdatum  DATE           NOT NULL
);
