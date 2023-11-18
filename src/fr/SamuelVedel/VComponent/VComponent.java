package fr.SamuelVedel.VComponent;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public abstract class VComponent
	implements KeyListener, MouseListener, MouseMotionListener {
	
	public static final int NO_ADJUSTMENT = 0;
	public static final int ADJUSTMENT_BY_WIDTH_AND_HEIGHT = 1;
	public static final int ADJUSTMENT_BY_WIDTH = 2;
	public static final int ADJUSTMENT_BY_HEIGHT = 3;
	public static final int ADJUSTMENT_BY_THE_SMALLEST = 4;
	
	public static final int NO_ALIGNMENT = 0;
	public static final int CENTER_ALIGNMENT = 1;
	public static final int BOTTOM_ALIGNMENT = 2;
	
//	public static final int LEFT_ALIGNMENT = 0;
//	public static final int CENTER_ALIGNMENT = 1;
//	public static final int RIGHT_ALIGNMENT = 2;
//	public static final int TOP_ALIGNMENT = LEFT_ALIGNMENT;
//	public static final int BOTTOM_ALIGNMENT = RIGHT_ALIGNMENT;
	
	private int[] widthReference = new int[2];
	private int[] heightReference = new int[2];
	private int autoAdjustment = NO_ADJUSTMENT;
	private int autoAlignment = NO_ALIGNMENT;
//	private int horizontalAlignment = LEFT_ALIGNMENT;
//	private int verticalAlignment = TOP_ALIGNMENT;
	
	private int[] width = new int[2];
	private int[] height = new int[2];
	private int[] x = new int[2];
	private int[] y = new int[2];
	
	private ArrayList<KeyListener> keyLs = new ArrayList<>();
	private ArrayList<MouseListener> mouseLs = new ArrayList<>();
	private ArrayList<MouseMotionListener> mouseMotionLs = new ArrayList<>();
	
	private boolean focus = false;
	private boolean focusable = true;
	private boolean mouseIn = false;
	private boolean pressed = false;
	
	public VComponent(int x, int y, int w, int h, int widthReference, int heightReference) {
		this.x[0] = x;
		this.y[0] = y;
		this.width[0] = w;
		this.height[0] = h;
		this.widthReference[0] = widthReference;
		this.heightReference[0] = heightReference;
	}
	
	public VComponent(int x, int y, int w, int h) {
		this(x, y, w, h, 0, 0);
	}
	
	public int getX() {
		return x[0];
	}
	
	public int getActualX() {
		return x[1];
	}
	
	public void setX(int x) {
		this.x[0] = x;
	}
	
	public void setActualX(int x) {
		this.x[1] = x;
	}
	
	public int getY() {
		return y[0];
	}
	
	public int getActualY() {
		return y[1];
	}
	
	public void setY(int y) {
		this.y[0] = y;
	}
	
	public void setActualY(int y) {
		this.y[1] = y;
	}
	
	public int getWidth() {
		return width[0];
	}
	
	public int getActualWidth() {
		return width[1];
	}
	
	public void setWidth(int width) {
		this.width[0] = width;
	}
	
	public void setActualWidth(int width) {
		this.width[1] = width;
	}
	
	public int getHeight() {
		return height[0];
	}
	
	public int getActualHeight() {
		return height[1];
	}
	
	public void setHeight(int height) {
		this.height[0] = height;
	}
	
	public void setActualHeight(int height) {
		this.height[1] = height;
	}
	
	public int getWidthReference() {
		return widthReference[0];
	}
	
	public int getActualWidthReference() {
		return widthReference[1];
	}
	
	public void setWidthReference(int widthReference) {
		this.widthReference[0] = widthReference;
	}
	
	public void setActualWidthReference(int widthReference) {
		this.widthReference[1] = widthReference;
	}
	
	public int getHeightReference() {
		return heightReference[0];
	}
	
	public int getActualHeightReference() {
		return heightReference[1];
	}
	
	public void setHeightReference(int heightReference) {
		this.heightReference[0] = heightReference;
	}
	
	public void setActualHeightReference(int heightReference) {
		this.heightReference[1] = heightReference;
	}
	
	public int getAdjustment() {
		return autoAdjustment;
	}
	
	public void setAdjustment(int autoAdjustment) {
		this.autoAdjustment = autoAdjustment;
	}
	
	public int getAlignment() {
		return autoAlignment;
	}
	
	public void setAlignment(int autoAlignment) {
		this.autoAlignment = autoAlignment;
	}
	
//	public int getHorizontalAlignment() {
//		return horizontalAlignment;
//	}
//
//	public void setHorizontalAlignment(int horizontalAlignment) {
//		this.horizontalAlignment = horizontalAlignment;
//	}
//
//	public int getVerticalAlignment() {
//		return verticalAlignment;
//	}
//
//	public void setVerticalAlignment(int verticalAlignment) {
//		this.verticalAlignment = verticalAlignment;
//	}
	
	public void addKeyListener(KeyListener kl) {
		keyLs.add(kl);
	}
	
	public void removeKeyListener(KeyListener kl) {
		for (int i = keyLs.size()-1; i >= 0; i--) {
			if (keyLs.get(i) == kl) {
				keyLs.remove(i);
			}
		}
	}
	
	public void removeAllKeyListener() {
		keyLs.removeAll(keyLs);
	}
	
	public void addMouseListener(MouseListener ml) {
		mouseLs.add(ml);
	}
	
	public void removeMouseListener(MouseListener ml) {
		for (int i = mouseLs.size()-1; i >= 0; i--) {
			if (mouseLs.get(i) == ml) {
				mouseLs.remove(i);
			}
		}
	}
	
	public void removeAllMouseListener() {
		mouseLs.removeAll(mouseLs);
	}
	
	public void addMouseMotionListener(MouseMotionListener mml) {
		mouseMotionLs.add(mml);
	}
	
	public void removeMouseMotionListener(MouseMotionListener mml) {
		for (int i = mouseMotionLs.size()-1; i >= 0; i--) {
			if (mouseMotionLs.get(i) == mml) {
				mouseMotionLs.remove(i);
			}
		}
	}
	
	public void removeAllMouseMotionListener() {
		mouseMotionLs.removeAll(mouseMotionLs);
	}
	
	public boolean hasFocus() {
		return focus;
	}
	
	public void setFocus(boolean focus) {
		this.focus = focus;
	}
	
	public boolean isFocusable() {
		return focusable;
	}
	
	public void setFocusable(boolean focusable) {
		this.focusable = focusable;
		if (focusable == false) focus = false;
	}
	
	public boolean isMouseIn() {
		return mouseIn;
	}
	
	public boolean isPressed() {
		return pressed;
	}
	
	protected void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
	
	// fonctions des listeners --------------
	// KeyListener
	@Override
	public void keyTyped(KeyEvent e) {
		if (focus) {
			for (KeyListener kl : keyLs) {
				kl.keyTyped(e);
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (focus) {
			for (KeyListener kl : keyLs) {
				kl.keyPressed(e);
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (focus) {
			for (KeyListener kl : keyLs) {
				kl.keyReleased(e);
			}
		}
	}
	
	//MouseListener
	@Override
	public void mouseClicked(MouseEvent e) {
		for (MouseListener ml : mouseLs) {
			ml.mouseClicked(e);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (mouseIn) {
			pressed = true;
			if (focusable) focus = true;
			for (MouseListener ml : mouseLs) {
				ml.mousePressed(e);
			}
		} else {
			focus = false;
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (pressed) {
			pressed = false;
			for (MouseListener ml : mouseLs) {
				ml.mouseReleased(e);
			}
			if (mouseIn) {
				mouseClicked(e);
			}
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		mouseIn = true;
		for (MouseListener ml : mouseLs) {
			ml.mouseEntered(e);
		}
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		mouseIn = false;
		for (MouseListener ml : mouseLs) {
			ml.mouseExited(e);
		}
	}
	
	// MouseMotionListener
	@Override
	public void mouseDragged(MouseEvent e) {
		checkForEnteredAndExited(e);
		if (pressed && mouseIn) {
			for (MouseMotionListener mml : mouseMotionLs) {
				mml.mouseDragged(e);
			}
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		checkForEnteredAndExited(e);
		if (!pressed && mouseIn) {
			for (MouseMotionListener mml : mouseMotionLs) {
				mml.mouseMoved(e);
			}
		}
	}
	
	private void checkForEnteredAndExited(MouseEvent e) {
		if (e.getX() > x[1] && e.getX() < x[1]+width[1]
			&& e.getY() > y[1] && e.getY() < y[1]+height[1]) {
			if (!mouseIn) {
				mouseEntered(e);
			}
		} else if (mouseIn) {
			mouseExited(e);
		}
	}
	// fin des fonctions des listeners ------
//	public void addMlToAComponent(Component c) {
//		c.addMouseListener(this);
//	}
//	
//	public void removeMlToAComponent(Component c) {
//		c.removeMouseListener(this);
//	}
//	
//	public void addMmlToAComponent(Component c) {
//		c.addMouseMotionListener(this);
//	}
//	
//	public void removeMmlToAComponent(Component c) {
//		c.removeMouseMotionListener(this);
//	}
//	
//	public void addKlToAComponent(Component c) {
//		c.addKeyListener(this);
//	}
//	
//	public void removeKlToAComponent(Component c) {
//		c.removeKeyListener(this);
//	}
	
	public void adjust(int widthRefrence, int heightRefrence) {
		this.widthReference[1] = widthRefrence;
		this.heightReference[1] = heightRefrence;
		
		switch (autoAdjustment) {
		case NO_ADJUSTMENT :
			width[1] = width[0];
			height[1] = height[0];
			
			x[1] = x[0];
			y[1] = y[0];
			break;
		case ADJUSTMENT_BY_WIDTH_AND_HEIGHT :
			width[1] = widthReference[1]*width[0]/widthReference[0];
			height[1] = heightReference[1]*height[0]/heightReference[0];
			
			x[1] = widthReference[1]*x[0]/widthReference[0];
			y[1] = heightReference[1]*y[0]/heightReference[0];
			break;
		case ADJUSTMENT_BY_WIDTH :
			width[1] = widthReference[1]*width[0]/widthReference[0];
			height[1] = widthReference[1]*height[0]/widthReference[0];
			
			x[1] = widthReference[1]*x[0]/widthReference[0];
			if (autoAlignment == NO_ALIGNMENT) {
				y[1] = widthReference[1]*y[0]/widthReference[0];
			} else if (autoAlignment == CENTER_ALIGNMENT) {
				y[1] = heightReference[1]*(y[0]+height[0]/2)/heightReference[0]-height[1]/2;
			} else {
				y[1] = heightReference[1]-widthReference[1]*(heightReference[0]-(y[0]+height[0]))/widthReference[0]-widthReference[1]*height[0]/widthReference[0];
			}
			break;
		case ADJUSTMENT_BY_HEIGHT :
			width[1] = heightReference[1]*width[0]/heightReference[0];
			height[1] = heightReference[1]*height[0]/heightReference[0];
			
			if (autoAlignment == NO_ALIGNMENT) {
				x[1] = heightReference[1]*x[0]/heightReference[0];
			} else if (autoAlignment == CENTER_ALIGNMENT) {
				x[1] = widthReference[1]*(x[0]+width[0]/2)/widthReference[0]-width[1]/2;
			} else {
				x[1] = widthReference[1]-heightReference[1]*(widthReference[0]-(x[0]+width[0]))/heightReference[0]-heightReference[1]*width[0]/heightReference[0];
			}
			y[1] = heightReference[1]*y[0]/heightReference[0];
			break;
		default :
			if (widthReference[0]*width[0]/widthReference[1] >= heightReference[0]*width[0]/heightReference[1]) {
				autoAdjustment = ADJUSTMENT_BY_WIDTH;
				adjust(widthRefrence, heightRefrence);
			} else {
				autoAdjustment = ADJUSTMENT_BY_HEIGHT;
				adjust(widthRefrence, heightRefrence);
			}
			autoAdjustment = ADJUSTMENT_BY_THE_SMALLEST;
		}
	}
	
	public abstract void display(Color c2, Graphics2D g2d);
}
