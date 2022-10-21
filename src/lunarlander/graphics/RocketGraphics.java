package lunarlander.graphics;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import lunarlander.constants.Constants;
import lunarlander.logic.Control.GameState;
import lunarlander.utilities.Utilities;
import lunarlander.logic.RocketLogic;

public class RocketGraphics {

	private GUI gui;
	private RocketLogic logic;
	private BufferedImage rocketImage;

	public RocketGraphics(GUI gui, RocketLogic logic) {
		this.gui = gui;
		this.logic = logic;
		this.rocketImage = Utilities.loadImage(Constants.ROCKET_IMAGE_PATH);
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
		Graphics2D graphics = (Graphics2D) gui.getGuiGraphics();
		int x = (int) logic.getPosition().getX();
		int y = (int) logic.getPosition().getY();
		AffineTransform backup = graphics.getTransform();
		AffineTransform affineTransform = AffineTransform.getRotateInstance(//
				Math.toRadians(logic.getOrientation() - 270)//
				, logic.getCenter().getX(), logic.getCenter().getY());
		graphics.setTransform(affineTransform);
		graphics.drawImage(Utilities.scaleImage(rocketImage, logic.getScale()), x, y, null);
		graphics.setTransform(backup);
	}

}
