package com.atompacman.lereza.core.profile.harmonic;

import java.util.Arrays;

import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.key.KeyProfile;
import com.atompacman.lereza.core.profile.melodic.MelodicProfile;
import com.atompacman.lereza.core.profile.tool.ProfileReportFormatter;

public class HarmonicProfile extends Profile {

	//------------ CONSTRUCTORS ------------\\

	public HarmonicProfile() {
		super(Arrays.asList(
				BasicStatisticsProfile.class,
				PartHarmonicProfile.class, 
				MelodicProfile.class,
				KeyProfile.class));
	}


	//------------ REPORT ------------\\

	public ProfileReportFormatter getReportFormatter() {
		importChildReportFormatters();
		return formatter;
	}
}
