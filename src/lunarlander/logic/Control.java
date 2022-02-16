package lunarlander.logic;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import lunarlander.graphics.GUI;
import lunarlander.logic.RocketLogic.RocketState;

public class Control implements Runnable {

	private GUI gui;

	private RocketLogic rocketLogic;
	private GameStageLogic gameStageLogic;

	private HashSet<Integer> keysPressed = new HashSet<>();

	private GameState gameState;
	private boolean closed;

	private double scale;
	private int originalHeight;

	public enum GameState {
		START, GAMELOOP, END
	}

	public Control() {
		this.closed = false;
		this.gameState = GameState.START;
		this.gui = new GUI(this);
		this.gameStageLogic = new GameStageLogic(this);
		this.rocketLogic = new RocketLogic(this);
		this.originalHeight = gui.getHeight();
		this.scale = (double) gui.getHeight() / originalHeight;
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
		calculateScale();
		updateGameState();

		gameStageLogic.update(gameState);
		rocketLogic.update(gameState);

		gui.repaint();
	}

	private void calculateScale() {
		scale = (double) gui.getHeight() / originalHeight;
	}

	private void updateGameState() {
		switch (gameState) {
		case START:
			if (keysPressed.contains(0)) {
				gameState = GameState.GAMELOOP;
			}
			break;
		case GAMELOOP:
			if (rocketLogic.getRocketState() != RocketState.FLYING) {
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
		gameStageLogic.graphics(gameState);
		switch (gameState) {
		case START:
			gui.drawTextScreen("Press 'SPACE' to start!");
			break;
		case GAMELOOP:
//			gui.drawGameStage(gameStageLogic.getPoints());
			gui.drawRocket(rocketLogic.getRocket().getPosition()//
					, rocketLogic.getRocket().getPositionCenter(), rocketLogic.getRocket().getAngle());
			gui.drawHUD();
			break;
		case END:
			String text;
			if (rocketLogic.getRocket().isAlive()) {
				text = "Successful landing.";
			} else {
				text = "You died.";
			}

//			gui.drawGameStage(gameStageLogic.getPoints());
			gui.drawRocket(rocketLogic.getRocket().getPosition()//
					, rocketLogic.getRocket().getPositionCenter(), rocketLogic.getRocket().getAngle());
			gui.drawTextScreen(text, "Press 'ENTER' to restart!");
			break;
		default:
		}
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public GUI getGui() {
		return gui;
	}

	public GameStageLogic getGameStageLogic() {
		return gameStageLogic;
	}

	/**
	 * To be removed. Exists only for testing purposes.
	 * 
	 * @return
	 */
	public RocketLogic getRocketLogic() {
		return rocketLogic;
	}

	public Set<Integer> getKeysPressed() {
		return keysPressed;
	}

}