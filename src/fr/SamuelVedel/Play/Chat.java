package fr.SamuelVedel.Play;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.JOptionPane;

import fr.SamuelVedel.FOD.UsefulTh;
import fr.SamuelVedel.Play.Enemy.BigSlime;
import fr.SamuelVedel.Play.Enemy.Blob;
import fr.SamuelVedel.Play.Enemy.Bomber;
import fr.SamuelVedel.Play.Enemy.DarkGuy;
import fr.SamuelVedel.Play.Enemy.FlyingHead;
import fr.SamuelVedel.Play.Enemy.Healer;
import fr.SamuelVedel.Play.Enemy.LilHead;
import fr.SamuelVedel.Play.Enemy.LilSlime;
import fr.SamuelVedel.Play.Enemy.LilSnake;
import fr.SamuelVedel.Play.Enemy.MedSlime;
import fr.SamuelVedel.Play.Enemy.Ninja;
import fr.SamuelVedel.Play.Enemy.ShieldMan;
import fr.SamuelVedel.Play.Enemy.Soul;
import fr.SamuelVedel.Play.Enemy.Summoner;

/*  /\_/\
 * (=°-°=) )
 *  )   ( (
 * (_____))
 */

/**
 * Class créée le 10/02/2023
 * 
 * @author Samuel Vedel
 *
 */
public class Chat {
	
	private Play play;
	private Room room;
	
	private Message[] msgs = new Message[10];
	private int textSize = 12;
	
	private int gap;
	private int rectW;
	private int rectH;
	
	public Chat(Room room) {
		this.room = room;
		this.play = room.play;
	}
	
	private class Message {
		private String text;
		private double time = 0;
		private static final int MAX_TIME = 7*60; // 7 secondes
		private int w = 0;
		private int h = 0;
		
		private int fontStyle;
		
		private Message(String text, int fontStyle) {
			this.text = text;
			this.fontStyle = fontStyle;
		}
		
		private void actions(double delta) {
			time += delta;
		}
		
		private void setWH(Graphics2D g2d) {
			if (w == 0 && h == 0) {
				w = UsefulTh.getTextW(text, g2d);
				h = UsefulTh.getTextH(text, g2d);
			}
		}
		
