package lunarlander.logic;

import lunarlander.constants.Constants;
import lunarlander.data.GameStage;
import lunarlander.data.Point;
import lunarlander.graphics.GameStageGraphics;
import lunarlander.logic.Control.GameState;

public class GameStageLogic {

	private Control control;
	private GameStage gameStage;
	private GameStageGraphics graphics;

	public GameStageLogic(Control control) {
		this.control = control;
		this.graphics = new GameStageGraphics(control.getGui(), this);
		createGameStage();
	}

	private void initialise() {
		this.gameStage = new GameStage(Constants.INITIAL_GRAVITY, Constants.INITIAL_GAMESTAGE_HEIGHT,
				Constants.INITIAL_GAMESTAGE_WIDTH);
	}

	/**
	 * Sets all the points of the game stage. Scaled to fit window size.
	 */
	private void createGameStage() {
		if (gameStage == null) {
			initialise();
		}

		resizeAdjust();
		gameStage.setNumberOfPoints(30);// TODO remove number of points and switch to using array length
		int width = gameStage.getWidth();
		int height = gameStage.getHeight();
		Point[] points = { //
				new Point(false, 0, height * 0.55), new Point(false, width * 0.07, height * 0.4),
				new Point(false, width * 0.08, height * 0.42), new Point(false, width * 0.15, height * 0.27),
				new Point(false, width * 0.19, height * 0.5), new Point(false, width * 0.2, height * 0.48),
				new Point(false, width * 0.23, height * 0.75), new Point(false, width * 0.27, height * 0.58),
				new Point(false, width * 0.3, height * 0.58), new Point(false, width * 0.33, height * 0.65),
				new Point(false, width * 0.4, height * 0.62), new Point(false, width * 0.42, height * 0.64),
				new Point(false, width * 0.46, height * 0.52), new Point(false, width * 0.5, height * 0.77),
				new Point(false, width * 0.51, height * 0.75), new Point(false, width * 0.53, height * 0.85),
				new Point(false, width * 0.55, height * 0.83), new Point(false, width * 0.59, height * 0.87),
				new Point(false, width * 0.7, height * 0.87), new Point(false, width * 0.72, height * 0.69),
				new Point(false, width * 0.73, height * 0.71), new Point(false, width * 0.76, height * 0.64),
				new Point(false, width * 0.81, height * 0.75), new Point(false, width * 0.84, height * 0.72),
				new Point(false, width * 0.87, height * 0.72), new Point(false, width * 0.89, height * 0.68),
				new Point(false, width * 0.92, height * 0.72), new Point(false, width * 0.94, height * 0.65),
				new Point(false, width * 0.95, height * 0.67), new Point(false, width, height * 0.55) };
		gameStage.setPoints(points);
	}

	private void resizeAdjust() {
		if (control.getGui() != null) {
			gameStage.setHeight(control.getGui().getHeight());
			gameStage.setWidth(control.getGui().getWidth());
		}
	}

	@SuppressWarnings("unused")
	private void generateRandomMap() {
		for (int i = 1; i < gameStage.getPoints().length - 1; i++) {
			int x = gameStage.getWidth() / (gameStage.getPoints().length - 1) * i;
			int y = (int) ((0.95 * gameStage.getHeight())//
					- Math.random() * (gameStage.getHeight() * 0.75));
			Point p = new Point(false, x, y);
			gameStage.setPoint(i, p);
		}

	}

	/**
	 * Handles everything game stage related that needs to be updated dependant on
	 * game state.
	 * 
	 * @param gameState Current game state
	 */
	public void update(GameState gameState) {
		switch (gameState) {
		case START:
			break;
		case GAMELOOP:
			createGameStage();
			break;
		case END:
			createGameStage();
			break;
		default:
		}
	}

	public void graphics(GameState gameState) {
		graphics.update(gameState);
	}

	public double getGravity() {
		return gameStage.getGravitation();
	}

	/**
	 * To be removed if possible
	 * 
	 * @return
	 */
	public int getWidth() {
		return gameStage.getWidth();
	}

	/**
	 * Definetely to be removed! Exists now for testing purposes only
	 * 
	 * @return
	 */
	public int getHeight() {
		return gameStage.getHeight();
	}

	public Point[] getPoints() {
		return gameStage.getPoints();
	}

}
