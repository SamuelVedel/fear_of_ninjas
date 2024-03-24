package fr.svedel.fod.play.enemy;

import java.awt.Graphics2D;

import fr.svedel.fod.UsefulTh;
import fr.svedel.fod.play.Bullet;
import fr.svedel.fod.play.Entity;
import fr.svedel.fod.play.Room;

/**
 * Class créée le 28/06/2022 (aussi très tôt)
 * 
 * @author Samuel Vedel
 *
 */
public class Blob extends Enemy {
	
	private int[][] texture;
	
	private double tContact = 0;
	private final int maxTContact = 10;
	
	public Blob(Room room) {
		super(room);
		type = BLOB_TYPE;
		name = "blob";
		initVar();
		takeEnemiesPowers();
		spawnToFly();
		initTexture();
	}
	
	private void initVar() {
		alpha = UsefulTh.getAlpha(x, y, target.x, target.y);
		w = UsefulTh.cubeW+UsefulTh.pixelW;
		h = UsefulTh.cubeH+UsefulTh.pixelH;
		v = 3./5*UsefulTh.pixelW;
		maxLife = 20;
		life = maxLife;
		cadence = 60;
	}
	
	private void initTexture() {
		texture = UsefulTh.readTex("textures/enemies/blob/blob.texture");
	}
	
	@Override
	public void actions(double delta) {
		super.actions(delta);
		
		actionToFly(delta);
		
		if (target.x+target.w > x && target.x < x+w
			&& target.y+target.h > y && target.y < y+h) { // touché
			if (tContact >= maxTContact) die(this);
			tContact += delta;
		} else {
			tContact = 0;
		}
		
		// tire dans une de ses 8 direction aléatoirement
		if (tShoot >= cadence) {
			double alpha = Math.PI/2+UsefulTh.rand.nextInt(8)*2*Math.PI/8;
			room.bullets.add(new Bullet(x+w/2-bulletW/2, y+h/2-bulletH/2, alpha, this));
			tShoot -= cadence;
		}
		tShoot += delta;
		
		endOfActions(delta);
	}
	
	@Override
	public void die(Entity killer) {
		if (killer != this) canDieWithDeathsBullets = false;
		super.die(killer);
		// waw c'est bien foutu
		int nBullet = 8;
		if (/*room.me.x+room.me.w > x && room.me.x < x+w
			&& room.me.y+room.me.h > y && room.me.y < y+h*/
			killer == this) { // touché
			nBullet = 4;
		}
		
		for (int i = 0; i < nBullet; i++) {
			room.bullets.add(new Bullet(x+w/2-bulletW/2, y+h/2-bulletH/2, Math.PI/4+i*2*Math.PI/nBullet, this));
		}
	}
	
//	@Override
//	public boolean lookToTheRight() {
//		return v*Math.cos(alpha) >= 0;
//	}
	
	@Override
	public void display(Graphics2D g2d) {
		displayLife(g2d);
		UsefulTh.displayTex(texture, (int)x, (int)y, w, h, play.color, g2d);
	}
}
