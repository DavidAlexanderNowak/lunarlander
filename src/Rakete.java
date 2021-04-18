public class Rakete {
	private Punkt position;
	private Punkt geschwindigkeitsVektor;
	private double beschleunigungsRate;//Abhängig von Gravitation
	double neigungsrate = 2;
	private double neigung = 270;// 0 +x // 90 +y // 180 -x // 270 -y
	private Punkt gravitation;//Vektor
	public boolean stopp = false;
	
	public Rakete(double gravitation) {
		this.gravitation = new Punkt(0,gravitation);
		beschleunigungsRate = 2*gravitation;
		position = new Punkt(0,0);
		geschwindigkeitsVektor = new Punkt(0,0);
	}
	
	public void beschleunigen() {
		stopp = false;//wenn diese Zeile hier ist kann man vom Boden wieder abheben (Debug?)
		//gleichmäßig beschleunigte Bewegung (Danke Mercedes Belz)
		geschwindigkeitsVektor.vektorAddieren(new Punkt(true,beschleunigungsRate,neigung*Math.PI/180));
	}
	
	public void steuern(int richtung) {
		neigung += neigungsrate*richtung;//Richtung kann 1 oder -1 sein
//		Neigung auf 0 bis 360 begrenzen (gut für das HUD)
		if(neigung > 360) {
			neigung -= 360;
		}
		if(neigung < 0) {
			neigung += 360;
		}
	}
	
	public void positionUpdate() {
		geschwindigkeitsVektor.vektorAddieren(gravitation);
		if(stopp) {
			geschwindigkeitsVektor = new Punkt(0,0);
		}
		position.vektorAddieren(geschwindigkeitsVektor);
	}
	
	public void seitenAustritt(int randLinks, int randRechts) {
		//Austritt Links
		boolean links = false;
		if(getMittelpunkt().getX() < randLinks) {
			links = true;
		}
		//Austritt rechts
		boolean rechts = false;
		if(getMittelpunkt().getX() > randRechts) {
			rechts = true;
		}
		//Korrektur
		if(links) {
			position.setX(randRechts-50);
		}
		if(rechts) {
			position.setX(randLinks-50);
		}
	}
	
	public Punkt getPosition() {
		return position;
	}

	public void setStopp(boolean stopp) {
		this.stopp = stopp;
	}

	public double getNeigung() {
		return this.neigung;
	}
	
	public Punkt getMittelpunkt() {
		Punkt mittelpunkt = new Punkt(position);
		mittelpunkt.vektorAddieren(new Punkt(50,50));
		return mittelpunkt;
	}
}