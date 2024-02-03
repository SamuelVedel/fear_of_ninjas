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
		oldX = x;
		oldY = y;
		
		move(delta);
		double dist = stopOnCase();
		
		if (dist != 0) {
			// mange des pommes
			for (Apple ap : apples) {
				ap.eat();
			}
			
			int oldDirection = direction;
			
			// peut tourner al�atoirement
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
			
			// ajoute de quoi indique au corps qu'il � tourn�
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
	
	private int getBodyLength() {
		int len = 0;
		for (int i = 0; i < body.size(); ++i) {
			len += body.get(i).getLength();
		}
		return len;
	}
	
	/**
	 * appel� quand le seprent tourne
	 */
	private void addBodyPart(int newDirection) {
		double bx = 0;
		double by = 0;
		double bw = 0;
		double bh = 0;
		switch (newDirection) {
		case leftDirection:
			bx = x+w;
			by = y;
			bw = 0;
			bh = h;
			break;
		case upDirection:
			bx = x;
			by = y+h;
			bw = w;
			bh = 0;
			break;
		case rightDirection:
			bx = x;
			by = y;
			bw = 0;
			bh = h;
			break;
		case downDirection:
			bx = x;
			by = y;
			bw = w;
			bh = 0;
			break;
		}
		body.add(0, new BodyPart(bx, by, bw, bh, newDirection));
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
			body.get(0).followHead(x, y, w, h);
			//body.get(0).move(dist);
			int bodySize = body.size();
			body.get(bodySize-1).reduce();
			if (body.get(bodySize-1).noMoreReasonToBe) {
				body.remove(bodySize-1);
			}
		}
	}
	
	/**
	 * si les serpent d�pase une case, il l'arr�te au niveau
	 * de la case et renvoie la distance � laquelle il l'a d�pass�
	 */
	private double stopOnCase() {
		int ix = (int)(x)/UsefulTh.cubeW;
		int iy = (int)(y)/UsefulTh.cubeH;
		
		int oldIx = (int)(oldX)/UsefulTh.cubeW;
		int oldIy = (int)(oldY)/UsefulTh.cubeH;
		
		double ret = 0;
		if (oldIx != ix) {
			if (direction == leftDirection) {
				ret = oldIx*UsefulTh.cubeW-x;
				x = oldIx*UsefulTh.cubeW;
			} else if (direction == rightDirection) {
				ret = ix*UsefulTh.cubeW-x;
				x = ix*UsefulTh.cubeW;
			}
		} else if (oldIy != iy) {
			if (direction == upDirection) {
				ret = oldIy*UsefulTh.cubeH-y;
				y = oldIy*UsefulTh.cubeH;
			} else if (direction == downDirection) {
				ret = iy*UsefulTh.cubeH-y;
				y = iy*UsefulTh.cubeH;
			}
		}
		
		//if (body.size() > 0) body.get(0).stopOnCase();
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
		for (int i = 1; i < body.size(); i++) {
			BodyPart bp = body.get(i);
			if (x+w > bp.x && x < bp.x+bp.w
				&& y+h > bp.y && y < bp.y+bp.h) {
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
		
		// coordonn�es et dimensions pour l'affichage
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
	
	private class BodyPart {
		private double x, y;
		private double w, h;
		private int direction;
		private boolean noMoreReasonToBe = false;
		
		private BodyPart(double x, double y, double w, double h, int direction) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.direction = direction;
		}
		
		private double getLength() {
			switch (direction) {
			case leftDirection:
			case rightDirection:
				return w;
			default:
				return h;
			}
		}
		
		private void stopOnCase() {
			switch(direction) {
			case leftDirection:
				x = (int)((int)(x/UsefulTh.cubeW+1)*UsefulTh.cubeW);
			case rightDirection:
				w = (int)((int)(x+w)/UsefulTh.cubeW)*UsefulTh.cubeW-x;
				break;
			case upDirection:
				y = (int)((int)(y/UsefulTh.cubeH+1)*UsefulTh.cubeH);
			case downDirection:
				h = (int)((int)(y+h)/UsefulTh.cubeH)*UsefulTh.cubeH-y;
				break;
			}
		}
		
		private void move(double dist) {
			switch (direction) {
			case leftDirection:
				w += dist;
				x -= dist;
				break;
			case rightDirection:
				w += dist;
				break;
			case upDirection:
				h += dist;
				y -= dist;
				break;
			case downDirection:
				h += dist;
				break;
			}
		}
		
		private void followHead(double hx, double hy, double hw, double hh) {
			switch (direction) {
			case leftDirection:
				w += hx+hw-x;
				x = hx+hw;
				break;
			case rightDirection:
				w += hx-x-w;
				break;
			case upDirection:
				h += hy+hh-y;
				y = hy+hh;
				break;
			case downDirection:
				h += hy-y-h;
				break;
			}
		}
		
		private void reduce() {
			double dist = getBodyLength()-length;
			if (getLength()-dist < 0) {
				noMoreReasonToBe = true;
			} else if (dist > 0) {
				switch (direction) {
				case rightDirection:
					x += dist;
				case leftDirection:
					w -= dist;
					break;
				case downDirection:
					y += dist;
				case upDirection:
					h -= dist;
					break;
				}
			}
		}
		
		private double[] getEndPos() {
			double x2 = 0;
			double y2 = 0;
			switch (direction) {
			case leftDirection:
				x2 = x+w-UsefulTh.cubeW;
				y2 = y;
				break;
			case rightDirection:
				x2 = x;
				y2 = y;
				break;
			case upDirection:
				x2 = x;
				y2 = y+h-UsefulTh.cubeH;
				break;
			case downDirection:
				x2 = x;
				y2 = y;
				break;
			}
			return new double[] {x2, y2};
		}
		
		private void display(Graphics2D g2d) {
			g2d.fillRect((int)x, (int)y, (int)w, (int)h);
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
							if (x1+UsefulTh.cubeW > bp.x && x1 < bp.x+bp.w
								&& y1+UsefulTh.cubeH > bp.y && y1 < bp.y+bp.h) {
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
