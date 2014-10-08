package com.atompacman.lereza.common.solfege;

import java.util.ArrayList;
import java.util.List;

public enum Value {
	
	SIXTYFORTH, THIRTYSECONTH, SIXTEENTH, EIGHTH, QUARTER, HALF, WHOLE;
	
	
	//------------ STATIC CONSTRUCTORS ------------\\

	public static Value fromTimeunit(int length) {
		double exponent = Math.log10(length) / Math.log10(2);
		if ((exponent - (int) exponent) != 0) {
			throw new IllegalArgumentException("Length \"" + length + 
					"\" cannot be converted to a simple value.");
		}
		return Value.values()[(int)exponent];
	}
	
	
	//------------ TO TIMEUNIT ------------\\

	public int toTimeunit() {
		return (int) Math.pow(2, this.ordinal());
	}
	
	
	//------------ SPLIT INTO VALUES ------------\\

	public static List<Value> splitIntoValues(int length) {
		List<Value> values = new ArrayList<Value>();
		
		while (length > WHOLE.toTimeunit()) {
			values.add(WHOLE);
			length -= WHOLE.toTimeunit();
		}
		double exponent = Math.log10(length) / Math.log10(2);
		
		while ((exponent - (int) exponent) != 0) {
			int biggestLength = (int) Math.pow(2, (int) exponent);
			values.add(fromTimeunit(biggestLength));
			length -= biggestLength;
			exponent = Math.log10(length) / Math.log10(2);
		}
		values.add(Value.values()[(int)exponent]);

		return values;
	}
}