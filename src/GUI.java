import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GUI extends JFrame {
	private Steuerung dieSteuerung;

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
		setVisible(true);
	}

	@Override
	public void paint(Graphics graphics) {
		if (doubleBufferImage == null) {
			doubleBufferImage = createImage(this.getSize().width, this.getSize().height);
			doubleBufferGraphics = doubleBufferImage.getGraphics();
		}
		doubleBufferGraphics.clearRect(0, 0, this.getSize().width, this.getSize().height);
		dieSteuerung.grafik();
		graphics.drawImage(doubleBufferImage, 0, 0, this);
	}

	public void hintergrundLoeschen() {
		doubleBufferGraphics.setColor(Color.white);
		doubleBufferGraphics.fillRect(0, 0, getSize().width, getSize().height);
	}

	public void initialiseStartScreen() {
		
	}
	
	public void drawStartScreen() {

	}

	public void initialiseHUD() {
		
	}
	
	public void drawHUD() {
		
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