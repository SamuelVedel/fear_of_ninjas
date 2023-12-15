package fr.SamuelVedel.Play;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.SamuelVedel.FOD.UsefulTh;
import fr.SamuelVedel.Play.Cube.Cube;
import fr.SamuelVedel.Play.Debuff.Debuff;
import fr.SamuelVedel.Play.Debuff.Petrification;
import fr.SamuelVedel.Play.Debuff.Poison;
import fr.SamuelVedel.Play.Enemy.LilSnake;
import fr.SamuelVedel.Play.Enemy.Soul;
import fr.SamuelVedel.Play.Enemy.Turret;
import fr.SamuelVedel.Play.Particle.ClassicParticle;

/*  ___
 * (° °)
 *  ) (
 * (( ))
 */

/**
 * Class mère de toute les entité vivante du jeux.
 * <p>
 * {@code regeneration()} doit être appeler avec les action
 * des Class qui en hérite, {@code collisionWithCube()} aussi,
 * et aussi un petit {@code if (life <= 0) die();}
 * <p>
 * Class créée le 17/06/2022
 * 
 * @author Samuel Vedel
 *
 */
public abstract class Entity {
	
	public boolean alive = true;
	/** indique si l'entité a fait pop une âme en mourrant */
	public boolean diedWithASoul = false;
	
	protected boolean canDieWithASoul = true;
	protected boolean canDieWithDeathsBullets = true;
	
	protected Play play;
	protected Room room;
	
	public static final int ME_TYPE = 0;
	public static final int FLYING_HEAD_TYPE = 1;
	public static final int DARK_GUY_TYPE = 2;
	public static final int SHIELD_MAN_TYPE = 3;
	public static final int NINJA_TYPE = 4;
	public static final int BLOB_TYPE = 5;
	public static final int DRINKING_DUCK = 6;
	public static final int SUMMONER_TYPE = 7;
	public static final int LIL_HEAD_TYPE = 8;
	public static final int SOUL_TYPE = 9;
	public static final int BOMBER_TYPE = 10;
	public static final int BOMB_TYPE = 11;
	public static final int TURRET_TYPE = 12;
	public static final int BUNRI_TYPE = 13;
	public static final int LIL_SLIME_TYPE = 14;
	public static final int MED_SLIME_TYPE = 15;
	public static final int BIG_SLIME_TYPE = 16;
	public static final int SNAKE_BOSS_TYPE = 17;
	public static final int LIL_SNAKE_TYPE = 18;
	public static final int HEALER_TYPE = 19;
	public int type;
	public String name;
	
	public static final int MY_CLAN = 0;
	public static final int ENEMY_CLAN = 1;
	public int clan;
	
	public int w, h;
	public double oldX, oldY;
	public double x, y;
	
	protected double v;
	public double vFall = 0;
	protected double aFall = UsefulTh.g;
	public double vJump = -/*11.5*/12.1;
	public int numJump = 0;
	public int maxNumJump = 1;
	
	public int life;
	public int maxLife = 1;
	public double tRegen = 0;
	/**
	 * temps necessaire pour regagner un pv en 60ème de seconde <br>
	 * si{@code  = 0} : pas de régén.
	 */
	protected int regen = 0;
	
	protected int bulletW = UsefulTh.pixelW;
	protected int bulletH = UsefulTh.pixelH;
	protected int bulletV = 7;
	protected int bulletDamage = 10;
	/** décompte de temps qui sert pour tirer */
	protected double tShoot;
	/** temps entre deux tire en 60ème de seconde */
	protected int cadence;
	public int bounce = 0;
	
	protected int punchDamage = bulletDamage;
	protected int punchCadence = cadence;
	protected double tPunch;
	
	/** % de chance de coup critique */
	public int critChance = 1;
	
	/**
	 * sert à savoir si on viens de toucher
	 * un trampoline ou si avant on a toucher
	 * un autre sol pour savoir comment faire
	 * rebondir
	 */
	public boolean bumpTouch = false;
	
	public int[] powers = new int[Power.values().length];
	
	public Debuff[] debuffs;
	
	public Entity(Room room) {
		this.room = room;
		this.play = room.play;
		
		debuffs = new Debuff[] {
				new Poison(this, room),
				new Petrification(this, room)
		};
	}
	
//	protected void debuffsPreActions() {
//		for (Debuff d: debuffs) {
//			d.preAction();
//		}
//	}
	
	protected void debuffsActions(double delta) {
		for (Debuff d: debuffs) {
			d.action(delta);
		}
	}
	
