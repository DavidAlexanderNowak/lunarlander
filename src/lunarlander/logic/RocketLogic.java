package lunarlander.logic;

import lunarlander.data.Point;
import lunarlander.data.Rocket;
import lunarlander.graphics.RocketGraphics;
import lunarlander.logic.Control.GameState;

public class RocketLogic {

	private Control control;
	private Rocket rocket;
	private CollisionChecker collisionChecker;
	private RocketGraphics graphics;

	private RocketState rocketState;

	public enum RocketState {
		FLYING, LANDED, CRASHED
	}

	public RocketLogic(Control control) {
		this.rocketState = RocketState.FLYING;
		this.control = control;
		this.rocket = new Rocket(control.getGameStageLogic().getGravity());
		this.collisionChecker = new CollisionChecker(rocket, control);
		this.graphics = new RocketGraphics(control.getGui(), this);
	}

	private void resetPosition() {
		rocket.reset();
	}

	public void update(GameState gameState) {
		switch (gameState) {
		case START:
			rocket.setScale(getScale());
			resetPosition();
			break;
		case GAMELOOP:
			rocket.setScale(getScale());
			input();
			collisions();
			break;
		default:
		}
	}

	private void input() {
		if (control.getKeysPressed().contains(0)) {
			rocket.accelerate();
		}
		if (control.getKeysPressed().contains(1)) {
			rocket.steer(Rocket.Direction.RIGHT);
		}
		if (control.getKeysPressed().contains(2)) {
			rocket.steer(Rocket.Direction.LEFT);
		}
		if (control.getKeysPressed().contains(4)) {
			rocket.resetOrientation();
		}
	}

	private void collisions() {
		rocketState = collisionChecker.check();
		rocket.sideExit(0, control.getGameStageLogic().getWidth());
		rocket.positionUpdate();
	}

	public void graphics(GameState gameState) {
		graphics.update(gameState);
	}

	public RocketState getRocketState() {
		return rocketState;
	}

	public Point getPosition() {
		return rocket.getPosition();
	}

	public Point getCenter() {
		return rocket.getPositionCenter();
	}

	public double getOrientation() {
		return rocket.getOrientation();
	}

	public double getScale() {
		return control.getScale();
	}

	/**
	 * To be removed this method is for testing purpose
	 * 
	 * @return
	 */
	public Rocket getRocket() {
		return rocket;
	}

}
