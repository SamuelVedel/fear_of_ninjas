package fr.svedel.fod;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Random;

import fr.svedel.fod.play.Play;
import fr.svedel.fod.play.Power;
import fr.svedel.fod.play.addskill.AddSkill;

/*  ___
 * (° °)
 *  ) (
 * (( ))
 */

/**
 * Class qui contient plein de variable
 * et de fonction utiles partout
 * d'où le nom UsefulTh qui dérive de
 * useful things
 * <p>
 * Class créée le 13/01/2023
 * 
 * @author Samuel Vedel
 *
 */
public abstract class UsefulTh {
	
	public static Random rand = new Random();
	
	public static final Color BACKGROUND_COLOR = Color.DARK_GRAY.darker().darker();
	
	/** largreur d'un cube de base */
	public static final int CUBE_W = 30;
	/** hauteur d'un cube de base */
	public static final int CUBE_H = CUBE_W;
	/** largeur d'un pixel de base */
	public static final int PIXEL_W = CUBE_W/6; // 5
	/** hauteur d'un pixel de base */
	public static final int PIXEL_H = CUBE_H/6; // 5
	
	/** force du poid */
	public static final double g = 0.75/5*PIXEL_H;
	
	/** hauteur d'écran pour laquelle le zoom est nul */
	public static final int HEIGHT_FOR_NO_SCALE = 19*CUBE_H;
	
	public static int[][] cursor = readMat("textures/cursor/cursor1.texture");
	public static int cursorW = 3*PIXEL_W;
	public static int cursorH = 3*PIXEL_H;
	
	/** contient toutes les textures déjà ouvertes */
	private static HashMap<String, int[][]> textures = new HashMap<>();
	/** contient toutes les textures déjà retournées */
	private static HashMap<int[][], int[][]> reverseXTexs = new HashMap<>();
	
	/**
	 * lit des fichiers contenant des matrices
	 * et les renvoit sous forme de matrice
	 * 
	 * @param file le chemin de la matrice à partir
	 * @return la matrice sous forme de matrice d'entier
	 */
	public static int[][] readMat(String file) {
		if (file == null) return new int[][] {{0}};
		int w = 0, h = 0;
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			String line = reader.readLine();
			int lineR = 0;
			
			w = line.split(" ").length;
			
			while (line != null) {
				line = reader.readLine();
				lineR++;
			}
			
			h = lineR;
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int[][] mat = new int[h][w];
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
				String line = reader.readLine();
				int lineR = 0;
				
				while (line != null) {
					String s [] = line.split(" ");
					
					for (int i = 0; i < s.length; i++) {
						mat[lineR][i] = Integer.parseInt(s[i]);
					}
					
					line = reader.readLine();
					lineR++;
				}
				
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		return mat;
	}
	
	/**
	 * retourne la texture qui correspond à file,
	 * mais si cette texture à déjà était chargé via cette fonction,
	 * alors elle n'est pas recréée, on retourne juste le poiteur
	 * de la texture.
	 * <br>
	 * si la texture n'a jamais était chargé via cette fonction,
	 * alors elle est chargé
	 * 
	 * @param file chemin de la texture
	 * @return
	 */
	public static int[][] readTex(String file) {
		int i = -1;
		for (int j = 0; j < textures.size(); j++) {
			if (file.equals(textures.keySet().toArray()[j].toString())) {
				i = j;
				break;
			}
		}
		
		if (i < 0) textures.put(file, readMat(file));
		return textures.get(file);
	}
	
