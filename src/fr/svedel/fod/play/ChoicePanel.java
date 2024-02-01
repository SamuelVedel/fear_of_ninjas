package fr.svedel.fod.play;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import fr.svedel.fod.UsefulTh;
import fr.svedel.vcomponent.VActionListener;
import fr.svedel.vcomponent.VComponent;
import fr.svedel.vcomponent.VPanel;

/**
 * 
 * Class créée le 22/08/2022
 * 
 * @author Samuel Vedel
 *
 */
public class ChoicePanel {
	
	private Play play;
	
	private ChoiceButton[] choices = new ChoiceButton[3];
	
	public Power finalChoice = null;
	
	private double percentOnScreen = 0;
	private double v = 0;
	private double a = 0.50;
	
	private final int buttonW = 150;
	private final int buttonH = 300;
	private final int widthReference = 800;
	private final int heightReference = 600;
	
	private VPanel vp = new VPanel(0, 35, widthReference, heightReference, widthReference, heightReference);
	
	public VActionListener val = new VActionListener() {
		
		@Override
		public void action(VComponent source, MouseEvent e) {
			if (finalChoice == null) {
				for (ChoiceButton cb : choices) {
					cb.setUsable(false);
					if (source == cb) {
						finalChoice = cb.pow;
						/*break;*/
					}
				}
			}
		}
	};
	
	public ChoicePanel(Play play) {
		this.play = play;
		
		vp.setAdjustment(VComponent.ADJUSTMENT_BY_WIDTH);
		vp.setAlignment(VComponent.CENTER_ALIGNMENT);
		vp.addMlToAComponent(play.playP);
		vp.addMmlToAComponent(play.playP);
		for (int i = 0; i < choices.length; i++) {
			choices[i] = new ChoiceButton(widthReference/4*(i+1)-buttonW/2, heightReference/2-buttonH/2, buttonW, buttonH, widthReference, heightReference/*, play*/);
			choices[i].addVActionListener(val);
			choices[i].setAdjustment(VComponent.ADJUSTMENT_BY_WIDTH);
			choices[i].setAlignment(VComponent.CENTER_ALIGNMENT);
			choices[i].setUsable(false);
			vp.add(choices[i]);
		}
		
		this.refresh();
	}
	
	public void actions(double delta) {
		if (play.phase == Play.CHOICE_PHASE && percentOnScreen < 100) {
			percentOnScreen += v*delta;
			v += a*delta;
		} else if (play.phase == Play.PLAY_PHASE && percentOnScreen > 0) {
			percentOnScreen += v*delta;
			v -= a*delta;
			if (percentOnScreen <= 0) {
				percentOnScreen = 0;
				v = 0;
			}
		}
		if (percentOnScreen > 100) {
			percentOnScreen = 100;
			v = 0;
		}
	}
	
	public void refresh() {
		for (int i = 0; i < choices.length; i++) {
			ChoiceButton cb = choices[i];
			boolean isDoubled = true; // pour savoir si il y à deux pouvoir pareil
			while (isDoubled) {
				cb.pow = Power.getRandPowerForMe();
				isDoubled = false;
				for (int i2 = 0; i2 < i; i2++) {
					if (choices[i2].pow == cb.pow) isDoubled = true;
				}
			}
			cb.setUsable(true);
		}
		finalChoice = null;
	}
	
	public void display(Graphics2D g2d) {
		if (percentOnScreen > 0) {
			g2d.translate(0, -play.playP.getHeight()*(100-percentOnScreen)/100);
			
			g2d.setColor(UsefulTh.BACKGROUND_COLOR);
			g2d.fillRect(0, 0, play.playP.getWidth(), play.playP.getHeight());
			
			vp.adjust(play.playP.getWidth(), play.playP.getHeight());
			vp.display(play.color, g2d);
			
			g2d.setColor(play.color);
			g2d.setFont(new Font("ARIAL", Font.BOLD, (int)(50*play.scaleW)));
			String text = "Choisi un pouvoir";
			int textW = UsefulTh.getTextW(text, g2d);
			int textH = UsefulTh.getTextH(text, g2d);
			UsefulTh.drawString(text, play.playP.getWidth()/2-textW/2, (int)(60*play.scaleW)+textH/2, g2d);
			
			g2d.translate(0, play.playP.getHeight()*(100-percentOnScreen)/100);
		}
	}
}
