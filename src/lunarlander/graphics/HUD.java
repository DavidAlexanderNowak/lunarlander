package lunarlander.graphics;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import lunarlander.utilities.Utilities;

public class HUD {

	private GUI gui;

	private int x;
	private int y;
	private int width;
	private int height;

	private JPanel panel;

	public HUD(GUI gui) {
		this.gui = gui;
	}

	private void createPanel() {
		panel = new JPanel();
		panel.setBounds(x, y, width, height);
		panel.setLayout(null);
	}

	private void updateLocation() {
		y = gui.getControl().getGameStage().getHeight() / 100 + 30;
		x = gui.getControl().getGameStage().getWidth() * 39 / 100;
		width = gui.getControl().getGameStage().getWidth() * 26 / 100;
		height = gui.getControl().getGameStage().getHeight() / 10;
	}

	public void update() {
		updateLocation();
		createPanel();
		createLabels();
		gui.getContentPane().add(panel);
		panel.setLayout(null);
		panel.paintComponents(gui.getDoubleBufferGraphics());
	}

	private void createLabels() {
		Font labelFont = Utilities.createFittingFont(createLabel(null, "FontCalibration", this.x + this.width / 2//
				, this.y, this.width / 2, this.height / 2));

		String velocityString = "Velocity: " + (int) gui.getControl().getRocket().getVelocity();
		String axisVelocityString = "X: " + (int) gui.getControl().getRocket().getSpeed().getX()//
				+ "  Y: " + (int) gui.getControl().getRocket().getSpeed().getY();
		String orientationString = "Angle: " + gui.getControl().getRocket().getTrueOrientation() + "°";

		createLabel(labelFont, velocityString, x, y, width / 2, height / 2);
		createLabel(labelFont, axisVelocityString, x, y + height / 2, width / 2, height / 2);
		createLabel(labelFont, orientationString, x + width / 2, y + height / 2, width / 2, height / 2);
	}

	/**
	 * WARNING: Returns a JLabel only if font is unspecified (null). Returns null
	 * otherwise.
	 * 
	 * @param font
	 * @param string
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	private JLabel createLabel(Font font, String string, int x, int y, int width, int height) {
		JLabel label = new JLabel(string);
		label.setBounds(x, y, width, height);
		if (font == null) {
			return label;
		} else {
			label.setFont(font);
		}
		panel.add(label);
		return null;
	}

	public JPanel getPanel() {
		return panel;
	}

}
