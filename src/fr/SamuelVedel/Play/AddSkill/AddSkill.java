package fr.SamuelVedel.Play.AddSkill;


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
	
	private int keyOfActivation;
	private String keysName;
	
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
	
	public int getKeyOfActivation() {
		return keyOfActivation;
	}
	
	public String getKeysName() {
		return keysName;
	}
	
	public void setKeyOfActivation(int keyOfActivation, String keysName) {
		this.keyOfActivation = keyOfActivation;
		this.keysName = keysName;
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
		if (keyCode == keyOfActivation) {
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
