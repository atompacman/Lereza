package com.atompacman.lereza.profile.phrase;

import java.util.List;

import com.atompacman.lereza.profile.pattern.PatternProfile;
import com.atompacman.lereza.profile.tool.ProfileReportFormatter;
import com.atompacman.lereza.profile.Profile;

public class PhraseProfile extends Profile {

	private List<PatternProfile> patternProfile;

	
	//------------ GETTERS ------------\\

	public List<PatternProfile> getMotifProfiles() {
		return patternProfile;
	}
	
	
	//------------ FORMAT ------------\\

	public ProfileReportFormatter getReportFormatter() {
		return formatter;
	}
}
