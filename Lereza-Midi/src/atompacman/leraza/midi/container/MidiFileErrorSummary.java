package atompacman.leraza.midi.container;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.Parameters;

public class MidiFileErrorSummary {
	
	private int roundingOverThresholdCount;
	private int noteLengthZeroCount;
	private int noteRoundingTotalOffset;
	private int totalTimestamp;
	
	
	//////////////////////////////
	//       CONSTRUCTOR        //
	//////////////////////////////
	
	public MidiFileErrorSummary() {
		this.roundingOverThresholdCount = 0;
		this.noteLengthZeroCount = 0;
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

	public void adjustNoteRoundingTotalOffset(int offset) {
		this.noteRoundingTotalOffset += offset;
	}
	
	public void setTotalTimestamp(int totalTimestamp) {
		this.totalTimestamp = totalTimestamp;
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

	public int getNoteRoundingTotalOffset() {
		return noteRoundingTotalOffset;
	}
	
	
	//////////////////////////////
	//          PRINT           //
	//////////////////////////////
	
	public void print() {
		if (roundingOverThresholdCount != 0) {
			Log.infos("· Excessive note rounding    : " + roundingOverThresholdCount);
		}
		if (noteLengthZeroCount != 0) {
			Log.infos("· Notes rounded length zero  : " + noteLengthZeroCount);
		}
		if (Math.abs(noteRoundingTotalOffset) <= Parameters.TOTAL_ROUND_OFFSET_LIMIT) {
			Log.infos("· Total rounding offset      : " + noteRoundingTotalOffset + "/" + totalTimestamp);
		} else {
			Log.warng("· TOTAL ROUNDING OFFSET      : " + noteRoundingTotalOffset + "/" + totalTimestamp + " (WARNING: THIS CAN CAUSE RYTHMN ERRORS IN THE FUTURE)");
		}
	}
}
