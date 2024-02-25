package fr.svedel.fod.menu;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import fr.svedel.fod.FodButton;
import fr.svedel.fod.UsefulTh;
import fr.svedel.fod.VFrame;
import fr.svedel.vcomponent.VActionListener;
import fr.svedel.vcomponent.VButton;
import fr.svedel.vcomponent.VComponent;
import fr.svedel.vcomponent.VPanel;

public class Menu implements KeyListener, VActionListener, MouseMotionListener {
	
	private VFrame jf = new VFrame();
	protected int mouseX, mouseY;
	
	protected double scaleW;
	
	protected Color color;
	
	private MenuPainter menuP = new MenuPainter(this);
	
	public VPanel vp = new VPanel(0, 0, 800, UsefulTh.heightForNoScale,
								  800, UsefulTh.heightForNoScale);
	private final int buttonW = 220;
	private final int buttonH = 60;
	private final int buttonGap = 20;
	private FodButton[] buttons = {
			new FodButton(buttonGap, UsefulTh.heightForNoScale-2*(buttonH+buttonGap),
						  buttonW, buttonH, 800, UsefulTh.heightForNoScale, "Jeux"),
			/*new FodButton(buttonGap, UsefulTh.heightForNoScale-2*(buttonH+buttonGap),
			  buttonW, buttonH, 800, UsefulTh.heightForNoScale, "Option"),*/
			new FodButton(buttonGap, UsefulTh.heightForNoScale-buttonH-buttonGap,
						  buttonW, buttonH, 800, UsefulTh.heightForNoScale, "Sortie")
	};
	
	private boolean wantToPlay = false;
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 27) System.exit(0);
		else if (e.getKeyCode() == 122) {
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
	
	@Override
	public void action(VComponent source, MouseEvent e) {
		if (source == buttons[0]) wantToPlay = true;
		else if (source == buttons[1]) System.exit(0);
	}
	
	public Menu() {
		initJf();
		initColor();
		
		for (FodButton fb : buttons) {
			vp.add(fb);
			fb.getBorderWidth().setValue(2*UsefulTh.pixelW);
			fb.setAddW(10);
			fb.getFontSize().setValue(35);
			fb.addVActionListener(this);
			fb.setBorder(color);
			fb.setForeground(color);
		}
		vp.setAdjustment(VComponent.ADJUSTMENT_BY_HEIGHT);
		vp.addMlToAComponent(menuP);
		vp.addMmlToAComponent(menuP);
		
		action();
	}
	
	private void initJf() {
		jf.setTitle("Fear of ninjas");
		jf.setFullScreen(true);
		jf.setVoidCursor();
		jf.addKeyListener(this);
		menuP.addMouseMotionListener(this);
		jf.setContentPane(menuP);
		jf.setVisible(true);
	}
	
	/**
	 * initialise la deuxième couleur (1ere : noir) qui va composé la salle,
	 * la couleur ne peut pas être trop sombre
	 */
	private void initColor() {
		color = new Color(UsefulTh.rand.nextInt(256), UsefulTh.rand.nextInt(256),
						  UsefulTh.rand.nextInt(256));
		while (color.getRed()+color.getGreen()+color.getBlue() < 300) {
			color = new Color(UsefulTh.rand.nextInt(256), UsefulTh.rand.nextInt(256),
							  UsefulTh.rand.nextInt(256));
		}
	}
	
	private void action() {
		while (!wantToPlay) {
			jf.repaint();
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		jf.dispose();
	}
}
