package fr.svedel.fod.play.enemy;

import java.awt.Graphics2D;

import fr.svedel.fod.UsefulTh;
import fr.svedel.fod.play.Room;

public class ShieldMan extends Enemy {
	
	private int[][][] textures = new int[6][12][6];
	private int iTex = 0;
	private double tTex = 0;
	/** temps entre deux changement de textures quand on marche */
	private final int vTex = 10;
	
	public ShieldMan(Room room) {
		super(room);
		type = SHIELD_MAN_TYPE;
		name = "mec avec un bouclier";
		initVar();
		takeEnemiesPowers();
		initTextures();
		spawnToWalk();
		
		if (Math.sqrt(Math.pow(room.me.x-x, 2)+Math.pow(room.me.y-y, 2)) <= viewDistance) {
			if (room.me.x > x) direction = 1;
			else direction = -1;
		}
	}
	
	private void initTextures() {
		for (int i = 0; i < textures.length/2; i++) {
			textures[i] = UsefulTh.readTex("textures/enemies/shieldMan/shieldMan"+i+".texture");
		}
		for (int i = textures.length/2; i < textures.length; i++) {
			textures[i] = UsefulTh.reverseXTex(textures[i-3]);
		}
	}
	
	private void initVar() {
		w = UsefulTh.CUBE_W+2*UsefulTh.PIXEL_W;
		h = UsefulTh.CUBE_H*2;
		v = 2./5*UsefulTh.PIXEL_W;
		maxLife = 40;
		life = maxLife;
		punchDamage = 20;
		punchCadence = 90;
	}
	
	@Override
	public void actions(double delta) {
		super.actions(delta);
		
		followTarget = false;
		actionToWalk(delta);
		
		// g�re l'animation de marche
		if (tTex >= vTex) {
			if (direction > 0) {
				iTex++;
				if (iTex > 2) iTex = 1;
			} else if (direction < 0) {
				iTex++;
				if (2 >= iTex || iTex >= textures.length) iTex = 4;
			} else {
				if (iTex <= 2) iTex = 0;
				else iTex = 3;
			}
			tTex -= vTex;
		}
		tTex += delta;
		
		// attaque
		punch(delta);
		
		endOfActions(delta);
	}
	
	@Override
	public void display(Graphics2D g2d) {
		displayLife(g2d);
		UsefulTh.displayTex(textures[iTex], (int)x, (int)y, w, h, play.color, g2d);
	}
}
