package fr.svedel.fod.play;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.svedel.fod.FodButton;

/**
 * 
 * Class créée le 22/08/2022
 * 
 * @author Samuel Vedel
 *
 */
public class ChoiceButton extends FodButton {
	
	public Power pow;
	public Color color;
	
	public ChoiceButton(int x, int y, int w, int h, int widthReference,
						int heightReference/*, Play play*/) {
		super(x, y, w, h, widthReference, heightReference, null);
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	@Override
	public void display(Graphics2D g2d) {
		int currentX = getX().getCurrentValue();
		int currentY = getY().getCurrentValue();
		int currentWidth = getWidth().getCurrentValue();
		int currentHeight = getHeight().getCurrentValue();
		pow.detailDisplay(currentX, currentY,
						  currentWidth, currentHeight, color, g2d);
	}
}
