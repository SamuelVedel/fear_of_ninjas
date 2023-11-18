package fr.SamuelVedel.Play;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import fr.SamuelVedel.FOD.UsefulTh;
import fr.SamuelVedel.VComponent.VActionListener;
import fr.SamuelVedel.VComponent.VButton;
import fr.SamuelVedel.VComponent.VComponent;
import fr.SamuelVedel.VComponent.VPanel;

public class EndScreen {
	
	private Play play;
	
	private final int widthReference = 800;
	private final int heightReference  = UsefulTh.heightForNoScale;
	private final int buttonW = 400;
	private final int buttonH = 100;
	private final int buttonGap = 30;
	private VPanel vp = new VPanel(0, 0, widthReference, heightReference, widthReference, heightReference);
	private VButton[] buttons = {
			new VButton(widthReference/2-buttonW/2, heightReference/2-buttonH-buttonGap/2, buttonW, buttonH, widthReference, heightReference, "Rejouer"),
			new VButton(widthReference/2-buttonW/2, heightReference/2+buttonGap/2, buttonW, buttonH, widthReference, heightReference, "Quitter")
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
		
		for (VButton vb : buttons) {
			vp.add(vb);
			vb.setUsable(false);
			vb.setBorderSize(2*UsefulTh.pixelW);
			vb.setAddW(10);
			vb.setFontSize(35);
			vb.addVActionListener(val);
		}
		vp.setAdjustment(VComponent.ADJUSTMENT_BY_HEIGHT);
		vp.setAlignment(VComponent.CENTER_ALIGNMENT);
		vp.addMlToAComponent(play.playP);
		vp.addMmlToAComponent(play.playP);
	}
	
	public void setUsable(boolean boo) {
		for (VButton vb : buttons) {
			vb.setUsable(boo);
		}
	}
	
	public void display(Graphics2D g2d) {
		if (play.phase == Play.DEAD_PHASE) {
			g2d.setColor(new Color(44, 44, 44, 150));
			g2d.fillRect(0, 0, play.playP.getWidth(), play.playP.getHeight());
			
			vp.adjust(play.playP.getWidth(), play.playP.getHeight());
			vp.display(play.color, g2d);
		}
	}
}
