package com.atompacman.lereza.profile.harmonic;

import com.atompacman.lereza.profile.tool.ProfileReportFormatter;
import com.atompacman.lereza.profile.Profile;

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
