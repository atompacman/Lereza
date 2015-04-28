package com.atompacman.lereza.report;

import com.atompacman.atomlog.Log;
import com.atompacman.atomlog.Log.Verbose;

public abstract class Observation {

	//====================================== CONSTANTS ===========================================\\

	private static final int STACK_TRACE_LVL_MODIFIER = 5;
	
	
	
	//======================================= FIELDS =============================================\\

	private final StackTraceElement[] 	stack;
	private final long					time;
	


	//=================================== ABSTRACT METHODS =======================================\\

	//--------------------------------------- FORMAT ---------------------------------------------\\

	public abstract Verbose verbose();

	public abstract String format();
	
	
	
	//======================================= METHODS ============================================\\

	//--------------------------------- PACKAGE CONSTRUCTORS -------------------------------------\\

	Observation() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		int stackLen = stackTrace.length - STACK_TRACE_LVL_MODIFIER;
		this.stack = new StackTraceElement[stackLen];
		
		System.arraycopy(stackTrace, STACK_TRACE_LVL_MODIFIER, stack, 0, stackLen);
		this.time = System.nanoTime();
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public StackTraceElement[] getStack() {
		return stack;
	}
	
	public long getTime() {
		return time;
	}
	
	
	//----------------------------------------- LOG ----------------------------------------------\\

	void log() {
		String formatted = format();
		if (formatted == null) {
			return;
		}
		switch (verbose()) {
		case ERROR: if (Log.error() && Log.print(3, formatted)); break;
		case EXTRA: if (Log.extra() && Log.print(3, formatted)); break;
		case INFOS: if (Log.infos() && Log.print(3, formatted)); break;
		case VITAL: if (Log.vital() && Log.print(3, formatted)); break;
		case WARNG: if (Log.warng() && Log.print(3, formatted)); break;
		default:
		}
	}
}