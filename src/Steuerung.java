import java.util.HashSet;
import java.awt.event.KeyEvent;

public class Steuerung implements Runnable {
	private GUI dieGUI;
	private Rakete dieRakete;
	private Spielfeld dasSpielfeld;
	private HashSet<Integer> keysPressed = new HashSet<>();
	private GameState gameState;

	private enum GameState {
		START, GAMELOOP, END
	}

	public static void main(String[] args) {
		Thread thread = new Thread(new Steuerung());
		thread.start();
	}

	public Steuerung() {
		gameState = GameState.START;
		spielfeldInitialisieren();
		dieRakete = new Rakete(dasSpielfeld.getGravitation());
		dieGUI = new GUI(this);
	}

	public void keyPressed(int code) {
		switch (code) {
		case KeyEvent.VK_SPACE:
			keysPressed.add(0);
			break;
		case KeyEvent.VK_RIGHT:
			keysPressed.add(1);
			break;
		case KeyEvent.VK_LEFT:
			keysPressed.add(2);
			break;
		}
	}

	public void keyReleased(int code) {
		switch (code) {
		case KeyEvent.VK_SPACE:
			keysPressed.remove(0);
			break;
		case KeyEvent.VK_RIGHT:
			keysPressed.remove(1);
			break;
		case KeyEvent.VK_LEFT:
			keysPressed.remove(2);
			break;
		}
	}

	private void update() {
		updateGameState();
		switch (gameState) {
		case START:
			break;
		case GAMELOOP:
			gameLoopInput();
			kollisionen();
			break;
		case END:
			break;
		default:

		}
		dieGUI.repaint();
		System.out.println(gameState);
	}

	private void updateGameState() {
		switch (gameState) {
		case START:
			if (keysPressed.contains(0)) {
				gameState = GameState.GAMELOOP;
			}
			break;
		case GAMELOOP:
			if (dieRakete.isGelandet()) {
				gameState = GameState.END;
			}
			break;
		case END:
			break;
		default:

		}
	}

	private void gameLoopInput() {
		if (keysPressed.contains(0)) {// Schub - Leertaste
			dieRakete.beschleunigen();
		}
		if (keysPressed.contains(1)) {// Rechts - Pfeil Rechts
			dieRakete.steuern(1);
		}
		if (keysPressed.contains(2)) {// Links - Pfeil Links
			dieRakete.steuern(-1);
		}
	}

	private void kollisionen() {
		dieRakete.seitenAustritt(0, dasSpielfeld.getBreite());
		dieRakete.positionUpdate(); // Wenn positionUpdate über bodenBeruehrung ist
		bodenBeruehrung(); // dann kann man vom boden wieder abheben
	} // (siehe Rakete.beschleunigen erste Zeile) Debug glaub

	private void bodenBeruehrung() {
		boolean bodenBeruehrt = false;
		Punkt[] punkt = dasSpielfeld.getDerPunkt();
		Punkt[] hitPunkt = hitPunkteSetzen();
		dieRakete.setGelandet(false);
		for (int i = 0; i < dasSpielfeld.getAnzahlPunkte() - 1; i++) {
			double x1 = punkt[i].getX();
			double y1 = punkt[i].getY();
			double x2 = punkt[i + 1].getX();
			double y2 = punkt[i + 1].getY();
			double m = (y2 - y1) / (x2 - x1);// Gerade g zwischen p1 und p2 (i und i+1)
			double c = y1 - m * x1; // I 'y' = 'm'*'x'+c bei ' ' ist Wert bekannt
									// Ia c = 'y1' - 'm'*'x1'

			boolean landungFlach = (y2 == y1);

			// Kollision mit Unterseite als Gerade
			// hitPunkt 9 und 10 sind uL und uR
			double xL = hitPunkt[9].getX();
			double yL = hitPunkt[9].getY();
			double xR = hitPunkt[10].getX();
			double yR = hitPunkt[10].getY();
			// Ist die Unterseite im Intervall x1 x2?
			if ((x1 <= xL && xR <= x2) || (x1 <= xR && xL <= x2)) {
				// Geradensteigung m und Y-Achsenabschnitt c der Unterseite ermitteln
				double mU = (yR - yL) / (xR - xL);
				double cU = yR - mU * xR;
				// Schnittstelle berechnen von Spielfeld-Gerade und gU
				double xS = (cU - c) / (m - mU);
				// Ist die Schnittstelle im x Intervall der Gerade des Spielfelds und
				// Unterseite?
				if (((xL <= xS && xS <= xR) || (xR <= xS && xS <= xL)) && (x1 <= xS && xS <= x2)) {
					bodenBeruehrt = true;
				}
			}

			// Wenn mindestens einer der Kollisionspunkte auf der Geraden liegt
			for (int j = 0; j < 11; j++) {
				if (punktAufGerade(hitPunkt[j], c, x1, x2, m)) {
					bodenBeruehrt = true;
				}
				;
			}

			if (bodenBeruehrt) {
				if (landungFlach || dieRakete.isGelandet()) {
					/*
					 * Kollision auf einer flachen Fläche gilt als Landung, später noch Neigung und
					 * Geschwindigkeit berücksichtigen
					 */
					dieRakete.setGelandet(true);
				} else {
					dieRakete.setAlive(false);
				}
			}
		}
	}

