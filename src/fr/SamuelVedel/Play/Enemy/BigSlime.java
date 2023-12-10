package fr.SamuelVedel.Play.Enemy;

import java.awt.Graphics2D;

import fr.SamuelVedel.FOD.UsefulTh;
import fr.SamuelVedel.Play.Entity;
import fr.SamuelVedel.Play.Room;

public class BigSlime extends Enemy {

	private int[][] textures = new int[9][12];
	
	/**
	 * constructeur appeler par les {@code MedSlime}
	 * lors de la fusion
	 * 
	 * @param x
	 * @param y
	 * @param direction
	 * @param room
	 */
	public BigSlime(double x, double y, int direction, Room room) {
		super(room);
		type = BIG_SLIME_TYPE;
		name = "gros slime";
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
	
	public BigSlime(Room room) {
		this(0, 0, 0, room);
		spawnToWalk();
	}
	
	private void initVar() {
		w = 12*UsefulTh.pixelW;
		h = 9*UsefulTh.pixelH;
		v = 2;
		maxLife = 60;
		life = maxLife;
		punchCadence = 60;
	}
	
	private void initTextures() {
		textures = UsefulTh.readTex("textures/enemies/slime/bigSlime.texture");
	}
	
	@Override
	public void actions(double delta) {
		super.actions(delta);
		
		followMe = false;
		actionToWalk(delta);
		
		// attaque
		punch(delta);
		
		endOfActions(delta);
	}
	
	@Override
	public void die(Entity killer) {
		super.die(killer);
		
		// dimension d'un moyen slime
		// à changer si cette dimension
		// est modifier
		int medSW = 8*UsefulTh.pixelW;
		int medSH = 5*UsefulTh.pixelH;
		room.enemies.add(new MedSlime(x+w/2-medSW/2, y+h/2-medSH/2, 1, room));
		room.enemies.add(new MedSlime(x+w/2-medSW/2, y+h/2-medSH/2, -1, room));
	}
	
//	@Override
//	public boolean lookToTheRight() {
//		return direction > 0;
//	}
	
	@Override
	public void display(Graphics2D g2d) {
		UsefulTh.displayTex(textures, (int)x, (int)y, w, h, play.color, g2d);
		displayLife(g2d);
	}
}
