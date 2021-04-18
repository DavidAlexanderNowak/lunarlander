import java.util.HashSet;
import java.awt.event.KeyEvent;

public class Steuerung implements Runnable{
	private GUI dieGUI;
	private Rakete dieRakete;
	private Spielfeld dasSpielfeld;
	private HashSet<Integer> keysPressed = new HashSet<>();
	
	public static void main(String[] args) {
		Thread f = new Thread(new Steuerung());
		f.start();
	}
	
	public Steuerung() {
		dasSpielfeld = new Spielfeld(0.1,108*8);		
		spielfeldErstellen();
		dieRakete = new Rakete(dasSpielfeld.getGravitation());
		dieGUI = new GUI(this);
	}
	
	public void tasteGedrücktVerarbeiten(int code) {//KeyPressed
		switch (code){
		case KeyEvent.VK_SPACE:
			keysPressed.add(0);
		break;
		case KeyEvent.VK_RIGHT:
			keysPressed.add(1);
		break;
		case KeyEvent.VK_LEFT:
			keysPressed.add(2);
		break;
		
		}
	}
	
	public void tasteLosgelassenVerarbeiten(int code) {//KeyReleased
		switch (code){
		case KeyEvent.VK_SPACE:
			keysPressed.remove(0);
		break;
		case KeyEvent.VK_RIGHT:
			keysPressed.remove(1);
		break;
		case KeyEvent.VK_LEFT:
			keysPressed.remove(2);
		break;
		}
	}
	
	private void update() {
		input();//EINGABE
		kollisionen();//VERARBEITUNG
		//AUSGABE = grafik
	}
	
	private void input() {
		if(keysPressed.contains(0)) {//Schub - Leertaste
			dieRakete.beschleunigen();
		}
		if(keysPressed.contains(1)) {//Rechts - Pfeil Rechts
			dieRakete.steuern(1);
		}
		if(keysPressed.contains(2)) {//Links - Pfeil Links
			dieRakete.steuern(-1);
		}
	}
	
	private void kollisionen() {
		dieRakete.seitenAustritt(0, dasSpielfeld.getBreite());
		dieRakete.positionUpdate();	//Wenn positionUpdate über bodenBeruehrung ist
		bodenBeruehrung();			//dann kann man vom boden wieder abheben
									//(siehe Rakete.beschleunigen erste Zeile) Debug glaub
	}
	
	private void bodenBeruehrung() {
		boolean bodenBeruehrt = false;
		Punkt[] punkt = dasSpielfeld.getDerPunkt();
		Punkt[] hitPunkte = new Punkt[11];
		//Punkt S an der Spitze:
		Punkt s = hitPunktBerechnen(new Punkt(dieRakete.getMittelpunkt()), 50, Math.toRadians(dieRakete.getNeigung()));

		//Punkte oL, oR
		Punkt o = hitPunktBerechnen(new Punkt(dieRakete.getMittelpunkt()), -27, Math.toRadians(dieRakete.getNeigung()-180));
		Punkt oL = hitPunktBerechnen(new Punkt(o), 20, Math.toRadians(dieRakete.getNeigung()-90));
		Punkt oR = hitPunktBerechnen(new Punkt(o), 20, Math.toRadians(dieRakete.getNeigung()+90));
		
		//Punkte aL, aR
		Punkt a = hitPunktBerechnen(new Punkt(dieRakete.getMittelpunkt()), -10, Math.toRadians(dieRakete.getNeigung()-180));
		Punkt aL = hitPunktBerechnen(new Punkt(a), 20, Math.toRadians(dieRakete.getNeigung()-90));
		Punkt aR = hitPunktBerechnen(new Punkt(a), 20, Math.toRadians(dieRakete.getNeigung()+90));
		
		//Punkte bL, bR
		Punkt b = hitPunktBerechnen(new Punkt(dieRakete.getMittelpunkt()), 8, Math.toRadians(dieRakete.getNeigung()-180));
		Punkt bL = hitPunktBerechnen(new Punkt(b), 25, Math.toRadians(dieRakete.getNeigung()-90));
		Punkt bR = hitPunktBerechnen(new Punkt(b), 25, Math.toRadians(dieRakete.getNeigung()+90));
		
		//Punkte fl, fR
		Punkt f = hitPunktBerechnen(new Punkt(dieRakete.getMittelpunkt()), 33, Math.toRadians(dieRakete.getNeigung()-180));
		Punkt fL = hitPunktBerechnen(new Punkt(f), 30, Math.toRadians(dieRakete.getNeigung()-90));
		Punkt fR = hitPunktBerechnen(new Punkt(f), 30, Math.toRadians(dieRakete.getNeigung()+90));
		
		//Punkte uL, uR
		Punkt u = hitPunktBerechnen(new Punkt(dieRakete.getMittelpunkt()), 50, Math.toRadians(dieRakete.getNeigung()-180));
		Punkt uL = hitPunktBerechnen(new Punkt(u), 20, Math.toRadians(dieRakete.getNeigung()-90));
		Punkt uR = hitPunktBerechnen(new Punkt(u), 20, Math.toRadians(dieRakete.getNeigung()+90));

		for(int i=0; i < dasSpielfeld.getAnzahlPunkte()-1; i++) {
			double x1 = punkt[i].getX();
			double y1 = punkt[i].getY();
			double x2 = punkt[i+1].getX();
			double y2 = punkt[i+1].getY();
			double m = (y2-y1)/(x2-x1);//Gerade g zwischen p1 und p2 (i und i+1)
			double c = y1 - m * x1;	//I  'y' = 'm'*'x'+c    bei ' ' ist Wert bekannt
									//Ia c = 'y1' - 'm'*'x1'
			
						
//			-------------Kollision mit Unterseite als Gerade--------
//			Erst gucken ob es sich schneidet -> duh 2D schneidet sich immer...
//			Schnittpunkt errechnen
//			x vom Schnittpunkt in Intervall x1 x2?
//			...
//			Profit
//			-----------------------------------------------------
//			Gerade gU aufstellen
			double xL = uL.getX();
			double yL = uL.getY();
			double xR = uR.getX();
			double yR = uR.getY();
			if((x1 <= xL && xR <= x2)||(x1 <= xR && xL <= x2)) {
				double mU = (yR-yL)/(xR-xL);
				double cU = yR - mU * xR;
				//Schnittpunkt berechnen von Spielfeld Gerade und gU
				double xS = (cU-c)/(m-mU);
				if(
					((xL <= xS && xS <= xR)||(xR <= xS && xS <= xL))&&
					(x1 <= xS && xS <= x2)
				){
					bodenBeruehrt = true;
				}
			}
			//Wenn mindestens einer der Kollisionspunkte auf der Geraden liegt dann Stopp
			if(punktAufGerade(uL, c, x1, x2, m)) {
				bodenBeruehrt = true;
			};
			if(punktAufGerade(uR, c, x1, x2, m)) {
				bodenBeruehrt = true;
			};
			if(punktAufGerade(s, c, x1, x2, m)) {
				bodenBeruehrt = true;
			};
			if(punktAufGerade(fL, c, x1, x2, m)) {
				bodenBeruehrt = true;
			};
			if(punktAufGerade(fR, c, x1, x2, m)) {
				bodenBeruehrt = true;
			};
			if(punktAufGerade(oL, c, x1, x2, m)) {
				bodenBeruehrt = true;
			};
			if(punktAufGerade(oR, c, x1, x2, m)) {
				bodenBeruehrt = true;
			};
			if(punktAufGerade(u, c, x1, x2, m)) {
				bodenBeruehrt = true;
			};
			if(punktAufGerade(aL, c, x1, x2, m)) {
				bodenBeruehrt = true;
			};
			if(punktAufGerade(aR, c, x1, x2, m)) {
				bodenBeruehrt = true;
			};
			if(punktAufGerade(bL, c, x1, x2, m)) {
				bodenBeruehrt = true;
			};
			if(punktAufGerade(bR, c, x1, x2, m)) {
				bodenBeruehrt = true;
			};
		}
		//Später wenn der Boden berürht wird, gibt es keine Steuerung
		//-> Position wird gesetzt so dass es präzise aussieht auch wenn die
		//Rakete /bisschen drunter war -- oder Explosion gif
		dieRakete.setStopp(bodenBeruehrt);
	}
	
