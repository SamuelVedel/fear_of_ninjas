package fr.svedel.fod.play.addskill;

import fr.svedel.fod.play.Input;

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
	
	private int num = 0;
	private int maxNum = 0;
	/**
	 * Temps necessaire entre chaque utilisation
	 * (en 60ème de seconde)
	 */
	private int cooldown = 15*60; // 15 secondes
	/**
	 * Progretion du cooldonw tout simplement
	 */
	private double progressOfCooldown;
	
	private Input input;
	
	/**
	 * type et indice dans la liste des addSkills
	 */
	public static final int BOMB_TYPE = 0;
	public static final int TP_TYPE = 1;
	public int type;
	
	protected abstract void action();
	
	public int getNum() {
		return num;
	}
	
	public int getMaxNum() {
		return maxNum;
	}
	
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
	
	/**
	 * ajoute un skill
	 */
	public void addOne() {
		++maxNum;
		++num;
	}
	
	public void use() {
		if (num > 0) {
			action();
			--num;
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
		if (num < maxNum) {
			progressOfCooldown += delta;
			if (progressOfCooldown >= cooldown) {
				++num;
				if (num < maxNum) progressOfCooldown -= cooldown;
				else progressOfCooldown = 0;
			}
		}
	}
	
	/**
	 * @return Le pourcentage de progression du cooldown
	 */
	public double getProgression() {
		if (num == maxNum) return 100;
		if (cooldown != 0) return progressOfCooldown*100/cooldown;
		return 100;
	}
	
	public void resetUse() {
		num = maxNum;
		progressOfCooldown = 0;
	}
}
