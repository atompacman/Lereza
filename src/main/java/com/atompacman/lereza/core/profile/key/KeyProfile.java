package com.atompacman.lereza.core.profile.key;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.common.solfege.Key;
import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.modulation.ModulationProfile;
import com.atompacman.lereza.core.profile.tool.ProfileReportFormatter;

public class KeyProfile extends Profile {

	private List<ModulationProfile> modulationProfile;

	private Map<Integer, Map<Integer, Key>> pieceKeys;
	
	
	//------------ CONSTRUCTORS ------------\\

	public KeyProfile() {
		pieceKeys = new HashMap<Integer, Map<Integer, Key>>();
	}
	
	
	//------------ SETTERS ------------\\

	public void addKeyChange(int partNo, int timestamp, Key key) {
		if (pieceKeys.get(partNo) == null) {
			pieceKeys.put(partNo, new HashMap<Integer, Key>());
		}
		if (pieceKeys.get(partNo).get(timestamp) != null) {
			if (Log.warng() && Log.print("Replacing an existing key change."));
		}
		pieceKeys.get(partNo).put(timestamp, key);
	}
	
	
	//------------ GETTERS ------------\\

	public Map<Integer, Key> getKeyMapForPart(int partNo) {
		if (pieceKeys.get(partNo) == null) {
			throw new IllegalArgumentException("There is no key map for part " + partNo + ".");
		}
		return pieceKeys.get(partNo);
	}
	
	public List<ModulationProfile> getMotifProfiles() {
		return modulationProfile;
	}
	
	
	//------------ FORMAT ------------\\

	public ProfileReportFormatter getReportFormatter() {
		return formatter;
	}
}