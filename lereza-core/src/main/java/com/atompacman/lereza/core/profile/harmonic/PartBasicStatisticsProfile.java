package com.atompacman.lereza.core.profile.harmonic;

import java.util.HashMap;
import java.util.Map;

import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.tool.ProfileReportFormatter;

public class PartBasicStatisticsProfile extends Profile {
	
	protected int notesInPartCount;
	protected int unEmptyBarsInPartCount;
	protected int firstUnemptyBar;
	protected int lastUnemptyBar;
	protected int finalTimestamp;
	protected Map<Integer, Integer> notesInBarCount;
	protected Map<Integer, Integer> stackSizeCount;
	
	
	//------------ CONSTRUCTORS ------------\\

	public PartBasicStatisticsProfile() {
		this.firstUnemptyBar = -1;
		this.notesInBarCount = new HashMap<Integer, Integer>();
		this.stackSizeCount = new HashMap<Integer, Integer>();
	}
		
	
	//------------ GETTERS ------------\\

	public boolean isMonophonic() {
		return stackSizeCount.size() == 1 && stackSizeCount.get(1) != null;
	}
	
	
	//------------ PROFILE ------------\\

	public ProfileReportFormatter getReportFormatter() {
		return formatter;
	}
}
