package fr.svedel.fod.play.cube;

import fr.svedel.fod.UsefulTh;
import fr.svedel.fod.play.Entity;

public class Border extends Cube {
	
	private int capacity;
	
	public Border(int x, int y, int w, int h, int capacity, String chTex) {
		super(x, y, w, h, capacity, chTex);
		this.capacity = capacity;
	}
	
	public Border(int x, int y, int capacity, String chTex) {
		this(x, y, UsefulTh.CUBE_W, UsefulTh.CUBE_H, capacity, chTex);
	}
	
	@Override
	public boolean isAGround() {
		return type == BORD7 || type == BORD8 || type == BORD9 || type == BORD;
	}
	
	@Override
	protected int touched(Entity e) {
		if (capacity != NOTHING) {
			if ((capacity == BORD1 || capacity == BORD4 || capacity == BORD7 || capacity == BORD)
				&& e.oldX+e.w <= x) { // choc � gauche
				e.x = x-e.w;
				return LEFT_CONTACT;
			} 
			if ((capacity == BORD1 || capacity == BORD2 || capacity == BORD3 || capacity == BORD)
				&& e.oldY >= y+h) { // contact en bas
				e.y = y+h;
				e.vFall = 0.01;
				return DOWN_CONTACT;
			}
			if ((capacity == BORD9 || capacity == BORD6 || capacity == BORD3 || capacity == BORD)
				&& e.oldX >= x+w) { // choc � droite
				e.x = x+w;
				return LEFT_CONTACT;
			}
			if (isAGround() && e.oldY+e.h <= y) { // choc en haut
				e.y = y-e.h;
				e.vFall = 0;
				e.bumpTouch = false;
				e.resetNumJump();
				return UP_CONTACT;
			}
		}
		return CONTACT;
	}
}
