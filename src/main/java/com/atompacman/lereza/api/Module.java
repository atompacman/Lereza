package com.atompacman.lereza.api;

import com.atompacman.lereza.report.ActivityReports;
import com.atompacman.lereza.report.ReportManager;

public abstract class Module {
	
	//======================================= FIELDS =============================================\\

	protected ActivityReports reports;
	
	
	
	//======================================= METHODS ============================================\\
	
	//---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

	public Module() {
		if (!(getClass().equals(ReportManager.class))) {
			reports = Wizard.getModule(ReportManager.class).getActivityReports(getClass());
		}
	}
	

	//-------------------------------------- SHUTDOWN --------------------------------------------\\	
	
	public abstract void shutdown();
}
