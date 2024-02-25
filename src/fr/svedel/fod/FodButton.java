package fr.svedel.fod;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import fr.svedel.vcomponent.VAdjustInt;
import fr.svedel.vcomponent.VButton;

public class FodButton extends VButton {
	
	private VAdjustInt borderWidth = new VAdjustInt(50);
	
	/** taille qu'on ajoute au boutton lors du survol */
	private int addW = 20;
	
	public FodButton(int x, int y, int w, int h, int widthReference,
					 int heightReference, String text) {
		super(x, y, w, h, widthReference, heightReference, text);
		setBackground(Color.BLACK);
	}
	
	public VAdjustInt getBorderWidth() {
		return borderWidth;
	}
	
	public int getAddW() {
		return addW;
	}
	
	public void setAddW(int addW) {
		this.addW = addW;
	}
	
	@Override
	public void adjust(int widthReference, int heightReference) {
		int x0 = getX().getValue();
		int y0 = getY().getValue();
		int w0 = getWidth().getValue();
		int h0 = getHeight().getValue();
		if (isMouseIn()) {
			getX().setValue(x0-addW/2);
			getY().setValue(y0-addW/2);
			getWidth().setValue(w0+addW);
			getHeight().setValue(h0+addW);
		}
		super.adjust(widthReference, heightReference);
		adjustValue(borderWidth);
		
		if (isMouseIn()) {
			getX().setValue(x0);
			getY().setValue(y0);
			getWidth().setValue(w0);
			getHeight().setValue(h0);
		}
	}
	
	/*public void display(Color c2, Graphics2D g2d) {
		setBorder(c2);
		setForeground(c2);
		display(gd2);
	}*/
	
	@Override
	public void display(Graphics2D g2d) {
		int currentX = getX().getCurrentValue();
		int currentY = getY().getCurrentValue();
		int currentWidth = getWidth().getCurrentValue();
		int currentHeight = getHeight().getCurrentValue();
		int currentBorder = getBorderWidth().getCurrentValue();
		int currentFontSize = getFontSize().getCurrentValue();
		
		g2d.setColor(getBorder());
		g2d.fillRect(currentX, currentY, currentWidth, currentHeight);
		g2d.setColor(getBackground());
		g2d.fillRect(currentX+currentBorder, currentY+currentBorder,
			    currentWidth-2*currentBorder, currentHeight-2*currentBorder);
		
		if (getText() != null) {
			g2d.setColor(getForeground());
			g2d.setFont(new Font("ARIAL", Font.BOLD, currentFontSize));
			int textW = UsefulTh.getTextW(getText(), g2d);
			int textH = UsefulTh.getTextH(getText(), g2d);
			UsefulTh.drawString(getText(), currentX+currentWidth/2-textW/2,
								currentY+currentHeight/2+textH/2, g2d);
		}
	}
}
