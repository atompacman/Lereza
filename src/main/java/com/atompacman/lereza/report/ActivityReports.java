package com.atompacman.lereza.report;

import java.util.HashMap;
import java.util.Map;

import com.atompacman.lereza.api.Module;

public class ActivityReports {

	//======================================= FIELDS =============================================\\

	private final Map<Class<? extends Report>, Report> 	reports;
	private final Class<? extends Module> 				assocModule;



	//======================================= METHODS ============================================\\

	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

	ActivityReports(Class<? extends Module> assocModule) {
		this.reports = new HashMap<>();
		this.assocModule = assocModule;
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

	public Class<? extends Module> getAssociatedModule() {
		return assocModule;
	}
	
	
	//---------------------------------------- STATE ---------------------------------------------\\

	public boolean containsReport(Class<? extends Report> clazz) {
		return reports.containsKey(clazz);
	}
}
