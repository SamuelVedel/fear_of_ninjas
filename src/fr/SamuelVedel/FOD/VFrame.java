package fr.SamuelVedel.FOD;

import java.awt.Cursor;
import java.awt.GraphicsDevice;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class VFrame extends JFrame {
	private static final long serialVersionUID = 1366441344407311511L;
	
	private boolean fullScreen;
	
	public VFrame(/*String name*/) {
//		setName(name);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void setVoidCursor() {
		setCursor((Cursor) Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(32, 32, BufferedImage.TRANSLUCENT), new Point(0, 0), ""));
	}
	
	public boolean isInFullScreen() {
		return fullScreen;
	}
	
	public void setFullScreen(boolean fullScreen) {
		if (fullScreen && !this.fullScreen) {
			dispose();
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			setUndecorated(true);
			setResizable(false);
			if (System.getProperty("os.name").matches("Linux")) {
				GraphicsDevice gdevice = getGraphicsConfiguration().getDevice();
				gdevice.setFullScreenWindow(this);
			}
			setVisible(true);
			this.fullScreen = true;
		} else if (!fullScreen && this.fullScreen) {
			dispose();
			setUndecorated(false);
			setResizable(true);
			setSize(800, 600);
			setLocationRelativeTo(null);
			setVisible(true);
			this.fullScreen = false;
		}
	}
	
	public void toggleFullScreen() {
		if (isInFullScreen()) {
			setFullScreen(false);
		} else {
			setFullScreen(true);
		}
	}
}
