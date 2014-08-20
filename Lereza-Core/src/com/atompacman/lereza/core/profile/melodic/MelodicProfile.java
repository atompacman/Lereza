package com.atompacman.lereza.core.profile.melodic;

import java.util.List;

import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.phrase.PhraseProfile;
import com.atompacman.lereza.core.profile.tool.ProfileReportFormatter;

public class MelodicProfile extends Profile {

	private List<PhraseProfile> phraseProfile;

	
	public List<PhraseProfile> getMotifProfiles() {
		return phraseProfile;
	}
	
	
	//////////////////////////////
	//   GET REPORT FORMATTER   //
	//////////////////////////////
	
	public ProfileReportFormatter getReportFormatter() {
		return formatter;
	}
}
