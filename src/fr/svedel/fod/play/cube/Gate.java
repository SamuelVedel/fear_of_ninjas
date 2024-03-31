package fr.svedel.fod.play.cube;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.svedel.fod.UsefulTh;
import fr.svedel.fod.play.Bullet;
import fr.svedel.fod.play.Entity;
import fr.svedel.fod.play.Room;

public class Gate extends Cube {
	
	private Room room;
	private boolean active = false;
	
	public Gate(int x, int y, int w, int h, String chTex, Room room) {
		super(x, y, w, h, GATE, chTex);
		this.room = room;
	}
	
	public Gate(int x, int y, Room room) {
		this(x, y, UsefulTh.CUBE_W, 2*UsefulTh.CUBE_H, "gate.texture", room);
	}
	
	public void activate() {
		active = true;
	}
	
	@Override
	public int touched(Entity e) {
		if (active) {
			if (e.type == Entity.ME_TYPE) {
				room.refresh();
			}
			return CONTACT;
		}
		return NO_CONTACT;
	}
	
	public void touched(Bullet b) {
		if (active) {
			super.touched(b);
		}
	}
	
	@Override
	public void display(Color c2, Graphics2D g2d) {
		if (active) super.display(c2, g2d);
	}
}
