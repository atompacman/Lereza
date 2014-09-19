package com.atompacman.lereza.core.profile.phrase;

import java.util.List;

import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.pattern.PatternProfile;
import com.atompacman.lereza.core.profile.tool.ProfileReportFormatter;

public class PhraseProfile extends Profile {

	private List<PatternProfile> patternProfile;

	
	public List<PatternProfile> getMotifProfiles() {
		return patternProfile;
	}
	
	
	//////////////////////////////
	//   GET REPORT FORMATTER   //
	//////////////////////////////
	
	public ProfileReportFormatter getReportFormatter() {
		return formatter;
	}
}