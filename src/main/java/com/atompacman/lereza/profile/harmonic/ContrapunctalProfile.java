package com.atompacman.lereza.profile.harmonic;

import com.atompacman.lereza.profile.tool.ProfileReportFormatter;
import com.atompacman.lereza.profile.Profile;

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
