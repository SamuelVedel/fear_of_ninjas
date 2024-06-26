package fr.svedel.fod.play;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import fr.svedel.fod.UsefulTh;
import fr.svedel.fod.play.cube.Border;
import fr.svedel.fod.play.cube.Bump;
import fr.svedel.fod.play.cube.Cube;
import fr.svedel.fod.play.cube.Gate;
import fr.svedel.fod.play.cube.Switch;
import fr.svedel.fod.play.enemy.BigSlime;
import fr.svedel.fod.play.enemy.Blob;
import fr.svedel.fod.play.enemy.Bomber;
import fr.svedel.fod.play.enemy.DarkGuy;
import fr.svedel.fod.play.enemy.Enemy;
import fr.svedel.fod.play.enemy.FlyingHead;
import fr.svedel.fod.play.enemy.Healer;
import fr.svedel.fod.play.enemy.LilSlime;
import fr.svedel.fod.play.enemy.MedSlime;
import fr.svedel.fod.play.enemy.Ninja;
import fr.svedel.fod.play.enemy.ShieldMan;
import fr.svedel.fod.play.enemy.SnakeBoss;
import fr.svedel.fod.play.enemy.Summoner;
import fr.svedel.fod.play.enemy.Turret;
import fr.svedel.fod.play.particle.LightParticle;
import fr.svedel.fod.play.particle.Particle;

/*  ___
 * (� �)
 *  ) (
 * (( ))
 */

/**
 * Class des salles sur lesquel on joue.
 * Cette class contient une instance de {@code Me},
 * des instances de {@code Cube} pour construire la salle,
 * et des instance de {@code Bullet}.
 * <p>
 * Class cr��e le 20/06/2022
 * 
 * @author Samuel Vedel le boss
 *
 */
public class Room {
	
	public Play play;
	
	public boolean isABossLevel = false;
	
	private int level = 0;
	/** nombre de kill necessaire pour r�ussir le niveau */
	public int goal = 4;
	public int numOfKills;
	
	public int[][] plan;
	/** largeur de la salle */
	public int width;
	/** hauteur de la salle */
	public int height;
	
	public Chat cht;
	
	public Me me;
	
	public ArrayList<Enemy> enemies = new ArrayList<>();
	public int[] enemiesPowers = new int[Power.values().length];
	
	public Cube[][] cubes;
	/** liste des cubes qui doivent �x�cuter des actions */
	public ArrayList<Cube> cubesWithActions = new ArrayList<>();
	
	public ArrayList<Bullet> bullets = new ArrayList<>();
	
	public ArrayList<Item> items = new ArrayList<>();
	
	public ArrayList<Particle> particles = new ArrayList<>();
	
	public double transX, transY;
	
	public KeyListener kl = new KeyListener() {
		
		@Override
		public void keyTyped(KeyEvent e) {}
		
		@Override
		public void keyPressed(KeyEvent e) {
			me.move(e.getKeyCode());
			me.addSkillsKeyReac(e.getKeyCode());
			cht.doYouWantToWrite(e.getKeyCode());
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			me.stopMove(e.getKeyCode());
		}
	};
	
