package fr.SamuelVedel.Play.Particle;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Particules qui tombe, rétrécit, puis disparait
 * <p>
 * Class créée le 28/06/2022
 * 
 * @author Samuel Vedel
 *
 */
public abstract class Particle {
	
	public boolean noMoreReasonToBe = false;
	
	/**
	 * Effecute les action de la particule
	 */
	public abstract void action(double delta);
	
	/**
	 * Affiche la particule
	 * 
	 * @param c couleur de la particule
	 * @param g2d {@code Graphics2D} sur lequel il dessine
	 */
	public abstract void display(Color c, Graphics2D g2d);

}
