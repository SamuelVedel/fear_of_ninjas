package fr.SamuelVedel.VComponent;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class VSwitch extends VAbstractButton {
	
	private boolean on = false;
	
	private double borderWidth = 0.1;
	private Color onColor = Color.GREEN/*.darker()*/;
	private Color offColor = Color.RED;
	private Color background = Color./*LIGHT_GRAY*/DARK_GRAY;
	private Color foreground = Color.LIGHT_GRAY;
	
	private double roundD = 0.7;
	/**
	 * Pourcentage entre la position le rond est � gauche
	 * et le rond est � droite
	 */
	private int perOfRoundX = 0;
	private int roundV = 15;
	
	private double dOf0 = 0.4;
	private double d2Of0 = 0.25;
	private double wOf1 = 0.1;
	private double hOf1 = 0.3;
	
	private MouseListener ml = new MouseListener() {
		
		@Override
		public void mouseReleased(MouseEvent e) {}
		
		@Override
		public void mousePressed(MouseEvent e) {}
		
		@Override
		public void mouseExited(MouseEvent e) {}
		
		@Override
		public void mouseEntered(MouseEvent e) {}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if (isUsable()) {
				on = !on;
				executeVActLs(e);
			}
		}
	};
	
	public VSwitch(int x, int y, int w, int h, int widthReference, int heightReference) {
		super(x, y, w, h, widthReference, heightReference);
		removeMLForVActLs();
		addMouseListener(ml);
	}
	
	public VSwitch(int x, int y, int h, int widthReference, int heightReference) {
		this(x, y, 110*h/50, h, widthReference, heightReference);
	}
	
	public VSwitch(int x, int y, int w, int h) {
		this(x, y, w, h, 0, 0);
	}
	
	public VSwitch(int x, int y, int h) {
		this(x, y, h, 0, 0);
	}
	
	public boolean getState() {
		return on;
	}
	
	public void setState(boolean state) {
		on = state;
	}
	
	private void moveRound() {
		if (on) {
			if (perOfRoundX < 100) perOfRoundX += roundV;
			if (perOfRoundX > 100) perOfRoundX = 100;
		} else if (perOfRoundX > 0) {
			perOfRoundX -= roundV;
			if (perOfRoundX < 0) perOfRoundX = 0;
		}
	}
	
	@Override
	public void display(Color c2, Graphics2D g2d) {
		int bWidth = (int)(getActualHeight()*borderWidth);
		g2d.setColor(on? onColor: offColor);
		g2d.fillRoundRect(getActualX(), getActualY(), getActualWidth(), getActualHeight(), getActualHeight(), getActualHeight());
//		g2d.fillRect(getActualX(), getActualY(), getActualWidth(), getActualHeight());
		
		g2d.setColor(background);
		g2d.fillRoundRect(getActualX()+bWidth, getActualY()+bWidth,
						  getActualWidth()-bWidth*2, getActualHeight()-bWidth*2,
						  getActualHeight()-bWidth*2, getActualHeight()-bWidth*2);
//		g2d.fillRect(getActualX()+bWidth, getActualY()+bWidth,
//					 getActualWidth()-bWidth*2, getActualHeight()-bWidth*2);
		
		// affichage du 0
		g2d.setColor(foreground);
		int d0 = (int)(getActualHeight()*dOf0);
		int d20 = (int)(getActualHeight()*d2Of0);
		g2d.fillOval(getActualX()+getActualWidth()*3/4-d0/2, getActualY()+getActualHeight()/2-d0/2, d0, d0);
		g2d.setColor(background);
		g2d.fillOval(getActualX()+getActualWidth()*3/4-d20/2, getActualY()+getActualHeight()/2-d20/2, d20, d20);
		// affichage du 1
		int w1 = (int)(getActualHeight()*wOf1);
		int h1 = (int)(getActualHeight()*hOf1);
		g2d.setColor(foreground);
		g2d.fillRect(getActualX()+getActualWidth()*1/5-w1/2, getActualY()+getActualHeight()/2-h1/2, w1, h1);
		
		
		g2d.setColor(on? onColor: offColor);
		int d = (int)(getActualHeight()*roundD);
		int gap = (getActualHeight()-d)/2;
		g2d.fillOval((int)(getActualX()+gap+(getActualWidth()-2*gap-d)*((double)perOfRoundX/100)), getActualY()+gap, d, d);
//		g2d.fillRect((int)(getActualX()+gap+(getActualWidth()-2*gap-d)*((double)perOfRoundX/100)), getActualY()+gap, d, d);
		
		moveRound();
	}
}