		private void display(int x, int y, Graphics2D g2d) {
			g2d.setFont(new Font("ARIAL", fontStyle, (int)(textSize*play.scaleW)));
			Color c = play.color;
			g2d.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(((double)(MAX_TIME-time))/MAX_TIME*255)));
			UsefulTh.drawString(text, x, y, g2d);
		}
	}
	
	public void actions(double delta) {
		for (int i = 0; i < msgs.length; i++) {
			if (msgs[i] != null) {
				msgs[i].actions(delta);
				if (msgs[i].time > Message.MAX_TIME) {
					msgs[i] = null;
				}
			}
		}
	}
	
	public void addText(String text) {
		addText(text, Font.BOLD);
	}
	
	public void addText(String text, int fontStyle) {
		// décale tout les messages
		for (int i = msgs.length-1; i >= 1; i--) {
			msgs[i] = msgs[i-1];
		}
		
//		System.out.println(text);
		msgs[0] = new Message(text, fontStyle);
	}
	
	public void doYouWantToWrite(int keyCode) {
		if (play.phase == Play.PLAY_PHASE && keyCode == 84) { // touche 'T' appuyer
			play.phase = Play.PAUSE_PHASE;
			String s = JOptionPane.showInputDialog(null, "Ecit un truc", "Alors comme ça tu veux écrire", JOptionPane.QUESTION_MESSAGE);
			play.phase = Play.PLAY_PHASE;
			
			if (s != null && !s.matches("")) {
				if (s.charAt(0) != '\\') {
					addText("Message inutile : "+s);
				} else {
					cheat(s.substring(1));
				}
			}
		}
	}
	
	private void cheat(String commande) {
		String[] part = commande.split(" ");
		if (part[0].matches("takePower")) {
			takePower(part);
		} else if (part[0].matches("givePower")) {
			givePower(part);
		} else if (part[0].matches("summon")) {
			summon(part);
		} else if (part[0].matches("setLife")){
			setLife(part);
		} else if (part[0].matches("cleanRoom")) {
			cleanRoom();
		} else if (part[0].matches("setColor")) {
			setColor(part);
		} else if (part[0].matches("setNumOfKills")) {
			setNumOfKills(part);
		} else if (part[0].matches("exit")) {
			System.exit(0);
		} else if (part[0].matches("tp")) {
			room.me.tp();
		} else if (part[0].matches("setFps")) {
			setFps(part);
		} else {
			addText("Il n'y a pas de méthode "+part[0]);
		}
	}
	
	private void takePower(String[] args) {
		if (args.length >= 3) {
			int powI = -1;
			if (args[1].matches("speed")) {
				powI = 0;
			} else if (args[1].matches("crit")) {
				powI = 1;
			} else if (args[1].matches("bulletSpeed")) {
				powI = 2;
			} else if (args[1].matches("shield")) {
				powI = 3;
			} else if (args[1].matches("regen")) {
				powI = 4;
			} else if (args[1].matches("vampire")) {
				powI = 5;
			} else if (args[1].matches("deathsBullets")) {
				powI = 6;
			} else if (args[1].matches("moreLife")) {
				powI = 7;
			} else if (args[1].matches("bouncingBall")) {
				powI = 8;
			} else if (args[1].matches("drinkingDuck")) {
				powI = 9;
			} else if (args[1].matches("multipleJump")) {
				powI = 10;
			} else if (args[1].matches("stone2Birds")) {
				powI = 11;
			} else if (args[1].matches("nothing")) {
				powI = 12;
			} else if (args[1].matches("cadence")) {
				powI = 13;
			} else if (args[1].matches("turret")) {
				powI = 14;
			} else if (args[1].matches("poison")) {
				powI = 15;
			} else if (args[1].matches("bomb")) {
				powI = 16;
			} else if (args[1].matches("snakesOfPain")) {
				powI = 17;
			} else if (args[1].matches("petrification")) {
				powI = 18;
			} else if (args[1].matches("tp")) {
				powI = 19;
			}
			
			if (powI >= 0) {
				int num = 0;
				try {
					num = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					addText(args[2]+" ne peut pas être converti en entier");
				}
				if (num != 0) addText("Vous avez pris \""+Power.values()[powI].name.toLowerCase()+"\" "+num+" fois");
				for (int i = 0; i < num; i++) {
					room.me.addPower(Power.values()[powI]);
				}
			} else {
				addText("Le pouvoir "+args[1]+" n'existe pas");
			}
		} else {
			addText("Il n'y a pas assez d'arguments");
		}
	}
	
	private void givePower(String[] args) {
		if (args.length >= 3) {
			int powI = -1;
			if (args[1].matches("speed")) {
				powI = 0;
			} else if (args[1].matches("crit")) {
				powI = 1;
			} else if (args[1].matches("bulletSpeed")) {
				powI = 2;
			} else if (args[1].matches("shield")) {
				powI = 3;
			} else if (args[1].matches("regen")) {
				powI = 4;
			} else if (args[1].matches("vampire")) {
				powI = 5;
			} else if (args[1].matches("deathsBullets")) {
				powI = 6;
			} else if (args[1].matches("moreLife")) {
				powI = 7;
			} else if (args[1].matches("bouncingBall")) {
				powI = 8;
			} else if (args[1].matches("nothing")) {
				powI = 12;
			} else if (args[1].matches("cadence")) {
				powI = 13;
			} else if (args[1].matches("poison")) {
				powI = 15;
			} /*else if (args[1].matches("snakesOfPain")) {
				powI = 17;
			} */else if (args[1].matches("petrification")) {
				powI = 18;
			}
			
			if (powI >= 0) {
				int num = 0;
				try {
					num = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					addText(args[2]+" ne peut pas être converti en entier");
				}
				if (num != 0) addText("Vous leur avez donné \""+Power.values()[powI].name.toLowerCase()+"\" "+num+" fois");
				for (int i = 0; i < num; i++) {
					room.enemiesPowers[powI]++;
				}
			} else {
				addText("Le pouvoir "+args[1]+" n'existe pas pour les énemies");
			}
		} else {
			addText("Il n'y a pas assez d'arguments");
		}
	}
	
	private void summon(String[] args) {
		if (args.length >= 3) {
			int num = -1;
			try {
				num = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				addText(args[2]+" ne peut pas être converti en entier");
			}
			for (int i = 0; i < num; i++) {
				if (args[1].matches("blob")) {
					room.enemies.add(new Blob(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].matches("bomber")) {
					room.enemies.add(new Bomber(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].matches("darkGuy")) {
					room.enemies.add(new DarkGuy(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].matches("flyingHead")) {
					room.enemies.add(new FlyingHead(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].matches("ninja")) {
					room.enemies.add(new Ninja(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].matches("shieldMan")) {
					room.enemies.add(new ShieldMan(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].matches("summoner")) {
					room.enemies.add(new Summoner(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].matches("soul")) {
					room.enemies.add(new Soul(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].matches("lilSlime")) {
					room.enemies.add(new LilSlime(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].matches("medSlime")) {
					room.enemies.add(new MedSlime(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].matches("bigSlime")) {
					room.enemies.add(new BigSlime(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
				} else if (args[1].matches("lilSnake")) {
					room.enemies.add(new LilSnake(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].matches("lilHead")) {
					room.enemies.add(new LilHead(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].matches("healer")) {
					room.enemies.add(new Healer(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else {
					if (i == 0) addText("L'entity "+args[1]+" n'éxiste pas ou ne peut pas être invoqué");
				}
			}
		} else {
			addText("Il n'y a pas assez d'arguments");
		}
	}
	
	private void setLife(String[] args) {
		if (args.length >= 2) {
			int life = room.me.life;
			try {
				life = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				addText(args[1]+" ne peut pas être converti en entier");
			}
			
			if (life != room.me.life) {
				addText("Vous mettez votre vie à "+life);
				if (life > room.me.maxLife) life = room.me.maxLife;
				room.me.life = life;
				if (room.me.life <= 0) room.me.die(room.me);
			}
		} else {
			addText("Il n'y a pas assez d'arguments");
		}
	}
	
	private void cleanRoom() {
		room.enemies.removeAll(room.enemies);
		addText("Vous n'ettoyer cette pièce de tout ses énemies");
	}
	
	private void setColor(String[] args) {
		if (args.length >= 4) {
			Color c = play.color;
			int red = c.getRed();
			int green = c.getGreen();
			int blue = c.getBlue();
			try {
				red = Integer.parseInt(args[1]);
				green = Integer.parseInt(args[2]);
				blue = Integer.parseInt(args[3]);
				play.color = new Color(red, green, blue);
				addText("Vous changez la couleur pour "+red+" "+green+" "+blue);
			} catch (NumberFormatException e) {
				addText("Tous les arguments ne peuvent être convertis en entier");
			}
		} else {
			addText("Il n'y a pas assez d'arguments");
		}
	}
	
	private void setNumOfKills(String[] args) {
		if (args.length >= 2) {
			try {
				int num = Integer.parseInt(args[1]);
				room.numOfKills = num;
			} catch (NumberFormatException e) {
				addText(args[1]+" ne peut pas être converti en entier");
			}
		} else {
			addText("Il n'y a pas assez d'arguments");
		}
	}
	
	private void setFps(String[] args) {
		if (args.length >= 2) {
			try {
				int num = Integer.parseInt(args[1]);
				if (num > 0) {
					room.play.setFps(num);
					addText("Vous mettez les fps à "+num);
				} else addText("Les fps doivent être supérieur à 0");
			} catch (NumberFormatException e) {
				addText(args[1]+" ne peut pas être converti en entier");
			}
		} else {
			addText("Il n'y a pas assez d'arguments");
		}
	}
	
	/**
	 * actualise rectW et rectH
	 */
	public void initRectWH() {
		int nMsg = 0;
		int maxW = 0;
		int msgH = 0;
		for (int i = 0; i < msgs.length; i++) {
			if (msgs[i] != null) {
				nMsg++;
				Message msg = msgs[i];
				if (i == 0) msgH = msg.h;
				if (msg.w > maxW) {
					maxW = msg.w;
				}
			} else {
				break;
			}
		}
		
		if (nMsg > 0) {
			rectW = 2*gap+maxW;
			rectH = (nMsg+1)*gap+nMsg*msgH;
		} else {
			rectW = 0;
			rectH = 0;
		}
	}
	
	/**
	 * 
	 * 
	 * @param x abscisses bas du chat
	 * @param y ordonnées haut du chat
	 * @param g2d
	 */
	public void display(int x, int y, Graphics2D g2d) {
		g2d.setFont(new Font("ARIAL", Font.BOLD, (int)(textSize*play.scaleW)));
		// actualise les largeur et hauteur des messages
		for (Message msg : msgs) {
			if (msg != null) msg.setWH(g2d);
		}
		gap = (int)(10*play.scaleW);
		
		// dessiner un carée non opaque pour qu'on puisse bien distinguer le texte
		g2d.setColor(new Color(0, 0, 0, 70));
		initRectWH();
		g2d.fillRect(x, y-rectH, rectW, rectH);
		
		for (int i = 0; i < msgs.length; i++) {
			if (msgs[i] != null) {
				Message msg = msgs[i];
				msg.display(x+(gap), y-(i+1)*gap-i*msg.h, g2d);
			}
		}
	}
}
