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
 * code une cr�ature noir que te frappe et tire
 * � petite cadance
 * <p>
 * Classe cr��e le 16/08/2022
 * 
 * @author Samuel Vedel
 *
 */
public class DarkGuy extends Enemy {
	
	private int[][][] textures = new int[6][12][6];
	private int iTex = 0;
	private double tTex = 0;
	/** temps entre deux changement de textures quand on marche */
	private final int vTex = 10;
	
	public DarkGuy(Room room) {
		super(room);
		type = DARK_GUY_TYPE;
		name = "mec sombre";
		initVar();
		takeEnemiesPowers();
		initTextures();
		spawnToWalk();
	}
	
	private void initVar() {
		w = UsefulTh.CUBE_W;
		h = UsefulTh.CUBE_H*2;
		v = 2./5*UsefulTh.PIXEL_W;
		maxLife = 30;
		life = maxLife;
		cadence = 120;
		punchCadence = 60;
	}
	
	private void initTextures() {
		for (int i = 0; i < textures.length/2; i++) {
			textures[i] = UsefulTh.readTex("textures/enemies/darkGuy/darkGuy"+i+".texture");
		}
		for (int i = textures.length/2; i < textures.length; i++) {
			textures[i] = UsefulTh.reverseXTex(textures[i-3]);
		}
	}
	
	@Override
	public void actions(double delta) {
		super.actions(delta);
		
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
		shoot(target, delta);
		
		endOfActions(delta);
	}
	
	@Override
	public void display(Graphics2D g2d) {
		displayLife(g2d);
		UsefulTh.displayTex(textures[iTex], (int)x, (int)y, w, h, play.color, g2d);
	}
}
