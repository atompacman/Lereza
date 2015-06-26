package com.atompacman.lereza.core.profile.harmonic;

import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.tool.ProfileReportFormatter;

public class ContrapunctalProfile extends Profile {

	//------------ CONSTRUCTORS ------------\\

	public ContrapunctalProfile() {
		super(PolyphonicProfile.class);
	}
	
	
	//------------ REPORT ------------\\

	public ProfileReportFormatter getReportFormatter() {
		return formatter;
	}
}
