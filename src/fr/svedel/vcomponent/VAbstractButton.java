package fr.svedel.vcomponent;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public abstract class VAbstractButton extends VComponent {
	
	private ArrayList<VActionListener> vActLs = new ArrayList<>();
	
	private boolean usable = true;
	
	private MouseListener ml = new MouseListener() {
		
		@Override
		public void mouseReleased(MouseEvent e) {}
		
		@Override
		public void mousePressed(MouseEvent e) {
			if (!usable) setPressed(false);
		}
		
		@Override
		public void mouseExited(MouseEvent e) {}
		
		@Override
		public void mouseEntered(MouseEvent e) {}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if (usable) executeVActLs(e);
		}
	};
	
	public VAbstractButton(int x, int y, int w, int h, int widthReference, int heightReference) {
		super(x, y, w, h, widthReference, heightReference);
		addMouseListener(ml);
	}
	
	public VAbstractButton(int x, int y, int w, int h) {
		this(x, y, w, h, 0, 0);
	}
	
	public boolean isUsable() {
		return usable;
	}
	
	public void setUsable(boolean usable) {
		this.usable = usable;
	}
	
	public void addVActionListener(VActionListener val) {
		vActLs.add(val);
	}
	
	public void removeVActionListener(VActionListener val) {
		for (int i = vActLs.size()-1; i >= 0; i--) {
			if (vActLs.get(i) == val) {
				vActLs.remove(i);
			}
		}
	}
	
	public void removeAllVActionListener() {
		vActLs.removeAll(vActLs);
	}
	
	public void executeVActLs(MouseEvent e) {
		for (VActionListener val : vActLs) {
			val.action(this, e);
		}
	}
	
	/**
	 * Supprime le {@code MouseListener} qui execute les {@code VActionListener}
	 * quand on clique sur le boutton
	 */
	protected void removeMLForVActLs() {
		removeMouseListener(ml);
	}
}
