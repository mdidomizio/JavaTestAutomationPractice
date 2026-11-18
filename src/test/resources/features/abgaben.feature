# language: de
Funktionalität: Zollberechnung (REQ-03)
  Als Zollverwaltung
  möchte ich, dass der Zoll für jede Position korrekt berechnet wird,
  damit keine Anmeldung mit falschen Abgaben durchgeht.

  Szenario: Normaler Fall mit Zollsatz 4,5 Prozent
    Angenommen der Zollsatz für Warennummer "84713000" aus "CN" beträgt 4,5 Prozent
    Wenn der Zoll für einen Zollwert von 1000,00 EUR berechnet wird
    Dann beträgt der Zoll 45,00 EUR

  # TODO 1: Szenariogrundriss für die Freigrenze (REQ-03 b) mit einer Beispiele-Tabelle
  # TODO 2: Szenario für die kaufmännische Rundung (REQ-03 a)
  # TODO 3: Szenario "Tarifservice nicht erreichbar" (REQ-03 d)
  # TODO 4: Szenario für einen negativen Zollwert (REQ-03 c) - dafür brauchst du einen neuen Schritt
