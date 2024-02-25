package fr.svedel.vcomponent;

/**
 * Variable ajustable
 * 
 * @author Samuel Vedel
 *
 */
public class VAdjustInt {
	/** repr�sente la valeur initiale de la variable */
	private int value;
	/** valeur de la variable une fois ajust�e */
	private int currentValue;
	
	public VAdjustInt(int value) {
		this.value = value;
	}
	
	/**
	 * Retourne la valeur initiale de la variable
	 * 
	 * @return la valeur initiale
	 */
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * Retourne la valeur ajust� de la variable
	 * 
	 * @return la valeur ajust�
	 */
	public int getCurrentValue() {
		return currentValue;
	}
	
	public void setCurrentValue(int value) {
		currentValue = value;
	}
	
	public void adjust(int value, VAdjustInt reference) {
		if (reference == null) setCurrentValue(value);
		else {
			setCurrentValue(reference.getCurrentValue()*value/reference.getValue());
		}
	}
	
	/**
	 * Ajuste {@code currentValue} en fonction de la
	 * {@code currentValue} de {@code reference}.
	 * 
	 * Ou met {@code currentValue} � {@code value} si
	 * {@code reference == null}
	 * 
	 * @param reference
	 */
	public void adjust(VAdjustInt reference) {
		adjust(getValue(), reference);
	}
}
