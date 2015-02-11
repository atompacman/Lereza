package com.atompacman.lereza.report.anomaly;

import java.util.Date;

public class AnomalyOccurrence {

	//====================================== CONSTANTS ===========================================\\

	private static final int STACK_TRACE_LVL_MODIFIER = 4;
	
	
	
	//======================================= FIELDS =============================================\\

	private final StackTraceElement[] 	stack;
	private final Date					timestamp;
	private final String				details;
	

	
	//======================================= METHODS ============================================\\

	//--------------------------------- PACKAGE CONSTRUCTORS -------------------------------------\\

	AnomalyOccurrence(String details) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		int stackLen = stackTrace.length - STACK_TRACE_LVL_MODIFIER;
		this.stack = new StackTraceElement[stackLen];
		System.arraycopy(stackTrace, STACK_TRACE_LVL_MODIFIER, stack, 0, stackLen);
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
