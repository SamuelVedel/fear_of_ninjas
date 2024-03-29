package fr.svedel.fod.play;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.JOptionPane;

import fr.svedel.fod.UsefulTh;
import fr.svedel.fod.play.enemy.BigSlime;
import fr.svedel.fod.play.enemy.Blob;
import fr.svedel.fod.play.enemy.Bomber;
import fr.svedel.fod.play.enemy.DarkGuy;
import fr.svedel.fod.play.enemy.FlyingHead;
import fr.svedel.fod.play.enemy.Healer;
import fr.svedel.fod.play.enemy.LilHead;
import fr.svedel.fod.play.enemy.LilSlime;
import fr.svedel.fod.play.enemy.LilSnake;
import fr.svedel.fod.play.enemy.MedSlime;
import fr.svedel.fod.play.enemy.Ninja;
import fr.svedel.fod.play.enemy.ShieldMan;
import fr.svedel.fod.play.enemy.Soul;
import fr.svedel.fod.play.enemy.Summoner;

/*  /\_/\
 * (=�-�=) )
 *  )   ( (
 * (_____))
 */

/**
 * Class cr��e le 10/02/2023
 * 
 * @author Samuel Vedel
 *
 */
public class Chat {
	
	private Play play;
	private Room room;
	
	private Message[] msgs = new Message[20];
	private int textSize = 12;
	
	private int gap;
	private int rectW;
	private int rectH;
	
	public Chat(Room room) {
		this.room = room;
		this.play = room.play;
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
		// d�cale tout les messages
		for (int i = msgs.length-1; i >= 1; i--) {
			msgs[i] = msgs[i-1];
		}
		
//		System.out.println(text);
		msgs[0] = new Message(text, fontStyle);
	}
	
