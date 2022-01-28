package lunarlander.data;

import java.io.Serializable;

public class GameStage implements Serializable {
	private static final long serialVersionUID = 3L;
	private int width;
	private int height;
	private int numberOfPoints;
	private double gravitation;
	private Point[] points;

	public GameStage(double gravitation, int hoehe) {
		this.gravitation = gravitation;
		this.height = hoehe;
		width = hoehe * 16 / 9;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int hoehe) {
		this.height = hoehe;
	}

	public int getNumberOfPoints() {
		return numberOfPoints;
	}

	public double getGravitation() {
		return gravitation;
	}

	public void setNumberOfPoints(int numberOfPoints) {
		this.numberOfPoints = numberOfPoints;
		this.points = new Point[numberOfPoints];
	}

	public void setGravitation(double gravitation) {
		this.gravitation = gravitation;
	}

	public Point[] getPoints() {
		return points;
	}

	public void setPoints(Point[] points) {
		this.points = points;
	}

	public void setPoint(int index, Point points) {
		this.points[index] = points;
	}

}