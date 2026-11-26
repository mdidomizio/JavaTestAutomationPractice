# language: de
Funktionalität: Zollberechnung (REQ-03)
  Als Zollverwaltung
  möchte ich, dass der Zoll für jede Position korrekt berechnet wird,
  damit keine Anmeldung mit falschen Abgaben durchgeht.

  Szenario: Normaler Fall mit Zollsatz 4,5 Prozent
    Angenommen der Zollsatz für Warennummer "84713000" aus "CN" beträgt 4,5 Prozent
    Wenn der Zoll für einen Zollwert von 1000,00 EUR berechnet wird
    Dann beträgt der Zoll 45,00 EUR

  Szenariogrundriss: Zollwert bis zur Freigrenze ist zollfrei
    Angenommen der Zollsatz für Warennummer "84713000" aus "CN" beträgt 4,5 Prozent
    Wenn der Zoll für einen Zollwert von <zollwert> EUR berechnet wird
    Dann beträgt der Zoll 0,00 EUR
    Und der Tarifservice wird nicht abgefragt

    Beispiele:
      | zollwert |
      | 0,00     |
      | 100,00   |
      | 149,99   |
      | 150,00   |

  Szenario: Zollwert knapp über der Freig renze ist zollpflichtig
    Angenommen der Zollsatz für Warennummer "84713000" aus "CN" beträgt 4,5 Prozent
    Wenn der Zoll für einen Zollwert von 150,01 EUR berechnet wird
    Dann beträgt der Zoll 6,75 EUR

  #  151.00 x 4.5 / 100 = 6.795 -> kaufmännisch gerundet  -> 6.80
  Szenario: Exakt halber Cent wird aufgerundet
    Angenommen der Zollsatz für Warennummer "84713000" aus "CN" beträgt 4,5 Prozent
    Wenn der Zoll für einen Zollwert von 151,00 EUR berechnet wird
    Dann beträgt der Zoll 6,80 EUR

  Szenario: Tarifservice nicht erreichbar
    Angenommen der Tarifservice ist nicht erreichbar
    Wenn der Zoll für einen Zollwert von 151,00 EUR berechnet wird
    Dann wird ein Berechnungsfehler gemeldet

  Szenario: Negativer Zollwert wird abgelehnt
    Angenommen der Zollsatz für Warennummer "84713000" aus "CN" beträgt 4,5 Prozent
    Wenn der Zoll für einen Zollwert von -0,01 EUR berechnet wird
    Dann wird der Zollwert als ungültig abgelehnt
    Und der Tarifservice wird nicht abgefragt