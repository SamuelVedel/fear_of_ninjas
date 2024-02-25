package fr.svedel.vcomponent;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class VButton extends VAbstractButton {
	
	private String text;
	private VAdjustInt fontSize = new VAdjustInt(50);
	private VAdjustInt round = new VAdjustInt(50);
	
	// Couleur noraml du boutton.
	private Color background = new Color(0, 0, 0, 200);
	private Color foreground = new Color(255, 255, 255, 200);
	private Color border = new Color(255, 255, 255, 200);
	
	// Couleur du boutton quand il est survolé.
	private Color survolBackground = new Color(255, 255, 255, 200);
	private Color survolForeground = new Color(0, 0, 0, 200);
	private Color survolBorder = new Color(255, 255, 255, 200);
	
	public VButton(int x, int y, int w, int h, int widthReference, int heightReference, String text) {
		super(x, y, w, h, widthReference, heightReference);
		this.text = text;
	}
	
	public VButton(int x, int y, int w, int h, int widthReference, int heightReference) {
		this(x, y, w, h, widthReference, heightReference, null);
	}
	
	public VButton(int x, int y, int w, int h, String text) {
		this(x, y, w, h, 0, 0, text);
	}
	
	public VButton(int x, int y, int w, int h) {
		this(x, y, w, h, 0, 0);
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public VAdjustInt getFontSize() {
		return fontSize;
	}
	
	public VAdjustInt getRound() {
		return round;
	}
	
	public Color getBackground() {
		return background;
	}
	
	public void setBackground(Color background) {
		this.background = background;
	}
	
	public Color getForeground() {
		return foreground;
	}
	
	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}
	
	public Color getBorder() {
		return border;
	}
	
	public void setBorder(Color border) {
		this.border = border;
	}
	
	public Color getSurvolBackground() {
		return survolBackground;
	}
	
	public void setSurvolBackground(Color survolBackground) {
		this.survolBackground = survolBackground;
	}
	
	public Color getSurvolForeground() {
		return survolForeground;
	}
	
	public void setSurvolForeground(Color survolForeground) {
		this.survolForeground = survolForeground;
	}
	
	public Color getSurvolBorder() {
		return survolBorder;
	}
	
	public void setSurvolBorder(Color survolBorder) {
		this.survolBorder = survolBorder;
	}
	
	@Override
	public void adjust(int widthReference, int heightReference) {
		super.adjust(widthReference, heightReference);
		adjustValue(fontSize);
		adjustValue(round);
	}
	
	@Override
	public void display(Graphics2D g2d) {
		int currentX = getX().getCurrentValue();
		int currentY = getY().getCurrentValue();
		int currentWidth = getWidth().getCurrentValue();
		int currentHeight = getHeight().getCurrentValue();
		
		g2d.setColor(!isMouseIn()? background : survolBackground);
		g2d.fillRoundRect(currentX, currentY, currentWidth, currentHeight,
						  round.getCurrentValue(), round.getCurrentValue());
		
		g2d.setColor(isMouseIn()? border : survolBorder);
		g2d.drawRoundRect(currentX, currentY, currentWidth, currentHeight,
						  round.getCurrentValue(), round.getCurrentValue());
		
		if (text != null) {
			g2d.setColor(!isMouseIn()? foreground : survolForeground);
			g2d.setFont(new Font("ARIAL", Font.BOLD, fontSize.getCurrentValue()));
			int textW = UsefulTh.getTextW(text, g2d);
			int textH = UsefulTh.getTextH(text, g2d);
			UsefulTh.drawString(text, currentX+currentWidth/2-textW/2,
								currentY+currentHeight/2+textH/2, g2d);
		}
	}
}
