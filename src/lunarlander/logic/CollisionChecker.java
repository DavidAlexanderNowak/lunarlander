package lunarlander.logic;

import lunarlander.data.Point;
import lunarlander.data.Rocket;
import lunarlander.logic.RocketLogic.RocketState;

public class CollisionChecker {

	private Control control;
	private Rocket rocket;
	private Point[] gameStagePoints;

	public CollisionChecker(Rocket rocket, Control control) {
		this.rocket = rocket;
		this.control = control;
		this.gameStagePoints = control.getGameStageLogic().getPoints();
	}

	public RocketState check() {
		this.gameStagePoints = control.getGameStageLogic().getPoints();
		RocketState rocketState;
		boolean touchdown = false;
		rocket.setLanded(false);
		for (int i = 0; i < gameStagePoints.length - 1; i++) {
			if (undersideCollision(gameStagePoints[i], gameStagePoints[i + 1])) {
				touchdown = true;
			}
			if (hitPointsCheck(gameStagePoints[i], gameStagePoints[i + 1])) {
				touchdown = true;
			}
			if (touchdown) {
				Point point3 = null;
				if (i < gameStagePoints.length - 2) {
					point3 = gameStagePoints[i + 2];
				}
				rocket.setLanded(successfulLandingCheck(gameStagePoints[i], gameStagePoints[i + 1], point3));
				rocket.setAlive(rocket.isLanded());
				break;
			}
		}
		if (!rocket.isAlive()) {
			rocketState = RocketState.CRASHED;
		} else if (rocket.isLanded()) {
			rocketState = RocketState.LANDED;
		} else {
			rocketState = RocketState.FLYING;
		}
		return rocketState;
	}

	private boolean undersideCollision(Point point1, Point point2) {
		if (linesOverlapX(point1, point2, getHitPoints()[9], getHitPoints()[10])) {
			return linesIntersect(point1, point2, getHitPoints()[9], getHitPoints()[10]);
		}
		return false;
	}

	/**
	 * First line is part of the gamestage, second line is a line in the collision
	 * model of the rocket
	 * 
	 * 
	 * @param point1    start of first line
	 * @param point2    end of first line
	 * @param lineLeft  start of second line
	 * @param lineRight end of second line
	 * @return if any part of one line is in the same x-range as any part of the
	 *         other line
	 */
	private boolean linesOverlapX(Point point1, Point point2, Point lineLeft, Point lineRight) {
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

	private boolean successfulLandingCheck(Point point1, Point point2, Point point3) {
		if (rocket.getOrientation() != 270) {
			return false;
		}
		if (rocket.getSpeed().getLength() > 1.5) {
			return false;
		}
		if (!lineBetweenPoints(point1, point2, getHitPoints()[9], getHitPoints()[10])) {
			boolean intersect = false;
			boolean flatLeft = point1.getY() == point2.getY();
			boolean flatRight = point3 == null || point2.getY() == point3.getY();
			if (!flatLeft) {
				intersect = point1.getY() < getHitPoints()[9].getY();
			}
			if (!flatRight) {
				intersect = intersect || getHitPoints()[9].getY() > point3.getY();
			}
			return (flatLeft || flatRight) && !intersect && (point3 != null || flatLeft);
		}
		return point1.getY() == point2.getY();
	}

	private boolean lineBetweenPoints(Point point1, Point point2, Point lineLeft, Point lineRight) {
		return point1.getX() <= lineLeft.getX() && lineRight.getX() <= point2.getX();
	}

	private Point[] getHitPoints() {
		Point[] hitPoints = new Point[11];
		// Punkt S an der Spitze:
		hitPoints[0] = calculateHitPoint(new Point(rocket.getPositionCenter()), 50, Math.toRadians(rocket.getOrientation()));

		// Punkte oL, oR
		Point o = calculateHitPoint(new Point(rocket.getPositionCenter()), -27,
				Math.toRadians(rocket.getOrientation() - 180));
		hitPoints[1] = calculateHitPoint(new Point(o), 20, Math.toRadians(rocket.getOrientation() - 90));
		hitPoints[2] = calculateHitPoint(new Point(o), 20, Math.toRadians(rocket.getOrientation() + 90));

		// Punkte aL, aR
		Point a = calculateHitPoint(new Point(rocket.getPositionCenter()), -10,
				Math.toRadians(rocket.getOrientation() - 180));
		hitPoints[3] = calculateHitPoint(new Point(a), 20, Math.toRadians(rocket.getOrientation() - 90));
		hitPoints[4] = calculateHitPoint(new Point(a), 20, Math.toRadians(rocket.getOrientation() + 90));

		// Punkte bL, bR
		Point b = calculateHitPoint(new Point(rocket.getPositionCenter()), 8, Math.toRadians(rocket.getOrientation() - 180));
		hitPoints[5] = calculateHitPoint(new Point(b), 25, Math.toRadians(rocket.getOrientation() - 90));
		hitPoints[6] = calculateHitPoint(new Point(b), 25, Math.toRadians(rocket.getOrientation() + 90));

		// Punkte fl, fR
		Point f = calculateHitPoint(new Point(rocket.getPositionCenter()), 33, Math.toRadians(rocket.getOrientation() - 180));
		hitPoints[7] = calculateHitPoint(new Point(f), 30, Math.toRadians(rocket.getOrientation() - 90));
		hitPoints[8] = calculateHitPoint(new Point(f), 30, Math.toRadians(rocket.getOrientation() + 90));

		// Punkte uL, uR
		Point u = calculateHitPoint(new Point(rocket.getPositionCenter()), 50, Math.toRadians(rocket.getOrientation() - 180));
		hitPoints[9] = calculateHitPoint(new Point(u), 20, Math.toRadians(rocket.getOrientation() - 90));
		hitPoints[10] = calculateHitPoint(new Point(u), 20, Math.toRadians(rocket.getOrientation() + 90));

		return hitPoints;
	}

	private Point calculateHitPoint(Point startingPoint, int distance, double angle) {
		Point hitPoint = new Point(false, startingPoint.getX(), startingPoint.getY());
		Point vector = new Point(true, distance, angle);
		hitPoint.addVector(vector);
		return hitPoint;
	}

}
