package com.atompacman.lereza.core.profile.harmonic.polyphonic.contrapuntal;

import com.atompacman.lereza.core.profile.harmonic.polyphonic.PolyphonicProfile;
import com.atompacman.lereza.core.profile.tool.ProfileReportFormatter;

public class ContrapunctalProfile extends PolyphonicProfile {

	public ContrapunctalProfile(int nbParts) {
		super(nbParts);
	}
	
	
	//////////////////////////////
	//   GET REPORT FORMATTER   //
	//////////////////////////////
	
	public ProfileReportFormatter getReportFormatter() {
		return super.getReportFormatter();
	}
}
