package fr.svedel.vcomponent;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class VLabel extends VComponent {
	
	private String text;
	
	private Color color = Color.WHITE;
	
	private String fontName = "ARIAL";
	private int fontStyle = Font.BOLD;
	private VAdjustInt fontSize = new VAdjustInt(10);
	
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
	
	public VAdjustInt getFontSize() {
		return fontSize;
	}
	
	@Override
	public void adjust(int widthReference, int heightReference) {
		super.adjust(widthReference, heightReference);
		adjustValue(fontSize);
	}
	
	@Override
	public void display(Graphics2D g2d) {
		int currentX = getX().getCurrentValue();
		int currentY = getY().getCurrentValue();
		int currentWidth = getWidth().getCurrentValue();
		int currentHeight = getHeight().getCurrentValue();
		
		g2d.setFont(new Font(fontName, fontStyle, fontSize.getValue()));
		getWidth().setValue(UsefulTh.getTextW(text, g2d));
		getHeight().setValue(UsefulTh.getTextH(text, g2d));
		
		g2d.setColor(color);
		g2d.setFont(new Font(fontName, fontStyle, fontSize.getCurrentValue()));
		UsefulTh.drawString(text, currentX, currentY+currentHeight, g2d);
	}
}
