package lunarlander.logic;

import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.HashSet;

import lunarlander.data.GameStage;
import lunarlander.data.Point;
import lunarlander.data.Rocket;
import lunarlander.graphics.GUI;

public class Control implements Runnable, Serializable {
	private static final long serialVersionUID = 2L;

	private static final boolean TRUE = true;
	private GUI gui;
	private Rocket rocket;
	private GameStage gameStage;
	private HashSet<Integer> keysPressed = new HashSet<>();
	private GameState gameState;

	private enum GameState {
		START, GAMELOOP, END
	}

	public static void main(String[] args) {
		Thread thread = new Thread(new Control());
		thread.start();
	}

	public Control() {
		initialiseGameStage();
		rocket = new Rocket(gameStage.getGravitation());
		gui = new GUI(this);
		gameState = GameState.START;
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
		case KeyEvent.VK_ENTER:
			keysPressed.add(3);
			break;
		default:
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
		case KeyEvent.VK_ENTER:
			keysPressed.remove(3);
			break;
		default:
		}
	}

	private void update() {
		updateGameState();
		switch (gameState) {
		case START:
			resetPosition();
			break;
		case GAMELOOP:
			gameLoopInput();
			collisions();
			break;
		case END:
			break;
		default:
		}
		gui.repaint();
	}

	private void resetPosition() {
		rocket = new Rocket(gameStage.getGravitation());
	}

	private void updateGameState() {
		switch (gameState) {
		case START:
			if (keysPressed.contains(0)) {
				gameState = GameState.GAMELOOP;
			}
			break;
		case GAMELOOP:
			if (!rocket.isAlive() || rocket.isLanded()) {
				gameState = GameState.END;
			}
			break;
		case END:
			if (keysPressed.contains(3)) {
				gameState = GameState.START;
			}
			break;
		default:
		}
	}

	private void gameLoopInput() {
		if (keysPressed.contains(0)) {
			rocket.accelerate();
		}
		if (keysPressed.contains(1)) {
			rocket.steer(Rocket.Direction.RIGHT);
		}
		if (keysPressed.contains(2)) {
			rocket.steer(Rocket.Direction.LEFT);
		}
	}

	private void collisions() {
		touchdownCheck();
		rocket.sideExit(0, gameStage.getWidth());
		rocket.positionUpdate();
	}

	private void touchdownCheck() {
		boolean touchdown = false;
		rocket.setLanded(false);

		for (int i = 0; i < gameStage.getNumberOfPoints() - 1; i++) {
//			boolean landungFlach = (gameStage.getPoints()[i].getY() == gameStage.getPoints()[i + 1].getY());
			if (undersideCollision(gameStage.getPoints()[i], gameStage.getPoints()[i + 1])) {
				touchdown = true;
			}
			if (hitPointsCheck(gameStage.getPoints()[i], gameStage.getPoints()[i + 1])) {
				touchdown = true;
			}
			if (touchdown) {
//				if (landungFlach || rocket.isLanded()) {
//					rocket.setLanded(true);
//				} else {
				rocket.setAlive(false);
//				}
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

	public void graphics() {
		gui.clear();
		switch (gameState) {
		case START:
			gui.drawStartScreen();
			break;
		case GAMELOOP:
			gui.drawGameStage(gameStage.getPoints());
			gui.drawRocket(rocket.getPosition()//
					, rocket.getPositionCenter(), rocket.getAngle());
			gui.drawHUD();
			break;
		case END:
			gui.drawGameStage(gameStage.getPoints());
			gui.drawRocket(rocket.getPosition()//
					, rocket.getPositionCenter(), rocket.getAngle());
			break;
		default:
		}
	}

	private void initialiseGameStage() {
		gameStage = new GameStage(0.05, 108 * 8);
		gameStage.setNumberOfPoints(30);
		int breite = gameStage.getWidth();
		int hoehe = gameStage.getHeight();
		gameStage.setPoint(0, new Point(false, 0, hoehe * 0.55));
		gameStage.setPoint(1, new Point(false, breite * 0.07, hoehe * 0.4));
		gameStage.setPoint(2, new Point(false, breite * 0.08, hoehe * 0.42));
		gameStage.setPoint(3, new Point(false, breite * 0.15, hoehe * 0.27));
		gameStage.setPoint(4, new Point(false, breite * 0.19, hoehe * 0.5));
		gameStage.setPoint(5, new Point(false, breite * 0.2, hoehe * 0.48));
		gameStage.setPoint(6, new Point(false, breite * 0.23, hoehe * 0.75));
		gameStage.setPoint(7, new Point(false, breite * 0.27, hoehe * 0.58));
		gameStage.setPoint(8, new Point(false, breite * 0.3, hoehe * 0.58));
		gameStage.setPoint(9, new Point(false, breite * 0.33, hoehe * 0.65));
		gameStage.setPoint(10, new Point(false, breite * 0.4, hoehe * 0.62));
		gameStage.setPoint(11, new Point(false, breite * 0.42, hoehe * 0.64));
		gameStage.setPoint(12, new Point(false, breite * 0.46, hoehe * 0.52));
		gameStage.setPoint(13, new Point(false, breite * 0.5, hoehe * 0.77));
		gameStage.setPoint(14, new Point(false, breite * 0.51, hoehe * 0.75));
		gameStage.setPoint(15, new Point(false, breite * 0.53, hoehe * 0.85));
		gameStage.setPoint(16, new Point(false, breite * 0.55, hoehe * 0.83));
		gameStage.setPoint(17, new Point(false, breite * 0.59, hoehe * 0.87));
		gameStage.setPoint(18, new Point(false, breite * 0.7, hoehe * 0.87));
		gameStage.setPoint(19, new Point(false, breite * 0.72, hoehe * 0.69));
		gameStage.setPoint(20, new Point(false, breite * 0.73, hoehe * 0.71));
		gameStage.setPoint(21, new Point(false, breite * 0.76, hoehe * 0.64));
		gameStage.setPoint(22, new Point(false, breite * 0.81, hoehe * 0.75));
		gameStage.setPoint(23, new Point(false, breite * 0.84, hoehe * 0.72));
		gameStage.setPoint(24, new Point(false, breite * 0.87, hoehe * 0.72));
		gameStage.setPoint(25, new Point(false, breite * 0.89, hoehe * 0.68));
		gameStage.setPoint(26, new Point(false, breite * 0.92, hoehe * 0.72));
		gameStage.setPoint(27, new Point(false, breite * 0.94, hoehe * 0.65));
		gameStage.setPoint(28, new Point(false, breite * 0.95, hoehe * 0.67));
		gameStage.setPoint(29, new Point(false, breite, hoehe * 0.55));
	}

	@SuppressWarnings("unused")
	private void generateRandomMap() {
		for (int i = 1; i < gameStage.getNumberOfPoints() - 1; i++) {
			int x = gameStage.getWidth() / (gameStage.getNumberOfPoints() - 1) * i;
			int y = (int) ((0.95 * gameStage.getHeight())//
					- Math.random() * (gameStage.getHeight() * 0.75));
			Point p = new Point(false, x, y);
			gameStage.setPoint(i, p);
		}

	}

	@Override
	public void run() {
		double timePerTick = (double) 1000000000 / 60;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		while (TRUE) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			lastTime = now;
			if (delta >= 1) {
				update();
				delta--;
			}
		}
	}

	/**
	 * Is point on line <b>and</b> in an interval?
	 * 
	 * @param p  Point to check
	 * @param c  intercept of line-function
	 * @param x1 start of interval
	 * @param x2 end of interval
	 * @param m  slope of line-function
	 * @return whether Point p is part of m*x+c and in the interval [x1,x2]
	 */
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

	private Point calculateHitPoint(Point startingPoint, int distance, double angle) {
		Point hitPoint = new Point(false, startingPoint.getX(), startingPoint.getY());
		Point vector = new Point(true, distance, angle);
		hitPoint.addVector(vector);
		return hitPoint;
	}

	private Point[] getHitPoints() {
		Point[] hitPoints = new Point[11];
		// Punkt S an der Spitze:
		hitPoints[0] = calculateHitPoint(new Point(rocket.getPositionCenter()), 50, Math.toRadians(rocket.getAngle()));

		// Punkte oL, oR
		Point o = calculateHitPoint(new Point(rocket.getPositionCenter()), -27,
				Math.toRadians(rocket.getAngle() - 180));
		hitPoints[1] = calculateHitPoint(new Point(o), 20, Math.toRadians(rocket.getAngle() - 90));
		hitPoints[2] = calculateHitPoint(new Point(o), 20, Math.toRadians(rocket.getAngle() + 90));

		// Punkte aL, aR
		Point a = calculateHitPoint(new Point(rocket.getPositionCenter()), -10,
				Math.toRadians(rocket.getAngle() - 180));
		hitPoints[3] = calculateHitPoint(new Point(a), 20, Math.toRadians(rocket.getAngle() - 90));
		hitPoints[4] = calculateHitPoint(new Point(a), 20, Math.toRadians(rocket.getAngle() + 90));

		// Punkte bL, bR
		Point b = calculateHitPoint(new Point(rocket.getPositionCenter()), 8, Math.toRadians(rocket.getAngle() - 180));
		hitPoints[5] = calculateHitPoint(new Point(b), 25, Math.toRadians(rocket.getAngle() - 90));
		hitPoints[6] = calculateHitPoint(new Point(b), 25, Math.toRadians(rocket.getAngle() + 90));

		// Punkte fl, fR
		Point f = calculateHitPoint(new Point(rocket.getPositionCenter()), 33, Math.toRadians(rocket.getAngle() - 180));
		hitPoints[7] = calculateHitPoint(new Point(f), 30, Math.toRadians(rocket.getAngle() - 90));
		hitPoints[8] = calculateHitPoint(new Point(f), 30, Math.toRadians(rocket.getAngle() + 90));

		// Punkte uL, uR
		Point u = calculateHitPoint(new Point(rocket.getPositionCenter()), 50, Math.toRadians(rocket.getAngle() - 180));
		hitPoints[9] = calculateHitPoint(new Point(u), 20, Math.toRadians(rocket.getAngle() - 90));
		hitPoints[10] = calculateHitPoint(new Point(u), 20, Math.toRadians(rocket.getAngle() + 90));

		return hitPoints;
	}

	public GameStage getGameStage() {
		return gameStage;
	}

	public Rocket getRocket() {
		return rocket;
	}
}