	private boolean punktAufGerade(Punkt p, double c, double x1, double x2, double m) {
		boolean istAufGerade = false;
		if((x1 <= p.getX())&&(p.getX() <= x2)){
			double cP = p.getY()-m*
						p.getX();
			if(c <= cP) {
				istAufGerade = true;
			}
		}
		return istAufGerade;
	}
	
	private Punkt hitPunktBerechnen(Punkt ausgangspunkt, int abstand, double winkel) {
		Punkt hitPunkt = new Punkt(ausgangspunkt.getX(),ausgangspunkt.getY());
		Punkt vektor = new Punkt(true, abstand, winkel);
		hitPunkt.vektorAddieren(vektor);
		return hitPunkt;
	}
	
	public void grafik() {
		//Hintergrund löschen
		dieGUI.hintergrundLoeschen();
		//Spielfeld zeichnen
		dieGUI.spielfeldZeichnen(dasSpielfeld.getAnzahlPunkte(),dasSpielfeld.getDerPunkt());
		//Rakete zeichnen
		dieGUI.raketeZeichnen(dieRakete.getPosition(), dieRakete.getMittelpunkt(), dieRakete.getNeigung());
	}
	
	
	private void spielfeldErstellen() {
		dasSpielfeld.setAnzahlPunkte(20);
		
		//Erster Punkt immer gleich
		dasSpielfeld.setPunkt(0, new Punkt(0,dasSpielfeld.getHoehe()*0.8));
		
		//Der Rest
		for(int i = 1; i < dasSpielfeld.getAnzahlPunkte()-1; i++) {
			int x = dasSpielfeld.getBreite() /
					(dasSpielfeld.getAnzahlPunkte()-1) * i;
			int y = (int)(
				(0.95*dasSpielfeld.getHoehe())-
				Math.random()*(dasSpielfeld.getHoehe()*0.75)
			);
			Punkt p = new Punkt(x,y);
			dasSpielfeld.setPunkt(i, p);
		}
		
		
		//Letzter Punkt immer gleich
		dasSpielfeld.setPunkt(
			dasSpielfeld.getAnzahlPunkte()-1,
			new Punkt(dasSpielfeld.getBreite(), dasSpielfeld.getHoehe()*0.8)
		);
	}
	
	
	@Override
	public void run(){
		double timePerTick = 1000000000 / 60;
	    double delta = 0;
	    long now;
	    long lastTime = System.nanoTime();
		while(true) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
		   	lastTime = now;

		    if (delta >= 1) {
		    
		    //Spielogik
		    	
		    	//Aktualisieren
		    	update();
			
		    	//Zeichnen
		    	dieGUI.repaint();
		    	delta--;
		    }
		}
	}

	public Spielfeld getDasSpielfeld() {
		return dasSpielfeld;
	}
}