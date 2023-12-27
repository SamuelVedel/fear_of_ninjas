package fr.SamuelVedel.Play;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.SamuelVedel.VComponent.VButton;

/**
 * 
 * Class créée le 22/08/2022
 * 
 * @author Samuel Vedel
 *
 */
public class ChoiceButton extends VButton {
	
	public Power pow;
	
	public ChoiceButton(int x, int y, int w, int h, int widthReference, int heightReference/*, Play play*/) {
		super(x, y, w, h, widthReference, heightReference, null);
	}
	
	@Override
	public void display(Color c2, Graphics2D g2d) {
		pow.detailDisplay(getActualX(), getActualY(), getActualWidth(), getActualHeight(), c2, g2d);
	}
}
