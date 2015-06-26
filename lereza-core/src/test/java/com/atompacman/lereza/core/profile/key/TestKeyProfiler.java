package com.atompacman.lereza.core.profile.key;

import org.junit.Test;

import com.atompacman.lereza.core.profile.key.KeyProfilerTestHelper;
import com.atompacman.lereza.resources.TestFileDetector;

public class TestKeyProfiler {

	//------------ PROFILE ------------\\

	@Test (expected = IllegalArgumentException.class)
	public void case1() {
		KeyProfilerTestHelper.profileTestPiece(TestFileDetector.detectSingleFileForCurrentTest());
	}
	
//	@Test (expected = IllegalArgumentException.class)
//	public void case2() {
//		KeyProfile profile = profileTestPiece(TestFileDetector.detectSingleFileForCurrentTest());
//	}
}
