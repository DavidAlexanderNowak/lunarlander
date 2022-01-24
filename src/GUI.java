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
	private Steuerung dieSteuerung;

	// HUD
	private JPanel hud;
	private JLabel lblGeschwindigkeit;
	private JLabel lblGeschwindigkeitXY;
	private JLabel lblSchub;
	private JLabel lblNeigung;
	private Font labelFont;
	private String labelText;
	private int stringWidth;
	private int componentWidth;
	private int hudX;
	private int hudY = 30;
	private int hudWidth;
	private int hudHeight;

	private JPanel contentPane;
	private Image doubleBufferImage;
	private Graphics doubleBufferGraphics;

	private BufferedImage raketeImage;

	private final int hoehe;
	private final int breite;

	public GUI(Steuerung dieSteuerung) {
		this.dieSteuerung = dieSteuerung;

		setTitle("Lunar Lander");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		raketeImage = Utilities.ladeBild("/player.png");

		hoehe = dieSteuerung.getDasSpielfeld().getHoehe();
		breite = dieSteuerung.getDasSpielfeld().getBreite();

		setBounds(0, 0, breite, hoehe);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				dieSteuerung.keyPressed(e.getKeyCode());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				dieSteuerung.keyReleased(e.getKeyCode());
			}
		});
		initialiseHUD();
		setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		if (doubleBufferImage == null) {
			doubleBufferImage = createImage(this.getSize().width, this.getSize().height);
			doubleBufferGraphics = doubleBufferImage.getGraphics();
		}
		doubleBufferGraphics.clearRect(0, 0, this.getSize().width, this.getSize().height);
		dieSteuerung.grafik();
		drawHUD();
		g.drawImage(doubleBufferImage, 0, 0, this);
	}

	public void hintergrundLoeschen() {
		doubleBufferGraphics.setColor(Color.white);
		doubleBufferGraphics.fillRect(0, 0, getSize().width, getSize().height);
	}

	public void drawStartScreen() {

	}

	private void initialiseHUD() {
		// hud Dimensionen berechnen
		hudX = breite * 39 / 100;
		hudWidth = breite * 26 / 100;
		hudHeight = hoehe / 10;

		// ANFANG HUD erstellen
		hud = new JPanel();
		hud.setBounds(hudX, hudY, hudWidth, hudHeight);
		contentPane.add(hud);
		hud.setLayout(null);

		// Alle HUD Elemente erstellen (schon ausgefüllt für die Skalierung)
		lblGeschwindigkeit = new JLabel(
				"Geschwindigkeit: " + dieSteuerung.getDieRakete().getGeschwindigkeitsVektor().getL());
		lblGeschwindigkeit.setBounds(hudX, hudY, hudWidth / 2, hudHeight / 2);

		lblGeschwindigkeitXY = new JLabel("X: " + "Y: ");
		lblGeschwindigkeitXY.setBounds(hudX, hudY + hudHeight / 2, hudWidth / 2, hudHeight / 2);

		lblSchub = new JLabel("Schub: ");
		lblSchub.setBounds(hudX + hudWidth / 2, hudY, hudWidth / 2, hudHeight / 2);

		lblNeigung = new JLabel("Neigung: ");
		lblNeigung.setBounds(hudX + hudWidth / 2, hudY + hudHeight / 2, hudWidth / 2, hudHeight / 2);

		// HUD Font anpassen(danke stackoverflow)
		labelFont = lblGeschwindigkeit.getFont();
		labelText = lblGeschwindigkeit.getText();
		stringWidth = lblGeschwindigkeit.getFontMetrics(labelFont).stringWidth(labelText);
		componentWidth = lblGeschwindigkeit.getWidth();

		// Find out how much the font can grow in width.
		double widthRatio = (double) componentWidth / (double) stringWidth;

		int newFontSize = (int) (labelFont.getSize() * widthRatio);
		int componentHeight = lblGeschwindigkeit.getHeight();

		// Pick a new font size so it will not be larger than the height of label.
		int fontSizeToUse = Math.min(newFontSize, componentHeight);

		// Set the label's font size to the newly determined size.
		lblGeschwindigkeit.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
		lblGeschwindigkeitXY.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
		lblNeigung.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
		lblSchub.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));

		hud.add(lblGeschwindigkeit);
		hud.add(lblGeschwindigkeitXY);
		hud.add(lblSchub);
		hud.add(lblNeigung);
	}

	public void drawHUD() {
		lblGeschwindigkeit
				.setText("Geschwindigkeit: " + (int) dieSteuerung.getDieRakete().getGeschwindigkeitsVektor().getL());
		lblGeschwindigkeitXY.setText("X: " + (int) dieSteuerung.getDieRakete().getGeschwindigkeitsVektor().getX()
				+ "   Y: " + (int) dieSteuerung.getDieRakete().getGeschwindigkeitsVektor().getY());
		lblSchub.setText("Schub: " + "legacy " + "%");
		lblNeigung.setText("Neigung: " + (int) (dieSteuerung.getDieRakete().getNeigung()) + "°");
		hud.paintComponents(doubleBufferGraphics);
	}

	public void spielfeldZeichnen(Punkt[] derPunkt) {
		doubleBufferGraphics.setColor(Color.black);
		for (int i = 0; i < derPunkt.length - 1; i++) {
			doubleBufferGraphics.drawLine((int) (derPunkt[i].getX()), (int) (derPunkt[i].getY()),
					(int) (derPunkt[i + 1].getX()), (int) (derPunkt[i + 1].getY()));
		}
	}

	public void raketeZeichnen(Punkt position, Punkt mitte, double neigung) {
		Graphics2D graphics2D = (Graphics2D) doubleBufferGraphics;
		int drawLocationX = (int) position.getX();
		int drawLocationY = (int) position.getY();
		AffineTransform backup = graphics2D.getTransform();
		AffineTransform a = AffineTransform.getRotateInstance(Math.toRadians(neigung - 270)//
				, mitte.getX(), mitte.getY());
		graphics2D.setTransform(a);
		graphics2D.drawImage(raketeImage, drawLocationX, drawLocationY, null);
		graphics2D.setTransform(backup);
	}
}