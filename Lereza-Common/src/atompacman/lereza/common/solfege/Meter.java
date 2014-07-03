package atompacman.lereza.common.solfege;

public class Meter {
	private int numerator;
	private int denominator;
	
	
	public Meter(int numerator, int denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}
	
	public int getNumerator() {
		return numerator;
	}
	public int getDenominator() {
		return denominator;
	}
}
