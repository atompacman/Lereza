package com.atompacman.lereza.profile.key;

import com.atompacman.lereza.profile.tool.ProfileReportFormatter;
import com.atompacman.lereza.profile.Profile;

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