	/**
	 * affiche une texture
	 * 
	 * @param tex la texture a afficher (sous forme de matrice d'entier)
	 * @param x les abscisses
	 * @param y les ordonnées
	 * @param w la longeur
	 * @param h la hauteur
	 * @param c2 la deuxième couleur
	 * @param g le {@code Graphics2D} sur lequel don dessine
	 */
	public static void displayTex(int[][] tex, int x, int y, int w, int h, Color c2, Graphics2D g2d) {
		if (tex != null) {
			int pW = w/tex[0].length;
			int pH = h/tex.length;
			
			for (int iY = 0; iY < tex.length; iY++) {
				for (int iX = 0; iX < tex[iY].length; iX++) {
					if (tex[iY][iX] != 0) {
						switch (tex[iY][iX]) {
						case 1:
							g2d.setColor(Color.BLACK);
							break;
						case 2:
							g2d.setColor(c2);
							break;
						case 3:
							g2d.setColor(c2.darker());
							break;
						}
						/*if (tex[iY][iX] == 1) {
							g2d.setColor(Color.BLACK);
						} else if (tex[iY][iX] == 2) {
							g2d.setColor(c2);
							
//							int cx = x/pixelW+iX;
//							int cy = y/pixelH+iY;
//							if (cx > 255) cx = 255;
//							if (cy > 255) cy = 255;
//							if (cx < 0) cx = 0;
//							if (cy < 0) cy = 0;
//							g2d.setColor(new Color(cx, cy, (cx+cy)/2));
							
//							int r = rand.nextInt(256);
//							int g = rand.nextInt(256);
//							int b = rand.nextInt(256);
//							g2d.setColor(new Color(r, g, b));
						}*/
						g2d.fillRect(x+iX*pW, y+iY*pH, pW, pH);
					}
				}
			}
			
	//		Graphics2D g2d2 = (Graphics2D) g2d.create();
	//		g2d2.translate(x, y);
	//		g2d2.scale(w/tex[0].length, h/tex.length);
	//		
	//		for (int iY = 0; iY < tex.length; iY++) {
	//			for (int iX = 0; iX < tex[iY].length; iX++) {
	//				if (tex[iY][iX] != 0) {
	//					if (tex[iY][iX] == 1) {
	//						g2d2.setColor(Color.BLACK);
	//					} else if (tex[iY][iX] == 2){
	//						g2d2.setColor(c2);
	//					}
	//					g2d2.fillRect(iX, iY, 1, 1);
	//				}
	//			}
	//		}
	//		g2d2.dispose();
		}
	}
	
	/**
	 * inverse les abcsisses d'une matrice
	 * 
	 * @param mat matrice qu'on doit inverser
	 * @return une nouvel matrice qui est la matrice de base en inverser
	 */
	public static int[][] reverseXMat(int[][] mat) {
		int[][] newMat = new int[mat.length][mat[0].length];
		
		for (int iY = 0; iY < mat.length; iY++) {
			for (int iX = 0; iX < mat[iY].length; iX++) {
				newMat[iY][newMat[iY].length-1-iX] = mat[iY][iX];
			}
		}
		
		return newMat;
	}
	
	/**
	 * retourne une version de {@code mat} où les abscisses sont inversé
	 * mais si cette texture à déjà était intersé via cette fonction,
	 * alors elle n'est pas recréée, on retourne juste le poiteur
	 * de la texture.
	 * <br>
	 * si la texture n'a jamais était inversé, via cette fonction,
	 * alors elle est inversé.
	 * 
	 * @param mat
	 * @return
	 */
	public static int[][] reverseXTex(int[][] mat) {
		int i = -1;
		for (int j = 0; j < reverseXTexs.size(); j++) {
			if (mat == reverseXTexs.keySet().toArray()[j]) {
				i = j;
				break;
			}
		}
		
		if (i < 0) reverseXTexs.put(mat, reverseXMat(mat));
		return reverseXTexs.get(mat);
	}
	
	/**
	 * supprime toutes les textures chargé en mémoire
	 */
	public static void clearTextures() {
		for (int i = reverseXTexs.size()-1; i >= 0; i--) {
			reverseXTexs.remove(reverseXTexs.keySet().toArray()[i]);
		}
		for (int i = textures.size()-1; i >= 0; i--) {
			textures.remove(textures.keySet().toArray()[i]);
		}
	}
	
