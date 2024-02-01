package fr.svedel.fod.play.enemy;

import java.awt.Graphics2D;

import fr.svedel.fod.UsefulTh;
import fr.svedel.fod.play.Bullet;
import fr.svedel.fod.play.Entity;
import fr.svedel.fod.play.Item;
import fr.svedel.fod.play.Power;
import fr.svedel.fod.play.Room;
import fr.svedel.fod.play.cube.Cube;

/*  ___
 * (� �)
 *  ) (
 * (( ))
 */

/**
 * Class dont vont h�riter tout les �nemies
 * <p>
 * Class cr��e le 28/06/2022 (vers 2h shhhh)
 * 
 * @author Samuel Vedel
 *
 */
public abstract class Enemy extends Entity {
	
	/**
	 * indique la direction pour les �nemies qui marchent
	 * <ul>
	 * <li>{@code 1} : droite</li>
	 * <li>{@code 0} : bouge pas</li>
	 * <li>{@code -1} : gauche</li>
	 * </ul>
	 */
	public int direction = 0;
	protected double alpha;
	
	protected int viewDistance = 15*UsefulTh.cubeW;
	protected Entity target;
	protected boolean canSawTarget = false;;
	protected boolean followTarget = true;
	
	protected boolean dieWithAnIncrementationOfScore = true;
	protected boolean canDieWithAnItem = true;
	protected boolean canDieWithADrinkingDuck = true;
	
	public Enemy(Room room) {
		super(room);
		clan = Entity.ENEMY_CLAN;
		target = room.me;
	}
	
	protected void spawnToFly() {
		for (int iX = UsefulTh.rand.nextInt(room.cubes[0].length), iY = UsefulTh.rand.nextInt(room.cubes.length);;
				iX = UsefulTh.rand.nextInt(room.cubes[0].length), iY = UsefulTh.rand.nextInt(room.cubes.length)) {
			if(room.cubes[iY][iX] == null
			   && Math.sqrt(Math.pow(room.me.x+room.me.w/2-iX*UsefulTh.cubeW+w/2, 2)+Math.pow(room.me.y+room.me.h/2-iY*UsefulTh.cubeH+h/2, 2)) >= 7*UsefulTh.cubeW) {
				x = iX*UsefulTh.cubeW;
				y = iY*UsefulTh.cubeH;
				if (!collision()) break;
			}
		}
	}
	
	protected void spawnToWalk() {
		for (int iX = 0, iY = 0;; iX = UsefulTh.rand.nextInt(room.cubes[0].length), iY = UsefulTh.rand.nextInt(room.cubes.length)) {
			if(room.cubes[iY][iX] != null
			   && Math.sqrt(Math.pow(room.me.x+room.me.w/2-iX*UsefulTh.cubeW+w/2, 2)+Math.pow(room.me.y+room.me.h/2-iY*UsefulTh.cubeH+h/2, 2)) >= 7*UsefulTh.cubeW) {
				Cube c = room.cubes[iY][iX];
				if (c.isAGround()) { // c'est un sol
					x = iX*UsefulTh.cubeW;
					y = iY*UsefulTh.cubeH-h;
					if (!collision()) break;
				}
			}
		}
	}
	/** prend les am�lioration des �nnemies */
	protected void takeEnemiesPowers() {
		for (int i = 0; i < room.enemiesPowers.length; i++) {
			for (int j = 0; j < room.enemiesPowers[i]; j++) {
				addPower(Power.values()[i]);
			}
		}
	}
	
	public void actions(double delta) {
//		debuffsPreActions();
		
		regeneration(delta);
		
		if (!canSeeTarget()) {
			followTarget = false;
			canSawTarget = false;
		} else if (!canSawTarget) {
			followTarget = true;
			canSawTarget = true;
		}
		
		oldX = x;
		oldY = y;
		
		if (y >= room.height && type != SOUL_TYPE) die(this);
	}
	
	/**
	 * action � effectuer � la fin
	 */
	protected void endOfActions(double delta) {
		debuffsActions(delta);
	}
	