	/**
	 * Gère les collisions avec les bords et les cubes
	 * <p>
	 * A appeller avec les actions
	 * 
	 * @return si il y à un contact avec un cube
	 */
	protected boolean collision() {
		// collision avec les bords
		if (x < 0) x = 0;
		else if (x+w > room.plan[0].length*UsefulTh.cubeW) x = room.plan[0].length*UsefulTh.cubeW-w;
		
		boolean contact = false;
		
		// collisions avec les potentiels cubes porches
		for (int iY = (int)(y/UsefulTh.cubeH); iY < (int)(y/UsefulTh.cubeH)+(int)(h/UsefulTh.cubeH)+2; iY++) {
			for (int iX = (int)(x/UsefulTh.cubeW); iX < (int)(x/UsefulTh.cubeW)+(int)(w/UsefulTh.cubeW)+2; iX++) {
				if (room.isYInCubes(iY) && room.isXInCubes(iX) && room.cubes[iY][iX] != null) {
					if (room.cubes[iY][iX].contact(this) != Cube.NO_CONTACT) {
						contact = true;
					}
				}
			}
		}
		return contact;
	}
	
	/**
	 * Gère la régénération
	 * <p>
	 * A appeller avec les actions
	 */
	protected void regeneration(double delta) {
		// gère la régénération
		if (regen != 0 && tRegen >= regen) {
			life++;
			if (life > maxLife) life = maxLife;
			tRegen -= regen;
		}
		tRegen += delta;
	}
	
	public void resetNumJump() {
		numJump = maxNumJump;
	}
	
	/**
	 * fait mal
	 * 
	 * @param dammage dommage prit
	 * @param e mec qui te fait mal
	 */
	public void takeDammage(int dammage, Entity e) {
		if (life > 0) { // evite de mourir plusieurs fois
			// fait les dégats
			int x = powers[Power.shield.id]/*-e.powers[Power.strength.num]*/;
			if(x >= 0) {
				dammage *= Math.pow(0.8, x);
			} else {
//				dammage += Math.log(-x+1)/Math.log(2); //log2(-x+1)
//				dammage += dammage*(1-1/((double)-e.powers[Power.strength.num]/2+1));
			}
			
			life -= dammage;
			//
			
			// effet du vampirisme
			e.life += dammage*(1-1/((double)e.powers[Power.vampire.id]/2+1));
			if (e.life > e.maxLife) e.life = e.maxLife;
			
			// poison
			if (e.powers[Power.poison.id] > 0) debuffs[Debuff.POISON_TYPE].reset(e);
			
			// petrification
			if (UsefulTh.rand.nextInt(100) < e.powers[Power.petrification.id]*10) {
				debuffs[Debuff.PETRIFICATION_TYPE].reset(e);
			}
			
			// morts
			if (life <= 0) die(e);
			
			// snakesOfPain
			for (int i = 0; i < powers[Power.snakesOfPain.id]; i++) {
				room.enemies.add(new LilSnake(this.x+i*w/powers[Power.snakesOfPain.id], y+i*w/powers[Power.snakesOfPain.id], this, room));
			}
		}
	}
	
	public void addPower(Power pow) {
		powers[pow.id]++;
		if (pow == Power.speed) {
			v += 0.8;
		} else if (pow == Power.crit) {
			critChance += 10;
		} else if (pow == Power.bulletSpeed) {
			bulletV += 1;
		} else if (pow == Power.regen) {
			if (regen != 1) {
				regen *= 0.8;
				if(regen == 0) regen = 80;
			}
		} else if (pow == Power.moreLife) {
			maxLife += 10;
			life += 10;
		} else if (pow == Power.bouncingBall) {
			bounce++;
		} else if (pow == Power.multipleJump) {
			maxNumJump++;
		} else if (pow == Power.cadence) {
			cadence = (int)(1+(cadence-1)*0.9);
			punchCadence = (int)(1+(punchCadence-1)*0.9);
		} else if (pow == Power.turret) {
			if (play.phase == Play.PLAY_PHASE) {
				room.enemies.add(new Turret(this, room));
			}
		}
		
		// il y à seulement l'ajout de pouvoir au tourelle
		// pour moi dans la classe Me
		// et de bombe aussi
		// et normalement de bulletV sur bombe
	}
	
//	public void removePower(Power pow) {
//		powers[pow.num]--;
//		if (pow == Power.speed) {
//			v -= 0.8;
//		} else if (pow == Power.crit) {
//			critChance -= 10;
//		} else if (pow == Power.bulletSpeed) {
//			bulletV -= 1;
//		} else if (pow == Power.regen) {
//			// doit pas très bien marché
//			regen /= 0.8;
//			if (regen >= 60) regen = 0;
//		} else if (pow == Power.moreLife) {
//			maxLife -= 10;
//			if (life > maxLife) life = maxLife;
//		} else if (pow == Power.bouncingBall) {
//			bounce--;
//		} else if (pow == Power.multipleJump) {
//			maxNumJump--;
//		} else if (pow == Power.cadance) {
//			// pas a jour et doit pas bien marcher
//			cadence /= 0.8;
//			punchCadence /= 0.8;
//		}
//		//y'a rien pour la tourelle
//	}
	
