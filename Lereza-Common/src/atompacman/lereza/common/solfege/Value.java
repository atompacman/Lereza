package atompacman.lereza.common.solfege;

import java.util.ArrayList;
import java.util.List;

public enum Value {
	SIXTYFORTH, THIRTYSECONTH, SIXTEENTH, EIGHTH, QUARTER, HALF, WHOLE;
	
	public static Value lengthToValue(int length) {
		double exponent = Math.log10(length) / Math.log10(2);
		if ((exponent - (int) exponent) != 0) {
			return null;
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