	/**
	 * actions que vont executer les enemies volant pour voler
	 * <p>
	 * � appeler avec les actions
	 */
	protected void actionToFly(double delta) {
		// g�re le mouvement
		if (followTarget) {
			alpha = UsefulTh.getAlpha(x, y, target.x, target.y);
		}
		double vX = v*Math.cos(alpha);
		double vY = v*Math.sin(alpha);
		x += vX*delta;
		y += vY*delta;
		
		collision();
		
		// collision avec le haut et le bas
		if (y < 0) y = 0;
		else if (y+h > room.plan.length*UsefulTh.cubeH) y = room.plan.length*UsefulTh.cubeH-h;
		
		// revoie la trajectoire pour pas �tre bloqu� si il ne voit pas la cible
		if (!followTarget) {
			if (oldX == x || oldY == y) alpha = 2*Math.PI*UsefulTh.rand.nextDouble();
		} else if (x == oldX && y == oldY) {
			followTarget = false;
			alpha = 2*Math.PI*UsefulTh.rand.nextDouble();
		}
	}
	
	/**
	 * actions que vont evectuer les �nemies qui marchent pour marcher
	 * <p>
	 * � appeler avec les actions
	 */
	protected void actionToWalk(double delta) {
		walkFollowTarget();
		// g�re le mouvement
		walkAndFall(delta);
		collision();
		// evite les chutes dans le vide
		avoidFalling();
		// evite de rester bloqu� contre un mur
		avoidBeingStuck();
	}
	
	// ---- diff�rente fonction pour la fonction actionToWalk ----
	
	/** g�re la fa�on dont l'�nemie me suit */
	protected void walkFollowTarget() {
		if (followTarget) {
			if (target.x > x) direction = 1;
			else direction = -1;
			if (Math.sqrt((target.x-x)*(target.x-x)) < v) {
				direction = 0;
			}
		} else if (direction == 0) {
			direction = (UsefulTh.rand.nextBoolean()? 1 : -1);
		}
	}
	
	/** fait marcher et tomb� l'�nemie (g�re le mouvement quoi) */
	protected void walkAndFall(double delta) {
//		if (vFall == 0) numJump = maxNumJump;
		x += direction*v*delta;
		y += vFall*delta;
		vFall += aFall*delta;
	}
	
	/** �vite qu'un �nemie fonce dans le vide */
	protected void avoidFalling() {
		int iX = (int)((x+(direction > 0? w : 0))/UsefulTh.cubeW);
		int iY = (int)(y/UsefulTh.cubeH)+2;
		if (room.isXInCubes(iX) && room.isYInCubes(iY) && room.cubes[iY][iX] == null) { // si il y � le vide devant
			// regarde si il peut se laisser tomb�
			boolean willFall = true;
			for (int jX = 0; jX < 2 && room.isXInCubes(iX+(direction > 0? jX : -jX)); jX++) {
				for (int jY = (jX == 1? 0 : 2); room.isYInCubes(iY+jY); jY++) {
					if (room.cubes[iY+jY][iX+(direction > 0? jX : -jX)] != null) {
						Cube c = room.cubes[iY+jY][iX+(direction > 0? jX : -jX)];
						if (c.isAGround()) { // c'est un sol
							willFall = false;
							break;
						}
					}
				}
			}
			if (willFall) { // sinon regarde si il peut sauter
				if (needToJump()) jump();
				else {
					followTarget = false;
					direction = -direction;
				}
			}
		}
	}
	
	/**
	 * Evite pour les �nemies qui marchent de rester bloqu�
	 * contre un mur, et du coup soit de sauter si il y � quelque
	 * chose au dessus, sinon de faire demi-tour et d'arr�ter de
	 * me suivre
	 */
	protected void avoidBeingStuck() {
		if (oldX == x && direction != 0) {
			if (needToJump()) jump();
			else {
				followTarget = false;
				direction = -direction;
			}
		}
	}
	
	// ---- voil� ----
	
	/**
	 * teste si la distance entre l'enemie et moi
	 * est inf�rieur ou �gal � {@code viewDistance}
	 * 
	 * @return se il me voit
	 */
	protected boolean canSeeTarget() {
		return Math.sqrt(Math.pow(target.x-x, 2)+Math.pow(target.y-y, 2)) <= viewDistance;
	}
	
