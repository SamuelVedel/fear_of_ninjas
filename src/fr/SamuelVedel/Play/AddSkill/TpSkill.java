package fr.SamuelVedel.Play.AddSkill;

import fr.SamuelVedel.Play.Input;
import fr.SamuelVedel.Play.Me;
//import fr.SamuelVedel.Play.Room;

public class TpSkill extends AddSkill {
	
//	private Room room;
	private Me user;
	
	public TpSkill(Me user) {
		this.user = user;
		type = TP_TYPE;
		setInput(new Input(Input.MOUSE_INPUT, 3, "M3")); // F pour Bombe
	}
	
	@Override
	protected void action() {
		user.tp();
	}
}
