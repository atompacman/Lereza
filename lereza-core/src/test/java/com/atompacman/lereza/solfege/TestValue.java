package com.atompacman.lereza.solfege;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.solfege.Value;

public class TestValue {
	
	//------------ STATIC CONSTRUCTORS ------------\\
	
	@Test
	public void testFromTimeunit() {
		assertEquals(Value.THIRTYSECONTH, Value.fromTimeunit(2));
	}
	
	@Test  (expected=IllegalArgumentException.class)
	public void testFromTimeunitVerification() {
		assertEquals(Value.THIRTYSECONTH, Value.fromTimeunit(7));
	}

	
	//------------ TO TIMEUNIT ------------\\

	@Test
	public void testToTimeunit() {
		assertEquals(Value.QUARTER.toTimeunit(), 16);
	}
	
	
	//------------ SPLIT INTO VALUES ------------\\

	@Test
	public void testSplitIntoValues() {
		List<Value> values = Value.splitIntoValues(77);
		assertEquals(values.size(), 4);
		
		values = Value.splitIntoValues(128);
		assertEquals(values.size(), 2);
	}
}
