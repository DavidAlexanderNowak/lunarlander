package lunarlander.logic;

import lunarlander.data.Point;

public class CollisionChecker {

	private Control control;

	public CollisionChecker(Control control) {
		this.control = control;
	}

	public void check() {
		boolean touchdown = false;
		control.getRocket().setLanded(false);
		for (int i = 0; i < control.getGameStage().getNumberOfPoints() - 1; i++) {
			if (undersideCollision(control.getGameStage().getPoints()[i], control.getGameStage().getPoints()[i + 1])) {
				touchdown = true;
			}
			if (hitPointsCheck(control.getGameStage().getPoints()[i], control.getGameStage().getPoints()[i + 1])) {
				touchdown = true;
			}
			if (touchdown) {
				control.getRocket().setAlive(false);
				break;
			}
		}
	}

	private boolean undersideCollision(Point point1, Point point2) {
		if (lineBetweenPoints(point1, point2, getHitPoints()[9], getHitPoints()[10])) {
			return linesIntersect(point1, point2, getHitPoints()[9], getHitPoints()[10]);
		}
		return false;
	}

	private boolean lineBetweenPoints(Point point1, Point point2, Point lineLeft, Point lineRight) {
		return point1.getX() <= lineLeft.getX() && lineRight.getX() <= point2.getX()
				|| point1.getX() <= lineRight.getX() && lineLeft.getX() <= point2.getX();
	}

	private boolean linesIntersect(Point point1, Point point2, Point lineLeft, Point lineRight) {
		double xS = calculateIntersection(point1, point2, lineLeft, lineRight);
		return pointInRangeOfTwoLines(xS, point1, point2, lineLeft, lineRight);
	}

	private double calculateIntersection(Point point1, Point point2, Point lineLeft, Point lineRight) {
		double m = (point2.getY() - point1.getY()) / (point2.getX() - point1.getX());
		double c = point1.getY() - m * point1.getX();
		double mLine = (lineRight.getY() - lineLeft.getY()) / (lineRight.getX() - lineLeft.getX());
		double cLine = lineRight.getY() - mLine * lineRight.getX();
		return (cLine - c) / (m - mLine);
	}

	private boolean pointInRangeOfTwoLines(double xS, Point point1, Point point2, Point lineLeft, Point lineRight) {
		return ((lineLeft.getX() <= xS && xS <= lineRight.getX())//
				|| lineRight.getX() <= xS && xS <= lineLeft.getX())//
				&& (point1.getX() <= xS && xS <= point2.getX());
	}

	private boolean hitPointsCheck(Point point1, Point point2) {
		double m = (point2.getY() - point1.getY()) / (point2.getX() - point1.getX());
		double c = point1.getY() - m * point1.getX();
		for (int j = 0; j < 11; j++) {
			if (isPointOnLine(getHitPoints()[j], c, point1.getX(), point2.getX(), m)) {
				return true;
			}
		}
		return false;
	}

	private boolean isPointOnLine(Point p, double c, double x1, double x2, double m) {
		boolean isOnLine = false;
		if ((x1 <= p.getX()) && (p.getX() <= x2)) {
			double cP = p.getY() - m * p.getX();
			if (c <= cP) {
				isOnLine = true;
			}
		}
		return isOnLine;
	}

	private Point[] getHitPoints() {
		Point[] hitPoints = new Point[11];
		// Punkt S an der Spitze:
		hitPoints[0] = calculateHitPoint(new Point(control.getRocket().getPositionCenter()), 50,
				Math.toRadians(control.getRocket().getAngle()));

		// Punkte oL, oR
		Point o = calculateHitPoint(new Point(control.getRocket().getPositionCenter()), -27,
				Math.toRadians(control.getRocket().getAngle() - 180));
		hitPoints[1] = calculateHitPoint(new Point(o), 20, Math.toRadians(control.getRocket().getAngle() - 90));
		hitPoints[2] = calculateHitPoint(new Point(o), 20, Math.toRadians(control.getRocket().getAngle() + 90));

		// Punkte aL, aR
		Point a = calculateHitPoint(new Point(control.getRocket().getPositionCenter()), -10,
				Math.toRadians(control.getRocket().getAngle() - 180));
		hitPoints[3] = calculateHitPoint(new Point(a), 20, Math.toRadians(control.getRocket().getAngle() - 90));
		hitPoints[4] = calculateHitPoint(new Point(a), 20, Math.toRadians(control.getRocket().getAngle() + 90));

		// Punkte bL, bR
		Point b = calculateHitPoint(new Point(control.getRocket().getPositionCenter()), 8,
				Math.toRadians(control.getRocket().getAngle() - 180));
		hitPoints[5] = calculateHitPoint(new Point(b), 25, Math.toRadians(control.getRocket().getAngle() - 90));
		hitPoints[6] = calculateHitPoint(new Point(b), 25, Math.toRadians(control.getRocket().getAngle() + 90));

		// Punkte fl, fR
		Point f = calculateHitPoint(new Point(control.getRocket().getPositionCenter()), 33,
				Math.toRadians(control.getRocket().getAngle() - 180));
		hitPoints[7] = calculateHitPoint(new Point(f), 30, Math.toRadians(control.getRocket().getAngle() - 90));
		hitPoints[8] = calculateHitPoint(new Point(f), 30, Math.toRadians(control.getRocket().getAngle() + 90));

		// Punkte uL, uR
		Point u = calculateHitPoint(new Point(control.getRocket().getPositionCenter()), 50,
				Math.toRadians(control.getRocket().getAngle() - 180));
		hitPoints[9] = calculateHitPoint(new Point(u), 20, Math.toRadians(control.getRocket().getAngle() - 90));
		hitPoints[10] = calculateHitPoint(new Point(u), 20, Math.toRadians(control.getRocket().getAngle() + 90));

		return hitPoints;
	}

	private Point calculateHitPoint(Point startingPoint, int distance, double angle) {
		Point hitPoint = new Point(false, startingPoint.getX(), startingPoint.getY());
		Point vector = new Point(true, distance, angle);
		hitPoint.addVector(vector);
		return hitPoint;
	}

}
