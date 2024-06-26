package fr.svedel.fod.play.enemy;

import java.awt.Graphics2D;

import fr.svedel.fod.UsefulTh;
import fr.svedel.fod.play.Room;

/*  ___
 * (� �)
 *  ) (
 * (( ))
 */

/**
 * Ennemie qui invoque des petite t�te
 * <p>
 * Class cr��e le 24/12/2022 (Veille de Noel) vers 00h20
 * 
 * @author Samuel Vedel
 *
 */
public class Summoner extends Enemy {
	
	private int[][][] textures = new int[6][][]/*[12][6]*/;
	private int iTex = 0;
	private double tTex = 0;
	/** temps entre deux changement de textures quand on marche */
	private final int vTex = 8;
	
	public int currentNumOfHead = 0;
	private int maxNumOfHead = 10;
	
	public Summoner(Room room) {
		super(room);
		type = SUMMONER_TYPE;
		name = "invocateur";
		initVar();
		takeEnemiesPowers();
		spawnToWalk();
		initTextures();
	}
	
	private void initVar() {
		w = UsefulTh.CUBE_W;
		h = UsefulTh.CUBE_H*2;
		v = 1./5*UsefulTh.PIXEL_W;
		maxLife = 100;
		life = maxLife;
		cadence = 150;
		tShoot = 1;
		punchDamage = 15;
		punchCadence = 60;
	}
	
	private void initTextures() {
		for (int i = 0; i < textures.length/2; i++) {
			textures[i] = UsefulTh.readTex("textures/enemies/summoner/summoner"+i+".texture");
		}
		for (int i = textures.length/2; i < textures.length; i++) {
			textures[i] = UsefulTh.reverseXTex(textures[i-3]);
		}
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
		// g�re l'invocation
		if (tShoot >= cadence) {
			if (currentNumOfHead < maxNumOfHead) {
				double x2;
				if (iTex <= 2) {
					x2 = x-5*UsefulTh.PIXEL_W;
				} else {
					x2 = x+w;;
				}
				room.enemies.add(new LilHead(x2, y+4*UsefulTh.PIXEL_H, this, room));
				currentNumOfHead++;
			}
			tShoot = 0;
		}
		tShoot += delta;
		
		endOfActions(delta);
	}
	
	@Override
	public void display(Graphics2D g2d) {
		displayLife(g2d);
		// une partie de la texture n'est pas comprise dans la hit box
		int wTex = w+6*UsefulTh.PIXEL_W;
		int hTex = h+UsefulTh.PIXEL_H;
		if (iTex <= 2) {
			UsefulTh.displayTex(textures[iTex], (int)x-5*UsefulTh.PIXEL_W, (int)y-UsefulTh.PIXEL_H, wTex, hTex, play.color, g2d);
		} else {
			UsefulTh.displayTex(textures[iTex], (int)x-UsefulTh.PIXEL_W, (int)y-UsefulTh.PIXEL_H, wTex, hTex, play.color, g2d);
		}
	}
}
