package fr.svedel.fod.play.particle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import fr.svedel.fod.UsefulTh;
import fr.svedel.fod.play.Play;

public class LightParticle extends Particle {
	
	private Play play;
	
	private int w;
	private int h;
	private double x;
	private double y;
	
	private double aX = (UsefulTh.rand.nextBoolean()? -1: 1)*0.01;
	private double vX;
	private double vY = 1;
	
	public LightParticle(Play play) {
		this.play = play;
		w = UsefulTh.rand.nextInt(150-60)+60;
		h = w;
		y = -h;
		x = UsefulTh.rand.nextInt(play.room.width-w);
	}
	
	@Override
	public void action(double delta) {
		x += vX*delta;
		y += vY*delta;
		vX += aX*delta;
		
		//if (UsefulTh.rand.nextInt(10) == 0) {
		if (UsefulTh.rand.nextDouble() < ((double)1/10)*delta) {
			aX *= -1;
		}
		
		if (y > play.room.height) noMoreReasonToBe = true;
	}

	@Override
	public void display(Color c, Graphics2D g2d) {
		Graphics2D g2d2 = (Graphics2D) g2d.create();
		g2d2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d2.setColor(new Color(play.color.getRed(), play.color.getGreen(), play.color.getBlue(), 5));
		g2d2.fillOval(((int)x), ((int)y), w, h);
		
		g2d2.setColor(new Color(play.color.getRed(), play.color.getGreen(), play.color.getBlue(), 10));
		int w2 = (int)(0.47*w);
		int h2 = (int)(0.47*h);
		g2d2.fillOval(((int)x+w/2-w2/2), ((int)y+h/2-h2/2), w2, h2);
		
		g2d2.setColor(new Color(play.color.getRed(), play.color.getGreen(), play.color.getBlue(), 50));
		w2 = (int)(0.1*w);
		h2 = (int)(0.1*h);
		g2d2.fillOval(((int)x+w/2-w2/2), ((int)y+h/2-h2/2), w2, h2);
		
		g2d2.dispose();
	}
	
}
