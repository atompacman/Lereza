package com.atompacman.lereza.core.profile.melodic;

import java.util.List;

import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.phrase.PhraseProfile;
import com.atompacman.lereza.core.profile.tool.ProfileReportFormatter;

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
