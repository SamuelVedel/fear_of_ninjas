package fr.SamuelVedel.Play.Debuff;

import fr.SamuelVedel.Play.Entity;
import fr.SamuelVedel.Play.Room;

public abstract class Debuff {
	
	protected Room room;
	
	/** entité qui à donner le débuff */
	protected Entity giver;
	/** entité qui subit le débuff */
	protected Entity victim;
	
	protected double t = 0;
	protected int tMax = 0;
	/**
	 * type et indice dans la liste des debuffs
	 */
	public static int POISON_TYPE = 0;
	public static int PETRIFICATION_TYPE = 1;
	public int type;
	
	public Debuff(Entity victim, Room room) {
		this.room = room;
		this.victim = victim;
	}
	
//	public void preAction() {
//		if (t < tMax) {
//			preEffect();
//		}
//	}
	
	public void action(double delta) {
		if (t < tMax) {
			effect(delta);
			t += delta;
		}
	}
	
	public void reset(Entity giver) {
		t = 0;
		this.giver = giver;
	}
	
//	protected abstract void preEffect();
	protected abstract void effect(double delta);
}
