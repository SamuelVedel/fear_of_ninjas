package fr.SamuelVedel.Play.AddSkill;

import fr.SamuelVedel.Play.Input;

/**
 * 
 * Correspond à des compétence supplémentaire acquise avec
 * des pouvoirs
 * <p>
 * Class créée le 14/05/2023
 * 
 * @author Samuel Vedel
 *
 */
public abstract class AddSkill {
	
	public boolean active = false;
	/**
	 * Temps necessaire entre chaque utilisation
	 * (en 60ème de seconde)
	 */
	private int cooldown;
	/**
	 * Progretion du cooldonw tout simplement
	 */
	private double progressOfCooldown;
	
	private Input input;
	
	/**
	 * type et indice dans la liste des addSkills
	 */
	public static final int BOMB_TYPE = 0;
	public int type;
	
	protected abstract void action();
	
	public int getCooldown() {
		return cooldown;
	}
	
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
		progressOfCooldown = cooldown;
	}
	
	public String getInputName() {
		return input.getName();
	}
	
	public void setInput(Input input) {
		this.input = input;
	}
	
	public boolean canBeUsed() {
		return progressOfCooldown >= cooldown && active;
	}
	
	public void use() {
		if (canBeUsed()) {
			progressOfCooldown = 0;
			action();
		}
	}
	
	public void keyReaction(int keyCode) {
		if (input.isKeyInput() && input.getCode() == keyCode) {
			use();
		}
	}
	
	public void mouseReaction(int button) {
		if (input.isMouseInput() && input.getCode() == button) {
			use();
		}
	}
	
	public void progress(double delta) {
		if (!canBeUsed()) {
			progressOfCooldown += delta;
		}
	}
	
	/**
	 * @return Le pourcentage de progression du cooldown
	 */
	public double getProgression() {
		if (cooldown != 0) {
			return progressOfCooldown*100/cooldown;
		} else {
			return 100;
		}
	}
	
	public void resetProgression() {
		progressOfCooldown = cooldown;
	}
}
