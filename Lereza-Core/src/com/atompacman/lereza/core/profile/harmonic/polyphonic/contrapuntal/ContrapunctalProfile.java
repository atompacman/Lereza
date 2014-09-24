package com.atompacman.lereza.core.profile.harmonic.polyphonic.contrapuntal;

import com.atompacman.lereza.core.profile.harmonic.polyphonic.PolyphonicProfile;
import com.atompacman.lereza.core.profile.tool.ProfileReportFormatter;

public class ContrapunctalProfile extends PolyphonicProfile {

	//------------ CONSTRUCTORS ------------\\

	public ContrapunctalProfile(int nbParts) {
		super(nbParts);
	}
	
	
	//------------ REPORT ------------\\

	public ProfileReportFormatter getReportFormatter() {
		return super.getReportFormatter();
	}
}
