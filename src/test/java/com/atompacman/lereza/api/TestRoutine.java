package com.atompacman.lereza.api;

import java.util.ArrayList;
import java.util.List;

public class TestRoutine {

	private String routineName;
	private List<Integer> testCasesID;
	
	
	//------------ CONSTRUCTORS ------------\\

	public TestRoutine(String routineName) {
		this.routineName = routineName;
		this.testCasesID = new ArrayList<Integer>();
	}
	
	
	//------------ SETTERS ------------\\

	public void addTestCase(int caseID) {
		testCasesID.add(caseID);
	}
	
	
	//------------ GETTERS ------------\\

	public String getName() {
		return routineName;
	}
	
	public List<Integer> getTestCasesID() {
		return testCasesID;
	}
}
