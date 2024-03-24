package fr.svedel.fod.play.enemy;

import java.awt.Graphics2D;

import fr.svedel.fod.UsefulTh;
import fr.svedel.fod.play.Room;
/**
 * Bunri, l'Ombre Pourpre
 * c'est un grand ninja
 * <p>
 * Class créée le 17/02/2023
 * 
 * @author Samuel Vedel
 *
 */
public class Bunri extends Enemy {
	
	private int[][][] textures = new int[6][24][12];
	private int iTex = 0;
	private double tTex = 0;
	/** temps entre deux changement de textures quand on marche */
	private final int vTex = 10;
	
	public Bunri(Room room) {
		super(room);
		type = BUNRI_TYPE;
		name = "Bunri, l'Ombre Pourpre";
		initVar();
		takeEnemiesPowers();
		initTextures();
		spawnToWalk();
	}
	
	private void initVar() {
		w = UsefulTh.cubeW*2;
		h = UsefulTh.cubeH*4;
		v = 2./5*UsefulTh.pixelW;
		maxLife = 400;
		life = maxLife;
		cadence = 120;
		punchCadence = 60;
	}
	
	private void initTextures() {
		for (int i = 0; i < textures.length/2; i++) {
			textures[i] = UsefulTh.readMat("textures/enemies/Bunri/Bunri"+i+".texture");
		}
		for (int i = textures.length/2; i < textures.length; i++) {
			textures[i] = UsefulTh.reverseXMat(textures[i-3]);
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
		shoot(target, delta);
		
		endOfActions(delta);
	}
	
	@Override
	public void display(Graphics2D g2d) {
		displayLife(g2d);
		UsefulTh.displayTex(textures[iTex], (int)x-(iTex <= 2? 4*UsefulTh.pixelW : 0), (int)y, w+4*UsefulTh.pixelW, h, play.color, g2d);
	}

}
