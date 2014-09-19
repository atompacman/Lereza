package com.atompacman.lereza.api.test.simple;

import java.io.File;

import org.junit.Test;

import com.atompacman.lereza.api.test.SystemTester;

public class TestMonophonicSequences {

	public static final String MONO_SEQ_TESTS_ROOT = 
			SystemTester.SYSTEM_TESTS_ROOT + "/simple/MonophonicSequences";


	@Test
	public void testMonophonicSequences() {
		File testFolder = new File(MONO_SEQ_TESTS_ROOT);
		for (File file : testFolder.listFiles()) {
			
		}
	}
}
