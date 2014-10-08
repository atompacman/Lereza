package com.atompacman.lereza.piece.container;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.Parameters;
import com.atompacman.lereza.common.solfege.RythmicSignature;
import com.atompacman.lereza.common.solfege.Value;
import com.atompacman.lereza.midi.MIDIFilePlayer;
import com.atompacman.lereza.midi.container.MIDINote;

public class Part {

	private List<OldBar> bars;
	private RythmicSignature rythmicSignature;


	//------------ CONSTRUCTORS ------------\\

	public Part(RythmicSignature rythmicSignature, int finalTimestamp) {
		this.bars = new ArrayList<OldBar>();
		int barLength = rythmicSignature.getBarTimeunitLength();
		int nbBars = (int) ((double) finalTimestamp / (double) barLength);
		for (int i = 0; i < nbBars; ++i) {
			bars.add(new OldBar(rythmicSignature, i));
		}
		this.rythmicSignature = rythmicSignature;
	}


	//------------ ADD ------------\\

	public void addNotes(Stack<MIDINote> notes, int timestamp) {
		int barLength = rythmicSignature.getBarTimeunitLength();
		int bar = (int) ((double) timestamp / (double) barLength);
 		int timeunit = timestamp % rythmicSignature.getBarTimeunitLength();

		for (MIDINote note : notes) {
			addNote(note, note.getLength(), timeunit, bar, false);
		}
		if (Parameters.NOTE_ADDING_AUDIO) {
			MIDIFilePlayer.getPlayer().playNoteStack(notes, 0, Parameters.NOTE_ADDING_AUDIO_TEMPO);
		}
		if (Parameters.NOTE_ADDING_VISUALISATION) {
			List<List<String>> barsToPrint = new ArrayList<List<String>>();
			barsToPrint.add(bars.get(bar).toStringList(false));

			if (bar > 0) {
				barsToPrint.add(0, bars.get(bar - 1).toStringList(true));
			}
			if (bar > 1) {
				barsToPrint.add(0, bars.get(bar - 2).toStringList(true));
			}
			printBars(barsToPrint);
		}
	}

	private void addNote(MIDINote note, int length, int timeunit, 
			int bar, boolean realNotePlaced) {
		List<Value> values = Value.splitIntoValues(length);

		for (Value value : values) {
			int endTimeunit = timeunit + value.toTimeunit();
			if (endTimeunit <= rythmicSignature.getBarTimeunitLength()) {
				bars.get(bar).addNote(note, value, timeunit, realNotePlaced);
			} else {
				int noteLength = rythmicSignature.getBarTimeunitLength() - timeunit;
				addNote(note, noteLength, timeunit, bar, realNotePlaced);

				noteLength = endTimeunit - rythmicSignature.getBarTimeunitLength();
				addNote(note, noteLength, 0, bar + 1, true);
			}
			timeunit += value.toTimeunit();
			if (timeunit >= rythmicSignature.getBarTimeunitLength()) {
				++bar;
				timeunit -= rythmicSignature.getBarTimeunitLength();
			}
			realNotePlaced = true;
		}
	}


	//------------ PRINT ------------\\

	public void printBars(List<List<String>> bars) {
		if (bars == null) {
			return;
		}

		int size = 0;

		for (List<String> bar : bars) {
			if (size == 0) {
				size = bar.size();
			} else {
				if (bar.size() != size) {
					if (Log.error() && Log.print("Cannot fusion bar string "
							+ "lists as they are not of the same size."));
					return;
				}
			}
		}
		for (int line = 0; line < bars.get(0).size(); ++line) {
			StringBuilder builder = new StringBuilder();
			builder.append(getHoriBarIfNeeded(line));
			for (List<String> bar : bars) {
				builder.append(bar.get(line));
				builder.append(getHoriBarIfNeeded(line));
			}
			if (Log.vital() && Log.print(builder.toString()));
		}
	}

	private char getHoriBarIfNeeded(int heigthOnPartition) {
		if ((heigthOnPartition > Parameters.BOTTOM_SECTION_HEIGHT &&
				heigthOnPartition < (Parameters.BOTTOM_SECTION_HEIGHT + 10) ||
				(heigthOnPartition > (Parameters.BOTTOM_SECTION_HEIGHT 
						+ 9 + Parameters.MIDDLE_SECTION_HEIGHT) &&
						heigthOnPartition < (Parameters.BOTTOM_SECTION_HEIGHT 
								+ 9 + Parameters.MIDDLE_SECTION_HEIGHT + 10)))) {
			return '|';
		}
		return ' ';
	}


	//------------ GETTERS ------------\\

	public OldBar getBarNo(int barNo) {
		return bars.get(barNo);
	}

	public RythmicSignature getRythmicSignature() {
		return rythmicSignature;
	}


	//------------ STATUS ------------\\

	public boolean isEmpty() {
		return bars.isEmpty();
	}

	public int getNbBars() {
		return bars.size();
	}
}
