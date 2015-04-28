package com.atompacman.lereza.profile.key;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.exception.PieceNavigatorException;
import com.atompacman.lereza.piece.container.Note;
import com.atompacman.lereza.piece.tool.PieceNavigator;
import com.atompacman.lereza.solfege.CircleOfFifths;
import com.atompacman.lereza.solfege.Tone;

public class PossibleKeysMap {

	public static class KeyInterval {
		
		public final int minKey;
		public final int maxKey;
		public final int timestamp;
		
		
		//------------ CONSTRUCTOR ------------\\

		public KeyInterval(int minKey, int maxKey, int timestamp) {
			this.minKey = minKey;
			this.maxKey = maxKey;
			this.timestamp = timestamp;
		}
	}
	
	
	private List<List<KeyInterval>> keyIntervals;
	
	
	//------------ CONSTRUCTOR ------------\\
	
	public PossibleKeysMap(PieceNavigator navig) {
		keyIntervals = new ArrayList<List<KeyInterval>>();
		
		while (!navig.endOfPiece()) {
			keyIntervals.add(new ArrayList<KeyInterval>());
			try {
				navig.goToFirstUnemptyBar();
			} catch(PieceNavigatorException e) {
				navig.goToNextPart();
				continue;
			}
			int partNo = navig.getPBT().part;
			navig.goToFirstNoteOfBar();
			while (!navig.endOfPart()) {
				int timestamp = navig.getCurrentTimestamp();
				
				for (Note note : navig.getCurrentStartingNoteStack()) {
					Tone tone = note.getPitch().getTone();
					int tonePos = CircleOfFifths.positionOf(tone);
					int minKeyPosInArray = Math.max(tonePos - 5, CircleOfFifths.minPosInCircle());
					int maxKeyPosInArray = Math.min(tonePos + 1, CircleOfFifths.maxPosInCircle());
					
					KeyInterval interval = new KeyInterval(minKeyPosInArray, 
							maxKeyPosInArray, timestamp);
					keyIntervals.get(partNo).add(interval);
				}
				navig.goToNextNote();
			}
			navig.goToNextPart();
		}
	}
	
	
	//------------ GET INTERVAL ------------\\

	public KeyInterval getInterval(int partNo, int toneIndex) {
		checkPartNoAndToneIndex(partNo, toneIndex);
		return keyIntervals.get(partNo).get(toneIndex);
	}
	
	
	//------------ TONE CAN BE OF KEY ------------\\

	public boolean toneCanBeOfKey(int partNo, int toneIndex, int key) {
		KeyInterval interval = getInterval(partNo, toneIndex);
		return key >= interval.minKey && key <= interval.maxKey;
	}


	//------------ OTHER ------------\\

	public int nbNotes(int partNo) {
		checkPartNo(partNo);
		return keyIntervals.get(partNo).size();
	}
	
	
	//------------ PRIVATE UTILS ------------\\

	private void checkPartNo(int partNo) {
		if (partNo < 0 || partNo >= keyIntervals.size()) {
			throw new IllegalArgumentException("Invalid part index \"" + partNo + "\".");
		}
	}
	
	private void checkPartNoAndToneIndex(int partNo, int toneIndex) {
		checkPartNo(partNo);
		if (toneIndex < 0 || toneIndex >= keyIntervals.get(partNo).size()) {
			throw new IllegalArgumentException("Invalid tone index \"" + toneIndex + "\".");
		}
	}
}
