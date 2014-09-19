package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.Context;
import com.atompacman.lereza.common.solfege.Context.Genre;

public class TestContext {
	
	//------------ OVERALL ------------\\

	@Test
	public void testContext() {
		Context context = new Context(Genre.BAROQUE.HIGH_BAROQUE.FUGUE);
		
		assertTrue(context.toString().equals("BAROQUE/HIGH_BAROQUE/FUGUE"));
		String answer = "[BAROQUE/EARLY_BAROQUE/FUGUE, BAROQUE/MIDDLE_BAROQUE/FUGUE]";
		assertTrue(context.findSimilarForms().toString().equals(answer));
	}
	
	
	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testValueOf() {
		Context context = Context.valueOf("BAROQUE", "EARLY_BAROQUE", "FUGUE");
		assertTrue(context.toString().equals("BAROQUE/EARLY_BAROQUE/FUGUE"));
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testWrongValueOf() {
		Context.valueOf("346346", "6346436", "awfawfaw");
	}
}
