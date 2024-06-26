package fr.svedel.fod.play;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import fr.svedel.fod.UsefulTh;
import fr.svedel.fod.VFrame;

/*  ___
 * (� �)
 *  ) (
 * (( ))
 */

/**
 * Class qui lie toute les autre et fait le jeux quoi <br>
 * c'est une passerelle
 * <p>
 * Class cr��e le 17/06/2022
 * 
 * @author Samuel Vedel
 *
 */
public class Play implements KeyListener, MouseMotionListener {
	
	private double oldTime;
	private double currentTime;
	private int fps = 60;
	
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	public VFrame jf = new VFrame();
	
	/** hauteur pour laquel le zoom est de 1 */
	public double scaleW;
	public PlayPainter playP = new PlayPainter(this);
	
	public Color color = Color.WHITE;
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
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { // ECHAP c'est pause
			if (phase == PLAY_PHASE) phase = PAUSE_PHASE;
			else if (phase == PAUSE_PHASE) phase = PLAY_PHASE;
		} else if (e.getKeyCode() == KeyEvent.VK_F11) { // F11 plein ecran <--> pas plein ecran
			jf.toggleFullScreen();
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
		initColor();
		initJf();
		actions();
	}
	
	private void initJf() {
		jf.setTitle("Fear of ninjas");
		jf.setFullScreen(true);
		jf.setVoidCursor();
		jf.setContentPane(playP);
		jf.addKeyListener(this);
		
		// ajout de room
		jf.addKeyListener(room.kl);
		playP.addMouseListener(room.ml);
		
		playP.addMouseMotionListener(this);
		jf.setVisible(true);
	}
	
	/**
	 * initialise la deuxi�me couleur (1ere : noir) qui va compos� la salle,
	 * la couleur ne peut pas �tre trop sombre
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
	 * on d�finit la diff�rence de temps entre deux appels de fa�on
	 * � ce que delta == 1 si il y � eu exactement un 60�me de seconde
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
//		room.refresh(); // servait � tester les niveaux de boss
		
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
			toolkit.sync();
			try {
				Thread.sleep(1000/fps);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void die() {
		phase = Play.DEAD_PHASE;
		endS.setUsable(true);
		endS.setColor(color);
	}
	
	public void restart() {
		initColor();
		jf.removeKeyListener(room.kl);
		playP.removeMouseListener(room.ml);
		room = new Room(this);
		jf.addKeyListener(room.kl);
		playP.addMouseListener(room.ml);
		phase = PLAY_PHASE;
		room.cht.addText("Allez d�fonce tout cette fois >:D");
	}
}
