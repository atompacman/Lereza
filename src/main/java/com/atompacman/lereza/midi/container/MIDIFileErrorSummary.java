package com.atompacman.lereza.midi.container;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.Parameters;

public class MIDIFileErrorSummary {
	
	private int roundingOverThresholdCount;
	private int noteLengthZeroCount;
	private int noteRoundingTotalOffset;
	private int totalTimestamp;
	
	
	//------------ CONSTRUCTORS ------------\\

	public MIDIFileErrorSummary() {
		this.roundingOverThresholdCount = 0;
		this.noteLengthZeroCount = 0;
		this.noteRoundingTotalOffset = 0;
	}

	
	//------------ SETTERS ------------\\

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

	
	//------------ GETTERS ------------\\

	public int getRoundingOverThresholdCount() {
		return roundingOverThresholdCount;
	}

	public int getNoteLengthZeroCount() {
		return noteLengthZeroCount;
	}

	public int getNoteRoundingTotalOffset() {
		return noteRoundingTotalOffset;
	}
	
	
	//------------ PRINT ------------\\

	public void print() {
		if (roundingOverThresholdCount != 0) {
			if (Log.infos() && Log.print("· Excessive note rounding    : " + roundingOverThresholdCount));
		}
		if (noteLengthZeroCount != 0) {
			if (Log.infos() && Log.print("· Notes rounded length zero  : " + noteLengthZeroCount));
		}
		if (Math.abs(noteRoundingTotalOffset) <= Parameters.TOTAL_ROUND_OFFSET_LIMIT) {
			if (Log.infos() && Log.print("· Total rounding offset      : " + noteRoundingTotalOffset + "/" + totalTimestamp));
		} else {
			if (Log.warng() && Log.print("· TOTAL ROUNDING OFFSET      : " + noteRoundingTotalOffset + "/" + totalTimestamp + " (WARNING: THIS CAN CAUSE RYTHMN ERRORS IN THE FUTURE)"));
		}
	}
}
