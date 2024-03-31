package fr.svedel.fod.menu;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import fr.svedel.fod.UsefulTh;

public class MenuPainter extends JPanel {
	
	private static final long serialVersionUID = 2791347777378939890L;
	
	private Menu menu;
	
	public MenuPainter(Menu menu) {
		this.menu = menu;
	}
	
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(UsefulTh.BACKGROUND_COLOR);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		menu.scaleW = (double)getHeight()/UsefulTh.HEIGHT_FOR_NO_SCALE;
		
		menu.vp.adjust(getWidth(), getHeight());
		menu.vp.display(g2d);
		
		UsefulTh.displayTex(UsefulTh.cursor,
							(int)(menu.mouseX-UsefulTh.cursorW*menu.scaleW/2),
							(int)(menu.mouseY-UsefulTh.cursorH*menu.scaleW/2),
							(int)(UsefulTh.cursorW*menu.scaleW),
							(int)(UsefulTh.cursorH*menu.scaleW), menu.color, g2d);
		
		g2d.dispose();
	}
}
