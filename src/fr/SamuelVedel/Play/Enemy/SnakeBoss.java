package fr.SamuelVedel.Play.Enemy;

import java.awt.Graphics2D;
import java.util.ArrayList;

import fr.SamuelVedel.FOD.UsefulTh;
import fr.SamuelVedel.Play.Entity;
import fr.SamuelVedel.Play.Item;
import fr.SamuelVedel.Play.Power;
import fr.SamuelVedel.Play.Room;
import fr.SamuelVedel.Play.Cube.Cube;
import fr.SamuelVedel.Play.Cube.Gate;
import fr.SamuelVedel.Play.Particle.ClassicParticle;

public class SnakeBoss extends Enemy {
	
	private int[][][] textures = new int[4][][];
	private int[][] appleTex = UsefulTh.readMat("textures/enemies/snakeBoss/apple.texture");
	
	private final int leftDirection = 0;
	private final int upDirection = 1;
	private final int rightDirection = 2;
	private final int downDirection = 3;
	
	private ArrayList<Integer[]> body = new ArrayList<>();
	private Apple[] apples = new Apple[35];
	
	public SnakeBoss(Room room) {
		super(room);
		type = SNAKE_BOSS_TYPE;
		name = "Snake boss";
		initVar();
		initTex();
		spawnToFly();
		initApples();
		for (int i = 0; i < 10; i++) {
			grow();
		}
	}
	
	private void initVar() {
		w = UsefulTh.cubeW;
		h = UsefulTh.cubeH;
		v = 3;
		punchCadence = 90;
		punchDamage = 40;
		direction = leftDirection;
	}
	
	private void initTex() {
		textures[0] = UsefulTh.readMat("textures/enemies/snakeBoss/snakeHead.texture");
		for (int i = 1; i < textures.length; i++) {
			textures[i] = UsefulTh.rotateMat(textures[i-1]);
		}
	}
	
	private void initApples() {
		for (int i = 0; i < apples.length; i++) {
			apples[i] = new Apple();
		}
	}
	
	@Override
	public void actions(double delta) {
		moove();
		
		if (x%UsefulTh.cubeW == 0 && y%UsefulTh.cubeH == 0) {
			// mange des pommes
			for (Apple ap : apples) {
				ap.eat();
			}
			
			// peut tourner aléatoirement
			if (UsefulTh.rand.nextInt(5) == 0) {
				int oldDirection = direction;
				randomTurn();
				if (isNeededToTurn()) {
					direction = oldDirection;
				}
			}
			
			// tourne s'il rencontre un mur
			if (isNeededToTurn()) {
				randomTurn();
				if (isNeededToTurn()) {
					direction = (direction+2)%4;
				}
			}
			
			eatHimself();
		}
		
		punch(delta);
	}
	
	private void grow() {
		for (int i = 0; i < UsefulTh.cubeW/v; i++) {
			if (body.size() != 0) {
				body.add(body.get(body.size()-1).clone());
			} else {
				body.add(new Integer[] {(int)x, (int)y});
			}
		}
	}
	
	private void moove() {
		if (body.size() > 0) {
			body.get(0)[0] = (int)x;
			body.get(0)[1] = (int)y;
		}
		
		for (int i = body.size()-1; i >= 1; i--) {
			body.get(i)[0] = body.get(i-1)[0];
			body.get(i)[1] = body.get(i-1)[1];
		}
		
		switch (direction) {
		case leftDirection :
			x -= v;
			break;
		case upDirection :
			y -= v;
			break;
		case rightDirection :
			x += v;
			break;
		case downDirection :
			y += v;
		}
	}
	
	/**
	 * Regarde si le serpend va se prendre un mur
	 * 
	 * @return
	 */
	private boolean isNeededToTurn() {
		int iX = (int)x/UsefulTh.cubeW;
		int iY = (int)y/UsefulTh.cubeW;
		switch (direction) {
		case leftDirection :
			return iX == 0 || room.cubes[iY][iX-1] != null;
		case upDirection :
			return iY == 0 || room.cubes[iY-1][iX] != null;
		case rightDirection :
			return iX == room.cubes[0].length-1 || room.cubes[iY][iX+1] != null;
		default : // down
			return iY == room.cubes.length-1 || room.cubes[iY+1][iX] != null;
		}
	}
	
	private void randomTurn() {
		// delta direction
		int dd = (UsefulTh.rand.nextBoolean()? 1: 3);
		direction = (direction+dd)%4;
	}
	
	private void eatHimself() {
		for (int i = 2*UsefulTh.cubeW/(int)v; i < body.size(); i++) {
			int x2 = body.get(i)[0];
			int y2 = body.get(i)[1];
			if (x == x2 && y == y2) {
				takeDammage(0, this);
			}
		}
	}
	
