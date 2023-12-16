package fr.SamuelVedel.Play;

import java.awt.Graphics2D;

import fr.SamuelVedel.FOD.UsefulTh;
import fr.SamuelVedel.Play.Enemy.Bomb;
import fr.SamuelVedel.Play.Enemy.Enemy;
import fr.SamuelVedel.Play.Particle.ClassicParticle;

/*  ___
 * (° °)
 *  ) (
 * (( ))
 */

/**
 * Class qui correspond au balles qui vollerons normalement
 * dans les quatres coins des salles. les collsisions
 * <p>
 * Class créée le 24/06/2022
 * 
 * @author Samuel Vedel
 *
 */
public class Bullet {
	
	private Play play;
	private Room room;
	public Entity shooter;
	/** 
	 * cet énemies de crain pas cette balle,
	 * c'est important pour coder le pouvoir
	 * stone2Birds
	 */
	public Entity safeEnemy = null;
	
	public int w, h;
	public double oldX, oldY;
	public double x, y;
	public double v;
	public double vX, vY;
	public int damage;
	private boolean crit = false;
	
	public int bounce;
	
	public boolean noMoreReasonToBe = false;
	
	/**
	 * Bah initialise l'objet quoi
	 * 
	 * @param x abscisses
	 * @param y ordonnées
	 * @param w largeur
	 * @param h hauteur
	 * @param alpha angle en radian
	 * @param v vitesse
	 * @param damage degat
	 */
	public Bullet(double x, double y, int w, int h, double alpha, double v, int damage, Entity shooter) {
		this.x = x-w/2;
		this.y = y-h/2;
		this.w = w;
		this.h = h;
		this.damage = damage;
		this.shooter = shooter;
		room = shooter.room;
		play = room.play;
		
		this.v = v;
		vX = v*Math.cos(alpha);
		vY = v*Math.sin(alpha);
		
		bounce = shooter.bounce;
		
		if (UsefulTh.rand.nextInt(100) < shooter.critChance) {
			this.damage *= 2;
			crit = true;
		}
	}
	
	public Bullet(double x, double y, double alpha, Entity shooter) {
		this(x, y, shooter.bulletW, shooter.bulletH, alpha, shooter.bulletV, shooter.bulletDamage, shooter);
	}
	
	/**
	 * Effectue les actions de la balle
	 */
	public void actions(double delta) {
		// gère l'avancé
		oldX = x;
		oldY = y;
		x += vX*delta;
		y += vY*delta;
		
		// gère les collisions avec les cubes
		if (room.isXInCubes((int) (x/UsefulTh.cubeW)) && room.isYInCubes((int) (y/UsefulTh.cubeH))) {
			if (room.cubes[(int) (y/UsefulTh.cubeH)][(int) (x/UsefulTh.cubeW)] != null) {
				room.cubes[(int) (y/UsefulTh.cubeH)][(int) (x/UsefulTh.cubeW)].contact(this);
			}
		} else {
			noMoreReasonToBe = true;
		}
		
		// gère le fait de transpercer des gens
		contact(shooter.room.me);
		for (int i = 0; i < room.enemies.size(); i++) {
			contact(room.enemies.get(i));
			if (noMoreReasonToBe) break;
		}
		
		// gère l'émision de particules en cas de coup critique
		//if (crit && UsefulTh.rand.nextInt(3) == 0) {
		if (crit && UsefulTh.rand.nextDouble() < ((double)1/3)*delta) {
			double pX = x;
			double pY = y;
			double pVX = (UsefulTh.rand.nextBoolean()? 1 : -1)*UsefulTh.rand.nextDouble();
			double pVY = (UsefulTh.rand.nextBoolean()? 1 : -1)*UsefulTh.rand.nextDouble();
			room.particles.add(new ClassicParticle(pX, pY, UsefulTh.pixelW/2, UsefulTh.pixelH/2, pVX, pVY));
		}
	}
	
