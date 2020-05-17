package com.fhermosilla.kalah.game.enums;


/**
 *
 * @author felipehermosilla
 * Player turn possible values
 */
public enum TurnValueEnum {
	FIRST_PLAYER("F"), SECOND_PLAYER("S");

	private String turnValue;

	TurnValueEnum(String turnValue) {
		this.turnValue = turnValue;
	}

	@Override
	public String toString() {
		return turnValue;
	}
}