	public void grafik() {
		dieGUI.hintergrundLoeschen();
		dieGUI.spielfeldZeichnen(dasSpielfeld.getAnzahlPunkte(), dasSpielfeld.getDerPunkt());
		dieGUI.raketeZeichnen(dieRakete.getPosition(), dieRakete.getMittelpunkt(), dieRakete.getNeigung());
	}

	private void spielfeldInitialisieren() {
		dasSpielfeld = new Spielfeld(0.05, 108 * 8);
		dasSpielfeld.setAnzahlPunkte(30);
		int breite = dasSpielfeld.getBreite();
		int hoehe = dasSpielfeld.getHoehe();
		// Alle Punkte von Hand eintragen
		dasSpielfeld.setPunkt(0, new Punkt(0, hoehe * 0.55));
		dasSpielfeld.setPunkt(1, new Punkt(breite * 0.07, hoehe * 0.4));
		dasSpielfeld.setPunkt(2, new Punkt(breite * 0.08, hoehe * 0.42));
		dasSpielfeld.setPunkt(3, new Punkt(breite * 0.15, hoehe * 0.27));
		dasSpielfeld.setPunkt(4, new Punkt(breite * 0.19, hoehe * 0.5));
		dasSpielfeld.setPunkt(5, new Punkt(breite * 0.2, hoehe * 0.48));
		dasSpielfeld.setPunkt(6, new Punkt(breite * 0.23, hoehe * 0.75));
		dasSpielfeld.setPunkt(7, new Punkt(breite * 0.27, hoehe * 0.58));
		dasSpielfeld.setPunkt(8, new Punkt(breite * 0.3, hoehe * 0.58));
		dasSpielfeld.setPunkt(9, new Punkt(breite * 0.33, hoehe * 0.65));
		dasSpielfeld.setPunkt(10, new Punkt(breite * 0.4, hoehe * 0.62));
		dasSpielfeld.setPunkt(11, new Punkt(breite * 0.42, hoehe * 0.64));
		dasSpielfeld.setPunkt(12, new Punkt(breite * 0.46, hoehe * 0.52));
		dasSpielfeld.setPunkt(13, new Punkt(breite * 0.5, hoehe * 0.77));
		dasSpielfeld.setPunkt(14, new Punkt(breite * 0.51, hoehe * 0.75));
		dasSpielfeld.setPunkt(15, new Punkt(breite * 0.53, hoehe * 0.85));
		dasSpielfeld.setPunkt(16, new Punkt(breite * 0.55, hoehe * 0.83));
		dasSpielfeld.setPunkt(17, new Punkt(breite * 0.59, hoehe * 0.87));
		dasSpielfeld.setPunkt(18, new Punkt(breite * 0.7, hoehe * 0.87));
		dasSpielfeld.setPunkt(19, new Punkt(breite * 0.72, hoehe * 0.69));
		dasSpielfeld.setPunkt(20, new Punkt(breite * 0.73, hoehe * 0.71));
		dasSpielfeld.setPunkt(21, new Punkt(breite * 0.76, hoehe * 0.64));
		dasSpielfeld.setPunkt(22, new Punkt(breite * 0.81, hoehe * 0.75));
		dasSpielfeld.setPunkt(23, new Punkt(breite * 0.84, hoehe * 0.72));
		dasSpielfeld.setPunkt(24, new Punkt(breite * 0.87, hoehe * 0.72));
		dasSpielfeld.setPunkt(25, new Punkt(breite * 0.89, hoehe * 0.68));
		dasSpielfeld.setPunkt(26, new Punkt(breite * 0.92, hoehe * 0.72));
		dasSpielfeld.setPunkt(27, new Punkt(breite * 0.94, hoehe * 0.65));
		dasSpielfeld.setPunkt(28, new Punkt(breite * 0.95, hoehe * 0.67));
		dasSpielfeld.setPunkt(29, new Punkt(breite, hoehe * 0.55));

//		für Test zwecke random Map
//		for(int i = 1; i < dasSpielfeld.getAnzahlPunkte()-1; i++) {
//			int x = dasSpielfeld.getBreite() / (dasSpielfeld.getAnzahlPunkte()-1) * i;
//			int y = (int)((0.95*dasSpielfeld.getHoehe())-Math.random()*(dasSpielfeld.getHoehe()*0.75));
//			Punkt p = new Punkt(x,y);
//			dasSpielfeld.setPunkt(i, p);
//		}
	}

