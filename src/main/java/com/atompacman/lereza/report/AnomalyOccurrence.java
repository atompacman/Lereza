package com.atompacman.lereza.report;

import com.atompacman.atomlog.Log.Verbose;
import com.atompacman.lereza.report.Anomaly.Info;

public class AnomalyOccurrence extends Observation {

	//======================================= FIELDS =============================================\\

	private final Info 		anomalyInfo;
	private final String	details;
	

	
	//======================================= METHODS ============================================\\

	//--------------------------------- PACKAGE CONSTRUCTORS -------------------------------------\\

	AnomalyOccurrence(Info anomalyInfo) {
		this(anomalyInfo, null);
	}
	
	AnomalyOccurrence(Info anomalyInfo, String details) {
		this.anomalyInfo = anomalyInfo;
		this.details = details;
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public Info getAnomalyInfo() {
		return anomalyInfo;
	}
	
	public String getDetails() {
		return details;
	}
	
	
	//---------------------------------------- STATE ---------------------------------------------\\

	public boolean hasDetails() {
		return details != null;
	}


	//--------------------------------------- FORMAT ---------------------------------------------\\

	public Verbose verbose() {
		switch (anomalyInfo.getImpact()) {
		case CRITIC: 	return Verbose.WARNG;
		case FATAL:  	return Verbose.ERROR;
		case MINIMAL: 	return Verbose.INFOS;
		case MODERATE: 	return Verbose.WARNG;
		case NONE: 		return Verbose.EXTRA;
		}
		return null;
	}
	
	public String format() {
		return details;
	}
}
