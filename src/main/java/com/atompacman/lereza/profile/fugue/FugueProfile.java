package com.atompacman.lereza.profile.fugue;

import java.util.Arrays;

import com.atompacman.lereza.profile.harmonic.ContrapunctalProfile;
import com.atompacman.lereza.profile.tool.ProfileReportFormatter;
import com.atompacman.lereza.profile.Profile;

public class FugueProfile extends Profile {
	
	//------------ CONSTRUCTORS ------------\\

	public FugueProfile() {
		super(Arrays.asList(
				ContrapunctalProfile.class,
				SubjectsProfile.class, 
				FugueStructureProfile.class));
	}
	
	
	//------------ REPORT ------------\\

	public ProfileReportFormatter getReportFormatter() {
		importChildReportFormatters();
		return formatter;
	}
}