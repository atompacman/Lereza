package com.atompacman.lereza.core.profile.harmonic;

import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.tool.ProfileReportFormatter;

public class PolyphonicProfile extends Profile {
	
	private HarmonicProfile harmonicProfile;
	
	
	//------------ GETTERS ------------\\

	public HarmonicProfile getHarmonicProfile() {
		return harmonicProfile;
	}
	
	
	//------------ REPORT ------------\\

	public ProfileReportFormatter getReportFormatter() {
		return formatter;
	}
}
