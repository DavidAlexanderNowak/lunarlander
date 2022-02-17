package lunarlander.graphics;

import java.awt.Color;

import lunarlander.logic.Control.GameState;
import lunarlander.logic.GameStageLogic;

public class GameStageGraphics {

	private GUI gui;
	private GameStageLogic logic;

	public GameStageGraphics(GUI gui, GameStageLogic logic) {
		this.gui = gui;
		this.logic = logic;
	}

	public void update(GameState gameState) {
		switch (gameState) {
		case GAMELOOP:
			draw();
			break;
		case END:
			draw();
			break;
		default:
		}
	}

	private void draw() {
		if (gui == null) {
			return;
		}
		gui.getGuiGraphics().setColor(Color.black);
		for (int i = 0; i < logic.getPoints().length - 1; i++) {
			gui.getGuiGraphics().drawLine((int) (logic.getPoints()[i].getX()),
					(int) (logic.getPoints()[i].getY()), (int) (logic.getPoints()[i + 1].getX()),
					(int) (logic.getPoints()[i + 1].getY()));
		}
	}

	public void setLogic(GameStageLogic logic) {
		this.logic = logic;
	}

	public void setGui(GUI gui) {
		this.gui = gui;
	}

}