	private void makeDeathParticuleAt(int x, int y) {
		int nParticles = (w+h/2);
		for (int j = 0; j < nParticles; j++) {
			double pX = x+UsefulTh.rand.nextInt(w);
			double pY = y+UsefulTh.rand.nextInt(h);
			double pVX = (UsefulTh.rand.nextBoolean()? 1 : -1)*UsefulTh.rand.nextDouble();
			double pVY = (UsefulTh.rand.nextBoolean()? 1 : -1)*UsefulTh.rand.nextDouble();
			room.particles.add(new ClassicParticle(pX, pY, UsefulTh.pixelW, UsefulTh.pixelH, pVX, pVY));
		}
	}
	
	@Override
	public void takeDammage(int dammage, Entity e) {
		if (body.size() != 0) {
			int x2 = body.get(body.size()-1)[0];
			int y2 = body.get(body.size()-1)[1];
			makeDeathParticuleAt(x2, y2);
			
			for (int i = 0; i < 3; i++) {
				room.enemies.add(new LilSnake(x2+i*3*UsefulTh.pixelW, y2+i*3*UsefulTh.pixelH, this, room));
			}
			
			for (int i = 0; i < UsefulTh.cubeW/v; i++) {
				body.remove(body.size()-1);
			}
			if (body.size() == 0) die(e);
		}
	}
	
	@Override
	public void die(Entity killer) {
		alive = false;
		
		makeDeathParticuleAt((int)x, (int)y);
		
		for (Apple ap: apples) {
			makeDeathParticuleAt(ap.x1, ap.y1);
		}
		
		for (int i = room.enemies.size()-1; i >= 0; i--) {
			Enemy en = room.enemies.get(i);
			if (en != this) {
				en.die(this);
			}
		}
		
		for (Cube[] cus: room.cubes) {
			for (Cube cu: cus) {
				if (cu != null && cu.type == Cube.GATE) {
					Gate ga = (Gate) cu;
					ga.activate();
				}
			}
		}
		
		room.items.add(new Item((int)x+w/2, (int)y+h, Power.snakesOfPain, room));
	}
	
	@Override
	public void display(Graphics2D g2d) {
		for (Apple ap : apples) {
			ap.display(g2d);
		}
		
		g2d.setColor(play.color);
		for (int i = body.size()-1; i >= 0; i--) {
			g2d.fillRect(body.get(i)[0], body.get(i)[1], w, h);
		}
		
		// coordonnées et dimensions pour l'affichage
		int x2 = (int)x;
		int y2 = (int)y;
		int w2 = w;
		int h2 = h;
		switch (direction) {
		case leftDirection :
			x2 -= 2*UsefulTh.pixelW;
			w2 += 2*UsefulTh.pixelW;
			break;
		case upDirection :
			y2 -= 2*UsefulTh.pixelH;
			h2 += 2*UsefulTh.pixelH;
			break;
		case rightDirection :
			x2 += 2*UsefulTh.pixelW;
			w2 += 2*UsefulTh.pixelW;
			break;
		case downDirection :
			y2 += 2*UsefulTh.pixelH;
			h2 += 2*UsefulTh.pixelH;
			break;
		}
		UsefulTh.displayTex(textures[direction], x2, y2, w2, h2, play.color, g2d);
	}
	
	private class Apple {
		
		private int w = UsefulTh.cubeW;
		private int h = UsefulTh.cubeH;
		private int x1;
		private int y1;
		
		public Apple() {
			setPosition();
		}
		
		private void setPosition() {
			boolean boo = true;
			while (boo) {
				x1 = UsefulTh.rand.nextInt(room.cubes[0].length)*UsefulTh.cubeW;;
				y1 = UsefulTh.rand.nextInt(room.cubes.length)*UsefulTh.cubeH;
				boo = false;
				
				if (room.cubes[y1/UsefulTh.cubeH][x1/UsefulTh.cubeW] != null
					|| (x1 == x && y1 == y)) {
					boo = true;
				} else {
					for (Apple ap : apples) {
						if (ap != null && ap != this && ap.x1 == x1 && ap.y1 == y1) {
							boo = true;
							break;
						}
					}
					
					if (!boo) {
						for (int i = body.size()-1; i >= 1; i--) {
							if (body.get(i)[0] == x1 && body.get(i)[1] == y1) {
								boo = true;
								break;
							}
						}
					}
				}
			}
		}
		
		private void eat() {
			if (x1 == x && y1 == y) {
				setPosition();
				grow();
			}
		}
		
		private void display(Graphics2D g2d) {
			UsefulTh.displayTex(appleTex, x1, y1-2*UsefulTh.pixelH, w, h+2*UsefulTh.pixelH, play.color, g2d);
		}
	}
}
