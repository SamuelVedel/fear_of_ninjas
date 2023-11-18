package fr.SamuelVedel.Menu;

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
import fr.SamuelVedel.VComponent.VActionListener;
import fr.SamuelVedel.VComponent.VButton;
import fr.SamuelVedel.VComponent.VComponent;
import fr.SamuelVedel.VComponent.VPanel;

public class Menu implements KeyListener, VActionListener, MouseMotionListener {
	
	private JFrame jf = new JFrame();
	private boolean fullScreen = true;
	protected int mouseX, mouseY;
	
	protected double scaleW;
	
	protected Color color;
	
	private MenuPainter menuP = new MenuPainter(this);
	
	public VPanel vp = new VPanel(0, 0, 800, UsefulTh.heightForNoScale, 800, UsefulTh.heightForNoScale);
	private final int buttonW = 220;
	private final int buttonH = 60;
	private final int buttonGap = 20;
	private VButton[] buttons = {
			new VButton(buttonGap, UsefulTh.heightForNoScale-2*(buttonH+buttonGap), buttonW, buttonH, 800, UsefulTh.heightForNoScale, "Jeux"),
			/*new VButton(buttonGap, UsefulTh.heightForNoScale-2*(buttonH+buttonGap), buttonW, buttonH, 800, UsefulTh.heightForNoScale, "Option"),*/
			new VButton(buttonGap, UsefulTh.heightForNoScale-buttonH-buttonGap, buttonW, buttonH, 800, UsefulTh.heightForNoScale, "Sortie")
	};
	
	private boolean wantToPlay = false;
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 27) System.exit(0);
		else if (e.getKeyCode() == 122) {
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
	
	@Override
	public void action(VComponent source, MouseEvent e) {
		if (source == buttons[0]) wantToPlay = true;
		else if (source == buttons[1]) System.exit(0);
	}
	
	public Menu() {
		initJf();
		initColor();
		
		for (VButton vb : buttons) {
			vp.add(vb);
			vb.setBorderSize(2*UsefulTh.pixelW);
			vb.setAddW(10);
			vb.setFontSize(35);
			vb.addVActionListener(this);
		}
		vp.setAdjustment(VComponent.ADJUSTMENT_BY_HEIGHT);
		vp.addMlToAComponent(menuP);
		vp.addMmlToAComponent(menuP);
		
		action();
	}
	
	private void initJf() {
		jf.setTitle("Fear of ninjas");
		jf.setSize(800, 600);
		jf.setLocationRelativeTo(null);
		jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
		jf.setUndecorated(true);
		jf.setResizable(false);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setCursor((Cursor) Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(32, 32, BufferedImage.TRANSLUCENT), new Point(0, 0), ""));
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
		color = new Color(UsefulTh.rand.nextInt(256), UsefulTh.rand.nextInt(256), UsefulTh.rand.nextInt(256));
		while (color.getRed()+color.getGreen()+color.getBlue() < 300) {
			color = new Color(UsefulTh.rand.nextInt(256), UsefulTh.rand.nextInt(256), UsefulTh.rand.nextInt(256));
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
