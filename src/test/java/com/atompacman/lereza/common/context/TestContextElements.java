package com.atompacman.lereza.common.context;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.helper.TestFileDetector;
import com.atompacman.lereza.exception.ContextElementsException;

public class TestContextElements {
	
	@Test
	public void completeTest() throws ContextElementsException {
		ContextElements.parseContextElements(TestFileDetector.detectSingleFileForCurrentTest());
		
		assertTrue(ContextElements.contains(ContextElementType.ERA, "Medieval"));
		assertTrue(ContextElements.contains(ContextElementType.CULTURE, "English"));
		assertTrue(!ContextElements.contains(ContextElementType.FORM, "Babouin"));
	}
}
