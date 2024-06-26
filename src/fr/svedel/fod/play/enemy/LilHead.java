package fr.svedel.fod.play.enemy;

import java.awt.Graphics2D;

import fr.svedel.fod.UsefulTh;
import fr.svedel.fod.play.Entity;
import fr.svedel.fod.play.Room;

/*  ___
 * (� �)
 *  ) (
 * (( ))
 */

/**
 * Ennemie invoqu� par l'invocateur (summoner)
 * <p>
 * Class cr��e le 24/12/2022 vers 00h20
 * 
 * @author Samuel Vedel
 *
 */
public class LilHead extends Enemy {
	
	private Summoner father;
	
	private int[][][] textures = new int[2][6][6];
	private int iTex = 0;
	
	public LilHead(double x, double y, Summoner father, Room room) {
		super(room);
		type = LIL_HEAD_TYPE;
		name = "petite t�te";
		dieWithAnIncrementationOfScore = false;
		this.x = x;
		this.y = y;
		this.father = father;
		initVar();
		takeEnemiesPowers();
		initTextures();
	}
	
	public LilHead(Room room) {
		this(0, 0, null, room);
		spawnToFly();
	}
	
	private void initVar() {
		alpha = UsefulTh.getAlpha(x, y, room.me.x, room.me.y);
		w = 5*UsefulTh.PIXEL_W;
		h = 5*UsefulTh.PIXEL_H;
		v = 2./5*UsefulTh.PIXEL_W;
		maxLife = 10;
		life = maxLife;
		cadence = 60;
	}
	
	private void initTextures() {
		textures[0] = UsefulTh.readTex("textures/enemies/lilHead/lilHead.texture");
		textures[1] = UsefulTh.reverseXTex(textures[0]);
	}
	
	@Override
	public void actions(double delta) {
		super.actions(delta);
		
		// g�re le sens de la texture
		if (/*lookToTheRight()*/v*Math.cos(alpha) >= 0) iTex = 0;
		else iTex = 1;
		actionToFly(delta);
		
		// attaque
		shoot(target, delta);
		
		endOfActions(delta);
	}
	
//	@Override
//	public boolean lookToTheRight() {
//		return v*Math.cos(alpha) >= 0;
//	}
	
	@Override
	public void die(Entity killer) {
		super.die(killer);
		if (father != null) father.currentNumOfHead--;
	}
	
	@Override
	public void display(Graphics2D g2d) {
		displayLife(g2d);
		UsefulTh.displayTex(textures[iTex], (int)x, (int)y, w, h, play.color, g2d);
	}
}
