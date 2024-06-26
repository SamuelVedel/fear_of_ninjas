package fr.svedel.fod.play;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import fr.svedel.fod.UsefulTh;
import fr.svedel.fod.play.addskill.AddSkill;
import fr.svedel.fod.play.addskill.BombSkill;
import fr.svedel.fod.play.addskill.TpSkill;
import fr.svedel.fod.play.debuff.Debuff;
import fr.svedel.fod.play.enemy.Enemy;
import fr.svedel.fod.play.particle.ClassicParticle;

/*  ___
 * (� �)
 *  ) (
 * (( ))
 */

/**
 * Class du personnage jouable
 * <p>
 * Class cr��e le 17/06/2022
 * 
 * @author Samuel Vedel
 *
 */
public class Me extends Entity {
	
	public int spawnX, spawnY;
	
	private boolean rightPress = false;
	private boolean leftPress = false;
	private boolean jumpPress = false;
	
	/**
	 * tableau contenant les textures
	 * de l'animation de marche
	 * qui sera surement la seule animation
	 */
	private int[][][] textures = new int[6][12][6];
	private int iTex = 0;
	private double tTex;
	/** temps entre deux changement de textures quand on marche */
	private final int vTex = 5;
	
	public int lifeBarX;
	public int lifeBarY;
	private final int lifeBarW = 45*UsefulTh.PIXEL_W;
	private final int lifeBarH = 9*UsefulTh.PIXEL_H;
	
	/**
	 * {@code boolean} qui indique s'il faut tirer (ou pas)
	 * (le nom est mal choisis)
	 * <ul>
	 * <li>{@code true} quand la sourie est presser</li>
	 * <li>{@code false} sinon</li>
	 * </ul>
	 */
	public boolean canShoot = false;
	
	public AddSkill[] addSkills;
	
	public Me(Room room) {
		super(room);
		type = ME_TYPE;
		name = "moi";
		initVar();
		initAddSkills();
		initTextures();
	}
	
	/**
	 * initialise les variables de bases.
	 */
	private void initVar() {
		clan = Entity.MY_CLAN;
		w = UsefulTh.CUBE_W;
		h = 2*UsefulTh.CUBE_H;
		v = 4./5*UsefulTh.PIXEL_W;
//		vJump = -11.5;
		maxLife = 100;
		life = maxLife;
		regen = 80;
		cadence = 30;
	}
	
	private void initAddSkills() {
		addSkills = new AddSkill[] {
			new BombSkill(this, room),
			new TpSkill(this)
		};
	}
	
	/**
	 * initialise les diff�rente textures qui servirons � animer
	 * le preso en remplissant {@code textures} des diff�rentes textures
	 * que peut prendre le personnage
	 * <p>
	 * <ul>
	 * en fonction des indices :
	 * <li>0 : bouge pas regarde � droite</li>
	 * <li>1-2 : marche vers la droite</li>
	 * <li>3 : bouge pas regarde � gauche</li>
	 * <li>4-5 : marche vers la gauche</li>
	 * </ul>
	 */
	private void initTextures() {
		for (int i = 0; i < textures.length/2; i++) {
			textures[i] = UsefulTh.readTex("textures/me/me"+i+".texture");
		}
		for (int i = textures.length/2; i < textures.length; i++) {
			textures[i] = UsefulTh.reverseXTex(textures[i-3]);
		}
	}
	
