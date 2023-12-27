package fr.SamuelVedel.Play.Enemy;

import java.awt.Graphics2D;

import fr.SamuelVedel.FOD.UsefulTh;
import fr.SamuelVedel.Play.Entity;
import fr.SamuelVedel.Play.Room;

/**
 * énemie qui heal trois énemies à côté si il y en à
 * et te vole ta vie si t'est trop près
 * <p>
 * class créée le 18/08/2023 vers 0h40
 * 
 * @author Samuel Vedel
 *
 */
public class Healer extends Enemy {
	
	private int[][][] textures = new int[6][][];
	private int iTex = 0;
	private double tTex = 0;
	/** temps entre deux changement de textures quand on marche */
	private final int vTex = 10;
	
	private double tHeal = 0;
	private final int interHeal = 3;
	private final int heal = 1;
	private final int distOfHeal = viewDistance;
	
	private final int maxNearest = 3;
	private Enemy[] nearest = new Enemy[maxNearest];
	
	private double tLifeSteal = 0;
	private final int interLifeSteal = 3;
	private final int lifeSteal = 1;
	private final int distOfLifeSteal = 7*UsefulTh.cubeW;
	
	/** allié le plus proche */
	private Entity nearestMe = null;
	
	public Healer(Room room) {
		super(room);
		type = HEALER_TYPE;
		name = "esprit de la forêt";
		initVar();
		takeEnemiesPowers();
		spawnToWalk();
		initTextures();
	}
	
	private void initVar() {
		w = UsefulTh.cubeW;
		h = UsefulTh.cubeH*2;
		v = 1;
		maxLife = 100;
		life = maxLife;
		cadence = 120;
		punchCadence = 60;
	}
	
	private void initTextures() {
		for (int i = 0; i < textures.length/2; i++) {
			textures[i] = UsefulTh.readTex("textures/enemies/healer/healer"+i+".texture");
		}
		for (int i = textures.length/2; i < textures.length; i++) {
			textures[i] = UsefulTh.reverseXTex(textures[i-3]);
		}
	}
	
	@Override
	public void actions(double delta) {
		super.actions(delta);
		
		followMe = false;
		if (nearest[0] != null) {
			if (nearest[0].x > x) direction = 1;
			else direction = -1;
			if (Math.sqrt((nearest[0].x-x)*(nearest[0].x-x)) < v) {
				direction = 0;
			}
		}
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
		
		// attaque
		punch(delta);
//		shoot(room.me);
		
		heal(delta);
		lifeSteal(delta);
		
		endOfActions(delta);
	}
	
	private void heal(double delta) {
		// recherche des tois énmeies les plus proches
		nearest = new Enemy[maxNearest];
		double minDist = 0;
		for (int j = 0; j < nearest.length; j++) {
			double dist = 0;
			for (int i = 0; i < room.enemies.size(); i++) {
				Enemy en = room.enemies.get(i);
				if (en.clan == clan && en != this && en.type != BOMB_TYPE) {
					double d = Math.sqrt(Math.pow(x+w/2-en.x-en.w/2, 2)+Math.pow(y+h/2-en.y-en.h/2, 2));
					if ((d < dist || nearest[j] == null) && d > minDist && d < distOfHeal) {
						nearest[j] = en;
						dist = d;
					}
				}
			}
			minDist = dist;
		}
		
		if (tHeal >= interHeal) {
			for (Enemy en : nearest) {
				if (en != null) {
					en.life += heal;
					if (en.life > en.maxLife) en.life = en.maxLife;
				}
			}
			tHeal = 0;
		}
		tHeal += delta;
	}
	
	private void lifeSteal(double delta) {
		// recherche l'allié le plus proche
		nearestMe = room.me;
		double dist = Math.sqrt(Math.pow(x+w/2-room.me.x-room.me.w/2, 2)+Math.pow(y+h/2-room.me.y-room.me.h/2, 2));
		if (dist > distOfLifeSteal) {
			nearestMe = null;
			dist = -1;
		}
		for (int i = 0; i < room.enemies.size(); i++) {
			Enemy en = room.enemies.get(i);
			if (en.clan != clan && en.type != DRINKING_DUCK) {
				double d = Math.sqrt(Math.pow(x+w/2-en.x-en.w/2, 2)+Math.pow(y+h/2-en.y-en.h/2, 2));
				if ((d < dist || nearestMe == null) && d < distOfLifeSteal) {
					nearestMe = en;
					dist = d;
				}
			}
		}
		
		if (tLifeSteal >= interLifeSteal) {
			if (nearestMe != null) {
				nearestMe.life -= lifeSteal;
				if (nearestMe.life <= 0) nearestMe.die(this);
				life += lifeSteal;
				if (life > maxLife) life = maxLife;
			}
			tLifeSteal = 0;
		}
		tLifeSteal += delta;
	}
	
	@Override
	public void display(Graphics2D g2d) {
		//affiche les lien de soins
		for (int i = 0; i < nearest.length; i++) {
			if (nearest[i] != null) {
				Enemy en = nearest[i];
				UsefulTh.drawSemiLine((int)x+w/2, (int)y+h/2, (int)en.x+en.w/2, (int)en.y+en.h/2, play.color.darker(), g2d);
			}
		}
		
		// affiche le lien de vol de vie
		if (nearestMe != null) {
			UsefulTh.drawSemiLine((int)x+w/2, (int)y+h/2, (int)nearestMe.x+nearestMe.w/2, (int)nearestMe.y+nearestMe.h/2, play.color.darker(), g2d);
		}
		
		displayLife((int)(x+w/2-(w+2*UsefulTh.pixelW)/2), (int)(y-4*UsefulTh.pixelH-1.3*3*UsefulTh.pixelH), w+2*UsefulTh.pixelW, 3*UsefulTh.pixelH, UsefulTh.pixelW, g2d);
		// une partie de la texture n'est pas comprise dans la hitbox
		int wTex = w+6*UsefulTh.pixelW;
		int hTex = h+4*UsefulTh.pixelH;
		UsefulTh.displayTex(textures[iTex], (int)x-3*UsefulTh.pixelW, (int)y-4*UsefulTh.pixelH, wTex, hTex, play.color, g2d);
	}
}
