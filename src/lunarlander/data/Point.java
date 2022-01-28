package lunarlander.data;

import java.io.Serializable;

public class Point implements Serializable {
	private static final long serialVersionUID = 4L;
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
	public Point(boolean polar, double length, double angle) {
		if (polar) {
			this.x = (length * java.lang.Math.cos(angle));
			this.y = (length * java.lang.Math.sin(angle));
		} else {
			this.x = length;
			this.y = angle;
		}
	}

	public Point(Point point) {
		this.x = point.x;
		this.y = point.y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getAngle() {
		double angle = Math.atan2(y, x);
		angle = angle / Math.PI * 180;
		return angle;
	}

	public double getLength() {
		return Math.sqrt(x * x + y * y);
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setLength(double length) {
		double angle = getAngle();
		x = length * Math.cos(angle);
		y = length * Math.sin(angle);
	}

	public void setAngle(double angle) {
		double length = getLength();
		x = length * Math.cos(angle);
		y = length * Math.sin(angle);
	}

	public void addVector(Point vector) {
		this.x = x + vector.x;
		this.y = y + vector.y;
	}
}