package fr.SamuelVedel.Play;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import fr.SamuelVedel.FOD.UsefulTh;

/*  ___
 * (° °)
 *  ) (
 * (( ))
 */

/**
 * represente l'entièreté des power up du jeux
 * <p>
 * Class créée le 21/08/2022
 * 
 * @author Samuel Vedel
 *
 */
public enum Power {
	
	/** augmente de 0.8 la vitesse */
	speed(0, "speed.txt", "Lapin Rapide", "Booste la vitesse"),
	
	/** augmente de 10% les chance de crit */
	crit(1, "crit.txt", "Balles Vénères", "Augmente de 10%\nles chances de critique"),
	
	/** augmente de 1 la vitese des balles */
	bulletSpeed(2, "bulletSpeed.txt", "Balles Rapides", "Augmente la\nvitesse des balles"),
	
	/** multiplie par 0.8 chaque dégat pris */
	shield(3, "shield.txt", "Bouclier un peu Stylé", "Diminue les dégâts subis"),
	
	/** 
	 * multiplie par 0.8 regen
	 * ou la met à 80 si elle est à 0
	 */
	regen(4, "regen.txt", "Coeur Puissant", "Augmente la régénération"),
	/**
	 * ajoute ça de vie à une entité {@code e} qui fait des dégats :<br>
	 * {@code dammage*(1-1/((double)e.powers[Power.vampire.num]/2+1))}
	 */
	vampire(5, "vampire.txt", "Vampire Cruel", "Recupère une partie\nde chaque dégât infligé"),
	
	/**
	 * tire 5 balles quand on tue quelqu'un<br>
	 * rajoute 2 balles par stack
	 * <p>
	 * ne marche pas sur les {@code Blob}
	 */
	deadsBullets(6, "deadsBullets.txt", "Balles de la Mort", "Des balles partent à\nl'execution d'un ennemi"),
	
	/** ajoute 10 pts de vie */
	moreLife(7, "moreLife.txt", "Grande Vitalité", "Ajoute 10 points de vie"),
	
	/** ajoute un rebond aux balles*/
	bouncingBall(8, "bounce.txt", "Balle Rebondissante", "Ajoute un rebond à\nchaque balle"),
	/**
	 * selon une chance de 100*(1-1/((double)killer.powers[Power.drinkingDuck.num]/8+1))
	 * quand un énemie meurt il fait apparaitre un canard potable
	 * et quand on boit ce canard on regagne 15 pts de vie
	 */
	drinkingDuck(9, "drinkingDuck.txt", "Canard Potable", "On peut le boire", true),
	/** ajoute un saut */
	multipleJump(10, "multipleJump.txt", "Pas de Lune", "Ajoute un saut", true),
	
	stone2Birds(11, "1stone2birds.txt", "D'une Pierre deux Coup",
				"Les balles ont +20% de\nchance de partir sur\nl'énemie le plus proche\naprès en avoir touché\nun", true),
	
	/** ne fait rien */
	nothing(12, "nothing.txt", "Rien", "Ne fait rien"),
	
	/**
	 * améliore la cadence comme ceci :
	 * <br> {@code cadence = (int)(1+(cadence-1)*0.9);}
	 * <br> {@code punchCadence = (int)(1+(punchCadence-1)*0.9);}
	 */
	cadence(13, "cadence.txt", "Cadence", "Augmente la cadence"),
	
	/** ajoute une tourelle */
	turret(14, "turret.txt", "Tourelle Fidèle", "Ajoute une tourelle\nà ton armée", true),
	
	poison(15, "poison.txt", "Fiole Empoisonné", "Mieux vaux la faire boire\nque la boire"),
	
	bomb(16, "bomb.txt", "Bomb PI/4", "Hey tu peut envoyer des\nbombes\n(ça me rappelle quelqu'un)", true),
	
	snakesOfPain(17, "snakesOfPain.txt", "Seprents de la douleure", "La douleure se matérialise\nen seprent", true, true),
	
	petrification(18, "petrification.txt", "Petrification", "+10% de chance de\npétrifier un énemies à\nchaque attaques");
	
	
	
	//strength(, "strength.txt", "Biceps Impressionant", "Agmente les dégats")
	
	public final int id;
	public final int[][] texture;
	public final boolean onlyForMe;
	public final boolean bossPower;
	
