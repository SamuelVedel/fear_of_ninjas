package fr.SamuelVedel.Play.Cube;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.SamuelVedel.FOD.UsefulTh;
import fr.SamuelVedel.Play.Entity;

public class Bump extends Cube {
	
	private int[][] tex2;
	private int t = 0;
	private boolean active = true;;
	
	private Bump(int x, int y, int w, int h) {
		super(x, y, w, h, BUMP, "bump0.txt");
		hasAction = true;
		tex2 = UsefulTh.readTex("textures/cube/bump1.txt");
	}
	
	public Bump(int x, int y) {
		this(x, y, UsefulTh.cubeW, UsefulTh.cubeH);
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
			UsefulTh.displayTex(tex2, x, y+2*UsefulTh.pixelH, w, 4*UsefulTh.pixelH, c2, g2d);
		}
	}
}