	/**
	 * appelle la fonction {@code punch(Entity e)} pour moi,
	 * puis pour tout les �nemies d'un clan diff�rent,
	 * c'est � dire les tourelles (aussi les canard potable
	 * mais leurs fonction {@code takeDammage} est vide donc
	 * on s'en fout)
	 */
	protected void punch(double delta) {
		if (clan != MY_CLAN) punch(room.me, delta);
		for (int i = room.enemies.size()-1; i >= 0; i--) {
			if (i < room.enemies.size() && room.enemies.get(i).clan != clan) {
				punch(room.enemies.get(i), delta);
			}
		}
	}
	
	/**
	 * Fait {@code punchDammage} d�gat � {@code e} si ils se
	 * touchent toute les {@code punchCadence} 60�me de seconde
	 * 
	 * @param e entit� � laquel il veut foutre des gros pain
	 */
	protected void punch(Entity e, double delta) {
		if (e.x+e.w > x && e.x < x+w
			&& e.y+e.h > y && e.y < y+h) { // touch�
			if (tPunch >= punchCadence) {
				if (UsefulTh.rand.nextInt(100) < critChance) {
					e.takeDammage(punchDamage*2, this);
				} else {
					e.takeDammage(punchDamage, this);
				}
				tPunch -= punchCadence;
			}
			tPunch += delta;
		} else if (tPunch < punchCadence) {
			tPunch += delta;
			if (tPunch > punchCadence) tPunch = punchCadence;
		}
	}
	
	/**
	 * G�re le tir
	 * <p>
	 * A appeler avec les actions pour les �nemies qui tirent
	 */
	protected void shoot(Entity e, double delta) {
		if (Math.sqrt(Math.pow(e.x-x, 2)+Math.pow(e.y-y, 2)) <= viewDistance) {
			if (tShoot >= cadence) {
				double x1 = x+w/2;
				double y1 = y+h/2;
				double x2 = e.x+e.w/2;
				double y2 = e.y+e.h/2;
				if (x1 != x2 || y1 != y2) {
					double alpha = UsefulTh.getAlpha(x1, y1, x2, y2);
					room.bullets.add(new Bullet(x+w/2-bulletW/2, y+h/2-bulletH/2, alpha, this));
				}
				tShoot -= cadence;
			}
			tShoot += delta;
		} else if (tShoot < cadence) {
			tShoot += delta;
			if (tShoot > cadence) tShoot = cadence;
		}
	}
	
	/**
	 * retourne si l'enemie r�-atteri sur quelque chose au-dessus
	 * s'il saute
	 * 
	 * @return je viens de le dire enfaite !
	 */
	public boolean needToJump() {
		int iX = (int)((x+(direction > 0? w : 0))/UsefulTh.cubeW);
		int iY = (int)((y+h)/UsefulTh.cubeH);
		if (room.isXInCubes(iX) && room.isYInCubes(iY)) {
			for (int jX = (direction > 0? 0 : 1); jX < 2 && room.isXInCubes(iX+(direction > 0? jX : -jX)); jX++) {
				for (int jY = 1; jY >= -3 && room.isYInCubes(iY+jY); jY--) {
					if (room.cubes[iY+jY][iX+(direction > 0? jX : -jX)] != null) {
						Cube c = room.cubes[iY+jY][iX+(direction > 0? jX : -jX)];
						if (c.isAGround()) { // c'est un sol
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * effetue un saut si l'enemie est au sol
	 */
	public void jump() {
		if (numJump != 0) {
			vFall = vJump;
			numJump--;
		}
	}
	
//	public void takeDammage(int dammage, Entity e) {
//		super.takeDammage(dammage, e);
//	}
	
	@Override
	public void die(Entity killer) {
		super.die(killer);
		if (killer.clan == MY_CLAN) {
			if (!room.isABossLevel && dieWithAnIncrementationOfScore) room.numOfKills += 1;
			if (!diedWithASoul) {
				if (canDieWithAnItem && UsefulTh.rand.nextInt(100) == 0) {
					room.items.add(new Item((int)x+w/2, (int)y+h, room));
				}
				if (canDieWithADrinkingDuck && UsefulTh.rand.nextInt(100) < 100*(1-1/((double)killer.powers[Power.DRINKING_DUCK.id]/9+1))) {
					room.enemies.add(new DrinkingDuck(room, x+w/2, y+h));
				}
			}
		}
	}
	
	public abstract void display(Graphics2D g2d);
}
