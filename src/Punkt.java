
public class Punkt{
	private double x;
	private double y;
	
	public Punkt(double x, double y) {//bei Erstellung werden direkt die Koordinaten zugewiesen
		this.x = x;
		this.y = y;
	}
	
	public Punkt(boolean modus, double l, double w) {
		//Polar Coords als Input -- l=vektorlänge -- w=winkel(nur für Vektoren)
		this.x = (l*java.lang.Math.cos(w));
		this.y = (l*java.lang.Math.sin(w));
	}
	
	public Punkt(Punkt p) {//Kopiert den gewünschten Punkt einfach
		this.x = p.x;
		this.y = p.y;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	
	public double getW() {
		double winkel = Math.atan2(y, x);
		winkel = winkel/Math.PI*180;
		return winkel;
	}
	public double getL() {
		double l = Math.sqrt(x*x+y*y);
		return l;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	
	public void setL(double l) {
		double w = getW();
		x = l * Math.cos(w);
		y = l * Math.sin(w);
	}
	public void setW(double w) {
		double l = getL();
		x = l * Math.cos(w);
		y = l * Math.sin(w);
	}
	
	public void vektorAddieren(Punkt v) {
		this.x = x + v.x;
		this.y = y + v.y;
	}
	
//	public double geradenSteigungBerechnen(Punkt p1) {
//OLD	//P1 soll der Punkt weiter Rechts sein (x von P1 > x von this)
//L		double m;
//D		int x1 = (int)(this.x);
//		int y1 = (int)(this.y);
//		int x2 = (int)(p1.getX());
//		int y2 = (int)(p1.getY());
//		//delta y durch delta x
//		
//		m = (y2-y1)/(x2-x1);
//		return m;
//	}
}