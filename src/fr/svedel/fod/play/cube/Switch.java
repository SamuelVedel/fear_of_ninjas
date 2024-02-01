package fr.svedel.fod.play.cube;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.svedel.fod.UsefulTh;
import fr.svedel.fod.play.Bullet;
import fr.svedel.fod.play.Entity;
import fr.svedel.fod.play.Item;

public class Switch extends Border {
	
	private int[][] tex2;
	private double t = 1;
	private boolean active;
	
	private Switch(int x, int y, int w, int h, boolean on) {
		super(x, y, w, h, BORD, "switchOn.texture");
		type = SWITCH;
		hasAction = true;
		active = on;
		
		tex2 = UsefulTh.readTex("textures/cube/switchOff.texture");
	}
	
	public Switch(int x, int y, boolean on) {
		this(x, y, UsefulTh.cubeW, UsefulTh.cubeH, on);
	}
	
	@Override
	public void actions(double delta) {
		if (t > (5*60)) {
			active = !active;
			t = 0;
		}
		t += delta;
	}
	
	@Override
	public boolean isAGround() {
		return active;
	}
	
	@Override
	public int touched(Entity e) {
		if (active) {
			return super.touched(e);
		}
		return NO_CONTACT;
	}
	
	@Override
	public void touched(Bullet b) {
		if (active) {
			super.touched(b);
		}
	}
	
	@Override
	public int touched(Item it) {
		if (active) {
			return super.touched(it);
		}
		return NO_CONTACT;
	}
	
	@Override
	public void display(Color c2, Graphics2D g2d) {
		if (active) {
			UsefulTh.displayTex(tex, x, y, w, h, c2, g2d);	
		} else {
			UsefulTh.displayTex(tex2, x, y, w, h, c2, g2d);
		}
	}
}
