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

	private GUI gui;
	private Rocket rocket;
	private GameStage gameStage;
	private CollisionChecker collisionChecker;
	private HashSet<Integer> keysPressed = new HashSet<>();
	private GameState gameState;
	private boolean closed;
	private double scale;
	private int originalHeight;

	private enum GameState {
		START, GAMELOOP, END
	}

	public Control() {
		closed = false;
		gameState = GameState.START;
		initialiseGameStage();
		rocket = new Rocket(gameStage.getGravitation());
		gui = new GUI(this);
		originalHeight = gui.getHeight();
		scale = gui.getHeight() / originalHeight;
		collisionChecker = new CollisionChecker(this);
	}

	public static void main(String[] args) {
		Thread thread = new Thread(new Control());
		thread.start();
	}

	@Override
	public void run() {
		double timePerTick = (double) 1000000000 / 60;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		while (!closed) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			lastTime = now;
			if (delta >= 1) {
				update();
				delta--;
			}
		}
	}

	private void update() {
		scale = gui.getHeight() / originalHeight;
		updateGameState();
		switch (gameState) {
		case START:
			resetPosition();
			break;
		case GAMELOOP:
			updateGameStage();
			gameLoopInput();
			collisions();
			break;
		case END:
			updateGameStage();
			break;
		default:
		}
		gui.repaint();
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

	private void resetPosition() {
		rocket = new Rocket(gameStage.getGravitation());
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
		if (keysPressed.contains(4)) {
			rocket.resetOrientation();
		}
	}

	private void collisions() {
		collisionChecker.check();
		rocket.sideExit(0, gameStage.getWidth());
		rocket.positionUpdate();
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
		case KeyEvent.VK_DOWN:
			keysPressed.add(4);
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
		case KeyEvent.VK_DOWN:
			keysPressed.remove(4);
			break;
		default:
		}
	}

	public void graphics() {
		gui.clear();
		switch (gameState) {
		case START:
			gui.drawTextScreen("Press 'SPACE' to start!");
			break;
		case GAMELOOP:
			gui.drawGameStage(gameStage.getPoints());
			gui.drawRocket(rocket.getPosition()//
					, rocket.getPositionCenter(), rocket.getAngle());
			gui.drawHUD();
			break;
		case END:
			String text;
			if (rocket.isAlive()) {
				text = "Successful landing.";
			} else {
				text = "You died.";
			}

			gui.drawGameStage(gameStage.getPoints());
			gui.drawRocket(rocket.getPosition()//
					, rocket.getPositionCenter(), rocket.getAngle());
			gui.drawTextScreen(text, "Press 'ENTER' to restart!");
			break;
		default:
		}
	}

	private void initialiseGameStage() {
		gameStage = new GameStage(0.05, 108 * 8, 108 * 8 * 16 / 9);
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

	private void updateGameStage() {
		gameStage.setHeight(gui.getHeight());
		gameStage.setWidth(gui.getWidth());
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

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public GameStage getGameStage() {
		return gameStage;
	}

	public Rocket getRocket() {
		return rocket;
	}
}