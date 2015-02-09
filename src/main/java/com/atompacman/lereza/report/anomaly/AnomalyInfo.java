package com.atompacman.lereza.report.anomaly;

import com.atompacman.lereza.api.Device;

public class AnomalyInfo {

	//===================================== INNER TYPES ==========================================\\

	public enum Impact {
		NONE, MINIMAL, MODERATE, CRITIC, FATAL;
	}
	
	public enum Recoverability {
		UNKNOWN, TRIVIAL, NORMAL, HARD, IMPOSSIBLE;
	}
	
	
	
	//======================================= FIELDS =============================================\\

	private final String name;
	private final String description;
	private final String consequences;
	
	private final Impact 			impact;
	private final Recoverability 	recoverability;
	
	private final Class<? extends Device> relatedDevice;


	
	//======================================= METHODS ============================================\\

	//---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

	public AnomalyInfo(String name, String description, String consequences, Impact impact,
			Recoverability recoverability, Class<? extends Device> relatedDevice) {
		
		this.name = name;
		this.description = description;
		this.consequences = consequences;
		
		this.impact = impact;
		this.recoverability = recoverability;
		
		this.relatedDevice = relatedDevice;
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getConsequences() {
		return consequences;
	}

	public Impact getImpact() {
		return impact;
	}

	public Recoverability getRecoverability() {
		return recoverability;
	}

	public Class<? extends Device> getRelatedDevice() {
		return relatedDevice;
	}
}
