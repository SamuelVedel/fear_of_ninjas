package fr.svedel.fod.play.enemy;

import java.awt.Graphics2D;
import java.util.ArrayList;

import fr.svedel.fod.UsefulTh;
import fr.svedel.fod.play.Entity;
import fr.svedel.fod.play.Item;
import fr.svedel.fod.play.Power;
import fr.svedel.fod.play.Room;
import fr.svedel.fod.play.cube.Cube;
import fr.svedel.fod.play.cube.Gate;
import fr.svedel.fod.play.particle.ClassicParticle;

public class SnakeBoss extends Enemy {
	
	private int[][][] textures = new int[4][][];
	private int[][] appleTex = UsefulTh.readMat("textures/enemies/snakeBoss/apple.texture");
	
	private final int leftDirection = 0;
	private final int upDirection = 1;
	private final int rightDirection = 2;
	private final int downDirection = 3;
	
	private int length = 0;
	private ArrayList<BodyPart> body = new ArrayList<>();
	private Apple[] apples = new Apple[35];
	
	public SnakeBoss(Room room) {
		super(room);
		type = SNAKE_BOSS_TYPE;
		name = "Snake boss";
		initVar();
		initTex();
		spawnToFly();
		initApples();
		addBodyPart(direction);
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
		//oldX = x;
		//oldY = y;
		
		move(delta);
		double dist = stopOnCase();
		
		if (dist != 0) {
			// mange des pommes
			for (Apple ap : apples) {
				ap.eat();
			}
			
			int oldDirection = direction;
			
			// peut tourner aléatoirement
			if (UsefulTh.rand.nextInt(5) == 0) {
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
			
			// ajoute de quoi indique au corps qu'il à tourné
			if (direction != oldDirection) {
				addBodyPart(direction);
			}
			
			eatHimself();
		}
		
		if (dist != 0) moveDist(dist);
		
		punch(delta);
	}
	
	private void grow() {
		length += UsefulTh.cubeW;
	}
	
	private double getBodyLength() {
		double len = 0;
		for (int i = 0; i < body.size(); ++i) {
			len += body.get(i).getLength();
		}
		return len;
	}
	
	/**
	 * appelé quand le seprent tourne
	 */
	private void addBodyPart(int newDirection) {
		double x1 = 0;
		double y1 = 0;
		double x2 = 0;
		double y2 = 0;
		switch (newDirection) {
		case leftDirection:
			x1 = x+w;
			y1 = y;
			x2 = x1;
			y2 = y1+h;
			break;
		case upDirection:
			x1 = x;
			y1 = y+h;
			x2 = x1+w;
			y2 = y1;
			break;
		case rightDirection:
			x1 = x;
			y1 = y;
			x2 = x1;
			y2 = y1+h;
			break;
		case downDirection:
			x1 = x;
			y1 = y;
			x2 = x1+w;
			y2 = y1;
			break;
		}
		body.add(0, new BodyPart(x1, y1, x2, y2, newDirection));
	}
	
	private void move(double delta) {
		oldX = x;
		oldY = y;
		moveDist(v*delta);
	}

	private void moveDist(double dist) {
		switch (direction) {
		case leftDirection :
			x -= dist;
			break;
		case upDirection :
			y -= dist;
			break;
		case rightDirection :
			x += dist;
			break;
		case downDirection :
			y += dist;
		}
		
		if (body.size() > 0) {
			body.get(0).followHead();
			//body.get(0).move(dist);
			int bodySize = body.size();
			body.get(bodySize-1).reduce();
			if (body.get(bodySize-1).noMoreReasonToBe) {
				body.remove(bodySize-1);
				body.get(bodySize-2).reduce();
			}
		}
	}
	
	/**
	 * si les serpent dépase une case, il l'arrête au niveau
	 * de la case et renvoie la distance à laquelle il l'a dépassé
	 */
	private double stopOnCase() {
		int ix = (int)x/UsefulTh.cubeW;
		int iy = (int)y/UsefulTh.cubeH;
		
		int oldIx = (int)oldX/UsefulTh.cubeW;
		int oldIy = (int)oldY/UsefulTh.cubeH;
		
		//room.cht.addText(""+ix+"/"+oldIx+" "+iy+"/"+oldIy);
		
		double ret = 0;
		if (oldIx != ix) {
			if (direction == leftDirection) {
				ret = oldIx*UsefulTh.cubeW-x;
				x = oldIx*UsefulTh.cubeW;
			} else if (direction == rightDirection) {
				ret = ix*UsefulTh.cubeW-x;
				x = ix*UsefulTh.cubeW;
			}
			if (body.size() > 0) body.get(0).stopOnCase();
		} else if (oldIy != iy) {
			if (direction == upDirection) {
				ret = oldIy*UsefulTh.cubeH-y;
				y = oldIy*UsefulTh.cubeH;
			} else if (direction == downDirection) {
				ret = iy*UsefulTh.cubeH-y;
				y = iy*UsefulTh.cubeH;
			}
			if (body.size() > 0) body.get(0).stopOnCase();
		}
		
		return (ret > 0? ret: -ret);
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
		for (int i = 2; i < body.size(); i++) {
			BodyPart bp = body.get(i);
			if (x+w > bp.x1 && x < bp.x2
				&& y+h > bp.y1 && y < bp.y2) {
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
		if (length > 0) {
			double[] endPos = body.get(body.size()-1).getEndPos();
			int x2 = (int) endPos[0];
			int y2 = (int) endPos[1];
			makeDeathParticuleAt(x2, y2);
			
			for (int i = 0; i < 3; i++) {
				room.enemies.add(new LilSnake(x2+i*3*UsefulTh.pixelW, y2+i*3*UsefulTh.pixelH, this, room));
			}
			
			length -= UsefulTh.cubeW;
			if (length <= 0) die(e);
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
		
		room.items.add(new Item((int)x+w/2, (int)y+h, Power.SNAKES_OF_PAIN, room));
	}
	
	@Override
	public void display(Graphics2D g2d) {
		for (Apple ap : apples) {
			ap.display(g2d);
		}
		
		g2d.setColor(play.color);
		for (int i = body.size()-1; i >= 0; i--) {
			body.get(i).display(g2d);
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
			w2 += 2*UsefulTh.pixelW;
			break;
		case downDirection :
			h2 += 2*UsefulTh.pixelH;
			break;
		}
		UsefulTh.displayTex(textures[direction], x2, y2, w2, h2, play.color, g2d);
	}
	
	private class BodyPart {
		private double x1, y1;
		private double x2, y2;
		private int direction;
		private boolean noMoreReasonToBe = false;
		
		private BodyPart(double x1, double y1, double x2, double y2, int direction) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.direction = direction;
		}
		
		private double getLength() {
			switch (direction) {
			case leftDirection:
			case rightDirection:
				return x2-x1;
			default:
				return y2-y1;
			}
		}
		
		private void stopOnCase() {
			switch(direction) {
			case leftDirection:
				x1 = (int)((int)(x1/UsefulTh.cubeW+1)*UsefulTh.cubeW);
				break;
			case rightDirection:
				x2 = (int)((int)(x2/UsefulTh.cubeW)*UsefulTh.cubeW);
				break;
			case upDirection:
				y1 = (int)((int)(y1/UsefulTh.cubeH+1)*UsefulTh.cubeH);
				break;
			case downDirection:
				y2 = (int)((int)(y2/UsefulTh.cubeH)*UsefulTh.cubeH);
				break;
			}
		}
		
		/*private void move(double dist) {
			switch (direction) {
			case leftDirection:
				x1 -= dist;
				break;
			case rightDirection:
				x2 += dist;
				break;
			case upDirection:
				y1 -= dist;
				break;
			case downDirection:
				y2 += dist;
				break;
			}
			}*/
		
		private void followHead() {
			switch (direction) {
			case leftDirection:
				x1 = x+w;
				break;
			case rightDirection:
				x2 = x;
				break;
			case upDirection:
				y1 = y+h;
				break;
			case downDirection:
				y2 = y;
				break;
			}
		}
		
		private void reduce() {
			double dist = getBodyLength()-length;
			if (getLength() < dist) {
				noMoreReasonToBe = true;
			} else if (dist > 0) {
				switch (direction) {
				case rightDirection:
					x1 += dist;
					break;
				case leftDirection:
					x2 -= dist;
					break;
				case downDirection:
					y1 += dist;
					break;
				case upDirection:
					y2 -= dist;
					break;
				}
			}
		}
		
		private double[] getEndPos() {
			double x3 = 0;
			double y3 = 0;
			switch (direction) {
			case leftDirection:
				x3 = x2-UsefulTh.cubeW;
				y3 = y1;
				break;
			case rightDirection:
				x3 = x1;
				y3 = y1;
				break;
			case upDirection:
				x3 = x1;
				y3 = y2-UsefulTh.cubeH;
				break;
			case downDirection:
				x3 = x1;
				y3 = y1;
				break;
			}
			return new double[] {x2, y2};
		}
		
		private void display(Graphics2D g2d) {
			g2d.fillRect((int)x1, (int)y1, (int)x2-(int)x1, (int)y2-(int)y1);
		}
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
						for (int i = 1; i < body.size(); i++) {
							BodyPart bp = body.get(i);
							if (x1+UsefulTh.cubeW > bp.x1 && x1 < bp.x2
								&& y1+UsefulTh.cubeH > bp.y1 && y1 < bp.y2) {
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
