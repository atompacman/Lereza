package com.atompacman.lereza.report;

import com.atompacman.atomlog.Log;
import com.atompacman.atomlog.Log.Verbose;

public class LogEntry extends Observation {

	//====================================== CONSTANTS ===========================================\\

	private static final int NO_TITLE = -1;
	
	
	
	//======================================= FIELDS =============================================\\

	private final String msg;
	private final Verbose verbose;
	private final int titleSpacing;
	
	
	
	//======================================= METHODS ============================================\\

	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

	public LogEntry(String msg, Verbose verbose) {
		this(msg, verbose, NO_TITLE);
	}
	
	public LogEntry(String msg, Verbose verbose, int titleSpacing) {
		this.msg = msg;
		this.verbose = verbose;
		this.titleSpacing = titleSpacing;
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public String getMsg() {
		return msg;
	}


	//--------------------------------------- FORMAT ---------------------------------------------\\
	
	public Verbose verbose() {
		return verbose;
	}

	public String format() {
		return msg;
	}
	

	//----------------------------------------- LOG ----------------------------------------------\\
	
	public void log() {
		switch (verbose) {
		case ERROR: if (!Log.error()) return; break;
		case EXTRA: if (!Log.extra()) return; break;
		case INFOS: if (!Log.infos()) return; break;
		case VITAL: if (!Log.vital()) return; break;
		case WARNG: if (!Log.warng()) return; break;
		default: 					  return;
		}
		
		if (titleSpacing == NO_TITLE) {
			Log.print(3, msg);
		} else {
			Log.title(3, msg, titleSpacing);
		}
	}
}
