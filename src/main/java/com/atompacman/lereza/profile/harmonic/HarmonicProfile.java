package com.atompacman.lereza.profile.harmonic;

import java.util.Arrays;

import com.atompacman.lereza.profile.key.KeyProfile;
import com.atompacman.lereza.profile.melodic.MelodicProfile;
import com.atompacman.lereza.profile.tool.ProfileReportFormatter;
import com.atompacman.lereza.profile.Profile;

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
