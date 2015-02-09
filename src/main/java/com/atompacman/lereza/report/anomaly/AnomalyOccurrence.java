package com.atompacman.lereza.report.anomaly;

import java.util.Date;

public class AnomalyOccurrence {

	//======================================= FIELDS =============================================\\

	private final StackTraceElement[] 	stack;
	private final Date					timestamp;
	private final String				details;
	

	
	//======================================= METHODS ============================================\\

	//--------------------------------- PACKAGE CONSTRUCTORS -------------------------------------\\

	AnomalyOccurrence(String details) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		int stackLen = stackTrace.length;
		this.stack = new StackTraceElement[stackLen - 1];
		System.arraycopy(stackTrace, 1, stack, 0, stackLen - 1);
		this.timestamp = new Date();
		this.details = details;
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public StackTraceElement[] getStack() {
		return stack;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public String getDetails() {
		return details;
	}
	
	
	//---------------------------------------- STATE ---------------------------------------------\\

	public boolean hasDetails() {
		return details != null;
	}


	//--------------------------------------- FORMAT ---------------------------------------------\\

	public String format() {
		return "";
	}
}
