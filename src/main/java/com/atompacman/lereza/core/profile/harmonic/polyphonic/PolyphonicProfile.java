package com.atompacman.lereza.core.profile.harmonic.polyphonic;

import com.atompacman.lereza.core.profile.harmonic.HarmonicProfile;
import com.atompacman.lereza.core.profile.tool.ProfileReportFormatter;

public class PolyphonicProfile extends HarmonicProfile {

	public PolyphonicProfile(int nbParts) {
		super(nbParts);
	}
	
	
	//------------ REPORT ------------\\

	public ProfileReportFormatter getReportFormatter() {
		return super.getReportFormatter();
	}
}
