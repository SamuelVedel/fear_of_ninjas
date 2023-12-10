package fr.SamuelVedel.Play.Enemy;

import java.awt.Graphics2D;

import fr.SamuelVedel.FOD.UsefulTh;
import fr.SamuelVedel.Play.Room;

/*  ___
 * (° °)
 *  ) (
 * (( ))
 * 
 */

/**
 * class créée le 24/02/2023
 * 
 * @author Samuel Vedel
 *
 */
public class LilSlime extends Enemy {
	
	private int[][][] textures = new int[2][5][8];
	private int iTex;
	
	/**
	 * indique si il à déjà était en contact avec
	 * un sol dans sa vie
	 */
	private boolean groundTouched = false;
	
	/**
	 * constructeur appeler par les {@code MedSlime}
	 * 
	 * @param x
	 * @param y
	 * @param direction
	 * @param room
	 */
	public LilSlime(double x, double y, int direction, Room room) {
		super(room);
		type = LIL_SLIME_TYPE;
		name = "petit slime";
		initVar();
		takeEnemiesPowers();
		initTextures();
		
		this.x = x;
		this.y = y;
		this.direction = direction;
		if (direction != 0) vFall = vJump;
	}
	
	public LilSlime(Room room) {
		this(0, 0, 0, room);
		spawnToWalk();
	}
	
	private void initVar() {
		w = 8*UsefulTh.pixelW;
		h = 5*UsefulTh.pixelH;
		v = 2;
		maxLife = 20;
		life = maxLife;
		punchCadence = 60;
	}
	
	private void initTextures() {
		textures[0] = UsefulTh.readTex("textures/enemies/slime/lilSlime.texture");
		textures[1] = UsefulTh.reverseXTex(textures[0]);
	}
	
	@Override
	public void actions(double delta) {
		super.actions(delta);
		
		if (!groundTouched) {
			x += v*direction*delta;
			y += vFall*delta;
			vFall += aFall*delta;
			
			collision();
			if (vFall == 0) {
				groundTouched = true;
			}
		} else {
			followMe = false;
			actionToWalk(delta);
			
			// fusionne
			if (alive) {
				for (int i = room.enemies.size()-1; i >= 0; i--) {
					if (room.enemies.get(i).type == LIL_SLIME_TYPE
						&& room.enemies.get(i) != this
						&& room.enemies.get(i).alive) {
						LilSlime ls = (LilSlime) room.enemies.get(i);
						if (x+w > ls.x && x < ls.x+ls.w
							&& y+h > ls.y && y < ls.y+ls.h) {
							alive = false;
							ls.alive = false;
							room.enemies.add(new MedSlime(x, y, (UsefulTh.rand.nextBoolean()? -1 : 1), room));
						}
					}
				}
			}
		}
		
		// gère l'animation de marche
		if (direction > 0) iTex = 0;
		else if (direction < 0) iTex = 1;
		
		// attaque
		punch(delta);
		
		endOfActions(delta);
	}
	
	@Override
	public void display(Graphics2D g2d) {
		UsefulTh.displayTex(textures[iTex], (int)x, (int)y, w, h, play.color, g2d);
		displayLife(g2d);
	}
}
