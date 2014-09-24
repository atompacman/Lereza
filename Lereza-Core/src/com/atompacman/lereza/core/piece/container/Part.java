package com.atompacman.lereza.core.piece.container;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.common.solfege.RythmicSignature;
import com.atompacman.lereza.common.solfege.Value;
import com.atompacman.lereza.core.piece.Parameters;

import atompacman.leraza.midi.container.MidiNote;
import atompacman.leraza.midi.io.MidiFilePlayer;

public class Part {

	private List<Measure> measures;
	private RythmicSignature rythmicSignature;


	//------------ CONSTRUCTORS ------------\\

	public Part(RythmicSignature rythmicSignature, int finalTimestamp) {
		this.measures = new ArrayList<Measure>();
		int measureLength = rythmicSignature.getMeasureTimeunitLength();
		int nbMeasures = (int) ((double) finalTimestamp / (double) measureLength);
		for (int i = 0; i < nbMeasures; ++i) {
			measures.add(new Measure(rythmicSignature, i));
		}
		this.rythmicSignature = rythmicSignature;
	}


	//------------ ADD ------------\\

	public void addNotes(Stack<MidiNote> notes, int timestamp) {
		int measureLength = rythmicSignature.getMeasureTimeunitLength();
		int measure = (int) ((double) timestamp / (double) measureLength);
		int timeunit = timestamp % rythmicSignature.getMeasureTimeunitLength();

		for (MidiNote note : notes) {
			addNote(note, note.getLength(), timeunit, measure, false);
		}
		if (Parameters.NOTE_ADDING_AUDIO) {
			MidiFilePlayer.getPlayer().playNoteStack(notes, 0, Parameters.NOTE_ADDING_AUDIO_TEMPO);
		}
		if (Parameters.NOTE_ADDING_VISUALISATION) {
			List<List<String>> measuresToPrint = new ArrayList<List<String>>();
			measuresToPrint.add(measures.get(measure).toStringList(false));

			if (measure > 0) {
				measuresToPrint.add(0, measures.get(measure - 1).toStringList(true));
			}
			if (measure > 1) {
				measuresToPrint.add(0, measures.get(measure - 2).toStringList(true));
			}
			printMeasures(measuresToPrint);
		}
	}

	private void addNote(MidiNote note, int length, int timeunit, 
			int measure, boolean realNotePlaced) {
		List<Value> values = Value.splitIntoValues(length);

		for (Value value : values) {
			int endTimeunit = timeunit + value.toTimeunit();
			if (endTimeunit <= rythmicSignature.getMeasureTimeunitLength()) {
				measures.get(measure).addNote(note, value, timeunit, realNotePlaced);
			} else {
				int noteLength = rythmicSignature.getMeasureTimeunitLength() - timeunit;
				addNote(note, noteLength, timeunit, measure, realNotePlaced);

				noteLength = endTimeunit - rythmicSignature.getMeasureTimeunitLength();
				addNote(note, noteLength, 0, measure + 1, true);
			}
			timeunit += value.toTimeunit();
			if (timeunit >= rythmicSignature.getMeasureTimeunitLength()) {
				++measure;
				timeunit -= rythmicSignature.getMeasureTimeunitLength();
			}
			realNotePlaced = true;
		}
	}


	//------------ PRINT ------------\\

	public void printMeasures(List<List<String>> measures) {
		if (measures == null) {
			return;
		}

		int size = 0;

		for (List<String> measure : measures) {
			if (size == 0) {
				size = measure.size();
			} else {
				if (measure.size() != size) {
					if (Log.error() && Log.print("Cannot fusion measure string "
							+ "lists as they are not of the same size."));
					return;
				}
			}
		}
		for (int line = 0; line < measures.get(0).size(); ++line) {
			StringBuilder builder = new StringBuilder();
			builder.append(getHoriBarIfNeeded(line));
			for (List<String> measure : measures) {
				builder.append(measure.get(line));
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

	public Measure getMeasureNo(int measureNo) {
		return measures.get(measureNo);
	}

	public RythmicSignature getRythmicSignature() {
		return rythmicSignature;
	}


	//------------ STATUS ------------\\

	public boolean isEmpty() {
		return measures.isEmpty();
	}

	public int getNbMeasures() {
		return measures.size();
	}
}
