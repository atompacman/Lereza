package com.atompacman.lereza.common.helper;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import com.atompacman.lereza.exception.TestFileDetectorException;

public class TestTestFileDetector {

	@Test
	public void singleFileExists() {
		String path = TestFileDetector.detectSingleFileForCurrentTest();
		assertTrue(path.equals("test\\common\\helper\\TestTestFileDetector\\singleFileExists.txt"));
	}
	
	@Test (expected=TestFileDetectorException.class)
	public void fileDoesntExists() {
		TestFileDetector.detectSingleFileForCurrentTest();
	}
	
	public void multipleFilesExists() {
		Map<String, String> testFilePath = TestFileDetector.detectAllFilesForCurrentTest();
		assertTrue(testFilePath.get("first").equals(
				"test\\common\\helper\\TestTestFileDetector\\multipleFilesExists-first.cpp"));
		assertTrue(testFilePath.get("second").equals(
				"test\\common\\helper\\TestTestFileDetector\\multipleFilesExists-second.txt"));
	}
	
	@Test (expected=TestFileDetectorException.class)
	public void detectIdenticalNames() {
		TestFileDetector.detectAllFilesForCurrentTest();
	}
	
	@Test (expected=TestFileDetectorException.class)
	public void multipleDelimiters() {
		TestFileDetector.detectAllFilesForCurrentTest();
	}
	
	@Test (expected=TestFileDetectorException.class)
	public void wrongDelimiter() {
		TestFileDetector.detectAllFilesForCurrentTest();
	}
	
	@Test (expected=TestFileDetectorException.class)
	public void moreThanOneFileExists() {
		TestFileDetector.detectSingleFileForCurrentTest();
	}
}
