package com.atompacman.lereza.profile.fugue;

import java.util.Arrays;

import com.atompacman.lereza.profile.tool.ProfileReportFormatter;
import com.atompacman.lereza.profile.Profile;

public class FugueStructureProfile extends Profile {

	//------------ CONSTRUCTORS ------------\\

	public FugueStructureProfile() {
		super(Arrays.asList(
				(Class<? extends Profile>) FugalEpisodeProfile.class,
				(Class<? extends Profile>) FugalExpositionProfile.class, 
				(Class<? extends Profile>) FugalBridgeProfile.class));
	}
	
	
	//------------ REPORT ------------\\
	
	public ProfileReportFormatter getReportFormatter() {
		importChildReportFormatters();
		return formatter;
	}
}
