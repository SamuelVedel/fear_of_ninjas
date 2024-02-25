package fr.svedel.vcomponent;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class VSwitch extends VAbstractButton {
	
	private boolean on = false;
	
	private double borderWidth = 0.1;
	private Color onColor = Color.GREEN/*.darker()*/;
	private Color offColor = Color.RED;
	private Color background = Color./*LIGHT_GRAY*/DARK_GRAY;
	private Color foreground = Color.LIGHT_GRAY;
	
	private boolean rounded = true;
	
	private double roundD = 0.7;
	/**
	 * Pourcentage entre la position le rond est à gauche
	 * et le rond est à droite
	 */
	private int perOfRoundX = 0;
	private int roundV = 15;
	
	/** diamètre extérieure du 0 */
	private double dOf0 = 0.4;
	/** diamètre intérieure du 0 */
	private double d2Of0 = 0.25;
	/** largeur du 1 */
	private double wOf1 = 0.1;
	/** hauteur du 1 */
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
	
	public boolean isRounded() {
		return rounded;
	}
	
	public void setRounded(boolean rounded) {
		this.rounded = rounded;
	}
	
	@Override
	public void display(Graphics2D g2d2) {
		Graphics2D g2d = (Graphics2D) g2d2.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// border width
		int bWidth = (int)(getHeight().getCurrentValue()*borderWidth);
		g2d.setColor(on? onColor: offColor);
		if (isRounded()) {
			g2d.fillRoundRect(getX().getCurrentValue(), getY().getCurrentValue(),
							  getWidth().getCurrentValue(), getHeight().getCurrentValue(),
							  getHeight().getCurrentValue(), getHeight().getCurrentValue());
		} else {
			g2d.fillRect(getX().getCurrentValue(), getY().getCurrentValue(),
						 getWidth().getCurrentValue(), getHeight().getCurrentValue());
		}
		
		g2d.setColor(background);
		if (isRounded()) {
		g2d.fillRoundRect(getX().getCurrentValue()+bWidth, getY().getCurrentValue()+bWidth,
						  getWidth().getCurrentValue()-bWidth*2, getHeight().getCurrentValue()-bWidth*2,
						  getHeight().getCurrentValue()-bWidth*2, getHeight().getCurrentValue()-bWidth*2);
		} else {
			g2d.fillRect(getX().getCurrentValue()+bWidth, getY().getCurrentValue()+bWidth,
						 getWidth().getCurrentValue()-bWidth*2, getHeight().getCurrentValue()-bWidth*2);
		}
		
		// affichage du 0
		g2d.setColor(foreground);
		int d0 = (int)(getHeight().getCurrentValue()*dOf0);
		int d20 = (int)(getHeight().getCurrentValue()*d2Of0);
		g2d.fillOval(getX().getCurrentValue()+getWidth().getCurrentValue()*3/4-d0/2,
					 getY().getCurrentValue()+getHeight().getCurrentValue()/2-d0/2, d0, d0);
		g2d.setColor(background);
		g2d.fillOval(getX().getCurrentValue()+getWidth().getCurrentValue()*3/4-d20/2,
					 getY().getCurrentValue()+getHeight().getCurrentValue()/2-d20/2, d20, d20);
		// affichage du 1
		int w1 = (int)(getHeight().getCurrentValue()*wOf1);
		int h1 = (int)(getHeight().getCurrentValue()*hOf1);
		g2d.setColor(foreground);
		g2d.fillRect(getX().getCurrentValue()+getWidth().getCurrentValue()*1/5-w1/2,
					 getY().getCurrentValue()+getHeight().getCurrentValue()/2-h1/2, w1, h1);
		
		// affichage du cercle
		g2d.setColor(on? onColor: offColor);
		int d = (int)(getHeight().getCurrentValue()*roundD);
		int gap = (getHeight().getCurrentValue()-d)/2;
		if (isRounded()) {
			g2d.fillOval((int)(getX().getCurrentValue()+gap+(getWidth().getCurrentValue()-2*gap-d)*((double)perOfRoundX/100)),
					 getY().getCurrentValue()+gap, d, d);
		} else {
			g2d.fillRect((int)(getX().getCurrentValue()+gap+(getWidth().getCurrentValue()-2*gap-d)*((double)perOfRoundX/100)),
						 getY().getCurrentValue()+gap, d, d);
		}
		
		moveRound();
		g2d.dispose();
	}
}

