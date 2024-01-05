package fr.SamuelVedel.Play;

import java.awt.Graphics2D;

import fr.SamuelVedel.FOD.UsefulTh;
import fr.SamuelVedel.Play.Cube.Cube;

/*  ___
 * (° °)
 *  ) (
 * (( ))
 */

/**
 * Code des itemes qui pourrons tombé des énimies dead
 * <p>
 * Class créer le 02/12/2022, elle avait était créer plus tôt
 * avec genre 3-5 variable mais vol d'ordi enfaite
 * 
 * @author Samuel Vedel
 *
 */
public class Item {
	
	public boolean noMoreReasonToBe = false;
	
	public Power pow;
	private Play play;
	private Room room;
	
	public int w = UsefulTh.cubeW/*-2*UsefulTh.pixelW*/;
	public int h = w;
	private double aFall = UsefulTh.g;
	private double vY;
	public double oldY;
	public double x, y;

	// variable pour quand il à touché le sol
	/** accélération de quand il à touché le sol */
	private double aY = -0.1;
	/** hauteurs auquelles la vitesse est nulle */
	private int minY, maxY;
	
	
	private boolean groundTouched = false;
	
	public Item(int x, int y, Power pow, Room room) {
		this.x = x-w/2;
		this.y = y-h;
		oldY = this.y;
		this.room = room;
		play = room.play;
		this.pow = pow;
	}
	
	public Item(int x, int y, Room room) {
		this(x, y, Power.getRandPowerForMe(), room);
	}
	
	public void actions(double delta) {
		oldY = y;
		y += vY*delta;
		if (!groundTouched) {
			vY += aFall*delta;
			
			// collisions avec les potentiels cubes porches
			for (int iY = (int)(y/UsefulTh.cubeH); iY < (int)(y/UsefulTh.cubeH)+(int)(h/UsefulTh.cubeH)+2; iY++) {
				for (int iX = (int)(x/UsefulTh.cubeW); iX < (int)(x/UsefulTh.cubeW)+(int)(w/UsefulTh.cubeW)+2; iX++) {
					if (room.isYInCubes(iY) && room.isXInCubes(iX) && room.cubes[iY][iX] != null) {
						if (room.cubes[iY][iX].contact(this) == Cube.UP_CONTACT) {
							groundTouched = true;
							vY = 0;
							maxY = (int) y;
							minY = (int) y-UsefulTh.cubeH;
						}
					}
				}
			}
		} else {
			vY += aY*delta;
			int center = minY+(maxY-minY)/2;
			if (y < minY) {
				y = minY;
				vY = 0;
			} else if (y > maxY) {
				y = maxY;
				vY = 0;
			} else if ((y >= center && aY > 0 ) || (y <= center && aY < 0)) {
				aY *= -1;
				y = center;
			}
		}
		
		// contact avec moi
		if (x+w > room.me.x && x < room.me.x+room.me.w
			&& y+h > room.me.y && y < room.me.y+room.me.h) {
			room.me.addPower(pow);
			room.cht.addText("Vous avez obtenu \""+pow.name.toLowerCase()+"\"");
			pow.sayEffectInAChat(room.cht);
			noMoreReasonToBe = true;
		}
		
		if (y > room.height) noMoreReasonToBe = true;;
	}
	
	public void display(Graphics2D g2d) {
		pow.display((int)x, (int)y, w, h, play.color, g2d);
	}
}
