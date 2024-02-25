package fr.svedel.vcomponent;

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
	
	private VAdjustInt widthReference;
	private VAdjustInt heightReference;
	private int autoAdjustment = NO_ADJUSTMENT;
	private int autoAlignment = NO_ALIGNMENT;
//	private int horizontalAlignment = LEFT_ALIGNMENT;
//	private int verticalAlignment = TOP_ALIGNMENT;
	
	private VAdjustInt width;
	private VAdjustInt height;
	private VAdjustInt x;
	private VAdjustInt y;
	
	private ArrayList<KeyListener> keyLs = new ArrayList<>();
	private ArrayList<MouseListener> mouseLs = new ArrayList<>();
	private ArrayList<MouseMotionListener> mouseMotionLs = new ArrayList<>();
	
	private boolean focus = false;
	private boolean focusable = true;
	private boolean mouseIn = false;
	private boolean pressed = false;
	
	public VComponent(int x, int y, int w, int h, int widthReference, int heightReference) {
		this.x = new VAdjustInt(x);
		this.y = new VAdjustInt(y);
		this.width = new VAdjustInt(w);
		this.height = new VAdjustInt(h);
		this.widthReference = new VAdjustInt(widthReference);
		this.heightReference = new VAdjustInt(heightReference);
	}
	
	public VComponent(int x, int y, int w, int h) {
		this(x, y, w, h, 0, 0);
	}
	
	public VAdjustInt getX() {
		return x;
	}
	
	public VAdjustInt getY() {
		return y;
	}
	
	public VAdjustInt getWidth() {
		return width;
	}
	
	public VAdjustInt getHeight() {
		return height;
	}
	
	public VAdjustInt getWidthReference() {
		return widthReference;
	}
	
	public VAdjustInt getHeightReference() {
		return heightReference;
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
		if (e.getX() > x.getCurrentValue()
			&& e.getX() < x.getCurrentValue()+width.getCurrentValue()
			&& e.getY() > y.getCurrentValue()
			&& e.getY() < y.getCurrentValue()+height.getCurrentValue()) {
			if (!mouseIn) {
				mouseEntered(e);
			}
		} else if (mouseIn) {
			mouseExited(e);
		}
	}
	// fin des fonctions des listeners ------
	
	public void adjustValue(VAdjustInt value, boolean preferenceForWidth) {
		switch (getAdjustment()) {
		case NO_ADJUSTMENT :
			value.adjust(null);
			break;
		case ADJUSTMENT_BY_WIDTH_AND_HEIGHT :
			if (preferenceForWidth) {
				value.adjust(widthReference);
			} else {
				value.adjust(heightReference);
			}
			break;
		case ADJUSTMENT_BY_WIDTH :
			value.adjust(widthReference);
			break;
		case ADJUSTMENT_BY_HEIGHT :
			value.adjust(heightReference);
			break;
		case ADJUSTMENT_BY_THE_SMALLEST :
			if (widthReference.getValue()*width.getValue()/widthReference.getCurrentValue()
				>= heightReference.getValue()*width.getValue()/heightReference.getCurrentValue()) {
				autoAdjustment = ADJUSTMENT_BY_WIDTH;
				adjustValue(value, preferenceForWidth);
			} else {
				autoAdjustment = ADJUSTMENT_BY_HEIGHT;
				adjustValue(value, preferenceForWidth);
			}
			autoAdjustment = ADJUSTMENT_BY_THE_SMALLEST;
		}
	}
	
	public void adjustValue(VAdjustInt value) {
		adjustValue(value, true);
	}
	
	public void adjust(int widthRefrence, int heightRefrence) {
		this.widthReference.setCurrentValue(widthRefrence);
		this.heightReference.setCurrentValue(heightRefrence);
		
		switch (autoAdjustment) {
		case NO_ADJUSTMENT :
			width.adjust(null);;
			height.adjust(null);
			
			x.adjust(null);;
			y.adjust(null);
			break;
		case ADJUSTMENT_BY_WIDTH_AND_HEIGHT :
			width.adjust(widthReference);
			height.adjust(heightReference);
			
			x.adjust(widthReference);
			y.adjust(heightReference);
			break;
		case ADJUSTMENT_BY_WIDTH :
			width.adjust(widthReference);
			height.adjust(widthReference);;
			
			x.adjust(widthReference);
			if (autoAlignment == NO_ALIGNMENT) {
				y.adjust(widthReference);
			} else if (autoAlignment == CENTER_ALIGNMENT) {
				y.adjust(y.getValue()+height.getValue()/2, heightReference);
				y.setCurrentValue(y.getCurrentValue()-height.getCurrentValue()/2);
			} else {
				y.adjust(heightReference.getValue()-(y.getValue()+height.getValue()), widthReference);
				y.setCurrentValue(heightReference.getCurrentValue()-y.getCurrentValue()-height.getCurrentValue());
				//y[1] = heightReference[1]-widthReference[1]*(heightReference[0]-(y[0]+height[0]))/widthReference[0]-widthReference[1]*height[0]/widthReference[0];
			}
			break;
		case ADJUSTMENT_BY_HEIGHT :
			width.adjust(heightReference);
			height.adjust(heightReference);
			
			if (autoAlignment == NO_ALIGNMENT) {
				x.adjust(heightReference);
			} else if (autoAlignment == CENTER_ALIGNMENT) {
				x.adjust(x.getValue()+width.getValue()/2, widthReference);
				x.setCurrentValue(x.getCurrentValue()-width.getCurrentValue()/2);
				//x[1] = widthReference[1]*(x[0]+width[0]/2)/widthReference[0]-width[1]/2;
			} else if (autoAlignment == BOTTOM_ALIGNMENT) {
				x.adjust(widthReference.getValue()-(x.getValue()+width.getValue()), heightReference);
				x.setCurrentValue(widthReference.getCurrentValue()-x.getCurrentValue()-width.getCurrentValue());
				//x[1] = widthReference[1]-heightReference[1]*(widthReference[0]-(x[0]+width[0]))/heightReference[0]-heightReference[1]*width[0]/heightReference[0];
			}
			y.adjust(heightReference);
			break;
		case ADJUSTMENT_BY_THE_SMALLEST :
			//if (widthReference[0]*width[0]/widthReference[1] >= heightReference[0]*width[0]/heightReference[1]) {
			if (widthReference.getValue()*width.getValue()/widthReference.getCurrentValue()
				>= heightReference.getValue()*width.getValue()/heightReference.getCurrentValue()) {
				autoAdjustment = ADJUSTMENT_BY_WIDTH;
				adjust(widthRefrence, heightRefrence);
			} else {
				autoAdjustment = ADJUSTMENT_BY_HEIGHT;
				adjust(widthRefrence, heightRefrence);
			}
			autoAdjustment = ADJUSTMENT_BY_THE_SMALLEST;
			break;
		}
	}
	
	public abstract void display(Graphics2D g2d);
}
