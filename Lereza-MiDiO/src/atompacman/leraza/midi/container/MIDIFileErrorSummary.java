package atompacman.leraza.midi.container;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.Parameters;

public class MIDIFileErrorSummary {
	
	private int roundingOverThresholdCount;
	private int noteLengthZeroCount;
	private int rawNoteLengthZeroCount;
	private int noteRoundingTotalOffset;
	
	
	//////////////////////////////
	//       CONSTRUCTOR        //
	//////////////////////////////
	
	public MIDIFileErrorSummary() {
		this.roundingOverThresholdCount = 0;
		this.noteLengthZeroCount = 0;
		this.rawNoteLengthZeroCount = 0;
		this.noteRoundingTotalOffset = 0;
	}

	
	//////////////////////////////
	//         SETTERS          //
	//////////////////////////////
	
	public void incrementRoundingOverThresholdCount() {
		++this.roundingOverThresholdCount;
	}

	public void incrementNoteLengthZeroCount() {
		++this.noteLengthZeroCount;
	}

	public void incrementRawNoteLengthZeroCount() {
		++this.rawNoteLengthZeroCount;
	}

	public void adjustNoteRoundingTotalOffset(int offset) {
		this.noteRoundingTotalOffset += offset;
	}

	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////
	
	public int getRoundingOverThresholdCount() {
		return roundingOverThresholdCount;
	}

	public int getNoteLengthZeroCount() {
		return noteLengthZeroCount;
	}

	public int getRawNoteLengthZeroCount() {
		return rawNoteLengthZeroCount;
	}

	public int getNoteRoundingTotalOffset() {
		return noteRoundingTotalOffset;
	}
	
	public boolean isAHomophonicPiece() {
		return (rawNoteLengthZeroCount > Parameters.MAX_NB_CHORDS_WITHOUT_HOMOPHONIC);
	}

	
	//////////////////////////////
	//          PRINT           //
	//////////////////////////////
	
	public void print() {
		Log.infos("· Musical texture prediction : " + (isAHomophonicPiece() ? "HOMOPHONIC" : "POLYPHONIC"));
		Log.infos("· Excessive note rounding    : " + roundingOverThresholdCount);
		Log.infos("· Notes rounded length zero  : " + noteLengthZeroCount);
		if (rawNoteLengthZeroCount == 0) {
			Log.infos("· NOTES OF RAW LENGTH ZERO   : 0");
		} else {
			Log.infos("· NOTES OF RAW LENGTH ZERO   : " + rawNoteLengthZeroCount + " (WARNING: CHORDS ARE NOT NORMAL IN POLYPHONY)");
		}
		if (noteRoundingTotalOffset == 0) {
			Log.infos("· TOTAL ROUNDING OFFSET      : 0");
		} else {
			Log.warng("· TOTAL ROUNDING OFFSET      : " + noteRoundingTotalOffset + " (WARNING: THIS CAN CAUSE RYTHMN ERRORS IN THE FUTURE)");
		}
	}
}
