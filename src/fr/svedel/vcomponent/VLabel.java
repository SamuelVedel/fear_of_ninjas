package fr.svedel.vcomponent;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class VLabel extends VComponent {
	
	private String text;
	
	private Color color = Color.WHITE;
	
	private String fontName = "ARIAL";
	private int fontStyle = Font.BOLD;
	private int[] fontSize = {10, 0};
	
	public VLabel(int x, int y, int widthReference, int heightReference, String text) {
		super(x, y, 0, 0, widthReference, heightReference);
		this.text = text;
	}
	
	public VLabel(int x, int y, int widthReference, int heightReference) {
		this(x, y, widthReference, heightReference, null);
	}
	
	public VLabel(int x, int y, String text) {
		this(x, y, 0, 0, text);
	}
	
	public VLabel(int x, int y) {
		this(x, y, 0, 0);
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public String getFontName() {
		return fontName;
	}
	
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	
	public int getFontStyle() {
		return fontStyle;
	}
	
	public void setFontStyle(int fontStyle) {
		this.fontStyle = fontStyle;
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
	public void adjust(int widthReference, int heightReference) {
		super.adjust(widthReference, heightReference);
		switch (getAdjustment()) {
		case NO_ADJUSTMENT :
			fontSize[1] = fontSize[0];
			break;
		case ADJUSTMENT_BY_WIDTH_AND_HEIGHT :
			fontSize[1] = getActualWidthReference()*fontSize[0]/getWidthReference();
			break;
		case ADJUSTMENT_BY_WIDTH :
			fontSize[1] = getActualWidthReference()*fontSize[0]/getWidthReference();
			break;
		case ADJUSTMENT_BY_HEIGHT :
			fontSize[1] = getActualHeightReference()*fontSize[0]/getHeightReference();
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
	}
	
	@Override
	public void display(Color c2, Graphics2D g2d) {
		g2d.setFont(new Font(fontName, fontStyle, fontSize[0]));
		setWidth(UsefulTh.getTextW(text, g2d));
		setHeight(UsefulTh.getTextH(text, g2d));
		
		g2d.setColor(color);
		g2d.setFont(new Font(fontName, fontStyle, fontSize[1]));
		g2d.drawString(text, getActualX(), getActualY()+getActualHeight());
	}
}
