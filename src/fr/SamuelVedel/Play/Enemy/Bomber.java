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
 * Bombardier qui balance des bombes qui font initilement
 * 30 de dégat avec un angle de PI/4, Enemie de classe 2
 * <p>
 * Class créée le 16/01/2023
 * 
 * @author Samuel Vedel
 * @see Bomb
 *
 */
public class Bomber extends Enemy {
	
	private int[][][] textures = new int[6][12][6];
	private int iTex = 0;
	private double tTex = 0;
	/** temps entre deux changement de textures quand on marche */
	private final int vTex = 10;
	
	public Bomber(Room room) {
		super(room);
		type = BOMBER_TYPE;
		name = "bombardier";
		initVar();
		takeEnemiesPowers();
		initTextures();
		spawnToWalk();
	}
	
	private void initVar() {
		w = UsefulTh.cubeW;
		h = UsefulTh.cubeH*2;
		v = 2.5;
		maxLife = 50;
		life = maxLife;
		cadence = 150;
		punchCadence = 60;
		bulletDamage = 40;
		bulletV = 15;
		alpha = -Math.PI/4;
	}
	
	private void initTextures() {
		for (int i = 0; i < textures.length/2; i++) {
			textures[i] = UsefulTh.readTex("textures/enemies/bomber/bomber"+i+".txt");
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
		
		// attaque
		punch(delta);
		// gère le lancer de bombe
		if (Math.sqrt(Math.pow(room.me.x-x, 2)+Math.pow(room.me.y-y, 2)) <= viewDistance) {
			if (tShoot >= cadence) {
				if (room.me.x < x) {
					room.enemies.add(new Bomb(room, -bulletV*Math.cos(alpha), bulletV*Math.sin(alpha), bulletDamage, this));
				} else {
					room.enemies.add(new Bomb(room, bulletV*Math.cos(alpha), bulletV*Math.sin(alpha), bulletDamage, this));
				}
				tShoot = 0;
			}
			tShoot += delta;
		} else if (tShoot < cadence) {
			tShoot += delta;
		}
		
		endOfActions(delta);
	}
	
	@Override
	protected void walkFollowMe() {
		if (followMe) {
			/** distance à l'aquel il jette la bombe */;
			int d = Math.abs((int)(bulletV*Math.cos(alpha)*(-bulletV*Math.sin(alpha)+Math.sqrt(Math.pow(bulletV*Math.sin(alpha), 2)+2*aFall*(h-4*UsefulTh.pixelH)))/aFall));
			
			if (room.me.x > x) direction = 1;
			else direction = -1;
			
			int addX = (lookToTheRight()? -3*UsefulTh.pixelW: w);
			if (Math.abs(Math.sqrt((room.me.x-(x+addX))*(room.me.x-(x+addX)))-d) < v) {
				direction = 0;
			} else if(Math.sqrt((room.me.x-(x+addX))*(room.me.x-(x+addX))) < d) {
				direction *= -1;
			}
		} else if (direction == 0) {
			direction = (UsefulTh.rand.nextBoolean()? 1 : -1);
		}
	}
	
	public boolean lookToTheRight() {
		return iTex < textures.length/2;
	}
	
	@Override
	public void display(Graphics2D g2d) {
		displayLife(g2d);
		// une partie de la texture n'est pas compris dans la hit box
		int wTex = w+3*UsefulTh.pixelW;
		int hTex = h;
		if (iTex <= 2) {
			UsefulTh.displayTex(textures[iTex], (int)x-3*UsefulTh.pixelW, (int)y, wTex, hTex, play.color, g2d);
		} else {
			UsefulTh.displayTex(textures[iTex], (int)x, (int)y, wTex, hTex, play.color, g2d);
		}
	}
}
