# Lunar Lander
Lunar Lander in Java programmiert. Entstand aus einem Schulprojekt.

## Spielprinzip
Lunar Lander ist ein 2D Spiel.
Eine Rakete soll auf der Mondoberfläche gelandet werden.
Diese Rakete hat Schub in eine Richtung, beides kann gesteuert werden.
Die Mondoberfläche ist bergig, es gibt Landeflächen die flach sind auf denen eine Landung möglich ist.
Wenn die Rakete auf einer Landefläche mit geeigneter Geschwindigkeit landet ist es gut,
ist man zu schnell oder berührt man die Mondoberfläche jenseits einer Landefläche explodiert die Rakete.

## Roadmap
**Grundlegende Ziele:**

<ins>Eine Rakete die folgende Eigenschaften hat:</ins>
- steuerbar in Schubkraft und Neigung
- wird von der Mondgravitation angezogen
- tritt bei Austritt auf der Seite auf der anderen Seite wieder ein

<ins>Ein Spielfeld das folgende Eigenschaften hat:</ins>
- besitzt mehrere flache Landeplattformen
- besteht aus einem relativ bergigen Terrain
- einmalig von mir erstellt

<ins>Sonstige Funktionen:</ins>
- wenn die Rakete auf der Landeplattform, mit nicht zu hoher Geschwindigkeit, landet und stehen bleibt hat, man gewonnen, das Level startet neu und man bekommt Punkte
- wenn die Rakete zu schnell ist oder irgendwo sonst landet, hat man verloren
- HUD mit Telemetrie Daten (1.Geschwindigkeit 2.Winkel 3.Schub 4.Geschwindigkeit aufgeteilt in x und y)
- alle dargestellten Elemente werden abhängig von Fensterbreite oder Höhe skaliert

**Ziele darüber hinaus:**

<ins>Eine Menü das folgende Eigenschaften hat:</ins>
- ein Knopf, mit dem man das Spiel startet
- ein Knopf, mit dem man den Planeten auswählen kann, auf dem man spielt
- ein Knopf, mit dem man festlegen kann, ob die Rakete begrenzten Tank haben soll (siehe Unten)

<ins>Zusätzliche Funktionen der Rakete:</ins>
- bei Austritt nach oben „Signalverlust“ also keine Steuerung möglich
- wird von der Gravitation des eingestellten Planeten angezogen
- hat Möglichkeit des begrenzten Tanks, das heißt der Spieler kann nur eine begrenzte Zeit lang Schub geben

<ins>Zusätzliche Funktionen des Spielfelds:</ins>
- ein Mond-Spielfeld, ein Mars-Spielfeld, ein Erd-Spielfeld, von denen jedes eine andere Gravitation hat
- jedes dieser Spielfelder wird einmalig von mir erstellt

<ins>Sonstige Funktionen:</ins>
- bei Austritt nach oben keine Datenanzeige auf HUD „Signalverlust“
- HUD zeigt zusätzlich Tankstand an

**Ziele wenn das alles klappt:**

<ins>Zusätzliche Funktionen für das Spielfeld:</ins>
- das Spielfeld wird bei jedem Spielstart je nach Einstellung zufällig generiert
- in einem anderen Modus (einstellbar im Menü) wird ein Spielfeld generiert das sich immer weiter nach rechts erweitert und Landeplattformen hat, die wie Checkpoints oder Tankstellen funktionieren
- je nach Einstellung gibt es Kanonen die auf die Rakete schießen, sofern sie sich noch über einer gewissen Höhe aufhält
- Spielfeld Hintergrund ist Fake 3D mit mehreren Schichten 2D Terrain-Silhouetten die sich relativ zur Rakete bewegen

<ins>Zusätzliche Funktionen für das Menü:</ins>
- Kosmetische Einstellungen für Rakete und Spielfeld

<ins>Zusätzliche Sonstige Funktionen:</ins>
- Alltime Highscore und andere Statistiken werden geführt und über das Menü einsehbar
- Credits

## Momentaner Entwicklungsstand
Dieses Projekt wird ab und zu in meiner Freizeit von mir vorangetrieben.
Updates folgen also keinem Zeitplan und sind auch nicht regelmäßig, in manchen Wochen wird nichts gemacht, in manchen sehr viel.
Sollte daran etwas ändern wird es hier zu sehen sein.

## License
[MIT](https://choosealicense.com/licenses/mit/)