	public void doYouWantToWrite(int keyCode) {
		if (play.phase == Play.PLAY_PHASE && keyCode == 84) { // touche 'T' appuyer
			play.phase = Play.PAUSE_PHASE;
			String s = JOptionPane.showInputDialog(null, "Ecit un truc", "Alors comme �a tu veux �crire", JOptionPane.QUESTION_MESSAGE);
			play.phase = Play.PLAY_PHASE;
			
			if (s != null && !s.equals("")) {
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
		if (part[0].equals("takePower")) {
			takePower(part);
		} else if (part[0].equals("givePower")) {
			givePower(part);
		} else if (part[0].equals("summon")) {
			summon(part);
		} else if (part[0].equals("setLife")){
			setLife(part);
		} else if (part[0].equals("cleanRoom")) {
			cleanRoom();
		} else if (part[0].equals("setColor")) {
			setColor(part);
		} else if (part[0].equals("setNumOfKills")) {
			setNumOfKills(part);
		} else if (part[0].equals("exit")) {
			System.exit(0);
		} else if (part[0].equals("tp")) {
			room.me.tp();
		} else if (part[0].equals("setFps")) {
			setFps(part);
		} else if (part[0].equals("restart")) {
			play.restart();
		} else {
			addText("Il n'y a pas de m�thode "+part[0]);
		}
	}
	
	private void takePower(String[] args) {
		if (args.length >= 3) {
			int powI = -1;
			if (args[1].equals("speed")) {
				powI = 0;
			} else if (args[1].equals("crit")) {
				powI = 1;
			} else if (args[1].equals("bulletSpeed")) {
				powI = 2;
			} else if (args[1].equals("shield")) {
				powI = 3;
			} else if (args[1].equals("regen")) {
				powI = 4;
			} else if (args[1].equals("vampire")) {
				powI = 5;
			} else if (args[1].equals("deathsBullets")) {
				powI = 6;
			} else if (args[1].equals("moreLife")) {
				powI = 7;
			} else if (args[1].equals("bouncingBall")) {
				powI = 8;
			} else if (args[1].equals("drinkingDuck")) {
				powI = 9;
			} else if (args[1].equals("multipleJump")) {
				powI = 10;
			} else if (args[1].equals("stone2Birds")) {
				powI = 11;
			} else if (args[1].equals("nothing")) {
				powI = 12;
			} else if (args[1].equals("cadence")) {
				powI = 13;
			} else if (args[1].equals("turret")) {
				powI = 14;
			} else if (args[1].equals("poison")) {
				powI = 15;
			} else if (args[1].equals("bomb")) {
				powI = 16;
			} else if (args[1].equals("snakesOfPain")) {
				powI = 17;
			} else if (args[1].equals("petrification")) {
				powI = 18;
			} else if (args[1].equals("tp")) {
				powI = 19;
			}
			
			if (powI >= 0) {
				int num = 0;
				try {
					num = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					addText(args[2]+" ne peut pas �tre converti en entier");
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
			if (args[1].equals("speed")) {
				powI = 0;
			} else if (args[1].equals("crit")) {
				powI = 1;
			} else if (args[1].equals("bulletSpeed")) {
				powI = 2;
			} else if (args[1].equals("shield")) {
				powI = 3;
			} else if (args[1].equals("regen")) {
				powI = 4;
			} else if (args[1].equals("vampire")) {
				powI = 5;
			} else if (args[1].equals("deathsBullets")) {
				powI = 6;
			} else if (args[1].equals("moreLife")) {
				powI = 7;
			} else if (args[1].equals("bouncingBall")) {
				powI = 8;
			} else if (args[1].equals("nothing")) {
				powI = 12;
			} else if (args[1].equals("cadence")) {
				powI = 13;
			} else if (args[1].equals("poison")) {
				powI = 15;
			} /*else if (args[1].equals("snakesOfPain")) {
				powI = 17;
			} */else if (args[1].equals("petrification")) {
				powI = 18;
			}
			
			if (powI >= 0) {
				int num = 0;
				try {
					num = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					addText(args[2]+" ne peut pas �tre converti en entier");
				}
				if (num != 0) addText("Vous leur avez donn� \""+Power.values()[powI].name.toLowerCase()+"\" "+num+" fois");
				for (int i = 0; i < num; i++) {
					room.enemiesPowers[powI]++;
				}
			} else {
				addText("Le pouvoir "+args[1]+" n'existe pas pour les �nemies");
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
				addText(args[2]+" ne peut pas �tre converti en entier");
			}
			for (int i = 0; i < num; i++) {
				if (args[1].equals("blob")) {
					room.enemies.add(new Blob(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].equals("bomber")) {
					room.enemies.add(new Bomber(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].equals("darkGuy")) {
					room.enemies.add(new DarkGuy(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].equals("flyingHead")) {
					room.enemies.add(new FlyingHead(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].equals("ninja")) {
					room.enemies.add(new Ninja(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].equals("shieldMan")) {
					room.enemies.add(new ShieldMan(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].equals("summoner")) {
					room.enemies.add(new Summoner(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].equals("soul")) {
					room.enemies.add(new Soul(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].equals("lilSlime")) {
					room.enemies.add(new LilSlime(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].equals("medSlime")) {
					room.enemies.add(new MedSlime(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].equals("bigSlime")) {
					room.enemies.add(new BigSlime(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
				} else if (args[1].equals("lilSnake")) {
					room.enemies.add(new LilSnake(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].equals("lilHead")) {
					room.enemies.add(new LilHead(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else if (args[1].equals("healer")) {
					room.enemies.add(new Healer(room));
					if (i == 0) addText("Invocation de "+num+" "+room.enemies.get(room.enemies.size()-1).name);
					
				} else {
					if (i == 0) addText("L'entity "+args[1]+" n'�xiste pas ou ne peut pas �tre invoqu�");
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
				addText(args[1]+" ne peut pas �tre converti en entier");
			}
			
			if (life != room.me.life) {
				addText("Vous mettez votre vie � "+life);
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
		addText("Vous n'ettoyer cette pi�ce de tout ses �nemies");
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
				addText("Tous les arguments ne peuvent �tre convertis en entier");
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
				addText(args[1]+" ne peut pas �tre converti en entier");
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
					addText("Vous mettez les fps � "+num);
				} else addText("Les fps doivent �tre sup�rieur � 0");
			} catch (NumberFormatException e) {
				addText(args[1]+" ne peut pas �tre converti en entier");
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
	 * @param y ordonn�es haut du chat
	 * @param g2d
	 */
	public void display(int x, int y, Graphics2D g2d) {
		g2d.setFont(new Font("ARIAL", Font.BOLD, (int)(textSize*play.scaleW)));
		// actualise les largeur et hauteur des messages
		for (Message msg : msgs) {
			if (msg != null) msg.setWH(g2d);
		}
		gap = (int)(10*play.scaleW);
		
		// dessiner un car�e non opaque pour qu'on puisse bien distinguer le texte
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
	
	
	private class Message {
		private String text;
		private double time = 0;
		private static final int MAX_TIME = 10*60; // 10 secondes //7*60; // 7 secondes
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
}
