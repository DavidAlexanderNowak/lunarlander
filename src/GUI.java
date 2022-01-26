import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GUI extends JFrame {
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
		initialiseKeyListeners();
		setVisible(true);
	}

	private void initialiseWindowSettings() {
		setTitle("Lunar Lander");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, this.control.getDasSpielfeld().getBreite()//
				, this.control.getDasSpielfeld().getHoehe());
	}

	private void initialiseContentPane() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setLayout(null);
	}

	private void initialiseKeyListeners() {
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

	public void drawStartScreen() {

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
		int hudX = control.getDasSpielfeld().getBreite() * 39 / 100;
		int hudWidth = control.getDasSpielfeld().getBreite() * 26 / 100;
		int hudHeight = control.getDasSpielfeld().getHoehe() / 10;
		hud.setBounds(hudX, HUD_Y, hudWidth, hudHeight);
	}

	private void createHudLabels(JPanel hud) {
		int hudX = hud.getX();
		int hudY = hud.getY();
		int hudWidth = hud.getWidth();
		int hudHeight = hud.getHeight();

		JLabel speedLabel = new JLabel("Speed: "//
				+ (int) control.getDieRakete().getGeschwindigkeitsVektor().getL());
		speedLabel.setBounds(hudX, hudY, hudWidth / 2, hudHeight / 2);

		JLabel axisSpeedLabel = new JLabel("X: "//
				+ (int) control.getDieRakete().getGeschwindigkeitsVektor().getX()//
				+ "  Y: "//
				+ (int) control.getDieRakete().getGeschwindigkeitsVektor().getY());
		axisSpeedLabel.setBounds(hudX, hudY + hudHeight / 2//
				, hudWidth / 2, hudHeight / 2);

		JLabel legacyElementLabel = new JLabel("Schub: " + "legacy " + "%");
		legacyElementLabel.setBounds(hudX + hudWidth / 2, hudY//
				, hudWidth / 2, hudHeight / 2);

		JLabel angleLabel = new JLabel("Angle: "//
				+ (int) (control.getDieRakete().getNeigung()) + "°");
		angleLabel.setBounds(hudX + hudWidth / 2, hudY//
				+ hudHeight / 2, hudWidth / 2, hudHeight / 2);

		Font labelFont = Utilities.createFont(legacyElementLabel);
		speedLabel.setFont(labelFont);
		axisSpeedLabel.setFont(labelFont);
		angleLabel.setFont(labelFont);
		legacyElementLabel.setFont(labelFont);

		hud.add(speedLabel);
		hud.add(axisSpeedLabel);
		hud.add(legacyElementLabel);
		hud.add(angleLabel);
	}

	public void drawGameStage(Punkt[] derPunkt) {
		doubleBufferGraphics.setColor(Color.black);
		for (int i = 0; i < derPunkt.length - 1; i++) {
			doubleBufferGraphics.drawLine((int) (derPunkt[i].getX()), (int) (derPunkt[i].getY()),
					(int) (derPunkt[i + 1].getX()), (int) (derPunkt[i + 1].getY()));
		}
	}

	public void drawRocket(Punkt position, Punkt mitte, double neigung) {
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
}