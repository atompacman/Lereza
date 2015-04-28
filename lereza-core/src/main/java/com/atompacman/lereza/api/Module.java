package com.atompacman.lereza.api;

import com.atompacman.lereza.report.Report;
import com.atompacman.lereza.report.ReportManager;

public abstract class Module {
	
	//======================================= FIELDS =============================================\\

	protected Report report;
	
	
	
	//======================================= METHODS ============================================\\
	
	//---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

	public Module() {
		if (!(getClass().equals(ReportManager.class))) {
			report = Wizard.getModule(ReportManager.class).getReport(getClass());
		}
	}
	

	//-------------------------------------- SHUTDOWN --------------------------------------------\\	
	
	public abstract void shutdown();
}
