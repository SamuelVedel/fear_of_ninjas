package fr.svedel.fod.play.addskill;

import fr.svedel.fod.play.Input;
import fr.svedel.fod.play.Me;
//import fr.svedel.fod.play.Room;

public class TpSkill extends AddSkill {
	
//	private Room room;
	private Me user;
	
	public TpSkill(Me user) {
		this.user = user;
		type = TP_TYPE;
		setInput(new Input(Input.MOUSE_INPUT, 3, "M3")); // F pour Bombe
	}
	
	@Override
	protected void actions() {
		user.tp();
	}
}
