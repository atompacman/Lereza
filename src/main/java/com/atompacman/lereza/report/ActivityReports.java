package com.atompacman.lereza.report;

import java.util.HashMap;
import java.util.Map;

import com.atompacman.lereza.api.Device;

public class ActivityReports {

	//======================================= FIELDS =============================================\\

	private final Map<Class<? extends Report>, Report> 	reports;
	private final Class<? extends Device> 				coveredDevice;



	//======================================= METHODS ============================================\\

	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

	ActivityReports(Class<? extends Device> relatedDevice) {
		this.reports = new HashMap<>();
		this.coveredDevice = relatedDevice;
	}


	//--------------------------------------- GETTERS --------------------------------------------\\

	@SuppressWarnings("unchecked")
	public <T extends Report> T getReport(Class<T> clazz) {
		T report = (T) reports.get(clazz);
		if (report == null) {
			try {
				report = clazz.newInstance();
				reports.put(clazz, report);
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return report;
	}

	public Class<? extends Device> getRelatedDevice() {
		return coveredDevice;
	}
	
	
	//---------------------------------------- STATE ---------------------------------------------\\

	public boolean containsReport(Class<? extends Report> clazz) {
		return reports.containsKey(clazz);
	}
}
