package com.atompacman.lereza.report.anomaly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atompacman.atomlog.Log;
import com.atompacman.lereza.report.Report;

public class AnomalyReport extends Report {

	//======================================= FIELDS =============================================\\

	private Map<Anomaly, List<AnomalyOccurrence>> occurrences;



	//======================================= METHODS ============================================\\

	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

	public AnomalyReport() {
		this.occurrences = new HashMap<>();
	}


	//--------------------------------------- RECORD ---------------------------------------------\\

	public void record(Anomaly anomaly) {
		record(anomaly, null, false);
	}

	public void record(Anomaly anomaly, String details) {
		record(anomaly, details, false);
	}

	public void record(Anomaly anomaly, String details, boolean onlyPrintDetails) {
		AnomalyOccurrence occ = new AnomalyOccurrence(details);
		getOccurrences(anomaly).add(occ);

		if (onlyPrintDetails) {
			if (Log.warng() && Log.print(details));
		} else {
			if (Log.warng() && Log.print(occ.format()));
		}
	}


	//--------------------------------------- GETTERS --------------------------------------------\\

	public <T extends Anomaly> List<AnomalyOccurrence> getOccurrences(T anomaly) {
		List<AnomalyOccurrence> occurrences = this.occurrences.get(anomaly);
		if (occurrences == null) {
			occurrences = new ArrayList<>();
		}
		return occurrences;
	}

	public int getNumOccurrencesOf(Anomaly anomaly) {
		return getOccurrences(anomaly).size();
	}
}
