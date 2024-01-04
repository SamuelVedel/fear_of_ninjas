package fr.SamuelVedel.Play.Enemy;

import java.awt.Graphics2D;

import fr.SamuelVedel.FOD.UsefulTh;
import fr.SamuelVedel.Play.Entity;
import fr.SamuelVedel.Play.Room;

public class MedSlime extends Enemy {
	
	private int[][][] textures = new int[2][6][10];
	private int iTex;
	
	/**
	 * indique si il à déjà était en contact avec
	 * un sol dans sa vie
	 */
	private boolean groundTouched = false;
	
	/**
	 * constructeur appeler par les {@code BigSlime}
	 * ou les {@code LilSlime} lors de la fusion
	 * 
	 * @param x
	 * @param y
	 * @param direction
	 * @param room
	 */
	public MedSlime(double x, double y, int direction, Room room) {
		super(room);
		type = MED_SLIME_TYPE;
		name = "moyen slime";
		canDieWithASoul = false;
		canDieWithDeathsBullets = false;
		dieWithAnIncrementationOfScore = false;
		canDieWithAnItem = false;
		canDieWithADrinkingDuck = false;
		initVar();
		takeEnemiesPowers();
		initTextures();
		
		this.x = x;
		this.y = y;
		this.direction = direction;
		if (direction != 0) vFall = vJump;
	}
	
	public MedSlime(Room room) {
		this(0, 0, 0, room);
		spawnToWalk();
	}
	
	private void initVar() {
		w = 10*UsefulTh.pixelW;
		h = 6*UsefulTh.pixelH;
		v = 2;
		maxLife = 40;
		life = maxLife;
		punchCadence = 60;
	}
	
	private void initTextures() {
		textures[0] = UsefulTh.readTex("textures/enemies/slime/medSlime.texture");
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
			followTarget = false;
			actionToWalk(delta);
			
			// fusionne
			if (alive) {
				for (int i = room.enemies.size()-1; i >= 0; i--) {
					if (room.enemies.get(i).type == MED_SLIME_TYPE
						&& room.enemies.get(i) != this
						&& room.enemies.get(i).alive) {
						MedSlime ms = (MedSlime) room.enemies.get(i);
						if (x+w > ms.x && x < ms.x+ms.w
							&& y+h > ms.y && y < ms.y+ms.h) {
							alive = false;
							ms.alive = false;
							room.enemies.add(new BigSlime(x, y, (UsefulTh.rand.nextBoolean()? -1 : 1), room));
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
	public void die(Entity killer) {
		super.die(killer);
		
		/* 
		 * dimension d'un petit slime
		 * à changer si cette dimension
		 * est modifier
		 */
		int lilSW = 8*UsefulTh.pixelW;
		int lilSH = 5*UsefulTh.pixelH;
		room.enemies.add(new LilSlime(x+w/2-lilSW/2, y+h/2-lilSH/2, 1, room));
		room.enemies.add(new LilSlime(x+w/2-lilSW/2, y+h/2-lilSH/2, -1, room));
	}
	
	@Override
	public void display(Graphics2D g2d) {
		UsefulTh.displayTex(textures[iTex], (int)x, (int)y, w, h, play.color, g2d);
		displayLife(g2d);
	}
}
