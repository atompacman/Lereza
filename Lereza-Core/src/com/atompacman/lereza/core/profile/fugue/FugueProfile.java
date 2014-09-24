package com.atompacman.lereza.core.profile.fugue;

import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.harmonic.polyphonic.contrapuntal.ContrapunctalProfile;
import com.atompacman.lereza.core.profile.instrument.PianoProfile;
import com.atompacman.lereza.core.profile.rythmic.RythmicProfile;
import com.atompacman.lereza.core.profile.thematic.ThematicProfile;
import com.atompacman.lereza.core.profile.tool.ProfileReportFormatter;

public class FugueProfile extends Profile {

	private ContrapunctalProfile contrapuntalProfile;
	private RythmicProfile rythmicProfile;
	private ThematicProfile thematicProfile;
	private SubjectsProfile subjectsProfile;
	private FugueStructureProfile fugualStructureProfile;
	private PianoProfile pianoProfile;
	
	
	//------------ CONSTRUCTORS ------------\\

	public FugueProfile(int nbParts) {
		contrapuntalProfile = new ContrapunctalProfile(nbParts);
		rythmicProfile = new RythmicProfile();
		thematicProfile = new ThematicProfile();
		subjectsProfile = new SubjectsProfile();
		fugualStructureProfile = new FugueStructureProfile();
		pianoProfile = new PianoProfile();
	}
	
	
	//------------ GETTERS ------------\\

	public ContrapunctalProfile getContrapuntalProfile() {
		return contrapuntalProfile;
	}
	
	public RythmicProfile getRythmicProfile() {
		return rythmicProfile;
	}
	
	public ThematicProfile getThematicProfile() {
		return thematicProfile;
	}

	public SubjectsProfile getSubjectProfiles() {
		return subjectsProfile;
	}
	
	public FugueStructureProfile getFugualStructureProfile() {
		return fugualStructureProfile;
	}

	public PianoProfile getPianoProfile() {
		return pianoProfile;
	}

	
	//------------ REPORT ------------\\

	public ProfileReportFormatter getReportFormatter() {
		formatter.importChildProfile(contrapuntalProfile.getReportFormatter());
		return formatter;
	}
}
