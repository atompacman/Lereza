package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.Value;

public class TestValue {
	
	//------------ STATIC CONSTRUCTORS ------------\\
	
	@Test
	public void testFromTimeunit() {
		assertTrue(Value.THIRTYSECONTH == Value.fromTimeunit(2));
	}
	
	@Test  (expected=IllegalArgumentException.class)
	public void testFromTimeunitVerification() {
		assertTrue(Value.THIRTYSECONTH == Value.fromTimeunit(7));
	}

	
	//------------ TO TIMEUNIT ------------\\

	@Test
	public void testToTimeunit() {
		assertTrue(Value.QUARTER.toTimeunit() == 16);
	}
	
	
	//------------ SPLIT INTO VALUES ------------\\

	@Test
	public void testSplitIntoValues() {
		List<Value> values = Value.splitIntoValues(77);
		assertTrue(values.size() == 4);
		
		values = Value.splitIntoValues(128);
		assertTrue(values.size() == 2);
	}
}
