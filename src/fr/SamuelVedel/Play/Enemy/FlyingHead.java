package fr.SamuelVedel.Play.Enemy;

import java.awt.Graphics2D;

import fr.SamuelVedel.FOD.UsefulTh;
import fr.SamuelVedel.Play.Room;

/*  ___
 * (° °)
 *  ) (
 * (( ))
 */

/**
 * C'est un énemie, et enfaite, c'est
 * une tête qui vole, enfin comme dans
 * son nom quoi
 * <p>
 * Class créée le 28/06/2022 (mais pas à 2h cette fois)
 * 
 * @author Samuel Vedel
 *
 */
public class FlyingHead extends Enemy {
	
	private int[][][] textures = new int[2][6][6];
	private int iTex = 0;
	
	public FlyingHead(Room room) {
		super(room);
		type = FLYING_HEAD_TYPE;
		name  = "tête volante";
		initVar();
		spawnToFly();
		takeEnemiesPowers();
		initTextures();
	}
	
	private void initVar() {
		alpha = UsefulTh.getAlpha(x, y, room.me.x, room.me.y);
		w = UsefulTh.cubeW;
		h = UsefulTh.cubeH;
		v = 2;
		maxLife = 20;
		life = maxLife;
		cadence = 60;
		punchCadence = 60;
	}
	
	private void initTextures() {
		textures[0] = UsefulTh.readTex("textures/enemies/flyingHead/flyingHead.texture");
		textures[1] = UsefulTh.reverseXTex(textures[0]);
	}
	
	@Override
	public void actions(double delta) {
		super.actions(delta);
		
		// gère le sens de la texture
		if (v*Math.cos(alpha) >= 0) iTex = 0;
		else iTex = 1;
		actionToFly(delta);
		
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
