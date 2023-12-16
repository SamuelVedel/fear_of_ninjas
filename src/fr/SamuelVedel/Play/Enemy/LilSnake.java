package fr.SamuelVedel.Play.Enemy;

import java.awt.Graphics2D;

import fr.SamuelVedel.FOD.UsefulTh;
import fr.SamuelVedel.Play.Entity;
import fr.SamuelVedel.Play.Power;
import fr.SamuelVedel.Play.Room;

public class LilSnake extends Enemy {
	
	private int[][][] textures = new int[6][3][10];
	private int iTex = 0;
	private double tTex = 0;
	/** temps entre deux changement de textures quand on marche */
	private final int vTex = 10;
	
	public LilSnake(double x, double y, Entity owner, Room room) {
		super(room);
		type = LIL_SNAKE_TYPE;
		name = "petit serpend";
		canDieWithASoul = false;
		initVar();
		if (owner == null || (owner != null && owner.type == Entity.SNAKE_BOSS_TYPE)) {
			takeEnemiesPowers();
		}
		initTextures();
		this.x = x;
		this.y = y;
		
		if (owner != null) {
			clan = owner.clan;
			
			// prend les pouvoir de owner
			for (int i = 0; i < owner.powers.length; i++) {
				for (int j = 0; j < owner.powers[i]; j++) {
					Power pow = Power.values()[i];
					if (pow != Power.TURRET && pow != Power.SNAKES_OF_PAIN) {
						addPower(pow);
					}
				}
			}
		}
	}
	
	public LilSnake(Room room) {
		this(0, 0, null, room);
		spawnToWalk();
	}
	
	private void initVar() {
		w = 10*UsefulTh.pixelW;
		h = 3*UsefulTh.pixelH;
		v = 2;
		maxLife = 20;
		life = maxLife;
		punchCadence = 60;
	}
	
	private void initTextures() {
		for (int i = 0; i < textures.length/2; i++) {
			textures[textures.length/2+i] = UsefulTh.readTex("textures/enemies/lilSnake/lilSnake"+i+".texture");
		}
		for (int i = 0; i < textures.length/2; i++) {
			textures[i] = UsefulTh.reverseXTex(textures[i+textures.length/2]);
		}
	}
	
	@Override
	public void actions(double delta) {
		super.actions(delta);
		
		followMe = false;
		actionToWalk(delta);
		
		// gère l'animation de marche
		if (tTex >= vTex) {
			if (direction > 0) {
				iTex++;
				if (iTex > 2) iTex = 0;
			} else if (direction < 0) {
				iTex++;
				if (2 >= iTex || iTex >= textures.length) iTex = 3;
			} else {
				if (iTex <= 2) iTex = 0;
				else iTex = 3;
			}
			tTex -= vTex;
		}
		tTex += delta;
		
		// attaque
		punch(delta);
		
		endOfActions(delta);
	}
	
	@Override
	public void display(Graphics2D g2d) {
		displayLife(g2d);
		UsefulTh.displayTex(textures[iTex], (int)x, (int)y, w, h, play.color, g2d);
	}
}
