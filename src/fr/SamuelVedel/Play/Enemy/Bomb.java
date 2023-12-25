package fr.SamuelVedel.Play.Enemy;

import java.awt.Graphics2D;

import fr.SamuelVedel.FOD.UsefulTh;
import fr.SamuelVedel.Play.Entity;
import fr.SamuelVedel.Play.Me;
import fr.SamuelVedel.Play.Room;
import fr.SamuelVedel.Play.Particle.ClassicParticle;

/*  ___
 * (° °)
 *  ) (
 * (( ))
 * 
 */

/**
 * Bombes utilisé par le bombardier
 * <p>
 * Class créée le 16/01/2023
 * 
 * @author Samuel Vedel
 * @see Bomber
 *
 */
public class Bomb extends Enemy {
	private Entity shooter;
	
	private int[][] tex = UsefulTh.readTex("textures/enemies/bomb/bomb.texture");
	
	private boolean crit = false;
	
	private Bomb(Room room, double vX, double vY, int damage, Entity shooter) {
		super(room);
		type = BOMB_TYPE;
		name = "bomb";
		clan = shooter.clan;
		this.shooter = shooter;
		punchDamage = damage;
		v = vX;
		vFall = vY;
		initVar();
		
		if (UsefulTh.rand.nextInt(100) < shooter.critChance) {
			punchDamage *= 2;
			crit = true;
		}
	}
	
	public Bomb(Room room, double vX, double vY, int damage, Bomber shooter) {
		this(room, vX, vY, damage, (Entity)shooter);
		if (shooter.lookToTheRight()) x = shooter.x-w;
		else x = shooter.x+shooter.w;
		y = shooter.y+4*UsefulTh.pixelH;
	}
	
	public Bomb(Room room, double vX, double vY, int damage, Me shooter) {
		this(room, vX,vY, damage, (Entity)shooter);
		if (shooter.lookToTheRight()) x = shooter.x-w;
		else x = shooter.x+shooter.w;
		y = shooter.y+4*UsefulTh.pixelH;
	}
	
	public void initVar() {
		w = 3*UsefulTh.pixelW;
		h = 3*UsefulTh.pixelH;
	}
	
	@Override
	public void actions(double delta) {
		x += v*delta;
		y += vFall*delta;
		vFall += aFall*delta;
		
		if (collision()) explode();
		
		// collision avec moi
		if (clan != Entity.MY_CLAN) {
			if (x+w > room.me.x && x < room.me.x+room.me.w
				&& y+h > room.me.y && y < room.me.y+room.me.h) {
				explode();
			}
		}
		
		// collision avec les énemies
		for (int i = room.enemies.size()-1; i >= 0; i--) {
			Enemy en = room.enemies.get(i);
			if (x+w > en.x && x < en.x+en.w
				&& y+h > en.y && y < en.y+en.h
				&& clan != en.clan) {
				explode();
			}
		}
		
		if (crit && UsefulTh.rand.nextInt(3) == 0) {
			double pX = x+UsefulTh.pixelH/2;
			double pY = y-1.5*UsefulTh.pixelH;
			double pVX = (UsefulTh.rand.nextBoolean()? 1 : -1)*UsefulTh.rand.nextDouble();
			double pVY = (UsefulTh.rand.nextBoolean()? 1 : -1)*UsefulTh.rand.nextDouble();
			room.particles.add(new ClassicParticle(pX, pY, UsefulTh.pixelW/2, UsefulTh.pixelH/2, pVX, pVY));
		}
	}
	
	public void explode() {
		if (clan != Entity.MY_CLAN) attack(room.me);
		
		// pour la tourelle et pour si un jour j'envoie des bombes
		for (int i = room.enemies.size()-1; i >= 0; i--) {
			if (room.enemies.get(i).clan != clan) {
				attack(room.enemies.get(i));
			}
		}
		alive = false;
		
		// effet de particule
		double pX = x;
		double pY = y;
		int nParticles = /*UsefulTh.rand.nextInt(11-5)+5*/500;
		for (int i = 0; i < nParticles; i++) {
			double theta = 2*Math.PI*UsefulTh.rand.nextDouble();
			double pV = 3*UsefulTh.rand.nextDouble();
			double pVX = pV*Math.cos(theta);
			double pVY = pV*Math.sin(theta);
			room.particles.add(new ClassicParticle(pX, pY, (int)(UsefulTh.pixelW/1.5), (int)(UsefulTh.pixelH/1.5), pVX, pVY));
		}
	}
	
	public void attack(Entity e) {
		double d = Math.sqrt(Math.pow(e.x+e.w/2-x-w/2, 2)+Math.pow(e.y+e.h/2-y-h/2, 2));
		if (d < 4*UsefulTh.cubeW) {
			e.takeDammage((int)(punchDamage-d*10/UsefulTh.cubeW), shooter);
		}
	}
	
	public void takeDammage(int dammage, Entity e) {}
	
	public void display(Graphics2D g2d) {
		UsefulTh.displayTex(tex, (int)x, (int)y-2*UsefulTh.pixelH, w, h+2*UsefulTh.pixelH, play.color, g2d);
	}
}
