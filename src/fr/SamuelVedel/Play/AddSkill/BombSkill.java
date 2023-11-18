package fr.SamuelVedel.Play.AddSkill;

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
		setCooldown(30*60); // 30 sec
		setKeyOfActivation(70, "F"); // F pour Bombe
	}
	
	@Override
	protected void action() {
		if (user.lookToTheRight()) {
			room.enemies.add(new Bomb(room, Math.cos(angle)*bombV, Math.sin(angle)*bombV, bombDammage, user));
		} else {
			room.enemies.add(new Bomb(room, Math.cos(angle-Math.PI/2)*bombV, Math.sin(angle-Math.PI/2)*bombV, bombDammage, user));
		}
	}
	
}
