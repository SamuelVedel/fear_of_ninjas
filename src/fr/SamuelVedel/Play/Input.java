package fr.SamuelVedel.Play;

/**
 * Sert à représenter les input (clavier, sourie)
 * <p>
 * Class créée le 15/12/2023 (youhou mon aniv des 20 ans
 * (bon en vrai ok il est 00:20 c'est plutôt le 16 mais shhhh))
 * 
 * @author Samuel Vedel
 *
 */
public class Input {
	public static int KEY_INPUT = 0;
	public static int MOUSE_INPUT = 1;
	
	private int type;
	private int code;
	private String name;
	
	public Input(int type, int code, String name) {
		this.type = type;
		this.code = code;
		this.name = name;
	}
	
	public boolean isKeyInput() {
		return type == KEY_INPUT;
	}
	
	public boolean isMouseInput() {
		return type == MOUSE_INPUT;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public void setKeyInput(int code, String name) {
		type = KEY_INPUT;
		this.code = code;
		this.name = name;
	}
	
	public void setMouseInput(int code, String name) {
		type = MOUSE_INPUT;
		this.code = code;
		this.name = name;
	}
}
