package com.atompacman.lereza.profile.key;

import java.util.List;

import com.atompacman.atomlog.Log;
import com.atompacman.lereza.profile.Profile;
import com.atompacman.lereza.profile.Profiler;
import com.atompacman.lereza.profile.key.PossibleKeysMap.KeyInterval;
import com.atompacman.lereza.solfege.CircleOfFifths;
import com.atompacman.lereza.solfege.Key;

public class KeyProfiler extends Profiler {

	private KeyProfile profile;
	private PossibleKeysMap possibleKeysMap;
	private PartKeyProfile currPartKeyProfile;
	

	//------------ PREPARE ------------\\

	protected void prepare() {
		profile = new KeyProfile();
		possibleKeysMap = new PossibleKeysMap(navig);
	}


	//------------ VERIFY PROFILABILITY ------------\\

	protected boolean verifyProfilability() {
		return true;
	}


	//------------ PROFILE ------------\\

	protected Profile profile() {
		for (int i = 0; i < piece.numParts(); ++i) {
			int finalTimestamp = piece.getPartNo(i).finalTU();
			currPartKeyProfile = new PartKeyProfile(possibleKeysMap.nbNotes(i), finalTimestamp);
			basicAnalysis(i);
			findKeyPath(i);
			profile.addSubProfile(currPartKeyProfile);
		}
		return profile;
	}

	private void basicAnalysis(int partNo) {
		for (int i = 0; i < possibleKeysMap.nbNotes(partNo); ++i) {
			KeyInterval interval = possibleKeysMap.getInterval(partNo, i);
			for (int key = interval.minKey; key <= interval.maxKey; ++key) {
				currPartKeyProfile.incrementPossibleNotesForKey(key);
			}
		}
	}
	
	private void findKeyPath(int partNo) {
		if(Log.infos() && Log.title("Detecting keys for part " + (partNo + 1), 1));

		if (singleKeyForAllPart(partNo)) {
			return;
		}
	}
	
	private boolean singleKeyForAllPart(int partNo) {
		List<Key> perfectKeys = currPartKeyProfile.getPerfectKeys();
		
		if (perfectKeys.isEmpty()) {
			return false;
		}
		
		int bestScore = 0;
		int bestKey = 0;
		
		for (Key key : perfectKeys) {
			int tonePosInCircle = CircleOfFifths.positionOf(key.getTone());
			int score = computeKeyCorrelation(partNo, tonePosInCircle);
			if (score > bestScore) {
				bestScore = score;
				bestKey = tonePosInCircle;
			}
		}
		
		currPartKeyProfile.addKeyChange(0, bestKey);
		
		return true;
	}
	
	private int computeKeyCorrelation(int partNo, int key) {
		return computeKeyCorrelation(partNo, key, 0, possibleKeysMap.nbNotes(partNo));
	}
	
	private int computeKeyCorrelation(int partNo, int key, int startIndex, int endIndex) {
		int score = 0;
		
		for (int i = startIndex; i < endIndex; ++i) {
			KeyInterval interval = possibleKeysMap.getInterval(partNo, i);
			
			switch (interval.maxKey - key) {
			case 0: score += 3; break;
			case 1: score += 5; break;
			case 2: score += 2; break;
			}
		}
		
		return score;
	}
}
