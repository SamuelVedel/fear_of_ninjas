package fr.svedel.fod.play;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import fr.svedel.fod.FodButton;
import fr.svedel.fod.UsefulTh;
import fr.svedel.vcomponent.VActionListener;
import fr.svedel.vcomponent.VComponent;
import fr.svedel.vcomponent.VPanel;

public class EndScreen {
	
	private Play play;
	
	private final int widthReference = 800;
	private final int heightReference  = UsefulTh.HEIGHT_FOR_NO_SCALE;
	private final int buttonW = 400;
	private final int buttonH = 100;
	private final int buttonGap = 30;
	private VPanel vp = new VPanel(0, 0, widthReference, heightReference,
								   widthReference, heightReference);
	private FodButton[] buttons = {
			new FodButton(widthReference/2-buttonW/2, heightReference/2-buttonH-buttonGap/2,
						  buttonW, buttonH, widthReference, heightReference, "Rejouer"),
			new FodButton(widthReference/2-buttonW/2, heightReference/2+buttonGap/2,
						  buttonW, buttonH, widthReference, heightReference, "Quitter")
	};
	
	public VActionListener val = new VActionListener() {
		
		@Override
		public void action(VComponent source, MouseEvent e) {
			if (source == buttons[0]) {
				play.restart();
			} else if (source == buttons[1]) {
				play.phase = Play.END_PHASE;
				play.jf.setVisible(false);
			}
			setUsable(false);
		}
	};
	
	public EndScreen(Play play) {
		this.play = play;
		
		for (FodButton fb : buttons) {
			vp.add(fb);
			fb.setUsable(false);
			fb.getBorderWidth().setValue(2*UsefulTh.PIXEL_W);
			fb.setAddW(10);
			fb.getFontSize().setValue(35);
			fb.addVActionListener(val);
		}
		vp.setAdjustment(VComponent.ADJUSTMENT_BY_HEIGHT);
		vp.setAlignment(VComponent.CENTER_ALIGNMENT);
		vp.addMlToAComponent(play.playP);
		vp.addMmlToAComponent(play.playP);
	}
	
	public void setUsable(boolean boo) {
		for (FodButton fb : buttons) {
			fb.setUsable(boo);
		}
	}
	
	public void setColor(Color color) {
		for (FodButton fb : buttons) {
			fb.setBorder(play.color);
			fb.setForeground(play.color);
		}
	}
	
	public void display(Graphics2D g2d) {
		if (play.phase == Play.DEAD_PHASE) {
			g2d.setColor(new Color(44, 44, 44, 150));
			g2d.fillRect(0, 0, play.playP.getWidth(), play.playP.getHeight());
			
			vp.adjust(play.playP.getWidth(), play.playP.getHeight());
			vp.display(g2d);
		}
	}
}
