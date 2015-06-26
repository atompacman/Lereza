package com.atompacman.lereza.core.resources.context;

import java.util.EnumMap;
import java.util.Map;

public class Context {
	
	private Map<ContextElementType, String> values;
	
	
	// ------------ CONSTRUCTORS ------------\\

	public Context(String... theValues) {
		values = new EnumMap<ContextElementType, String>(ContextElementType.class);

		int expectedSize = ContextElementType.values().length;
		int actualSize = theValues.length;
		if (actualSize != expectedSize) {
			throw new IllegalArgumentException("A Context object must be made from an "
					+ "array of " + expectedSize + " values but had " + actualSize + ".");
		}
		
		for (int i = 0; i < actualSize; ++i) {
			ContextElementType type = ContextElementType.values()[i];
			String value = theValues[i];
			
			values.put(type, value);
		}
	}
	
	
	// ------------ GETTERS ------------\\

	public String valueOf(ContextElementType type) {
		return values.get(type);
	}
}
