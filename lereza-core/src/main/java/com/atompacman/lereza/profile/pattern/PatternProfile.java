package com.atompacman.lereza.profile.pattern;

import java.util.List;

import com.atompacman.lereza.profile.motif.MotifProfile;
import com.atompacman.lereza.profile.tool.ProfileReportFormatter;
import com.atompacman.lereza.profile.Profile;

public class PatternProfile extends Profile {

	private List<MotifProfile> motifProfiles;

	
	//------------ GETTERS ------------\\

	public List<MotifProfile> getMotifProfiles() {
		return motifProfiles;
	}
	
	
	//------------ FORMAT ------------\\

	public ProfileReportFormatter getReportFormatter() {
		return formatter;
	}
}
