
public class Punkt {
	private double x;
	private double y;

	/**
	 * @param polar  </br>
	 *               <ul>
	 *               <li><b>false</b> cartesian coordinates.</br>
	 *               <li><b>true</b> polar coordinates.
	 *               </ul>
	 * @param length x for cartesian mode
	 * @param angle  y for cartesian mode
	 */
	public Punkt(boolean polar, double length, double angle) {
		if (polar) {
			this.x = (length * java.lang.Math.cos(angle));
			this.y = (length * java.lang.Math.sin(angle));
		}else {
			this.x = length;
			this.y = angle;
		}
	}

	public Punkt(Punkt point) {
		this.x = point.x;
		this.y = point.y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getW() {
		double winkel = Math.atan2(y, x);
		winkel = winkel / Math.PI * 180;
		return winkel;
	}

	public double getL() {
		double l = Math.sqrt(x * x + y * y);
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
}