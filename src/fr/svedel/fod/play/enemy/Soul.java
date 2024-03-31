package fr.svedel.fod.play.enemy;

import java.awt.Graphics2D;

import fr.svedel.fod.UsefulTh;
import fr.svedel.fod.play.Entity;
import fr.svedel.fod.play.Room;
import fr.svedel.fod.play.particle.ClassicParticle;

/**
 * Class créée le 14/01/2023
 * 
 * @author Samuel Vedel
 *
 */
public class Soul extends Enemy {
	
	private int[][][] textures = new int[8][10][6];
	private int iTex = 0;
	private double tTex = 0;
	/** temps entre deux changement de textures quand on marche */
	private final int vTex = 10;
	
	public Soul(double x, double y, Room room) {
		super(room);
		type = SOUL_TYPE;
		name = "âme";
		canDieWithASoul = false;
		initVar();
		takeEnemiesPowers();
		initTextures();
		this.x = x;
		this.y = y;
		if (collision()) {
			tp();
		}
	}
	
	public Soul(Room room) {
		this(0, 0, room);
		spawnToFly();
	}
	
	private void initVar() {
		alpha = UsefulTh.getAlpha(x, y, room.me.x, room.me.y);
		w = UsefulTh.CUBE_W;
		h = 10*UsefulTh.PIXEL_H;
		v = 2./5*UsefulTh.PIXEL_W;
		maxLife = 30;
		life = maxLife;
		cadence = 60;
		punchCadence = 60;
	}
	
	private void initTextures() {
		for (int i = 0; i < textures.length/2; i++) {
			textures[i] = UsefulTh.readTex("textures/enemies/soul/soul"+i+".texture");
		}
		for (int i = textures.length/2; i < textures.length; i++) {
			textures[i] = UsefulTh.reverseXTex(textures[3-(i-4)]);
		}
	}
	
	@Override
	public void actions(double delta) {
		super.actions(delta);
		if (y >= room.height) tp();
		
		// gère l'animation de marche
		if (tTex >= vTex) {
			if (v*Math.cos(alpha) >= 0) {
				iTex++;
				if (iTex > 3) iTex = 0;
			} else {
				iTex++;
				if (3 >= iTex || iTex >= textures.length) iTex = 4;
			}
			tTex -= vTex;
		}
		tTex += delta;
		
		actionToFly(delta);
		
		// attaque
		punch(delta);
		shoot(target, delta);
		
//		if (UsefulTh.rand.nextInt(300) == 0) {
		if (UsefulTh.rand.nextDouble() < (double)1/300*delta) {
			tp(/*bonjour*/);
		}
		
		endOfActions(delta);
	}
	
	public void tp() {
		if (alive) {
			// effet de particule
			int nParticles = (w+h/2);
			for (int i = 0; i < nParticles; i++) {
				double pX = x+UsefulTh.rand.nextInt(w);
				double pY = y+UsefulTh.rand.nextInt(h);
				double pVX = (UsefulTh.rand.nextBoolean()? 1 : -1)*UsefulTh.rand.nextDouble()
					*0.2*UsefulTh.PIXEL_W;
				double pVY = (UsefulTh.rand.nextBoolean()? 1 : -1)*UsefulTh.rand.nextDouble()
					*0.2*UsefulTh.PIXEL_H;
				room.particles.add(new ClassicParticle(pX, pY, UsefulTh.PIXEL_W/2, UsefulTh.PIXEL_H/2, pVX, pVY));
			}
			
			//téléportation
			double theta = 0;
			double r = 0;
			for (boolean boo = true; boo;) {
				x -= r*Math.cos(theta);
				y -= r*Math.sin(theta);
				theta = 2*Math.PI*UsefulTh.rand.nextDouble();
				r = (UsefulTh.rand.nextInt(15-2)+2)*UsefulTh.CUBE_W;
				x += r*Math.cos(theta);
				y += r*Math.sin(theta);
				
				if (collision() || Math.sqrt(Math.pow(x+w/2-(room.me.x+room.me.w/2), 2)+Math.pow(y+h/2-(room.me.y+room.me.h/2), 2)) < 5*UsefulTh.CUBE_W
					|| x < 0 || y < 0 || x+w > room.width || y+h > room.height) {
					boo = true;
				} else boo = false;
			}
		}
	}
	
	@Override
	public void takeDammage(int dammage, Entity e) {
		super.takeDammage(dammage, e);
		tp();
	}
	
	@Override
	public void display(Graphics2D g2d) {
		displayLife(g2d);
		UsefulTh.displayTex(textures[iTex], (int)x, (int)y, w, h, play.color, g2d);
	}
}