	/**
	 * effectue une rotation sur la matrcie {@code mat} de -PI/4
	 * 
	 * @param mat la matrice que l'on souhaite rotater
	 * @return une version rotater de la matrice {@code mat}
	 */
	public static int[][] rotateMat(int[][] mat) {
		int[][] newMat = new int[mat[0].length][mat.length];
		for (int iY = 0; iY < mat[0].length; iY++) {
			for (int iX = 0; iX < mat.length; iX++) {
				newMat[iY][iX] = mat[mat.length-1-iX][iY];
			}
		}
		return newMat;
	}
	
	/**
	 * retourne l'angle formé par la droite passant par les deux point
	 * sur la droite horizontal passant par le point 1
	 * 
	 * @param x1 abscisses du point 1
	 * @param y1 ordonnées du point 1
	 * @param x2 abscisses du point 2
	 * @param y2 ordonnéss du point 2
	 * @return l'angle en radian
	 */
	public static double getAlpha(double x1, double y1, double x2, double y2) {
		double deltaX = x2-x1;
		double deltaY = y2-y1;
		double alpha = Math.atan(deltaY/deltaX);
		if (deltaX < 0) alpha += Math.PI;
		return alpha;
	}
	
//	/**
//	 * Affiche une matrice dans la console
//	 * 
//	 * @param mat la matrice à afficher
//	 */
//	static void printMat(int[][] mat) {
//		for (int y = 0; y < mat.length; y++) {
//			for (int x = 0; x < mat[y].length; x++) {
//				System.out.print(mat[y][x]+" ");
//			}
//			System.out.println();
//		}
//	}
	
	/**
	 * Retourne la largeur d'un texte
	 * 
	 * @param text texte en question
	 * @param g2d <code>Graphics2D</code> qui sert à connaitre le <code>Font</code> à étudier
	 * @return la largeur du texte
	 */
	public static int getTextW(String text, Graphics2D g2d) {
		FontMetrics fm = g2d.getFontMetrics();
		Rectangle2D textBounds = fm.getStringBounds(text, g2d);
		return (int) textBounds.getWidth();
	}
	
	/**
	 * <p>Retourne la hauteur d'un texte, mais à un décalge par rapport à la réalité <br>
	 * il faut avec la police Arial enlevé la taille de la police*20/50.</p>
	 * 
	 * <p>J'aimerais bien faire des test pour plein de police histoie de tout avoir
	 * facilement</p>
	 * 
	 * @param text texte en question
	 * @param g2d <code>Graphics2D</code> qui sert à connaitre le <code>Font</code> à étudier
	 * @return la hauteur du texte
	 */
	public static int getTextH(String text, Graphics2D g2d) {
		FontMetrics fm = g2d.getFontMetrics();
		Rectangle2D textBounds = fm.getStringBounds(text, g2d);
		return (int) textBounds.getHeight()-g2d.getFont().getSize()*20/50;
	}
	
