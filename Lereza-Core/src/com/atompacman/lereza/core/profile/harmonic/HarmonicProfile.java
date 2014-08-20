package com.atompacman.lereza.core.profile.harmonic;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.harmonic.polyphonic.PolyphonicProfile;
import com.atompacman.lereza.core.profile.key.KeyProfile;
import com.atompacman.lereza.core.profile.melodic.MelodicProfile;
import com.atompacman.lereza.core.profile.tool.ProfileReportFormatter;

public abstract class HarmonicProfile extends Profile {

	private BasicStatisticsProfile basicStatisticsProfile;
	private List<PartHarmonicProfile> partProfiles;
	private MelodicProfile melodicProfile;
	private KeyProfile keyProfile;
	
	
	//////////////////////////////
	//       CONSTRUCTOR        //
	//////////////////////////////
	
	public HarmonicProfile (int nbParts) {
		this.basicStatisticsProfile = new BasicStatisticsProfile(nbParts);
		this.partProfiles = new ArrayList<PartHarmonicProfile>();
		for (int i = 0; i < nbParts; ++i) {
			this.partProfiles.add(new PartHarmonicProfile());
		}
		basicStatisticsProfile.isPolyphonic = this instanceof PolyphonicProfile;
	}

	
	//////////////////////////////
	//       GET PROFILE        //
	//////////////////////////////
	
	public BasicStatisticsProfile getBasicStatisticsProfile() {
		return basicStatisticsProfile;
	}
	
	public PartHarmonicProfile getPartProfile(int partNo) {
		return partProfiles.get(partNo);
	}
	
	public MelodicProfile getMelodicProfile() {
		return melodicProfile;
	}
	
	public KeyProfile getKeyProfile() {
		return keyProfile;
	}
	
	
	//////////////////////////////
	//   GET REPORT FORMATTER   //
	//////////////////////////////
	
	public ProfileReportFormatter getReportFormatter() {
		formatter.importChildProfile(basicStatisticsProfile.getReportFormatter());
		return formatter;
	}
}
