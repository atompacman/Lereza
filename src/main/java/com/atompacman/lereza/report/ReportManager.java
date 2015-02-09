package com.atompacman.lereza.report;

import java.util.HashMap;
import java.util.Map;

import com.atompacman.lereza.api.Device;

public class ReportManager implements Device {

	//====================================== SINGLETON ===========================================\\

	private static class InstanceHolder {
		private static final ReportManager instance = new ReportManager();
	}

	public static ReportManager getInstance() {
		return InstanceHolder.instance;
	}


	
	//======================================= FIELDS =============================================\\

	private Map<Class<? extends Device>, ActivityReports> actReports;
	
	
	
	//======================================= METHODS ============================================\\

	//---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

	private ReportManager() {
		actReports = new HashMap<>();
	}
	
		
	//--------------------------------------- GETTERS --------------------------------------------\\

	public ActivityReports getActivityReports(Class<? extends Device> clazz) {
		ActivityReports ar = actReports.get(clazz);
		if (ar == null) {
			ar = new ActivityReports(clazz);
			actReports.put(clazz, ar);
		}
		return ar;
	}
	
	public <T extends Report> T getReport(Class<? extends Device> devClazz, Class<T> reportClazz) {
		return getActivityReports(devClazz).getReport(reportClazz);
	}


	//-------------------------------------- SHUTDOWN --------------------------------------------\\

	public void shutdown() {
		
	}
}
