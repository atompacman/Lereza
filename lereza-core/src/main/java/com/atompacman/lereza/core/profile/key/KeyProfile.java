package com.atompacman.lereza.core.profile.key;

import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.tool.ProfileReportFormatter;

public class KeyProfile extends Profile {
	
	//------------ CONSTRUCTORS ------------\\

	public KeyProfile() {
		super(PartKeyProfile.class);
	}

	//------------ FORMAT ------------\\

	public ProfileReportFormatter getReportFormatter() {
		return formatter;
	}
}