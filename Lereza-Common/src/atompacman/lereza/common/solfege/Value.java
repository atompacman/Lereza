package atompacman.lereza.common.solfege;

import java.util.ArrayList;
import java.util.List;

public enum Value {
	
	SIXTYFORTH, THIRTYSECONTH, SIXTEENTH, EIGHTH, QUARTER, HALF, WHOLE;
	
	
	public int toTimeunit() {
		return (int) Math.pow(2, this.ordinal());
	}
	
	public static Value timeunitToValue(int length) {
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
			values.add(timeunitToValue(biggestLength));
			length -= biggestLength;
			exponent = Math.log10(length) / Math.log10(2);
		}
		values.add(Value.values()[(int)exponent]);

		return values;
	}
	
	public static List<Value> splitInTwoAt(Value value, int timeunit) {
		int valueTimeunitLength = value.toTimeunit();
		
		if (valueTimeunitLength <= timeunit || timeunit == 0) {
			return null;
		}
		List<Value> halves = new ArrayList<Value>();
		halves.add(timeunitToValue(timeunit));
		halves.add(timeunitToValue(valueTimeunitLength - timeunit));
		
		return halves;
	}
	
	public static List<Value> getValueList(Value min, Value max) {
		List<Value> valueList = new ArrayList<Value>();
		
		for (int i = min.ordinal(); i <= max.ordinal(); ++i) {
			valueList.add(Value.values()[i]);
		}
		return valueList;
	}
}