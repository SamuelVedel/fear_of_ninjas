package fr.SamuelVedel.Play.AddSkill;

import fr.SamuelVedel.Play.Input;
import fr.SamuelVedel.Play.Me;
import fr.SamuelVedel.Play.Room;
import fr.SamuelVedel.Play.Enemy.Bomb;

public class BombSkill extends AddSkill {
	
	private Room room;
	
	private Me user;
	
	private int bombV = 15;
	private int bombDammage = 40;
	private final double angle = -Math.PI/4;
	
	public BombSkill(Me user, Room room) {
		this.user = user;
		this.room = room;
		type = BOMB_TYPE;
		setInput(new Input(Input.KEY_INPUT, 70, "F")); // F pour Bombe
	}
	
	@Override
	protected void action() {
		double alpha = angle;
		if (room.getMouseXInRoom() < user.x) {
		        alpha -= Math.PI/2;
		}
		room.enemies.add(new Bomb(room, Math.cos(alpha)*bombV, Math.sin(alpha)*bombV, bombDammage, user));
	}
	
}
