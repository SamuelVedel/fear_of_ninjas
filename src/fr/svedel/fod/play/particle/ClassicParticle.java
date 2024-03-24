package fr.svedel.fod.play.particle;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.svedel.fod.UsefulTh;

public class ClassicParticle extends Particle {
	
	private double w, h;
	private double x, y;
	private double vX, vY;
	private double aY = 0.1/5*UsefulTh.pixelH;
	
//	private double tNarrowing;
	/** temps pour que {@code w} et {@code h} diminuent de 1 */
//	private int narrowing = 10;
	
	private int[][] tex = null;
	
	/**
	 * Initialise la particule
	 * 
	 * @param x abscisses de la particule
	 * @param y ordoonées de la particule
	 * @param w largeur de la particule
	 * @param h hauteur de la particule
	 * @param vX vitesse des abscisses de la particule
	 * @param vY vitesse des ordonnées de la particule
	 * @param chTex
	 */
	public ClassicParticle(double x, double y, int w, int h, double vX, double vY, String chTex) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.vX = vX;
		this.vY = vY;
		if (chTex != null) {
			tex = UsefulTh.readTex(chTex);
		}
	}
	
	public ClassicParticle(double x, double y, int w, int h, double vX, double vY) {
		this(x, y, w, h, vX, vY, null);
	}
	
	/**
	 * Effecute les action de la particule
	 */
	@Override
	public void action(double delta) {
		// gère le déplacement
		x += vX*delta;
		y += vY*delta;
		vY += aY*delta;
		
		// gère le rétrécissement
//		if (tNarrowing >= narrowing) {
			w *= Math.pow(0.75, 0.1*delta);
			h *= Math.pow(0.75, 0.1*delta);
			if ((int)w == 0 || (int)h == 0) {
				noMoreReasonToBe = true;
			}
//			tNarrowing = 0;
//		}
//		tNarrowing += delta;
	}
	
	/**
	 * Affiche la particule
	 * 
	 * @param c couleur de la particule
	 * @param g2d {@code Graphics2D} sur lequel il dessine
	 */
	@Override
	public void display(Color c, Graphics2D g2d) {
		g2d.setColor(c);
		if (tex == null) {
			g2d.fillRect((int)x, (int)y, (int)w, (int)h);
		} else {
			Graphics2D g2d2 = (Graphics2D) g2d.create();
//			g2d2.translate
			g2d2.translate(x, y);
			g2d2.scale((double)w/tex[0].length, (double)h/tex.length);
//			System.out.println((double)tex[0].length/w+" "+(double)tex.length/h);
			UsefulTh.displayTex(tex, 0, 0, tex[0].length, tex.length, c, g2d2);
			g2d2.dispose();
		}
	}
}
