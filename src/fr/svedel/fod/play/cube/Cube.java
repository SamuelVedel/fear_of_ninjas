package fr.svedel.fod.play.cube;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.svedel.fod.UsefulTh;
import fr.svedel.fod.play.Bullet;
import fr.svedel.fod.play.Entity;
import fr.svedel.fod.play.Item;

/*  ___
 * (° °)
 *  ) (
 * (( ))
 */

/**
 * Class correspondant à tout les cube qui servirons à constuire les salles.
 * <p>
 * Class créée le 17/06/2022
 * 
 * @author Samuel Vedel
 *
 */
public class Cube {
	/*
	 * on définit les différent type de capacité.
	 * bordn est pour les différnet type de bordure
	 * le numéro correspond si on regarde le pavé numérique
	 * au type de collision que cela implique.
	 */
	public static final int NOTHING = 0;
	public static final int BORD1 = 1;
	public static final int BORD2 = 2;
	public static final int BORD3 = 3;
	public static final int BORD4 = 4;
	public static final int BORD6 = 6;
	public static final int BORD7 = 7;
	public static final int BORD8 = 8;
	public static final int BORD9 = 9;
	public static final int BORD = 10;
	public static final int BUMP = 11;
	public static final int SWITCH = 12;
	public static final int GATE = 13;
	
	// type de contact
	public static final int NO_CONTACT = 0;
	public static final int CONTACT = 1;
	public static final int UP_CONTACT = 2;
	public static final int RIGHT_CONTACT = 3;
	public static final int DOWN_CONTACT = 4;
	public static final int LEFT_CONTACT = 5;
	
	public int w, h;
	public int x, y;
	protected int[][] tex;
	public int type;
	
	protected boolean hasAction = false;
	
	/**
	 * initialise le cube
	 * 
	 * @param x les abscisses
	 * @param y les ordonnées
	 * @param w la longeur
	 * @param h la hauteur
	 * @param chTex le chemin de la texture à partir de textures\cube\
	 */
	public Cube(int x, int y, int w, int h, String chTex) {
		this(x, y, w, h, NOTHING, chTex);
	}
	
	/**
	 * initialise le cube
	 * 
	 * @param x les abscisses
	 * @param y les ordonnées
	 * @param chTex le chemin de la texture à partir de textures\cube\
	 */
	public Cube(int x, int y, String chTex) {
		this(x, y, UsefulTh.cubeW, UsefulTh.cubeH, chTex);
	}
	
	/**
	 * initialise le cube
	 * 
	 * @param x les abscisses
	 * @param y les ordonnées
	 * @param w la longeur
	 * @param h la hauteur
	 * @param capacity la capacité du cube
	 * @param chTex le chemin de la texture à partir de textures\cube\
	 */
	protected Cube(int x, int y, int w, int h, int type, String chTex) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.type = type;
		
		tex = UsefulTh.readTex("textures/cube/"+chTex);
	}
	
	public void actions(double delta) {};
	
	public boolean isAGround() {
		return false;
//		return type == BORD7 || type == BORD8 || type == BORD9 || type == BORD || type == SWITCH_ON;
	}
	
	public boolean hasActions() {
		return hasAction;
	}
	
	/**
	 * Gère les collision avec les objet de class {@code Entity}
	 * 
	 * @param e entité sur laquel on check les collision
	 */
	public int contact(Entity e) {
		if (e.x+e.w > x && e.x < x+w
			&& e.y+e.h > y && e.y < y+h) { // touché
			return touched(e);
		}
		return NO_CONTACT;
	}
	
	protected int touched(Entity e) {
		return CONTACT;
	}
	
	/**
	 * Gère les collisiosn avec les objet de class {@Bullet}
	 * 
	 * @param b balle sur laquel on check les collision
	 */
	public void contact(Bullet b) {
		if (b.x > x && b.x < x+w
			&& b.y > y && b.y < y+h) { // touché
			touched(b);
		}
	}
	
	protected void touched(Bullet b) {
		// avant quand les balles pouvait passer à travers cetain blocs comme le prso
//		if ((capacity == Cube.BORD1 || capacity == Cube.BORD4 || capacity == Cube.BORD7)
//			 && b.oldX <= x) { // choc à gauche
//			if (b.bounce <= 0) b.destroy();
//			else b.vX *= -1;
//			b.bounce --;
//		} else if ((capacity == Cube.BORD1 || capacity == Cube.BORD2 || capacity == Cube.BORD3)
//					&& b.oldY >= y+h) { // choc en bas
//			if (b.bounce <= 0) b.destroy();
//			else b.vY *= -1;
//			b.bounce --;
//		} else if ((capacity == Cube.BORD9 || capacity == Cube.BORD6 || capacity == Cube.BORD3)
//					&& b.oldX >= x+w) { // choc à droite
//			if (b.bounce <= 0) b.destroy();
//			else b.vX *= -1;
//			b.bounce --;
//		} else if ((capacity == Cube.BORD7 || capacity == Cube.BORD8 || capacity == Cube.BORD9)
//					&& b.oldY <= y) { // choc en haut
//			if (b.bounce <= 0) b.destroy();
//			else b.vY *= -1;
//			b.bounce --;
//		}
		
		// maintenant les balles ne passent à travers aucun blocs (ou presque)
		// sauf les switch off
		if (b.bounce <= 0) b.destroy();
		else {
			b.x -= b.vX;
			b.y -= b.vY;
			if (b.oldX <= x || b.oldX >= x+w) b.vX *= -1;
			else if (b.oldY >= y+h || b.oldY <= y) b.vY *= -1;
			else b.destroy();
			b.bounce --;
		}
	}
	
	public int contact(Item it) {
		if (type != Cube.NOTHING
			&& it.x+it.w > x && it.x < x+w
			&& it.y+it.h > y && it.y < y+h) { // touché
			return touched(it);
		}
		return NO_CONTACT;
	}
	
	public int touched(Item it) {
		if (isAGround() && it.oldY+it.h <= y) { // choc en haut
			it.y = y-it.h;
			return UP_CONTACT;
		}
		return NO_CONTACT;
	}
	
	/**
	 * affiche le cube
	 * 
	 * @param c2 seconde couleur
	 * @param g2d {@code Graphics2D} sur lequel on le dessine
	 */
	public void display(Color c2, Graphics2D g2d) {
		UsefulTh.displayTex(tex, x, y, w, h, c2, g2d);
	}
}
