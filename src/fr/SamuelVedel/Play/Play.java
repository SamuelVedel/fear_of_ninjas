package fr.SamuelVedel.Play;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import fr.SamuelVedel.FOD.UsefulTh;

/*  ___
 * (° °)
 *  ) (
 * (( ))
 */

/**
 * Class qui lie toute les autre et fait le jeux quoi <br>
 * c'est une passerelle
 * <p>
 * Class créée le 17/06/2022
 * 
 * @author Samuel Vedel
 *
 */
public class Play implements KeyListener, MouseMotionListener {
	
	private double oldTime;
	private double currentTime;
	private int fps = 60;
	
	public JFrame jf = new JFrame();
	private boolean fullScreen = true;
	
	/** hauteur pour laquel le zoom est de 1 */
	public double scaleW;
	public PlayPainter playP = new PlayPainter(this);
	
	public Color color;
//	public Color color2;
	
	public static final int PLAY_PHASE = 0;
	public static final int PAUSE_PHASE = 1;
	public static final int CHOICE_PHASE = 2;
	public static final int DEAD_PHASE = 3;
	public static final int END_PHASE = 4;
	public int phase = PLAY_PHASE;
	
	public Room room = new Room(this);
	
	public ChoicePanel choiceP = new ChoicePanel(this);
	
	public EndScreen endS = new EndScreen(this);
	
	public double mouseX = -1;
	public double mouseY = -1;
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 27) { // ECHAP c'est pause
			if (phase == PLAY_PHASE) phase = PAUSE_PHASE;
			else if (phase == PAUSE_PHASE) phase = PLAY_PHASE;
		}
		else if (e.getKeyCode() == 122) { // F11 plein ecran <--> pas plein ecran
			jf.dispose();
			if (fullScreen) {
				jf.setUndecorated(false);
				jf.setResizable(true);
				jf.setSize(800, 600);
				jf.setLocationRelativeTo(null);
				fullScreen = false;
			} else {
				jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
				jf.setUndecorated(true);
				jf.setResizable(false);
				fullScreen = true;
			}
			jf.setVisible(true);
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	public Play() {
		initJf();
		initColor();
		actions();
	}
	
	private void initJf() {
		jf.setTitle("Fear of ninjas");
		jf.setSize(800, 600);
		jf.setLocationRelativeTo(null);
		jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
		jf.setUndecorated(true);
		jf.setResizable(false);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setContentPane(playP);
		jf.addKeyListener(this);
		
		// ajout de room
		jf.addKeyListener(room.kl);
		playP.addMouseListener(room.ml);
		
		playP.addMouseMotionListener(this);
		jf.setCursor((Cursor) Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(32, 32, BufferedImage.TRANSLUCENT), new Point(0, 0), ""));
		jf.setVisible(true);
	}
	
	/**
	 * initialise la deuxième couleur (1ere : noir) qui va composé la salle,
	 * la couleur ne peut pas être trop sombre
	 */
	private void initColor() {
		int sum = 0;
		final double minSum = 300;
		do {
			color = new Color(UsefulTh.rand.nextInt(256), UsefulTh.rand.nextInt(256), UsefulTh.rand.nextInt(256));
			sum = color.getRed()+color.getGreen()+color.getBlue();
		} while (sum < minSum);
		
//		double factor = 69/(double)sum;
//		color2 = new Color((int)(color.getRed()*factor), (int)(color.getGreen()*factor), (int)(color.getBlue()*factor));
	}
	
	public void setFps(int fps) {
		this.fps = fps;
	}
	
	/**
	 * on définit la différence de temps entre deux appels de façon
	 * à ce que delta == 1 si il y à eu exactement un 60ème de seconde
	 * 
	 * @return
	 */
	private double getDelta() {
		oldTime = currentTime;
		currentTime = ((double)System.currentTimeMillis())/(1000/60);
		return currentTime-oldTime;
	}
	
	private void actions() {
		room.cht.addText("Bienvenu et bonne partie (^_^)");
//		room.refresh(); // servait à tester les niveaux de boss
		
		getDelta();
		while (phase != END_PHASE) {
			double delta = getDelta();
			
			if (phase == PLAY_PHASE) {
				room.actions(delta);
				
				if (room.numOfKills >= room.goal) {
					phase = Play.CHOICE_PHASE;
					initColor();
					choiceP.refresh();
				}
			} else if (phase == CHOICE_PHASE) {
				if (choiceP.finalChoice != null) {
					room.me.addPower(choiceP.finalChoice);
					room.cht.addText("Vous avez choisis \""+choiceP.finalChoice.name.toLowerCase()+"\"");
//					choiceP.finalChoice.sayEffectInAChat(room.cht);
					phase = PLAY_PHASE;
					room.refresh();
				}
			}
			choiceP.actions(delta);
			
			jf.repaint();
			try {
				Thread.sleep(1000/fps);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void restart() {
		jf.removeKeyListener(room.kl);
		playP.removeMouseListener(room.ml);
		room = new Room(this);
		jf.addKeyListener(room.kl);
		playP.addMouseListener(room.ml);
		phase = PLAY_PHASE;
		room.cht.addText("Allez défonce tout cette fois >:D");
	}
}
