package fr.SamuelVedel.Play.Enemy;

import java.awt.Graphics2D;

import fr.SamuelVedel.FOD.UsefulTh;
import fr.SamuelVedel.Play.Entity;
import fr.SamuelVedel.Play.Room;

/**
 * Class créée le 16/12/2022
 * 
 * @author Samuel Vedel
 *
 */
public class DrinkingDuck extends Enemy {
	private int[][][] textures = new int[6][12][6];
	private int iTex = 0;
	private double tTex = 0;
	/** temps entre deux changement de textures quand on marche */
	private final int vTex = 10;
	
	public DrinkingDuck(Room room, double x, double y) {
		super(room);
		type = DRINKING_DUCK;
		name = "cannard potable";
		clan = MY_CLAN;
		followTarget = false;
		initTextures();
		initVar(x, y);
	}
	
	private void initTextures() {
		for (int i = 0; i < textures.length/2; i++) {
			textures[i] = UsefulTh.readTex("textures/enemies/drinkingDuck/drinkingDuck"+i+".texture");
		}
		for (int i = textures.length/2; i < textures.length; i++) {
			textures[i] = UsefulTh.reverseXTex(textures[i-3]);
		}
	}
	
	private void initVar(double x, double y) {
		w = UsefulTh.cubeW;
		h = UsefulTh.cubeH;
		this.x = x-w/2;
		this.y = y-h;
		v = 2;
		life = maxLife;
	}
	
	@Override
	public void actions(double delta) {
		oldX = x;
		oldY = y;
		
		if (y/UsefulTh.cubeH >= room.cubes.length) die(this);
		
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
		
		// si il me touche
		drink(room.me);
		if (alive) {
			for (int i = 0; i < room.enemies.size(); i++) {
				if (room.enemies.get(i).clan == clan) {
					drink(room.enemies.get(i));
					if (!alive) break;
				}
			}
		}
	}
	
	/**
	 * regarde si l'entité {@code e} n'a pas sa vie au maximum et si
	 * elle est en contact avec le canard potable si oui l'entité le boi
	 * 
	 * @param e Entité qui va peut être boire le canard potable
	 */
	private void drink(Entity e) {
		if (e.life != e.maxLife && e != this
			&&x+w > e.x && x < e.x+e.w
			&& y+h > e.y && y < e.y+e.h) { // touché
			e.life += 15;
			if (e.life > e.maxLife) {
				e.life = e.maxLife;
			}
			alive = false;
		}
	}
	
	@Override
	public void takeDammage(int dammage, Entity e) {}
	
	@Override
	public void display(Graphics2D g2d) {
		UsefulTh.displayTex(textures[iTex], (int)x, (int)y, w, h, play.color, g2d);
	}
}
