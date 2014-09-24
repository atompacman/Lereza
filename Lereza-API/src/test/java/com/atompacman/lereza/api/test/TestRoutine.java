package com.atompacman.lereza.api.test;

import java.util.ArrayList;
import java.util.List;

public class TestRoutine {

	private String routineName;
	private List<TestCase> testCases;
	
	
	//------------ CONSTRUCTORS ------------\\

	public TestRoutine(String routineName) {
		this.routineName = routineName;
		this.testCases = new ArrayList<TestCase>();
	}
	
	
	//------------ SETTERS ------------\\

	public void addTestCase(TestCase testCase) {
		testCases.add(testCase);
	}
	
	
	//------------ GETTERS ------------\\

	public String getName() {
		return routineName;
	}
	
	public List<TestCase> getTestCases() {
		return testCases;
	}
}