	@Override
	public void run() {
		double timePerTick = 1000000000 / 60;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		while (true) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			lastTime = now;
			if (delta >= 1) {
				// Spielogik
				// Aktualisieren
				update();
				// Zeichnen
				delta--;
			}
		}
	}

	private boolean punktAufGerade(Punkt p, double c, double x1, double x2, double m) {
		boolean istAufGerade = false;
		if ((x1 <= p.getX()) && (p.getX() <= x2)) {
			double cP = p.getY() - m * p.getX();
			if (c <= cP) {
				istAufGerade = true;
			}
		}
		return istAufGerade;
	}

	private Punkt hitPunktBerechnen(Punkt ausgangspunkt, int abstand, double winkel) {
		Punkt hitPunkt = new Punkt(ausgangspunkt.getX(), ausgangspunkt.getY());
		Punkt vektor = new Punkt(true, abstand, winkel);
		hitPunkt.vektorAddieren(vektor);
		return hitPunkt;
	}

	private Punkt[] hitPunkteSetzen() {
		Punkt[] hitPunkt = new Punkt[11];
		// Punkt S an der Spitze:
		hitPunkt[0] = hitPunktBerechnen(new Punkt(dieRakete.getMittelpunkt()), 50,
				Math.toRadians(dieRakete.getNeigung()));

		// Punkte oL, oR
		Punkt o = hitPunktBerechnen(new Punkt(dieRakete.getMittelpunkt()), -27,
				Math.toRadians(dieRakete.getNeigung() - 180));
		hitPunkt[1] = hitPunktBerechnen(new Punkt(o), 20, Math.toRadians(dieRakete.getNeigung() - 90));
		hitPunkt[2] = hitPunktBerechnen(new Punkt(o), 20, Math.toRadians(dieRakete.getNeigung() + 90));

		// Punkte aL, aR
		Punkt a = hitPunktBerechnen(new Punkt(dieRakete.getMittelpunkt()), -10,
				Math.toRadians(dieRakete.getNeigung() - 180));
		hitPunkt[3] = hitPunktBerechnen(new Punkt(a), 20, Math.toRadians(dieRakete.getNeigung() - 90));
		hitPunkt[4] = hitPunktBerechnen(new Punkt(a), 20, Math.toRadians(dieRakete.getNeigung() + 90));

		// Punkte bL, bR
		Punkt b = hitPunktBerechnen(new Punkt(dieRakete.getMittelpunkt()), 8,
				Math.toRadians(dieRakete.getNeigung() - 180));
		hitPunkt[5] = hitPunktBerechnen(new Punkt(b), 25, Math.toRadians(dieRakete.getNeigung() - 90));
		hitPunkt[6] = hitPunktBerechnen(new Punkt(b), 25, Math.toRadians(dieRakete.getNeigung() + 90));

		// Punkte fl, fR
		Punkt f = hitPunktBerechnen(new Punkt(dieRakete.getMittelpunkt()), 33,
				Math.toRadians(dieRakete.getNeigung() - 180));
		hitPunkt[7] = hitPunktBerechnen(new Punkt(f), 30, Math.toRadians(dieRakete.getNeigung() - 90));
		hitPunkt[8] = hitPunktBerechnen(new Punkt(f), 30, Math.toRadians(dieRakete.getNeigung() + 90));

		// Punkte uL, uR
		Punkt u = hitPunktBerechnen(new Punkt(dieRakete.getMittelpunkt()), 50,
				Math.toRadians(dieRakete.getNeigung() - 180));
		hitPunkt[9] = hitPunktBerechnen(new Punkt(u), 20, Math.toRadians(dieRakete.getNeigung() - 90));
		hitPunkt[10] = hitPunktBerechnen(new Punkt(u), 20, Math.toRadians(dieRakete.getNeigung() + 90));

		return hitPunkt;
	}

	public Spielfeld getDasSpielfeld() {
		return dasSpielfeld;
	}
}