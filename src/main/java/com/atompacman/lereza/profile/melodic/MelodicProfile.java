package com.atompacman.lereza.profile.melodic;

import java.util.List;

import com.atompacman.lereza.profile.phrase.PhraseProfile;
import com.atompacman.lereza.profile.tool.ProfileReportFormatter;
import com.atompacman.lereza.profile.Profile;

public class MelodicProfile extends Profile {

	private List<PhraseProfile> phraseProfile;

	
	//------------ GETTERS ------------\\

	public List<PhraseProfile> getMotifProfiles() {
		return phraseProfile;
	}
	
	
	//------------ FORMAT ------------\\

	public ProfileReportFormatter getReportFormatter() {
		return formatter;
	}
}
