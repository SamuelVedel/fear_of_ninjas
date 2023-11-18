package fr.SamuelVedel.Play.Enemy;

import java.awt.Graphics2D;

import fr.SamuelVedel.FOD.UsefulTh;
import fr.SamuelVedel.Play.Room;

/**
 * Class créée le 28/08/2022 (très tôt)
 * 
 * @author Samuel Vedel
 *
 */
public class Ninja extends Enemy {
	
	private int[][][] textures = new int[6][12][6];
	private int iTex = 0;
	private double tTex = 0;
	/** temps entre deux changement de textures quand on marche */
	private final int vTex = 5;
	
	public Ninja(Room room) {
		super(room);
		type = NINJA_TYPE;
		name = "ninja";
		initVar();
		takeEnemiesPowers();
		initTextures();
		spawnToWalk();
	}
	
	private void initVar() {
		w = UsefulTh.cubeW;
		h = UsefulTh.cubeH*2;
		v = 4.5;
		maxLife = 20;
		life = maxLife;
		punchDamage = 30;
	}
	
	private void initTextures() {
		for (int i = 0; i < textures.length/2; i++) {
			textures[i] = UsefulTh.readTex("textures/enemies/ninja/ninja"+i+".txt");
		}
		for (int i = textures.length/2; i < textures.length; i++) {
			textures[i] = UsefulTh.reverseXTex(textures[i-3]);
		}
	}
	
	@Override
	public void actions(double delta) {
		super.actions(delta);
		
		actionToWalk(delta);
		
		// gère l'animation de marche
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
		
		// gère le suicide
		if (room.me.x+room.me.w > x && room.me.x < x+w
			&& room.me.y+room.me.h > y && room.me.y < y+h) { // touché
			if (UsefulTh.rand.nextInt(100) < critChance) {
				room.me.takeDammage(punchDamage*2, this);
			} else {
				room.me.takeDammage(punchDamage, this);
			}
			die(this);
		}
		
		endOfActions(delta);
	}
	
	@Override
	public void display(Graphics2D g2d) {
		displayLife(g2d);
		UsefulTh.displayTex(textures[iTex], (int)x-(iTex <= 2? 2*UsefulTh.pixelW : 0), (int)y, w+2*UsefulTh.pixelW, h, play.color, g2d);
	}
}
