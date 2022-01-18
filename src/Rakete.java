public class Rakete {
	private Punkt position;
	private Punkt geschwindigkeitsVektor;
	private double beschleunigungsRate;// Abhängig von Gravitation
	private double neigungsrate = 2;
	private double neigung = 270;// 0 +x // 90 +y // 180 -x // 270 -y
	private Punkt gravitation;// Vektor
	private boolean gelandet = false;
	private boolean alive = true;

	public Rakete(double gravitation) {
		init(gravitation);
	}

	private void init(double gravitation) {
		this.gravitation = new Punkt(0, gravitation);
		beschleunigungsRate = 2 * gravitation;
		position = new Punkt(0, 0);
		geschwindigkeitsVektor = new Punkt(0, 0);
	}

	public void beschleunigen() {
		geschwindigkeitsVektor.vektorAddieren(new Punkt(true, beschleunigungsRate, neigung * Math.PI / 180));
	}

	public void steuern(int richtung) {
		neigung += neigungsrate * richtung;// Richtung kann 1 oder -1 sein
		neigung = keepDegreesInRange(neigung);
	}

	private double keepDegreesInRange(double value) {
		if (value > 360) {
			value -= 360;
		}
		if (value < 0) {
			value += 360;
		}
		return value;
	}

	public void positionUpdate() {
		geschwindigkeitsVektor.vektorAddieren(gravitation);
		position.vektorAddieren(geschwindigkeitsVektor);
	}

	public void seitenAustritt(int randLinks, int randRechts) {
		// Austritt Links
		boolean links = false;
		if (getMittelpunkt().getX() < randLinks) {
			links = true;
		}
		// Austritt rechts
		boolean rechts = false;
		if (getMittelpunkt().getX() > randRechts) {
			rechts = true;
		}
		// Korrektur
		if (links) {
			position.setX(randRechts - 50);
		}
		if (rechts) {
			position.setX(randLinks - 50);
		}
	}

	public Punkt getPosition() {
		return position;
	}

	public double getNeigung() {
		return this.neigung;
	}

	public Punkt getMittelpunkt() {
		Punkt mittelpunkt = new Punkt(position);
		mittelpunkt.vektorAddieren(new Punkt(50, 50));
		return mittelpunkt;
	}

	public boolean isGelandet() {
		return gelandet;
	}

	public void setGelandet(boolean gelandet) {
		this.gelandet = gelandet;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

}