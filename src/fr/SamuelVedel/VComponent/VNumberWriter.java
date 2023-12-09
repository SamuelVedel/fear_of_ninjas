package fr.SamuelVedel.VComponent;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class VNumberWriter extends VComponent {
	
	private int number = 0;
	private int maxNum = -1;
	
	private Color background = Color.WHITE;
	private Color border = Color.BLACK;
	private Color foreground = Color.BLACK;
	
	private int[] fontSize = new int[2];
	
	private int t = 0;
	/**
	 * temps entre l'apparition et la dispatition de la barre d'écriture
	 */
	private int dt = 15;
	
	private KeyListener kl = new KeyListener() {
		
		@Override
		public void keyTyped(KeyEvent e) {
			char ch = e.getKeyChar();
			if (ch >= 48 && ch <= 57) {
				int num = Integer.parseInt(""+ch);
				number = 10*number+num;
				if (number > maxNum && maxNum > -1) {
					number = maxNum;
				}
			}
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 8) {
				number /= 10;
			}
		}
	};
	
	public VNumberWriter(int x, int y, int w, int h, int widthReference, int heightReference) {
		super(x, y, w, h, widthReference, heightReference);
		addKeyListener(kl);
		fontSize[0] = 7*h/10;
	}
	public VNumberWriter(int x, int y, int h, int widthReference, int heightReference) {
		this(x, y, 50*h/30, h, widthReference, heightReference);
	}
	
	public VNumberWriter(int x, int y, int w, int h) {
		this(x, y, w, h, 0, 0);
	}
	
	public VNumberWriter(int x, int y, int h) {
		this(x, y, h, 0, 0);
	}
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public int getMaximumNumber() {
		return maxNum;
	}
	
	public void setMaximumNumber(int maxNum) {
		this.maxNum = maxNum;
	}
	
	public Color getBackground() {
		return background;
	}
	
	public void setBackground(Color background) {
		this.background = background;
	}
	
	public Color getBorder() {
		return border;
	}
	
	public void setBorder(Color border) {
		this.border = border;
	}
	
	public Color getForeground() {
		return foreground;
	}
	
	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}
	
	public int getFontSize() {
		return fontSize[0];
	}
	
	public void setFontSize(int fontSize) {
		this.fontSize[0] = fontSize;
	}
	
	public int getActualFontSize() {
		return fontSize[1];
	}
	
	public void setActualFontSize(int fontSize) {
		this.fontSize[1] = fontSize;
	}
	
	@Override
	public void display(Color c2, Graphics2D g2d) {
		g2d.setColor(background);
		g2d.fillRect(getActualX(), getActualY(), getActualWidth(), getActualHeight());
		
		if (border != null) {
			g2d.setColor(border);
			g2d.drawRect(getActualX(), getActualY(), getActualWidth(), getActualHeight());
		}
		
		fontSize[1] = fontSize[0]*getActualHeight()/getHeight();
		g2d.setColor(foreground);
		g2d.setFont(new Font("ARIAL", Font.BOLD, fontSize[1]));
		String text = Integer.toString(number);
		int textW = UsefulTh.getTextW(text, g2d);
		int textH = UsefulTh.getTextH(text, g2d);
		g2d.drawString(text, getActualX()+getActualWidth()/2-textW/2, getActualY()+getActualHeight()/2+textH/2);
		
		if (hasFocus()) {
			t += 1;
			if ((t-t%dt)%(2*dt) != 0) {
				g2d.drawLine(getActualX()+getActualWidth()/2+textW/2, getActualY()+getActualHeight()/2+textH/2,
							 getActualX()+getActualWidth()/2+textW/2, getActualY()+getActualHeight()/2-textH/2);
			}
		}
	}
}
