package com.atompacman.lereza.common.solfege;

public class Meter {
	
	private int numerator;
	private int denominator;
	
		
	//------------ CONSTRUCTORS ------------\\

	public Meter(int numerator, int denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}
	
	
	//------------ GETTERS ------------\\

	public int getNumerator() {
		return numerator;
	}
	
	public int getDenominator() {
		return denominator;
	}
	
	
	//------------ STRING ------------\\

	public String toString() {
		return numerator + "/" + denominator;
	}
}
