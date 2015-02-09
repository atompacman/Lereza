package com.atompacman.lereza.solfege;

public class Meter {
	
	private final int numerator;
	private final int denominator;
	
		
	//------------ CONSTRUCTOR ------------\\

	public Meter(int numerator, int denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}
	
	
	//------------ GETTERS ------------\\

	public int numerator() {
		return numerator;
	}
	
	public int denominator() {
		return denominator;
	}
	
	
	//------------ STRING ------------\\

	public String toString() {
		return numerator + "/" + denominator;
	}
}