	/**
	 * check si il y à collision avec {@code e}, si oui
	 * {@code de} prend des dommage et la balle disparait
	 * 
	 * @param e l'entité ou on check si y'a collision
	 */
	public void contact(Entity e) {
		if (e.clan != shooter.clan && e != safeEnemy) {
			if (e.x+e.w > x && e.x < x
				&& e.y+e.h > y && e.y < y) { // touché
				boolean canHit = true; // ce boolean sert pour les shieldMan
				if (e.type == Entity.SHIELD_MAN_TYPE) {
					Enemy en = (Enemy) e;
					if ((en.direction > 0 && oldX > en.oldX+en.w)
						|| (en.direction < 0 && oldX < en.oldX)) {
						canHit = false;
					}
				} else if (e.type == Entity.BOMB_TYPE) {
					Bomb b = (Bomb) e;
					b.explode();
				
				}
				if (canHit) {
					e.takeDammage(damage, shooter);
					
					// code que fait marcher le pouvoir stone2Birds
					if (UsefulTh.rand.nextInt(100) < shooter.powers[Power.STONE2BIRDS.id]*20) {
						Enemy closer = null;
						double d = -1;
						for (int i = 0; i < room.enemies.size(); i++) {
							Enemy en = room.enemies.get(i);
							if (en != e && en.clan != shooter.clan) {
								double d2 = Math.sqrt(Math.pow(e.x-en.x, 2)+Math.pow(e.y-en.y, 2));
								if (d2 < d || d < 0) {
									closer = en;
									d = d2;
								}
							}
						}
						if (closer != null) {
							double x1 = x+w/2;
							double y1 = y+h/2;
							double x2 = closer.x+closer.w/2;
							double y2 = closer.y+closer.h/2;
							if (x1 != x2 || y1 != y2) {
								double alpha = UsefulTh.getAlpha(x1, y1, x2, y2);
								vX = v*Math.cos(alpha);
								vY = v*Math.sin(alpha);
							}
							safeEnemy = e;
						} else destroy();
						
					} else {
						destroy();
					}
				} else {
					if (bounce <= 0) {
						destroy();
					} else {
						x -= vX;
						y -= vY;
						vX *= -1;
						bounce--;
					}
				}
			}
		}
	}
	
	/**
	 * check si il y à collisison avec {@code b}, si oui
	 * les deux balles disparaissent
	 * 
	 * @param b balle ou on check si y'a collisison
	 */
	public void contact(Bullet b) {
		if (b.shooter != shooter) {
			// on récupère la plus pettite dimension
			// des balle entre width et height
			int w1 = (w<h? w : h);
			int w2 = (b.w<b.h? b.w : b.h);
			
			if (x+w1/2 > b.x-w2/2 && x-w1/2 < b.x+w2/2
				&& y+w1/2 > b.y-w2/2 && y-w1/2 < b.y+w2) { // touché
				destroy();
				b.destroy();
			}
		}
	}
	
	public void destroy() {
		double pX = x;
		double pY = y;
		int nParticles = UsefulTh.rand.nextInt(11-5)+5;
		for (int i = 0; i < nParticles; i++) {
			double pVX = (UsefulTh.rand.nextBoolean()? 1 : -1)*UsefulTh.rand.nextDouble();
			double pVY = (UsefulTh.rand.nextBoolean()? 1 : -1)*UsefulTh.rand.nextDouble();
			room.particles.add(new ClassicParticle(pX, pY, (int)(UsefulTh.pixelW/1.5), (int)(UsefulTh.pixelH/1.5), pVX, pVY));
		}
		noMoreReasonToBe = true;
	}
	
	/**
	 * Affiche la balle
	 * 
	 * @param g2d {@code Graphics2D} sur lequel il dessine
	 */
	public void display(Graphics2D g2d) {
		g2d.setColor(play.color);
		g2d.fillRect((int)x, (int)y, w, h);
	}
}
