package com.atompacman.lereza.report;

import java.util.HashMap;
import java.util.Map;

import com.atompacman.lereza.api.Module;

public class ReportManager extends Module {

	//====================================== SINGLETON ===========================================\\

	private static class InstanceHolder {
		private static final ReportManager instance = new ReportManager();
	}

	public static ReportManager getInstance() {
		return InstanceHolder.instance;
	}


	
	//======================================= FIELDS =============================================\\

	private Map<Class<? extends Module>, Report> modReports;
	
	
	
	//======================================= METHODS ============================================\\

	//---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

	private ReportManager() {
		modReports = new HashMap<>();
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public Report getReport(Class<? extends Module> clazz) {
		Report ar = modReports.get(clazz);
		if (ar == null) {
			ar = new Report();
			modReports.put(clazz, ar);
		}
		return ar;
	}

	
	//-------------------------------------- SHUTDOWN --------------------------------------------\\

	public void shutdown() {
		
	}
}
