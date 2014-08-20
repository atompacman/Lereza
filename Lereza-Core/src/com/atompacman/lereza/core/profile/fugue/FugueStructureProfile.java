package com.atompacman.lereza.core.profile.fugue;

import java.util.List;

import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.section.FugalBridgeProfile;
import com.atompacman.lereza.core.profile.section.FugalEpisodeProfile;
import com.atompacman.lereza.core.profile.section.FugalExpositionProfile;
import com.atompacman.lereza.core.profile.tool.ProfileReportFormatter;

public class FugueStructureProfile extends Profile {

	private List<FugalEpisodeProfile> fugalEpisodeProfile;
	private List<FugalExpositionProfile> fugalExpositionProfile;
	private List<FugalBridgeProfile> fugalBridgeProfile;
	
	
	public List<FugalEpisodeProfile> getFugalEpisodeProfile() {
		return fugalEpisodeProfile;
	}
	
	public List<FugalExpositionProfile> getFugalExpositionProfile() {
		return fugalExpositionProfile;
	}
	
	public List<FugalBridgeProfile> getFugalBridgeProfile() {
		return fugalBridgeProfile;
	}
	
	
	//////////////////////////////
	//   GET REPORT FORMATTER   //
	//////////////////////////////
	
	public ProfileReportFormatter getReportFormatter() {
		return formatter;
	}
}
