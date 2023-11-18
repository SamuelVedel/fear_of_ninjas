package fr.SamuelVedel.VComponent;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class VButton extends VAbstractButton {
	
	private String text;
	private int[] fontSize = {50, 0};
	private int[] borderSize = {50, 0};
	
	private int addW = 20;
	
	// Couleur noraml du boutton.
	private Color background = new Color(0, 0, 0);
//	private Color foreground = new Color(255, 255, 255, 200);
//	private Color border = new Color(255, 255, 255, 200);
	
//	// Couleur du boutton quand il est survolé.
//	private Color survolBackground = new Color(255, 255, 255, 200);
//	private Color survolForeground = new Color(0, 0, 0, 200);
//	private Color survolBorder = new Color(255, 255, 255, 200);
	
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
	
	public int getBorderSize() {
		return borderSize[0];
	}
	
	public void setBorderSize(int borderSize) {
		this.borderSize[0] = borderSize;
	}
	
	public int getActualBorderSize() {
		return borderSize[1];
	}
	
	public void setActualBorderSize(int borderSize) {
		this.borderSize[1] = borderSize;
	}
	
	public int getAddW() {
		return addW;
	}
	
	public void setAddW(int addW) {
		this.addW = addW;
	}
	
	public Color getBackground() {
		return background;
	}
	
	public void setBackground(Color background) {
		this.background = background;
	}
	
//	public Color getForeground() {
//		return foreground;
//	}
//	
//	public void setForeground(Color foreground) {
//		this.foreground = foreground;
//	}
//	
//	public Color getBorder() {
//		return border;
//	}
//	
//	public void setBorder(Color border) {
//		this.border = border;
//	}
//	
//	public Color getSurvolBackground() {
//		return survolBackground;
//	}
//	
//	public void setSurvolBackground(Color survolBackground) {
//		this.survolBackground = survolBackground;
//	}
//	
//	public Color getSurvolForeground() {
//		return survolForeground;
//	}
//	
//	public void setSurvolForeground(Color survolForeground) {
//		this.survolForeground = survolForeground;
//	}
//	
//	public Color getSurvolBorder() {
//		return survolBorder;
//	}
//	
//	public void setSurvolBorder(Color survolBorder) {
//		this.survolBorder = survolBorder;
//	}
	
	@Override
	public void adjust(int widthReference, int heightReference) {
		int x0 = getX();
		int y0 = getY();
		int w0 = getWidth();
		int h0 = getHeight();
		if (isMouseIn()) {
			setX(x0-addW/2);
			setY(y0-addW/2);
			setWidth(w0+addW);
			setHeight(h0+addW);
		}
		
		super.adjust(widthReference, heightReference);
		switch (getAdjustment()) {
		case NO_ADJUSTMENT :
			fontSize[1] = fontSize[0];
			borderSize[1] = borderSize[0];
			break;
		case ADJUSTMENT_BY_WIDTH_AND_HEIGHT :
			fontSize[1] = getActualWidthReference()*fontSize[0]/getWidthReference();
			borderSize[1] = getActualWidthReference()*borderSize[0]/getWidthReference();
			break;
		case ADJUSTMENT_BY_WIDTH :
			fontSize[1] = getActualWidthReference()*fontSize[0]/getWidthReference();
			borderSize[1] = getActualWidthReference()*borderSize[0]/getWidthReference();
			break;
		case ADJUSTMENT_BY_HEIGHT :
			fontSize[1] = getActualHeightReference()*fontSize[0]/getHeightReference();
			borderSize[1] = getActualHeightReference()*borderSize[0]/getHeightReference();
			break;
		default :
//			if (widthReference[0]*w[0]/widthReference[1] >= heightReference[0]*w[0]/heightReference[1]) {
//				autoAdjustment = PitiButton.ADJUSTMENT_BY_WIDTH;
//				adjust(width, height);
//			} else {
//				autoAdjustment = PitiButton.ADJUSTMENT_BY_HEIGHT;
//				adjust(width, height);
//			}
//			autoAdjustment = PitiButton.ADJUSTMENT_BY_THE_SMALLEST;
		}
		
		setX(x0);
		setY(y0);
		setWidth(w0);
		setHeight(h0);
	}
	
	@Override
	public void display(Color c2, Graphics2D g2d) {
//		g2d.setColor(/*!isMouseIn()? */background/* : survolBackground*/);
//		g2d.fillRoundRect(getActualX(), getActualY(), getActualWidth(), getActualHeight(), round[1], round[1]);
//		
//		g2d.setColor(/*isMouseIn()? border : survolBorder*/c2);
//		g2d.drawRoundRect(getActualX(), getActualY(), getActualWidth(), getActualHeight(), round[1], round[1]);
		
		g2d.setColor(c2);
		g2d.fillRect(getActualX(), getActualY(), getActualWidth(), getActualHeight());
		g2d.setColor(Color.BLACK);
		g2d.fillRect(getActualX()+borderSize[1], getActualY()+borderSize[1],
				getActualWidth()-2*borderSize[1], getActualHeight()-2*borderSize[1]);
		
		if (text != null) {
			g2d.setColor(/*!isMouseIn()? foreground : survolForeground*/c2);
			g2d.setFont(new Font("ARIAL", Font.BOLD, fontSize[1]));
			int textW = UsefulTh.getTextW(text, g2d);
			int textH = UsefulTh.getTextH(text, g2d);
			UsefulTh.drawString(text, getActualX()+getActualWidth()/2-textW/2, getActualY()+getActualHeight()/2+textH/2, g2d);
		}
	}
}