	/**
	 * Affiche tout les pouvoirs et leurs quantité
	 * contenue dans une liste de pouvoir
	 * 
	 * @param powerList liste de pouvoir à afficher
	 * @param left si ou veut l'afficher à gauche, sinon c'est à droite
	 * @param up si on veut l'afficher en haut, sinon c'est en bas
	 * @param play
	 * @param g2d
	 */
	public static void displayPowerList(int[] powerList, boolean left, boolean up, Play play, Graphics2D g2d) {
		int powW = (int)(32*play.scaleW);
		int powH = (int)(50*play.scaleW);
		int gap = (int)(10*play.scaleW);
		int num = 0;
		
		int x;
		if (left) x = gap;
		else {
			int numOfPower = 0;
			for (int pow : powerList) {
				if (pow != 0) numOfPower++;
			}
			x = play.playP.getWidth()-numOfPower*(gap+powW);
		}
		int y;
		if (up) y = gap;
		else y = play.playP.getHeight()-gap-powH;
		
		for (int i = 0; i < powerList.length; i++) {
			if (powerList[i] != 0) {
				// affichage du pouvoir
				Power pow = Power.values()[i];
				pow.display(x+num*(gap+powW), y, powW, powH, play.color, g2d);
				
				String text = Integer.toString(powerList[i]);
				
				// pour les addSkills
				if (pow.isAddSkillPower()) {
					AddSkill as = play.room.me.addSkills[pow.addSkillId];
					text = as.getNum()+"/"+as.getMaxNum();
					
					// affichage de la touche
					g2d.setColor(play.color);
					g2d.setFont(new Font("ARIAL", Font.BOLD, (int)(12*play.scaleW)));
					String textI = as.getInputName();
					int textH = getTextH(textI, g2d);
					drawString(textI, x+num*(gap+powW)+(int)(4*play.scaleW), y+textH+(int)(4*play.scaleW), g2d);
					
					// affichage de la progression
					if (as.getProgression() != 100) {
						Color c = play.color;
						g2d.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 150));
						int h = (int)(powH*(100-as.getProgression())/100);
						g2d.fillRect(x+num*(gap+powW), y+powH-h, powW, h);
					}
				}
				
				// affichage du nombre de pocession du pouvoirs
				if (powerList[i] != 1 || pow.isAddSkillPower()) {
					g2d.setColor(play.color);
					g2d.setFont(new Font("ARIAL", Font.BOLD, (int)(12*play.scaleW)));
					int textW = getTextW(text, g2d);
					drawString(text, x+num*(gap+powW)+powW-textW-(int)(4*play.scaleW), y+powH-(int)(4*play.scaleW), g2d);
				}
				num++;
			}
		}
	}
	
	public static void drawString(String text, int x, int y, Graphics2D g2d) {
		Graphics2D g2d2 = (Graphics2D) g2d.create();
		g2d2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d2.drawString(text, x, y);
		g2d2.dispose();
	}
	
	/**
	 * ligne en poitillet
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param c
	 * @param g2d
	 */
	public static void drawSemiLine(int x1, int y1, int x2, int y2, Color c, Graphics2D g2d) {
		double distMax = Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
		double dist = 0;
		double v = 2*PIXEL_W+0.1;
		double x = x1;
		double y = y1;
		double alpha = getAlpha(x1, y1, x2, y2);
		
		g2d.setColor(c);
		while (dist < distMax) {
			g2d.fillRect((int)(x-x%PIXEL_W), (int)(y-y%PIXEL_H), PIXEL_W, PIXEL_H);
			
			x += v*Math.cos(alpha);
			y += v*Math.sin(alpha);
			dist += v;
		}
	}
	
	public static boolean deltaRandom(int rand, double delta) {
//		return UsefulTh.rand.nextDouble() < Math.pow(1/(double)rand, 1/delta);
		return UsefulTh.rand.nextDouble() < (1/(double)rand)*(delta);
	}
	
//	public static void drawCircularGradient(int x, int y, int rMax, Color c1, Color c2, Graphics2D g2d) {
//		int red1 = c1.getRed();
//		int green1 = c1.getGreen();
//		int blue1 = c1.getBlue();
//		int alpha1 = c1.getAlpha();
//		
//		int red2 = c2.getRed();
//		int green2 = c2.getGreen();
//		int blue2 = c2.getBlue();
//		int alpha2 = c2.getAlpha();
//		
//		for (int iX = -rMax; iX < rMax; iX++) {
//			for (int iY = -rMax; iY < rMax; iY++) {
//				int r = (int)Math.sqrt(iX*iX+iY*iY);
//				if (r < rMax) {
//					int red3 = red1+r*(red2-red1)/rMax;
//					int green3 = green1+r*(green2-green1)/rMax;
//					int blue3 = blue1+r*(blue2-blue1)/rMax;
//					int alpha3 = alpha1+r*(alpha2-alpha1)/rMax;
//					
//					g2d.setColor(new Color(red3, green3, blue3, alpha3));
//					g2d.fillRect(x+iX, y+iY, 1, 1);
//				}
//			}
//		}
//	}
}
