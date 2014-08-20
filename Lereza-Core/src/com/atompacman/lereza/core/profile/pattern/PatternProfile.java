package com.atompacman.lereza.core.profile.pattern;

import java.util.List;

import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.motif.MotifProfile;
import com.atompacman.lereza.core.profile.tool.ProfileReportFormatter;

public class PatternProfile extends Profile {

	private List<MotifProfile> motifProfiles;

	
	public List<MotifProfile> getMotifProfiles() {
		return motifProfiles;
	}
	
	
	//////////////////////////////
	//   GET REPORT FORMATTER   //
	//////////////////////////////
	
	public ProfileReportFormatter getReportFormatter() {
		return formatter;
	}
}
