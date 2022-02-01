package lunarlander.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import lunarlander.data.Point;
import lunarlander.logic.Control;
import lunarlander.utilities.Utilities;

public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;

	private Control control;

	private JPanel contentPane;
	private Image doubleBufferImage;
	private Graphics doubleBufferGraphics;

	private BufferedImage rocketImage;
	private int HUD_Y = 30;

	public GUI(Control dieSteuerung) {
		initialise(dieSteuerung);
	}

	private void initialise(Control control) {
		this.control = control;
		rocketImage = Utilities.loadImage("/player.png");
		initialiseWindowSettings();
		initialiseContentPane();
		initialiseListeners();
		setVisible(true);
	}

	private void initialiseWindowSettings() {
		setTitle("Lunar Lander");
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, this.control.getGameStage().getWidth()//
				, this.control.getGameStage().getHeight());
	}

	private void initialiseContentPane() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setLayout(null);
	}

	private void initialiseListeners() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				control.keyPressed(e.getKeyCode());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				control.keyReleased(e.getKeyCode());
			}
		});
		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
				control.setClosed(false);
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				control.youDie();
				control.setClosed(true);
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
	}

	@Override
	public void paint(Graphics graphics) {
		if (doubleBufferImage == null) {
			doubleBufferImage = createImage(this.getSize().width, this.getSize().height);
			doubleBufferGraphics = doubleBufferImage.getGraphics();
		}
		doubleBufferGraphics.clearRect(0, 0, this.getSize().width, this.getSize().height);
		control.graphics();
		graphics.drawImage(doubleBufferImage, 0, 0, this);
	}

	public void clear() {
		doubleBufferGraphics.setColor(Color.white);
		doubleBufferGraphics.fillRect(0, 0, getSize().width, getSize().height);
	}

	public void drawHUD() {
		JPanel hud = createHudJPanel();
		createHudLabels(hud);
		hud.paintComponents(doubleBufferGraphics);
	}

	private JPanel createHudJPanel() {
		JPanel hudJPanel = new JPanel();
		calculateHudDimensions(hudJPanel);
		contentPane.add(hudJPanel);
		hudJPanel.setLayout(null);
		return hudJPanel;
	}

	private void calculateHudDimensions(JPanel hud) {
		int hudX = control.getGameStage().getWidth() * 39 / 100;
		int hudWidth = control.getGameStage().getWidth() * 26 / 100;
		int hudHeight = control.getGameStage().getHeight() / 10;
		hud.setBounds(hudX, HUD_Y, hudWidth, hudHeight);
	}

	private void createHudLabels(JPanel hud) {
		int hudX = hud.getX();
		int hudY = hud.getY();
		int hudWidth = hud.getWidth();
		int hudHeight = hud.getHeight();

		JLabel speedLabel = new JLabel("Speed: "//
				+ (int) control.getRocket().getSpeed().getLength());
		speedLabel.setBounds(hudX, hudY, hudWidth / 2, hudHeight / 2);

		JLabel axisSpeedLabel = new JLabel("X: "//
				+ (int) control.getRocket().getSpeed().getX()//
				+ "  Y: "//
				+ (int) control.getRocket().getSpeed().getY());
		axisSpeedLabel.setBounds(hudX, hudY + hudHeight / 2//
				, hudWidth / 2, hudHeight / 2);

		JLabel legacyElementLabel = new JLabel("Schub: " + "legacy " + "%");
		legacyElementLabel.setBounds(hudX + hudWidth / 2, hudY//
				, hudWidth / 2, hudHeight / 2);

		JLabel angleLabel = new JLabel("Angle: "//
				+ control.getRocket().getTrueOrientation() + "°");
		angleLabel.setBounds(hudX + hudWidth / 2, hudY//
				+ hudHeight / 2, hudWidth / 2, hudHeight / 2);

		Font labelFont = Utilities.createFont(legacyElementLabel);
		speedLabel.setFont(labelFont);
		axisSpeedLabel.setFont(labelFont);
		angleLabel.setFont(labelFont);
//		legacyElementLabel.setFont(labelFont);

		hud.add(speedLabel);
		hud.add(axisSpeedLabel);
//		hud.add(legacyElementLabel);
		hud.add(angleLabel);
	}

	public void drawGameStage(Point[] derPunkt) {
		doubleBufferGraphics.setColor(Color.black);
		for (int i = 0; i < derPunkt.length - 1; i++) {
			doubleBufferGraphics.drawLine((int) (derPunkt[i].getX()), (int) (derPunkt[i].getY()),
					(int) (derPunkt[i + 1].getX()), (int) (derPunkt[i + 1].getY()));
		}
	}

	public void drawRocket(Point position, Point mitte, double neigung) {
		Graphics2D graphics2D = (Graphics2D) doubleBufferGraphics;
		int drawLocationX = (int) position.getX();
		int drawLocationY = (int) position.getY();
		AffineTransform backup = graphics2D.getTransform();
		AffineTransform affineTransform = AffineTransform//
				.getRotateInstance(Math.toRadians(neigung - 270)//
						, mitte.getX(), mitte.getY());
		graphics2D.setTransform(affineTransform);
		graphics2D.drawImage(rocketImage, drawLocationX, drawLocationY, null);
		graphics2D.setTransform(backup);
	}

	/**
	 * Draws a screen that contains only the text. Roughly in the middle of the
	 * screen.
	 * 
	 * @param text
	 */
	public void drawTextScreen(String... text) {
		JPanel endScreen = createDefaultJPanel();
		createScreenLabels(endScreen, text);
		endScreen.paintComponents(doubleBufferGraphics);
	}

	private JPanel createDefaultJPanel() {
		JPanel panel = new JPanel();
		calculateDefaultScreenDimensions(panel);
		contentPane.add(panel);
		panel.setLayout(null);
		return panel;
	}

	/**
	 * Puts the screen in the center, extending 10% left and right. Heightwise it is
	 * 20% From the top extending 10%.
	 * 
	 * @param screen
	 */
	private void calculateDefaultScreenDimensions(JPanel screen) {
		int x = control.getGameStage().getWidth() * 40 / 100;
		int y = control.getGameStage().getHeight() * 20 / 100;
		int width = control.getGameStage().getWidth() * 20 / 100;
		int height = control.getGameStage().getHeight() / 10;
		screen.setBounds(x, y, width, height);
	}

	private void createScreenLabels(JPanel screen, String[] text) {
		int x = screen.getX();
		int y = screen.getY();
		int width = screen.getWidth();
		int height = screen.getHeight();

		JLabel label1 = new JLabel(text[0], SwingConstants.CENTER);
		label1.setBounds(x, y, width, height / text.length);

		Font labelFont = Utilities.createFont(label1);
		label1.setFont(labelFont);

		screen.add(label1);

		if (text.length > 1) {
			JLabel label2 = new JLabel(text[1], SwingConstants.CENTER);
			label2.setBounds(x, y + height / text.length, width, height / text.length);

			labelFont = Utilities.createFont(label2);
			label2.setFont(labelFont);
			screen.add(label2);
		}
	}
}