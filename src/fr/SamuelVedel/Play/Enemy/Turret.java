package fr.SamuelVedel.Play.Enemy;

import java.awt.Color;
import java.awt.Graphics2D;
//import java.awt.RenderingHints;

import fr.SamuelVedel.FOD.UsefulTh;
import fr.SamuelVedel.Play.Bullet;
import fr.SamuelVedel.Play.Entity;
import fr.SamuelVedel.Play.Power;
import fr.SamuelVedel.Play.Room;
import fr.SamuelVedel.Play.Cube.Cube;

/*  ___
 * (° °)  <(hey, ceci est une tourelle)
 *  ) (
 * (( ))
 */

/**
 * tourelle qui correspond à la tourelle du pouvoir
 * tourelle fidèle
 * <p>
 * Class créée le 21/01/2023 
 * 
 * @author Samuel Vedel
 *
 */
public class Turret extends Enemy {
	
//	private int[][] headTex = UsefulTh.readTex("textures/enemies/turret/turretHead.texture");
	private int[][] feetTex = UsefulTh.readTex("textures/enemies/turret/turretFeet.texture");
	
	/**
	 * pourcentage qui définit la façon dont le canon
	 * est enfoncé dans la tête de la tourelle pour l'animation
	 * de tire :
	 * <ul>
	 * <li>si {@code cannonIn = 0} le canon n'est pas dans la tête</li>
	 * <li>si {@code cannonIn = 100} la cannon est enfoncer de un pixel dans la tourelle</li>
	 */
	private double cannonIn = 0;
	/** vitesse du canon pour l'animation */
	private double vCannon = 0;
	/** accélération du canon pour l'animation */
	private int aCannon = -15;
	
	public Turret(Entity owner, Room room) {
		super(room);
		type = TURRET_TYPE;
		name = "tourelle";
		clan = owner.clan;
		initVar();
		for (int i = 0; i < room.me.powers.length; i++) {
			if (i != Power.TURRET.id) {
				for (int j = 0; j < room.me.powers[i]; j++) {
					addPower(Power.values()[i]);
				}
			}
		}
		spawnToWalk();
	}
	
	private void initVar() {
		w = feetTex[0].length*UsefulTh.pixelW;
		h = feetTex.length*UsefulTh.pixelH;
		maxLife = 50;
		life = maxLife;
		cadence = 60;
	}
	
	protected void spawnToWalk() {
		// modifié pour qu'il puisse apparaitre près de moi
		for (int iX = 0, iY = 0;; iX = UsefulTh.rand.nextInt(room.cubes[0].length), iY = UsefulTh.rand.nextInt(room.cubes.length)) {
			if(room.cubes[iY][iX] != null) {
				Cube c = room.cubes[iY][iX];
				if (c.isAGround()) { // c'est un sol
					x = iX*UsefulTh.cubeW;
					y = iY*UsefulTh.cubeH-h;
					if (!collision()) break;
				}
			}
		}
	}
	
	@Override
	public void actions(double delta) {
		regeneration(delta);
		
//		y += vFall;
//		vFall += aFall;
//		if (y >= room.height) die(this);
//		collision();
		
		// trouve l'énemies le plus proche
		Enemy closer = null;
		double d = -1;
		for (int i = 0; i < room.enemies.size(); i++) {
			Enemy en = room.enemies.get(i);
			if (en.clan != clan) {
				double d2 = Math.sqrt(Math.pow(x+w/2-en.x-en.w/2, 2)+Math.pow(y-en.y-en.h/2, 2));
				if (d2 < d || d < 0) {
					closer = en;
					d = d2;
				}
			}
		}
		if (closer != null && d <= viewDistance) {
			double x1 = x+w/2;
			double y1 = y;
			double x2 = closer.x+closer.w/2;
			double y2 = closer.y+closer.h/2;
			if (x1 != x2 || y1 != y2) {
				alpha = UsefulTh.getAlpha(x1, y1, x2, y2);
			}
		}
		
		// gère l'animation de tire
		if (cannonIn > 0) {
			cannonIn += vCannon*delta;
			vCannon += aCannon*delta;
		} else if (cannonIn < 0) cannonIn = 0;
		
		// tir sur cette énemie
		if (closer != null  && d <= viewDistance) {
			if (tShoot >= cadence) {
				room.bullets.add(new Bullet(x+w/2-bulletW/2, y-bulletH/2, alpha, this));
				cannonIn = 100;
				vCannon = 0;
				tShoot -= cadence;
			}
			tShoot += delta;
		} else if (tShoot < cadence ){
			tPunch += delta;
			if (tPunch > punchCadence) tPunch = punchCadence;
		}
	}
	
	public void die(Entity killer) {
		super.die(killer);
		room.cht.addText("Une tourelle à était cassé par un(e) "+killer.name);
	}
	
	public void display(Graphics2D g2d) {
		// affiche la vie
		int lW = w+2*UsefulTh.pixelW;
		int lH = 3*UsefulTh.pixelH;
		displayLife((int)(x+w/2-lW/2), (int)(y-1.3*lH)-2*UsefulTh.pixelH, lW, lH, UsefulTh.pixelW, g2d);
		
		// affiche les pieds
		UsefulTh.displayTex(feetTex, (int)x, (int)y, w, h, play.color, g2d);
		
		// affiche la tête
		Graphics2D g2d2 = (Graphics2D) g2d.create();
//		if (alpha != 0 && alpha != Math.PI) {
//			g2d2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		}
		g2d2.rotate(alpha, x+w/2, y);
		int headW = 5*UsefulTh.pixelW;
		int headH = 4*UsefulTh.pixelW;
		int gunW = 4*UsefulTh.pixelW;
		int gunH = 2*UsefulTh.pixelW;
		g2d2.setColor(play.color);
		g2d2.fillRect((int)x+w/2-headW/2, (int)y-headH/2, headW, headH);
		g2d2.fillRect((int)x+w/2+headW/2-UsefulTh.pixelW-UsefulTh.pixelW*(int)cannonIn/100, (int)y-headH/2+UsefulTh.pixelH, gunW, gunH);
		g2d2.setColor(Color.BLACK);
		g2d2.fillRect((int)x+w/2-headW/2+UsefulTh.pixelW, (int)y-headH/2+UsefulTh.pixelH, headW-2*UsefulTh.pixelW, headH-2*UsefulTh.pixelH);
//		int headW = headTex[0].length*UsefulTh.pixelW;
//		int headH = headTex.length*UsefulTh.pixelH;
//		UsefulTh.displayTex(headTex, (int)(x+w/2-(headW-3*UsefulTh.pixelW)/2), (int)(y-headH/2), headW, headH, play.color, g2d2);
		g2d2.dispose();
	}
}