	/**
	 * effecute les actions
	 */
	public void actions(double delta) {
//		debuffsPreActions();
		
		regeneration(delta);
		
		// enregistre les ancienne coordonn�es
		oldX = x;
		oldY = y;
		
		// g�re la marche
		if (rightPress && !leftPress) { // doit aller � droite
			x += v*delta;
			// g�re l'animation
			if (tTex > vTex) {
				iTex++;
				if (iTex >= textures.length/2) iTex = 1;
				tTex -= vTex;
			}
			tTex += 1*delta;
		} else if (leftPress && !rightPress) { // doit aller � gauche
			x -= v*delta;
			// g�re l'animation
			if (tTex >= vTex) {
				iTex++;
				if (iTex < textures.length/2 || iTex >= textures.length) {
					iTex = textures.length/2+1;
				}
				tTex -= vTex;
			}
			tTex += 1*delta;
		}
		
		// g�re le saut et la chute
//		if (vFall == 0) numJump = maxNumJump;
		if (jumpPress) {
			if (numJump > 0) {
				vFall = vJump;
				jumpPress = false;
				numJump--;
			}
		}
		y += vFall*delta;
		vFall += aFall*delta;
		
		// g�re les possibles collisions
		collision();
		
		// g�re la chute dans le vide
		if (y > room.height) { // tomb� hors de la carte
			life /= 2;
			x = spawnX;
			y = spawnY;
		}
		
		// g�re le tire
//		canShoot = true;
		if (canShoot) {
			if (tShoot >= cadence) {
				double x1 = x+w/2;
				double y1 = y+h/2;
				double x2 = room.getMouseXInRoom();
				double y2 = room.getMouseYInRoom();
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
		if (!room.msPressed && canShoot) canShoot = false;
		
		debuffsActions(delta);
		
		// progression des cooldown des addSkills
		for (AddSkill as : addSkills) {
			as.progress(delta);
		}
	}
	
	public void addPower(Power pow) {
		super.addPower(pow);
		// ajoute le pouvoir aux tourelles
		if (pow != Power.TURRET) {
			for (int i = 0; i < room.enemies.size(); i++) {
				Enemy en = room.enemies.get(i);
				if (en.type == TURRET_TYPE && en.clan == clan) {
					en.addPower(pow);
				}
			}
		}
		
		// pouvoir bombe
		if (pow == Power.BOMB) {
			addSkills[AddSkill.BOMB_TYPE].addOne();
		}
		
		// pouvoir TP
		if (pow == Power.TP) {
			addSkills[AddSkill.TP_TYPE].addOne();
		}
	}
	
	public void resetAddSkillsProgession() {
		for (AddSkill as: addSkills) {
			as.resetUse();
		}
	}

	public void cancelAllDebuffs() {
		for (Debuff deb: debuffs) {
			deb.cancel();
		}
	}
	
	/**
	 * g�re l'activation des boolean nessesaire au mouvement
	 * <p>
	 * � mettre dans le {@code keyPressed(...)} d'un {@code KeyListener}
	 * 
	 * @param keyCode touche appuyer
	 */
	public void move(int keyCode) {
		if (play.phase == Play.PLAY_PHASE) {
			if (keyCode == 68) rightPress = true;
			else if (keyCode == 81) leftPress = true;
			else if (keyCode == 32/* || keyCode == 90*/) jumpPress = true;
		}
	}
	
	/**
	 * g�re la d�sactivation des boolean nessesaire au mouvement
	 * <p>
	 * � mettre dans le {@code keyReleased(...)} d'un {@code KeyListener}
	 * 
	 * @param keyCode touche relach�
	 */
	public void stopMove(int keyCode) {
		if (keyCode == 68) {
			rightPress = false;
			if (!leftPress) iTex = 0;
		}
		else if (keyCode == 81) {
			leftPress = false;
			if (!rightPress) iTex = textures.length/2;
		}
		else if (keyCode == 32) jumpPress = false;
	}
	
	public void addSkillsKeyReac(int keyCode) {
		if (play.phase == Play.PLAY_PHASE) {
			for (AddSkill as: addSkills) {
				as.keyReaction(keyCode);
			}
		}
	}
	
	public void addSkillsMouseReac(int button) {
		if (play.phase == Play.PLAY_PHASE) {
			for (AddSkill as: addSkills) {
				as.mouseReaction(button);
			}
		}
	}
	
	public boolean lookToTheRight() {
		return iTex < textures.length/2;
	}
	
	public void tp() {
		// effet de particule
		int nParticles = (w+h/2);
		for (int i = 0; i < nParticles; i++) {
			double pX = x+UsefulTh.rand.nextInt(w);
			double pY = y+UsefulTh.rand.nextInt(h);
			double pVX = (UsefulTh.rand.nextBoolean()? 1 : -1)*UsefulTh.rand.nextDouble()
						 *0.2*UsefulTh.PIXEL_W;
			double pVY = (UsefulTh.rand.nextBoolean()? 1 : -1)*UsefulTh.rand.nextDouble()
						 *0.2*UsefulTh.PIXEL_H;
			room.particles.add(new ClassicParticle(pX, pY, UsefulTh.PIXEL_W/2,
												   UsefulTh.PIXEL_H/2, pVX, pVY));
		}
		
		x = room.getMouseXInRoom()-w/2;
		y = room.getMouseYInRoom()-h/2;
	}
	
	@Override
	public void die(Entity killer) {
		super.die(killer);
		room.cht.addText("un(e) "+killer.name+" vient de vous tuer");
		play.die();
	}
	
	public void displayMyLife(Graphics2D g2d) {
		lifeBarX = (int)(20*play.scaleW);
		lifeBarY = (int)(play.playP.getHeight()-(lifeBarH+20)*play.scaleW);
		int lW = (int)(lifeBarW*play.scaleW);
		int lH = (int)(lifeBarH*play.scaleW);
		displayLife(lifeBarX, lifeBarY, lW, lH, (int)((2*UsefulTh.PIXEL_W)*play.scaleW), g2d);
		
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("ARIAL", Font.BOLD, (int)(25*play.scaleW)));
		String text = life+"/"+maxLife;
		int textW = UsefulTh.getTextW(text, g2d);
		int textH = UsefulTh.getTextH(text, g2d);
		UsefulTh.drawString(text, lifeBarX+lW/2-textW/2, lifeBarY+lH/2+textH/2, g2d);
	}
	
	/**
	 * rafraichi Me pour le chagement de level
	 */
	public void refresh() {
		life = maxLife;
		resetAddSkillsProgession();
		cancelAllDebuffs();
		vFall = 0;
	}
	
	/**
	 * affiche le personnage
	 * 
	 * @param g2d {@code Graphics2D} lequel il dessine
	 */
	public void display(Graphics2D g2d) {
//		UsefulTh.drawSemiLine((int)x+w/2, (int)y+h/2, (int)((play.mouseX-room.transX)/play.scaleW), (int)((play.mouseY-room.transY)/play.scaleW), play.color, g2d);
		if (alive) {
			UsefulTh.displayTex(textures[iTex], (int)x, (int)y, w, h, play.color, g2d);
		}
	}
}
