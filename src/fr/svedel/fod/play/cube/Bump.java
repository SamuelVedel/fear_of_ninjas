package fr.svedel.fod.play.cube;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.svedel.fod.UsefulTh;
import fr.svedel.fod.play.Entity;

public class Bump extends Cube {
	
	private int[][] tex2;
	private double t = 0;
	private boolean active = true;;
	
	private Bump(int x, int y, int w, int h) {
		super(x, y, w, h, BUMP, "bump0.texture");
		hasAction = true;
		tex2 = UsefulTh.readTex("textures/cube/bump1.texture");
	}
	
	public Bump(int x, int y) {
		this(x, y, UsefulTh.CUBE_W, UsefulTh.CUBE_H);
	}
	
	@Override
	public void actions(double delta) {
		if (!active) {
			if (t > 15) {
				active = true;
				t = 0;
			}
			t += delta;
		}
	}
	
	@Override
	protected int touched(Entity e) {
		if (e.oldY+e.h <= y && active) { // choc en haut
			e.y = y-e.h;
			if (!e.bumpTouch) {
				e.vFall *= -1/*.5*/;
				e.bumpTouch = true;
			} else e.vFall *= -0.78;
			active = false;
			return UP_CONTACT;
		}
		return CONTACT;
	}
	
	@Override
	public void display(Color c2, Graphics2D g2d) {
		if (active) {
			UsefulTh.displayTex(tex, x, y, w, h, c2, g2d);
		} else {
			UsefulTh.displayTex(tex2, x, y+2*UsefulTh.PIXEL_H, w, 4*UsefulTh.PIXEL_H, c2, g2d);
		}
	}
}