	/**
	 * Bah fait mourir tous simplement,
	 * avec en bonus une explosion de particule.
	 */
	public void die(Entity killer) {
		alive = false;
		int nParticles = (w+h/2);
		for (int i = 0; i < nParticles; i++) {
			double pX = x+UsefulTh.rand.nextInt(w);
			double pY = y+UsefulTh.rand.nextInt(h);
			double pVX = (UsefulTh.rand.nextBoolean()? 1 : -1)*UsefulTh.rand.nextDouble();
			double pVY = (UsefulTh.rand.nextBoolean()? 1 : -1)*UsefulTh.rand.nextDouble();
			room.particles.add(new ClassicParticle(pX, pY, UsefulTh.pixelW, UsefulTh.pixelH, pVX, pVY));
		}
		if (canDieWithASoul && UsefulTh.rand.nextInt(10) == 0) {
			room.enemies.add(new Soul(x, y, room));
			diedWithASoul = true;
		} else if (killer != null) {
			if (killer.powers[Power.deadsBullets.id] > 0 && canDieWithDeathsBullets) {
				int nOfBullets = 5+2*killer.powers[Power.deadsBullets.id]-2;
				for (int i = 0; i < nOfBullets; i++) {
					room.bullets.add(new Bullet(x+w/2, y+h/2, killer.bulletW, killer.bulletH, 2*Math.PI*UsefulTh.rand.nextDouble(), killer.bulletV, killer.bulletDamage, killer));
				}
			}
		}
	}
	
//	public static void getNearestEnemyOf(double x, double y, int clan, int[] bannedEntity, int[] bannedType, Room room) {
//		Entity nearest = null;
//		double dist = -1;
//		if (room.me.clan == clan) {
//			nearest = room.me;
//			dist = Math.sqrt(Math.pow(x-room.me.x-room.me.w/2, 2)+Math.pow(y-room.me.y-room.me.h/2, 2));
//		}
//		for (int i = 0; i < room.enemies.size(); i++) {
//			Enemy en = room.enemies.get(i);
//			if (en.clan == clan /*&& bannedEntity.*/ && bannedType.indexOf) {
//				double d = Math.sqrt(Math.pow(x+w/2-en.x-en.w/2, 2)+Math.pow(y+h/2-en.y-en.h/2, 2));
//				if ((d < dist || nearest[j] == null) && d > minDist && d < distOfHeal) {
//					nearest[j] = en;
//					dist = d;
//				}
//			}
//		}
//	}
	
//	public abstract boolean lookToTheRight();
	
	/**
	 * Affiche la barre de vie, centré au dessus
	 * de la tête de l'entité et placé à 30% de 
	 * la hauteur de la barre au dessus de la
	 * tête de l'entité, et une bordure d'un pixel.
	 * Avec une largeur supérieur de 2 pixels à
	 * celle d'un cube et une hauteur de 3 pixels.
	 * 
	 * @param g2d {@code Graphics2D} sur lequel il dessine
	 */
	protected void displayLife(Graphics2D g2d) {
		displayLife(w+2*UsefulTh.pixelW, 3*UsefulTh.pixelH, g2d);
	}
	
	/**
	 * Affiche la barre de vie, centré au dessus
	 * de la tête de l'entité et placé à 30% de 
	 * la hauteur de la barre au dessus de la
	 * tête de l'entité, et une bordure d'un pixel.
	 * 
	 * @param lW largeur de la barre de vie
	 * @param lH hauteur de la barre de vie
	 * @param g2d {@code Graphics2D} sur lequel il dessine
	 */
	protected void displayLife(int lW, int lH, Graphics2D g2d) {
		displayLife((int)(x+w/2-lW/2), (int)(y-1.3*lH), lW, lH, UsefulTh.pixelW, g2d);
	}
	
	/**
	 * Affiche la barre de vie.
	 * 
	 * @param lX abscisses de la barre de vie
	 * @param lY ordonnées de la barre de vie
	 * @param lW largeur de la barre de vie
	 * @param lH hauteur de la barre de vie
	 * @param border taille de la bordure
	 * @param g2d {@code Graphics2D} sur lequel il dessine
	 */
	protected void displayLife(int lX, int lY, int lW, int lH, int border, Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(lX, lY, lW, lH);
		g2d.setColor(Color.DARK_GRAY);
		g2d.fillRect(lX+border/*+(int)((lW-2*border)*((double)life/maxLife))*/, lY+border, lW-2*border/*-(int)((lW-2*border)*((double)life/maxLife))*/, lH-2*border);
		if ((double)life/maxLife > 0.2) g2d.setColor(Color.GREEN);
		else g2d.setColor(Color.RED);
		g2d.fillRect(lX+border, lY+border, (int)((lW-2*border)*((double)life/maxLife)), (lH-2*border));
	}
}
