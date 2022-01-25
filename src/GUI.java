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

	// HUD
//	private JPanel hud;
	private int HUD_Y = 30;

	private JPanel contentPane;
	private Image doubleBufferImage;
	private Graphics doubleBufferGraphics;

	private BufferedImage rocketImage;

	public GUI(Control dieSteuerung) {
		initialise(dieSteuerung);
		setVisible(true);
	}

	private void initialise(Control control) {
		this.control = control;
		rocketImage = Utilities.ladeBild("/player.png");
		setTitle("Lunar Lander");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, this.control.getDasSpielfeld().getBreite()//
				, this.control.getDasSpielfeld().getHoehe());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		initialiseKeyListeners();
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

	public void initialiseStartScreen() {

	}

	public void drawStartScreen() {

	}

	private void createHUDLabels(JPanel hud) {
		int hudX = hud.getX();
		int hudY = hud.getY();
		int hudWidth = hud.getWidth();
		int hudHeight = hud.getHeight();

		JLabel speedLabel = new JLabel("Speed: "//
				+ control.getDieRakete().getGeschwindigkeitsVektor().getL());
		speedLabel.setBounds(hudX, hudY, hudWidth / 2, hudHeight / 2);

		JLabel axisSpeedLabel = new JLabel("X: " + "Y: ");
		axisSpeedLabel.setBounds(hudX, hudY + hudHeight / 2//
				, hudWidth / 2, hudHeight / 2);

		JLabel legacyElementLabel = new JLabel("Schub: ");
		legacyElementLabel.setBounds(hudX + hudWidth / 2, hudY//
				, hudWidth / 2, hudHeight / 2);

		JLabel angleLabel = new JLabel("Angle: ");
		angleLabel.setBounds(hudX + hudWidth / 2, hudY//
				+ hudHeight / 2, hudWidth / 2, hudHeight / 2);

		Font labelFont = createFont(speedLabel);
		speedLabel.setFont(labelFont);
		axisSpeedLabel.setFont(labelFont);
		angleLabel.setFont(labelFont);
		legacyElementLabel.setFont(labelFont);

		hud.add(speedLabel);
		hud.add(axisSpeedLabel);
		hud.add(legacyElementLabel);
		hud.add(angleLabel);

		speedLabel.setText("Speed: "//
				+ (int) control.getDieRakete().getGeschwindigkeitsVektor().getL());
		axisSpeedLabel.setText("X: "//
				+ (int) control.getDieRakete().getGeschwindigkeitsVektor().getX()//
				+ "   Y: "//
				+ (int) control.getDieRakete().getGeschwindigkeitsVektor().getY());
		legacyElementLabel.setText("Schub: " + "legacy " + "%");
		angleLabel.setText("Angle: "//
				+ (int) (control.getDieRakete().getNeigung()) + "°");
	
	}

	private Font createFont(JLabel label) {
		Font font = label.getFont();
		String labelText = label.getText();
		int stringWidth = label.getFontMetrics(font).stringWidth(labelText);
		int componentWidth = label.getWidth();

		// Find out how much the font can grow in width.
		double widthRatio = (double) componentWidth / (double) stringWidth;

		int newFontSize = (int) (font.getSize() * widthRatio);
		int componentHeight = label.getHeight();

		// Pick a new font size so it will not be larger than the height of label.
		int fontSizeToUse = Math.min(newFontSize, componentHeight);

		font = new Font(font.getName(), Font.PLAIN, fontSizeToUse);

		return font;
		
		// Set the label's font size to the newly determined size.
//		label.setFont(font);

	}

	private JPanel createHUDJPanel() {
		JPanel hudJPanel = new JPanel();
		calculateHUDDimensions(hudJPanel);
		contentPane.add(hudJPanel);
		hudJPanel.setLayout(null);
		return hudJPanel;
	}

	private void calculateHUDDimensions(JPanel hud) {
		int hudX = control.getDasSpielfeld().getBreite() * 39 / 100;
		int hudWidth = control.getDasSpielfeld().getBreite() * 26 / 100;
		int hudHeight = control.getDasSpielfeld().getHoehe() / 10;
		hud.setBounds(hudX, HUD_Y, hudWidth, hudHeight);
	}

	public void drawHUD() {
		JPanel hud = createHUDJPanel();
		createHUDLabels(hud);
		hud.paintComponents(doubleBufferGraphics);
// maybe do use a class field for hud and then only settext every update
//		lblGeschwindigkeit.setText("Speed: "//
//				+ (int) control.getDieRakete().getGeschwindigkeitsVektor().getL());
//		lblGeschwindigkeitXY.setText("X: "//
//				+ (int) control.getDieRakete().getGeschwindigkeitsVektor().getX()//
//				+ "   Y: "//
//				+ (int) control.getDieRakete().getGeschwindigkeitsVektor().getY());
//		lblSchub.setText("Schub: " + "legacy " + "%");
//		lblNeigung.setText("Angle: "//
//				+ (int) (control.getDieRakete().getNeigung()) + "°");
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
		AffineTransform a = AffineTransform.getRotateInstance(Math.toRadians(neigung - 270)//
				, mitte.getX(), mitte.getY());
		graphics2D.setTransform(a);
		graphics2D.drawImage(rocketImage, drawLocationX, drawLocationY, null);
		graphics2D.setTransform(backup);
	}
}