	public String name;
	/** description de l'effet du pouvoir */
	public String effect;
	
	Power(int id, String file, String name, String effect, boolean onlyForMe, boolean bossPower) {
		this.id = id;
		if (file != null) {
			this.texture = UsefulTh.readMat("textures/powers/"+file);
		} else {
			texture = new int[][] {{0}};
		}
		this.name = name;
		this.effect = effect;
		this.onlyForMe = onlyForMe;
		this.bossPower = bossPower;
	}
	
	Power(int id, String file, String name, String effect, boolean onlyForMe) {
		this(id, file, name, effect, onlyForMe, false);
	}
	
	Power(int id, String file, String name, String effect) {
		this(id, file, name, effect, false);
	}
	
	public static Power getRandPowerForMe() {
		Power pow;
		do {
			pow = Power.values()[UsefulTh.rand.nextInt(Power.values().length)];
		} while (pow.bossPower);
		return pow;
	}
	
	public static Power getRandPowerForEnemey() {
		Power pow;
		do {
			pow = Power.values()[UsefulTh.rand.nextInt(Power.values().length)];
		} while (pow.onlyForMe);
		return pow;
	}
	
	public void sayEffectInAChat(Chat cht) {
		for(String str : effect.split("\n")) {
			cht.addText("     | "+str, Font.PLAIN);
		}
	}
	
	/**
	 * affiche l'icone du pouvoir dans un rectangle noir
	 * 
	 * @param x abscisses du rectangle
	 * @param y ordonnées du rectangle
	 * @param w largeur du rectangle
	 * @param h hauteur du rectangle
	 * @param c2 deuxième couleur
	 * @param g2d
	 */
	public void display(int x, int y, int w, int h, Color c2, Graphics2D g2d) {
		int cW = 30;
//		int cH = 30;
		g2d.setColor(Color.BLACK);
		g2d.fillRect(x, y, w, h);
		
		g2d.setColor(c2);
		int border = w*2/cW;
		g2d.fillRect(x, y, w, border);
		g2d.fillRect(x+w-border, y, border, h);
		g2d.fillRect(x, y, border, h);
		g2d.fillRect(x, y+h-border, w, border);
		
		// affiche l'image tel que si w == cW
		// chaque pixel de l'icone fait un pixel de l'ecran
		double texW = w*texture[0].length/cW;
		double texH = w*texture.length/cW;
		Graphics2D g2d2 = (Graphics2D) g2d.create();
		g2d2.translate(x+w/2-texW/2, y+h/2-texH/2);
		g2d2.scale(texW/texture[0].length, texH/texture.length);
		UsefulTh.displayTex(texture, 0, 0, texture[0].length, texture.length, c2, g2d2);
		g2d2.dispose();
	}
	
	/**
	 * affiche l'icone du pouvoir dans un rectangle noir
	 * avec le nom et la description
	 * 
	 * @param x abscisses du rectangle
	 * @param y ordonnées du rectangle
	 * @param w largeur du rectangle
	 * @param h hauteur du rectangle
	 * @param c2 deuxième couleur
	 * @param g2d
	 */
	public void detailDisplay(int x, int y, int w, int h, Color c2, Graphics2D g2d) {
		display(x, y, w, h, c2, g2d);
		
		int cW = 30;
		
		g2d.setFont(new Font("ARIAL", Font.BOLD, w*4/cW));
		int textW = (UsefulTh.getTextW(name, g2d));
		int textH = (UsefulTh.getTextH(name, g2d));
		while (textW > w-2*(w*2/cW)) {
			g2d.setFont(new Font("ARIAL", Font.BOLD, g2d.getFont().getSize()-5));
			textW = (UsefulTh.getTextW(name, g2d));
			textH = (UsefulTh.getTextH(name, g2d));
		}
		UsefulTh.drawString(name, x+w/2-textW/2, y+h/5+textH/2, g2d);
		
		String[] texts = effect.split("\n");
		for (int i = 0; i < texts.length; i++) {
			String text = texts[i];
			g2d.setFont(new Font("ARIAL", Font.BOLD, w*2/cW));
			textW = (UsefulTh.getTextW(text, g2d));
			textH = (UsefulTh.getTextH(text, g2d));
			UsefulTh.drawString(text, x+w/2-textW/2, y+4*h/5+textH/2+(i-texts.length/2)*(textH+5), g2d);
		}
	}
}
