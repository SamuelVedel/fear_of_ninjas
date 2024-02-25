package fr.svedel.vcomponent;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

public abstract class UsefulTh {
	
	/**
	 * Retourne la largeur d'un texte
	 * 
	 * @param text texte en question
	 * @param g2d <code>Graphics2D</code> qui sert à connaitre le <code>Font</code> à étudier
	 * @return la largeur du texte
	 */
	protected static int getTextW(String text, Graphics2D g2d) {
		FontMetrics fm = g2d.getFontMetrics();
		Rectangle2D textBounds = fm.getStringBounds(text, g2d);
		return (int) textBounds.getWidth();
	}
	
	/**
	 * <p>Retourne la hauteur d'un texte, mais à un décalge par rapport à la réalité <br>
	 * il faut avec la police Arial enlevé la taille de la police*20/50.</p>
	 * 
	 * <p>J'aimerais bien faire des test pour plein de police histoie de tout avoir
	 * facilement</p>
	 * 
	 * @param text texte en question
	 * @param g2d <code>Graphics2D</code> qui sert à connaitre le <code>Font</code> à étudier
	 * @return la hauteur du texte
	 */
	protected static int getTextH(String text, Graphics2D g2d) {
		FontMetrics fm = g2d.getFontMetrics();
		Rectangle2D textBounds = fm.getStringBounds(text, g2d);
		return (int) textBounds.getHeight()-g2d.getFont().getSize()*20/50;
	}
	
	public static void drawString(String text, int x, int y, Graphics2D g2d) {
		Graphics2D g2d2 = (Graphics2D) g2d.create();
		g2d2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d2.drawString(text, x, y);
		g2d2.dispose();
	}
}
