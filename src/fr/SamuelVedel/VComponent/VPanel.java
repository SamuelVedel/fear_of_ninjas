package fr.SamuelVedel.VComponent;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class VPanel extends VComponent {
	
	private ArrayList<VComponent> vcList = new ArrayList<>();
//	private Vector<VComponent> vcList = new Vector<>();
	
//	private JPanel jp;
	
	private KeyListener kl = new KeyListener() {
		
		@Override
		public void keyTyped(KeyEvent e) {
			for (int i = vcList.size()-1; i >= 0; --i) {
				vcList.get(i).keyTyped(e);
			}
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			for (int i = vcList.size()-1; i >= 0; --i) {
				vcList.get(i).keyReleased(e);
			}
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			for (int i = vcList.size()-1; i >= 0; --i) {
				vcList.get(i).keyPressed(e);
			}
		}
	};
	private MouseListener ml = new MouseListener() {
		
		@Override
		public void mouseReleased(MouseEvent e) {
			for (int i = vcList.size()-1; i >= 0; --i) {
				vcList.get(i).mouseReleased(e);
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			for (int i = vcList.size()-1; i >= 0; --i) {
				vcList.get(i).mousePressed(e);
			}
//			if (hasFocus() && jp != null) {
//				jp.grabFocus();
//			}
		}
		
		@Override
		public void mouseExited(MouseEvent e) {}
		
		@Override
		public void mouseEntered(MouseEvent e) {}
		
		@Override
		public void mouseClicked(MouseEvent e) {}
	};
	private MouseMotionListener mml = new MouseMotionListener() {
		
		@Override
		public void mouseMoved(MouseEvent e) {
			for (int i = vcList.size()-1; i >= 0; --i) {
				vcList.get(i).mouseMoved(e);
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			for (int i = vcList.size()-1; i >= 0; --i) {
				vcList.get(i).mouseDragged(e);
			}
		}
	};
	
	public VPanel(int x, int y, int w, int h, int widthReference, int heightReference) {
		super(x, y, w, h, widthReference, heightReference);
		initListeners();
	}
	
	public VPanel(int x, int y, int w, int h) {
		this(x, y, w, h, 0, 0);
	}
	
	private void initListeners() {
		addKeyListener(kl);
		addMouseListener(ml);
		addMouseMotionListener(mml);
	}
	
//	public void addListenersToAJPanel(JPanel jp) {
//		jp.addKeyListener(this);
//		jp.addMouseListener(this);
//		jp.addMouseMotionListener(this);
//		this.jp = jp;
//	}
	
	public void addMlToAComponent(Component c) {
		c.addMouseListener(this);
	}
	
	public void removeMlToAComponent(Component c) {
		c.removeMouseListener(this);
	}
	
	public void addMmlToAComponent(Component c) {
		c.addMouseMotionListener(this);
	}
	
	public void removeMmlToAComponent(Component c) {
		c.removeMouseMotionListener(this);
	}
	
	public void addKlToAComponent(Component c) {
		c.addKeyListener(this);
	}
	
	public void removeKlToAComponent(Component c) {
		c.removeKeyListener(this);
	}
	
	public void add(VComponent vc) {
		vcList.add(vc);
		
		vc.setWidthReference(getWidth());
		vc.setHeightReference(getHeight());
		vc.setAdjustment(ADJUSTMENT_BY_WIDTH_AND_HEIGHT);
		vc.setAlignment(NO_ALIGNMENT);
	}
	
	public void remove(int index) {
		vcList.remove(index);
	}
	
	public void remove(VComponent vc) {
		for (int i = vcList.size()-1; i >= 0; i--) {
			if (vcList.get(i) == vc) {
				vcList.remove(i);
			}
		}
	}
	
	@Override
	public void adjust(int widthRefrence, int heightRefrence) {
		super.adjust(widthRefrence, heightRefrence);
		for (int i = vcList.size()-1; i >= 0; --i) {
			VComponent vc = vcList.get(i);
			vc.adjust(getActualWidth(), getActualHeight());
			vc.setActualX(vc.getActualX()+getActualX());
			vc.setActualY(vc.getActualY()+getActualY());
		}
	}
	
	@Override
	public void display(Color c2, Graphics2D g2d) {
		for (int i = vcList.size()-1; i >= 0; --i) {
			vcList.get(i).display(c2, g2d);
		}
	}
}
