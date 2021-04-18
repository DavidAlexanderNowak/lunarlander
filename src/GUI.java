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

public class GUI extends JFrame{
	private Steuerung dieSteuerung;
	
	private JPanel contentPane;
	private Image dbImage;//db = Double Buffer
	private Graphics dbg;//dbg = Double Buffer Graphics
	
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
		
		setBounds(0, 0, breite, hoehe);//x,y,Breite,Höhe
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				dieSteuerung.tasteGedrücktVerarbeiten(e.getKeyCode());
			}
			@Override
			public void keyReleased(KeyEvent e) {
				dieSteuerung.tasteLosgelassenVerarbeiten(e.getKeyCode());
			}
		});
		setVisible(true);
	}
	
	public void paint(Graphics g) {
		//Erstellen images
		if(dbImage == null) {//Layer 0
			dbImage = createImage(this.getSize().width,this.getSize().height);
			dbg = dbImage.getGraphics();
		}
		
		dbg.clearRect(0, 0, this.getSize().width,this.getSize().height);
				
		dieSteuerung.grafik();
		
		//dbImage anzeigen
		g.drawImage(dbImage, 0, 0, this);
		
	}
	
	public void hintergrundLoeschen() {
		dbg.setColor(Color.white);
		dbg.fillRect(0, 0, getSize().width, getSize().height);
	}
	
	public void spielfeldZeichnen(int anzahlPunkte, Punkt[] derPunkt) {
		dbg.setColor(Color.black);
		//Erstmal nur ne Linie aber später nach Klasse Spielfeld gemodelt oder so
//		dbg.drawLine(0, (int)(hoehe*0.95), breite, (int)(hoehe*0.95));
		for(int i=0; i < anzahlPunkte-1; i++) {
			dbg.drawLine(
				(int)(derPunkt[i].getX()),
				(int)(derPunkt[i].getY()),
				(int)(derPunkt[i+1].getX()),
				(int)(derPunkt[i+1].getY())
			);
		}
	}
	
	public void raketeZeichnen(Punkt position, Punkt mitte, double neigung) {
		//Danke Stack Overflow
		Graphics2D g2d = (Graphics2D) dbg;
		int drawLocationX = (int) position.getX();
		int drawLocationY = (int) position.getY();
		AffineTransform backup = g2d.getTransform();
		//rx = x rotation--ry = y rotation--angle=rotationswinkel
		AffineTransform a = AffineTransform.getRotateInstance(
			Math.toRadians(neigung-270), mitte.getX(), mitte.getY()
		);
		g2d.setTransform(a);
		g2d.drawImage(raketeImage, drawLocationX, drawLocationY, null);
		g2d.setTransform(backup);
	}
}