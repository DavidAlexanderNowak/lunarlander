
public class Spielfeld {
	private int breite;
	private int hoehe;
	private int anzahlPunkte;
	private double gravitation;
	private Punkt[] derPunkt;
	
	public Spielfeld(double gravitation, int hoehe) {
		this.gravitation = gravitation;
		this.hoehe = hoehe;
		breite = hoehe*16/9;
	}
	
	public int getBreite() {
		return breite;
	}

	public int getHoehe() {
		return hoehe;
	}

	public void setBreite(int breite) {
		this.breite = breite;
	}

	public void setHoehe(int hoehe) {
		this.hoehe = hoehe;
	}

	public int getAnzahlPunkte() {
		return anzahlPunkte;
	}
	public double getGravitation() {
		return gravitation;
	}
	
	public void setAnzahlPunkte(int anzahlPunkte) {
		this.anzahlPunkte = anzahlPunkte;
		this.derPunkt = new Punkt[anzahlPunkte];
	}
	public void setGravitation(double gravitation) {
		this.gravitation = gravitation;
	}

	public Punkt[] getDerPunkt() {
		return derPunkt;
	}

	public void setDerPunkt(Punkt[] derPunkt) {
		this.derPunkt = derPunkt;
	}
	
	public void setPunkt(int index, Punkt punkt) {
		this.derPunkt[index] = punkt;
	}
	
}
