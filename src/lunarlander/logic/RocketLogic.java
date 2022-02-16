package lunarlander.logic;

import lunarlander.data.Rocket;
import lunarlander.logic.Control.GameState;

public class RocketLogic {

	private Control control;
	private Rocket rocket;
	private CollisionChecker collisionChecker;

	private RocketState rocketState;

	public enum RocketState {
		FLYING, LANDED, CRASHED
	}

	public RocketLogic(Control control) {
		this.rocketState = RocketState.FLYING;
		this.control = control;
		this.rocket = new Rocket(control.getGameStageLogic().getGravity());
		this.collisionChecker = new CollisionChecker(rocket, control);
	}

	private void resetPosition() {
		rocket.reset();
	}

	public void update(GameState gameState) {
		switch (gameState) {
		case START:
			resetPosition();
			break;
		case GAMELOOP:
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

	public RocketState getRocketState() {
		return rocketState;
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
