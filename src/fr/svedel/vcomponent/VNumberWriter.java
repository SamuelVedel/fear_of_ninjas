package fr.svedel.vcomponent;

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
	
	private VAdjustInt fontSize;
	
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
		fontSize = new VAdjustInt(7*h/10);
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
	
	public VAdjustInt getFontSize() {
		return fontSize;
	}
	
	@Override
	public void adjust(int widthReference, int heightReference) {
		super.adjust(widthReference, heightReference);
		adjustValue(fontSize, false);
	}
	
	@Override
	public void display(Graphics2D g2d) {
		int currentX = getX().getCurrentValue();
		int currentY = getY().getCurrentValue();
		int currentWidth = getWidth().getCurrentValue();
		int currentHeight = getHeight().getCurrentValue();
		
		g2d.setColor(background);
		g2d.fillRect(currentX, currentY, currentWidth, currentHeight);
		
		if (border != null) {
			g2d.setColor(border);
			g2d.drawRect(currentX, currentY, currentWidth, currentHeight);
		}
		
		g2d.setColor(foreground);
		g2d.setFont(new Font("ARIAL", Font.BOLD, fontSize.getCurrentValue()));
		String text = Integer.toString(number);
		int textW = UsefulTh.getTextW(text, g2d);
		int textH = UsefulTh.getTextH(text, g2d);
		UsefulTh.drawString(text, currentX+currentWidth/2-textW/2,
							currentY+currentHeight/2+textH/2, g2d);
		
		if (hasFocus()) {
			t += 1;
			if ((t-t%dt)%(2*dt) != 0) {
				g2d.drawLine(currentX+currentWidth/2+textW/2,
							 currentY+currentHeight/2+textH/2,
							 currentX+currentWidth/2+textW/2,
							 currentY+currentHeight/2-textH/2);
			}
		}
	}
}