	public boolean msPressed = false;
	public MouseListener ml = new MouseListener() {
		
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == 1) {
				msPressed = true;
				me.canShoot = true;
			}
			me.addSkillsMouseReac(e.getButton());
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == 1) msPressed = false;
		}
		
		@Override
		public void mouseExited(MouseEvent e) {}
		
		@Override
		public void mouseEntered(MouseEvent e) {}
		
		@Override
		public void mouseClicked(MouseEvent e) {}
	};
	
	public Room(Play play) {
		this.play = play;
		cht = new Chat(this);
		me = new Me(this);
		
		loadAnyRoom();
		
		if (!isABossLevel) {
			for (int i = 0; i < 3; i++) {
				spawnEnemy();
			}
		}
	}
	
	
	/**
	 * charge une salle au hasard
	 */
	private void loadAnyRoom() {
		File folder = new File("rooms/");
		File[] listRooms = folder.listFiles();
		
		// trouve un dossier qui contient une salles et peut-�tre des variantes de la salle
		// ou directement un fichier d'une salle
		File file;
		do {
			file = listRooms[UsefulTh.rand.nextInt(listRooms.length)];
		} while (file.getName().charAt(0) == 'N'); // ne charge pas les map qui commence par 'N'
		
		if (!file.isFile()) loadARoom(file.getName());
		else createRoom(file.getName());
	}
	
	/**
	 * charge une des variantes d'une salle, qui sont dans le dossier rooms/folderName
	 * 
	 * @param folderName
	 */
	private void loadARoom(String folderName) {
		File folder = new File("rooms/"+folderName);
		File[] listRooms = folder.listFiles();
		
		String fileName;
		do {
			fileName = listRooms[UsefulTh.rand.nextInt(listRooms.length)].getName();
		} while (fileName.charAt(0) == 'N'); // ne charge pas les map qui commence par 'N'
		
		createRoom(folderName+"/"+fileName);
		initCubeWithAction();
	}
	
	/**
	 * Construit la salle � partir d'un .room
	 * qui contient les plans sous forme d'une matrice
	 * 
	 * @param fileName chemin de la salle � partir de rooms\
	 */
	private void createRoom(String fileName) {
		plan = UsefulTh.readMat("rooms/"+fileName);
		cubes = new Cube[plan.length][plan[0].length];
		for (int iY = 0; iY < plan.length; iY++) {
			for (int iX = 0; iX < plan[iY].length; iX++) {
				switch (plan[iY][iX]) {
				case 0 :
					break;
				case 1 :
					me.spawnX = iX*UsefulTh.CUBE_W;
					me.spawnY = (iY-1)*UsefulTh.CUBE_H-3*UsefulTh.PIXEL_H;
					me.x = me.spawnX;
					me.y = me.spawnY;
					break;
				case 2 :
					cubes[iY][iX] = new Border(iX*UsefulTh.CUBE_W, iY*UsefulTh.CUBE_H, Cube.BORD7, "cube7.texture");
					break;
				case 3 :
					cubes[iY][iX] = new Border(iX*UsefulTh.CUBE_W, iY*UsefulTh.CUBE_H, Cube.BORD8, "cube8.texture");
					break;
				case 4 :
					cubes[iY][iX] = new Border(iX*UsefulTh.CUBE_W, iY*UsefulTh.CUBE_H, Cube.BORD9, "cube9.texture");
					break;
				case 5 :
					cubes[iY][iX] = new Border(iX*UsefulTh.CUBE_W, iY*UsefulTh.CUBE_H, Cube.BORD4, "cube4.texture");
					break;
				case 6 :
					cubes[iY][iX] = new Cube(iX*UsefulTh.CUBE_W, iY*UsefulTh.CUBE_H, "cube5.texture");
					break;
				case 7 :
					cubes[iY][iX] = new Border(iX*UsefulTh.CUBE_W, iY*UsefulTh.CUBE_H, Cube.BORD6, "cube6.texture");
					break;
				case 8 :
					cubes[iY][iX] = new Border(iX*UsefulTh.CUBE_W, iY*UsefulTh.CUBE_H, Cube.BORD1, "cube1.texture");
					break;
				case 9 :
					cubes[iY][iX] = new Border(iX*UsefulTh.CUBE_W, iY*UsefulTh.CUBE_H, Cube.BORD2, "cube2.texture");
					break;
				case 10 :
					cubes[iY][iX] = new Border(iX*UsefulTh.CUBE_W, iY*UsefulTh.CUBE_H, Cube.BORD3, "cube3.texture");
					break;
				case 11 :
					cubes[iY][iX] = new Cube(iX*UsefulTh.CUBE_W, iY*UsefulTh.CUBE_H, "cube48.texture");
					break;
				case 12 :
					cubes[iY][iX] = new Cube(iX*UsefulTh.CUBE_W, iY*UsefulTh.CUBE_H, "cube24.texture");
					break;
				case 13 :
					cubes[iY][iX] = new Cube(iX*UsefulTh.CUBE_W, iY*UsefulTh.CUBE_H, "cube26.texture");
					break;
				case 14 :
					cubes[iY][iX] = new Cube(iX*UsefulTh.CUBE_W, iY*UsefulTh.CUBE_H, "cube68.texture");
					break;
				case 15 :
					cubes[iY][iX] = new Bump(iX*UsefulTh.CUBE_W, iY*UsefulTh.CUBE_H);
					break;
				case 16 :
					cubes[iY][iX] = new Border(iX*UsefulTh.CUBE_W, iY*UsefulTh.CUBE_H, Cube.BORD, "cube.texture");
					break;
				case 17 :
					cubes[iY][iX] = new Switch(iX*UsefulTh.CUBE_W, iY*UsefulTh.CUBE_H, true);
					break;
				case 18 :
					cubes[iY][iX] = new Switch(iX*UsefulTh.CUBE_W, iY*UsefulTh.CUBE_H, false);
					break;
				case 19 :
					cubes[iY][iX] = new Gate(iX*UsefulTh.CUBE_W, (iY-1)*UsefulTh.CUBE_H, this);
				}
			}
		}
		width = plan[0].length*UsefulTh.CUBE_W;
		height = plan.length*UsefulTh.CUBE_H;
	}
	
	private void initCubeWithAction() {
		cubesWithActions.removeAll(cubesWithActions);
		for (Cube[] cu : cubes) {
			for (Cube c : cu) {
				if (c != null && c.hasActions()) {
					cubesWithActions.add(c);
				}
			}
		}
	}
	
	public void actions(double delta) {
		// actions des balles
		for (int i = bullets.size()-1; i >= 0; i--) {
			Bullet b = bullets.get(i);
			b.actions(delta);
			for (int i2 = i-1; i2 >= 0; i2--) {
				b.contact(bullets.get(i2));
				if (b.noMoreReasonToBe) break;
			}
			if (b.noMoreReasonToBe) bullets.remove(i);
		}
		
		// actions des cubes
		for (Cube c : cubesWithActions) {
			c.actions(delta);
		}
		
		// actions de moi
		me.actions(delta);
		
		// actions des �nemies
		for (int i = enemies.size()-1; i >= 0; i--) {
			Enemy en = enemies.get(i);
			en.actions(delta);
			if (!en.alive) enemies.remove(i);
		}
		
		// actions des items
		for (int i = items.size()-1; i >= 0; i--) {
			Item it = items.get(i);
			it.actions(delta);
			if (it.noMoreReasonToBe) items.remove(i);
		}
		
		// actions des particules
		for(int i = particles.size()-1; i >= 0; i--) {
			Particle p = particles.get(i);
			p.action(delta);
			if (p.noMoreReasonToBe) particles.remove(i);
		}
		spawnLightParcticle(delta);
		
		// actions du chat
		cht.actions(delta);
		
		// spawn des �nemies
//		if (!isABossLevel && UsefulTh.rand.nextInt(500/(level/10+1)) == 0) {
		if (!isABossLevel && UsefulTh.deltaRandom((500/(level/10+1)), delta)) {
			spawnEnemy();
		}
	}
	
	private void spawnEnemy() {
		// type choisis le type de carrure d'�nemie
		int type = UsefulTh.rand.nextInt(20);
		if (type < 15) {
			int num = UsefulTh.rand.nextInt(4);
			switch (num) {
			case 0 :
				enemies.add(new FlyingHead(this));
				break;
			case 1 :
				enemies.add(new DarkGuy(this));
				break;
			case 2 :
				enemies.add(new Blob(this));
				break;
			case 3 :
				// fait appara�tre un slime
				int typeOfSlime = UsefulTh.rand.nextInt(20);
				if (typeOfSlime < 15) enemies.add(new LilSlime(this));
				else if (typeOfSlime < 19) enemies.add(new MedSlime(this));
				else enemies.add(new BigSlime(this));
				break;
			}
		} else if (type < 19) {
			int num = UsefulTh.rand.nextInt(3);
			switch (num) {
			case 0 :
				enemies.add(new ShieldMan(this));
				break;
			case 1 :
				enemies.add(new Ninja(this));
				break;
			case 2 :
				enemies.add(new Bomber(this));
				break;
			}
		} else {
			int num = UsefulTh.rand.nextInt(2);
				if (num == 0) { // summoner
					if (level >= 10) {
					/*// on regarde si il n'y en � pas d�j� un
					boolean canSpawn = true;
					for (Enemy en : enemies) {
						if (en.type == Entity.SUMMONER_TYPE) {
							canSpawn = false;
							break;
						}
					}
					if (canSpawn)*/ enemies.add(new Summoner(this));
				}
			} else {
				enemies.add(new Healer(this));
			}
		}
	}
	
	private void spawnLightParcticle(double delta) {
//		if (UsefulTh.rand.nextInt(600) == 0) {
		if (UsefulTh.deltaRandom(600, delta)) {
			particles.add(new LightParticle(play));
		}
	}
	
	/**
	 * verifie si l'abscisse {@code x} est
	 * compris dans {@code cubes}
	 * 
	 * @param x l'abscisse � verifier
	 * @return si c'est le cas
	 */
	public boolean isXInCubes(int x) {
		return 0 <= x && x < cubes[0].length;
	}
	
	/**
	 * verifie si l'ordonn�e {@code x} est
	 * compris dans {@code cubes}
	 * 
	 * @param y l'ordonn�e � verifier
	 * @return si c'est le cas
	 */
	public boolean isYInCubes(int y) {
		return 0 <= y && y < cubes.length;
	}
	
	public double getMouseXInRoom() {
		return (play.mouseX-transX)/play.scaleW;
	}
	
	public double getMouseYInRoom() {
		return (play.mouseY-transY)/play.scaleW;
	}
	
	public void refresh() {
		level++;
		
		if (level%10 == 0) isABossLevel = true;
		else isABossLevel = false;
		
		// change la map
		if (!isABossLevel) loadAnyRoom();
		else {
			loadARoom("NsnakeLevel");
		}
		
		me.refresh();
		
		//suprime tout
		enemies.removeAll(enemies);
		bullets.removeAll(bullets);
		items.removeAll(items);
		particles.removeAll(particles);
		UsefulTh.clearTextures();
		
		numOfKills = 0;
		if (!isABossLevel) goal += 2;
		
		// fait pop mes tourelles
		for (int i = 0; i < me.powers[Power.TURRET.id]; i++) {
			enemies.add(new Turret(me, this));
		}
		
		// ajout un pouvoir � l'�niemie si il le faut
		if (level%2 == 0) {
			Power pow = Power.getRandPowerForEnemey();
			enemiesPowers[pow.id]++;
			cht.addText("Vos opposants ont obtenu \""+pow.name.toLowerCase()+"\"");
			pow.sayEffectInAChat(cht);
		}
		
		// fait apparaitre trois �nemies
		if (!isABossLevel) {
			for (int i = 0; i < 3; i++) {
				spawnEnemy();
			}
		} else {
			enemies.add(new SnakeBoss(this));
		}
	}
	
	public void display(Graphics2D g2d) {
		transX = -((int)me.x)*play.scaleW+play.playP.getWidth()/2-me.w*play.scaleW/2;
		transY = -((int)me.y)*play.scaleW+play.playP.getHeight()/2-me.h*play.scaleW/2;
		
		if (plan != null) {
			if (transX > 0) transX = 0;
			if (transY > 0) transY = 0;
			if (transX < -(width*play.scaleW)+play.playP.getWidth()) transX = -(width*play.scaleW)+play.playP.getWidth();
			if (transY < -(height*play.scaleW)+play.playP.getHeight()) transY = -(height*play.scaleW)+play.playP.getHeight();
		}
		
		BufferedImage bi = new BufferedImage((int)(play.playP.getWidth()/play.scaleW), (int)(play.playP.getHeight()/play.scaleW), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2dbi = bi.createGraphics();
		
		g2dbi.setColor(UsefulTh.BACKGROUND_COLOR);
		g2dbi.fillRect(0, 0, bi.getWidth(), bi.getHeight());
		
		g2dbi.translate(transX/play.scaleW, transY/play.scaleW);
		//g2d.scale(play.scaleW, play.scaleW);
		
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).display(play.color, g2dbi);
		}
		
		// affichages des cubes qui sont pr�sent dans l'�cran
		for (int iY = (int)(-transY/(UsefulTh.CUBE_H*play.scaleW)); iY < (int)((play.playP.getHeight()-transY)/(UsefulTh.CUBE_H*play.scaleW))+1; iY++) {
			for (int iX = (int)(-transX/(UsefulTh.CUBE_W*play.scaleW)); iX < (int)((play.playP.getWidth()-transX)/(UsefulTh.CUBE_W*play.scaleW))+1; iX++) {
				if (isXInCubes(iX) && isYInCubes(iY) && cubes[iY][iX] != null) {
					cubes[iY][iX].display(play.color, g2dbi);
				}
			}
		}
		
		for (int i = 0; i < items.size(); ++i) {
			items.get(i).display(g2dbi);
		}
		
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).display(g2dbi);
		}
		
		me.display(g2dbi);
		
		for (int i = enemies.size()-1; i >= 0; i--) {
			enemies.get(i).display(g2dbi);
		}
		
		//g2d.scale(1/play.scaleW, 1/play.scaleW);
		//g2d.translate(-transX, -transY);
		
		g2d.drawImage(bi, 0, 0, (int)(bi.getWidth()*play.scaleW), (int)(bi.getHeight()*play.scaleW), null);
		g2dbi.dispose();
		
		me.displayMyLife(g2d);
		
		// affiche le quota de kill
		// code pas tr�s beau
		int gap = (int)(7*play.scaleW);
		g2d.setFont(new Font("ARIAL", Font.BOLD, (int)(25*play.scaleW)));
		String text;
		if (!isABossLevel) {
			text = "Level "+level+" : "+numOfKills+"/"+goal;
		} else {
			text = "Level "+level;
		}
		int textW = UsefulTh.getTextW(text, g2d);
		int textH = UsefulTh.getTextH(text, g2d);
		int edge = (int)(10*play.scaleW);
		g2d.setColor(new Color(0, 0, 0, 70));
		g2d.fillRect(play.playP.getWidth()-textW-edge-gap, edge-gap, textW+2*gap, textH+2*gap);
		g2d.setColor(play.color);
		UsefulTh.drawString(text, play.playP.getWidth()-textW-edge, textH+edge, g2d);
		
		//affiche le chat
		cht.display(me.lifeBarX, (int)(me.lifeBarY-5*play.scaleW), g2d);
		
//		for (int cX = 0; cX <= 255; cX++) {
//			for (int cY = 0; cY <= 255; cY++) {
//				g2d.setColor(new Color(cX, cY, (cX+cY)/2));
//				g2d.fillRect(cX, cY, 1, 1);
//			}
//		}
	}
}
