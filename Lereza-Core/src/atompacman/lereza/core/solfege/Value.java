package atompacman.lereza.core.solfege;

import java.util.ArrayList;
import java.util.List;

import atompacman.lereza.core.exception.BuilderException;

public enum Value {
	SIXTYFORTH, THIRTYSECONTH, SIXTEENTH, EIGHTH, QUARTER, HALF, WHOLE;
	
	public static Value lengthToValue(int length) {
		double exponent = Math.log10(length) / Math.log10(2);
		if ((exponent - (int) exponent) != 0) {
			try {
				throw new BuilderException("The length of a notation could not be converted to a precise value.");
			} catch (BuilderException e) {
				e.printStackTrace();
			}
		}
		return Value.values()[(int)exponent];
	}
	
	public static List<Value> splitIntoValues(int length) {
		List<Value> values = new ArrayList<Value>();
		
		double exponent = Math.log10(length) / Math.log10(2);
		
		while ((exponent - (int) exponent) != 0) {
			int biggestLength = (int) Math.pow(2, (int) exponent);
			values.add(lengthToValue(biggestLength));
			length -= biggestLength;
			exponent = Math.log10(length) / Math.log10(2);
		}
		values.add(Value.values()[(int)exponent]);

		return values;
	}
}