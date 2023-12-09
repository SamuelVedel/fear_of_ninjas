package fr.SamuelVedel.Play.Debuff;

import fr.SamuelVedel.Play.Entity;
import fr.SamuelVedel.Play.Room;

/*  ___
 * (* *)
 *  ) (
 * (( ))
 */

/**
 * Class créée le 17/08/2023
 * 
 * @author samue
 *
 */
public class Petrification extends Debuff {
	
	public Petrification(Entity victim, Room room) {
		super(victim, room);
		type = PETRIFICATION_TYPE;
		tMax = 2*60;
		t = tMax;
	}
	
//	@Override
//	protected void preEffect() {}
	
	@Override
	protected void effect(double delta) {
		victim.x = victim.oldX;
		victim.y = victim.oldY;
		victim.vFall = 0;
	}

}
