package fr.svedel.fod.play;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import fr.svedel.fod.UsefulTh;

/**
 * Class héritant de {@code JPanel} servant à
 * dessiner sur le {@code JFrame} de {@ode Play}
 * <p>
 * Class créée le 17/06/2022
 * 
 * @author Samuel Vedel
 *
 */
public class PlayPainter extends JPanel {
	private static final long serialVersionUID = -7369176626068358885L;
	
	private Play play;
	
//	private BufferedImage background = new BufferedImage(1, 600, BufferedImage.TYPE_INT_ARGB);
	
	public PlayPainter(Play play) {
		this.play = play;
//		initBackground();
	}
	
//	private void initBackground() {
//		Graphics2D g2d = (Graphics2D) background.createGraphics();
//		int c1 = 44;
//		int c2 = 0;
//		for (int i = 0; i < background.getHeight(); i++) {
//			double pow = 2;
//			int c3 = c1+(int)Math.pow(i, pow)*(c2-c1)/(int)Math.pow(background.getHeight(), pow);
//			g2d.setColor(new Color(c3, c3, c3));
//			g2d.fillRect(0, i, 1, 1);
//		}
//	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		// fond
		g2d.setColor(UsefulTh.BACKGROUND_COLOR);
//		g2d.setPaint(new GradientPaint(0, 0, play.color2, 0, getHeight(), UsefulTh.BACKGROUND_COLOR));
		g2d.fillRect(0, 0, play.playP.getWidth(), play.playP.getHeight());
//		g2d.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		
//		Color c2 = new Color(play.color.getRed(), play.color.getGreen(), play.color.getBlue(), 5);
//		g2d.setColor(c2);
//		g2d.fillRect(0, 0, getWidth(), (int)(getHeight()*0.6));
//		c2 = new Color(play.color.getRed(), play.color.getGreen(), play.color.getBlue(), 10);
//		g2d.setColor(c2);
//		g2d.fillRect(0, 0, getWidth(), (int)(getHeight()*0.2));
		
		play.scaleW = (double)getHeight()/UsefulTh.HEIGHT_FOR_NO_SCALE;
		
		play.room.display(g2d);
		play.choiceP.display(g2d);
		UsefulTh.displayPowerList(play.room.me.powers, false, false, play, g2d);
		UsefulTh.displayPowerList(play.room.enemiesPowers, true, true, play, g2d);
		
		play.endS.display(g2d);
		
		UsefulTh.displayTex(UsefulTh.cursor, (int)(play.mouseX-UsefulTh.cursorW*play.scaleW/2), (int)(play.mouseY-UsefulTh.cursorH*play.scaleW/2), (int)(UsefulTh.cursorW*play.scaleW), (int)(UsefulTh.cursorH*play.scaleW), play.color, g2d);
		
		g2d.dispose();
	}
}
