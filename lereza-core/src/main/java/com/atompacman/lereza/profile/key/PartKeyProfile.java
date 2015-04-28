package com.atompacman.lereza.profile.key;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.atompacman.lereza.profile.Profile;
import com.atompacman.lereza.profile.tool.ProfileReportFormatter;
import com.atompacman.lereza.solfege.Accidental;
import com.atompacman.lereza.solfege.CircleOfFifths;
import com.atompacman.lereza.solfege.Key;
import com.atompacman.lereza.solfege.Tone;
import com.atompacman.lereza.solfege.quality.Quality;

public class PartKeyProfile extends Profile {
	
	private Map<Integer, Key> keys;
	private Map<Integer, Integer> nbPossibleNotesForKeys;
	
	private int nbNotesInPart;
	private int finalTimestamp;
	
	
	//------------ CONSTRUCTORS ------------\\

	public PartKeyProfile(int nbNotesInPart, int finalTimestamp) {
		super(ModulationProfile.class);
		this.keys = new HashMap<Integer, Key>();
		this.nbPossibleNotesForKeys = new HashMap<Integer, Integer>();
		this.nbNotesInPart = nbNotesInPart;
		this.finalTimestamp = finalTimestamp;
	}
	
	
	//------------ SETTERS ------------\\

	public void addKeyChange(int timeunit, int tonePosInCircle) {
		if (keys.containsKey(timeunit)) {
			throw new IllegalArgumentException("A key change was "
					+ "already set at timeunit \"" + timeunit + "\".");
		}
		keys.put(timeunit, toKey(tonePosInCircle, Quality.MAJOR));
	}
	
	public void incrementPossibleNotesForKey(int tonePosInCircle) {
		Integer previousValue = nbPossibleNotesForKeys.get(tonePosInCircle);
		if (previousValue == null) {
			previousValue = 0;
		}
		nbPossibleNotesForKeys.put(tonePosInCircle, previousValue + 1);
	}
	
	
	//------------ GETTERS ------------\\

	public Key getKeyAt(int timestamp) {
		if (timestamp < 0 || timestamp >= finalTimestamp) {
			throw new IllegalArgumentException("Cannot get key a \"" + timestamp + 
					"\": Final timestamp of part is \"" + finalTimestamp + "\".");
		}
		List<Integer> timestamps = new ArrayList<Integer>(keys.keySet());
		Collections.sort(timestamps);
		
		int previous = 0;
		for (Integer currTimestamps : timestamps) {
			if (currTimestamps > timestamp) {
				break;
			}
			previous = currTimestamps;
		}
		return keys.get(previous);
	}
	
	public int getNbPossiblesNotesForKey(Key key) {
		if (key.getQuality() == Quality.MINOR) {
			key = key.parallelKey();
		}
		int keyTonePosIncircle = CircleOfFifths.positionOf(key.getTone());
		Integer nbPossibleTones = nbPossibleNotesForKeys.get(keyTonePosIncircle);
		
		if (nbPossibleTones == null) {
			return 0;
		}
		
		return nbPossibleTones;
	}
	
	public Key getSharpestKeyContainingANote() {
		return getExtremumKeyContainingANote(Accidental.SHARP);
	}
	
	public Key getFlatestKeyContainingANote() {
		return getExtremumKeyContainingANote(Accidental.FLAT);
	}
	
	private Key getExtremumKeyContainingANote(Accidental accidental) {
		int sharpestKeyPosInCircle = Integer.MIN_VALUE;
		int flatestKeyPosInCircle = Integer.MAX_VALUE;
		
		for (Entry<Integer, Integer> entry : nbPossibleNotesForKeys.entrySet()) {
			if (entry.getValue() == 0) {
				continue;
			}
			int keyPosInCircle = entry.getKey();
			
			if (keyPosInCircle > sharpestKeyPosInCircle) {
				sharpestKeyPosInCircle = keyPosInCircle;
			}
			if (keyPosInCircle < flatestKeyPosInCircle) {
				flatestKeyPosInCircle = keyPosInCircle;
			}
		}
		
		Tone extremumTone = null;
		
		if (accidental == Accidental.SHARP) {
			extremumTone = CircleOfFifths.toneAtPosition(sharpestKeyPosInCircle);
		} else {
			extremumTone = CircleOfFifths.toneAtPosition(flatestKeyPosInCircle);
		}
		
		return Key.valueOf(extremumTone, Quality.MAJOR);
	}
	
	public List<Key> getPerfectKeys() {
		List<Key> perfectKeys = new ArrayList<Key>();

		for (Entry<Integer, Integer> entry : nbPossibleNotesForKeys.entrySet()) {
			if (entry.getValue() == nbNotesInPart) {
				perfectKeys.add(toKey(entry.getKey(), Quality.MAJOR));
			}
		}
		return perfectKeys;
	}
	
	
	//------------ FORMAT ------------\\

	public ProfileReportFormatter getReportFormatter() {
		return formatter;
	}

	
	//------------ PRIVATE UTILS ------------\\

	protected static Key toKey(int tonePosInCircle, Quality quality) {
		Tone tone = CircleOfFifths.toneAtPosition(tonePosInCircle);
		Key key = Key.valueOf(tone, Quality.MAJOR);
		
		if (quality == Quality.MINOR) {
			key = key.parallelKey();
		}
		return key;
	}
}