package fr.SamuelVedel.Play.AddSkill;

import fr.SamuelVedel.Play.Input;
import fr.SamuelVedel.Play.Me;
//import fr.SamuelVedel.Play.Room;

public class TpSkill extends AddSkill {
	
//	private Room room;
	private Me user;
	
	public TpSkill(Me user/*, Room room*/) {
		this.user = user;
//		this.room = room;
		type = TP_TYPE;
		setCooldown(30*60); // 30 sec
		setInput(new Input(Input.MOUSE_INPUT, 3, "M3")); // F pour Bombe
	}
	
	@Override
	protected void action() {
		user.tp();
	}
}
