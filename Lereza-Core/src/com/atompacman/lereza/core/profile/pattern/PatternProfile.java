package com.atompacman.lereza.core.profile.pattern;

import java.util.List;

import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.motif.MotifProfile;
import com.atompacman.lereza.core.profile.tool.ProfileReportFormatter;

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
