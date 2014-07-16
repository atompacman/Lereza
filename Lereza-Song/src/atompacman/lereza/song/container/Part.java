package atompacman.lereza.song.container;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.MidiFileManager;
import atompacman.leraza.midi.container.MidiNote;
import atompacman.lereza.common.solfege.RythmicSignature;
import atompacman.lereza.common.solfege.Value;
import atompacman.lereza.song.Parameters;
import atompacman.lereza.song.exception.BuilderException;

public class Part {

	private List<Measure> measures;
	private RythmicSignature rythmicSignature;
	

	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////

	public Part(RythmicSignature rythmicSignature, int finalTimestamp) {
		this.measures = new ArrayList<Measure>();
		int nbMeasures = (int) ((double) finalTimestamp / ((double) rythmicSignature.getMeter().getNumerator() * (double) rythmicSignature.getQuarterNoteTimeunitLength()));
		for (int i = 0; i < nbMeasures; ++i) {
			measures.add(new Measure(rythmicSignature, i));
		}
		this.rythmicSignature = rythmicSignature;
	}


	//////////////////////////////
	//         ADD NOTE         //
	//////////////////////////////

	public void addNotes(Stack<MidiNote> notes, int timestamp) {
		int originalMeasure = (int) ((double) timestamp / (double) rythmicSignature.getMeasureTimeunitLength());
		int originalTimeunit = timestamp % rythmicSignature.getMeasureTimeunitLength();
		
		for (MidiNote note : notes) {
			int measure = originalMeasure;
			int timeunit = originalTimeunit;
			boolean realNotePlaced = false;
			
			List<Value> values = Value.splitIntoValues(note.getLength());

			for (Value value : values) {
				int endTimeunit = timeunit + value.toTimeunit();
				if (endTimeunit <= rythmicSignature.getMeasureTimeunitLength()) {
					measures.get(measure).addNote(note, value, timeunit, realNotePlaced);
				} else {
					List<Value> splitValue = Value.splitInTwoAt(value, rythmicSignature.getMeasureTimeunitLength() - timeunit);
					if (splitValue == null || splitValue.get(0) == null || splitValue.get(1) == null) {
						throw new BuilderException("Value " + value + "(" + value.toTimeunit() + " timeunits) cannot be split in two at " + (endTimeunit - rythmicSignature.getMeasureTimeunitLength()) + ".");
					}
					measures.get(measure).addNote(note, splitValue.get(0), timeunit, realNotePlaced);
					int timeunitOnOtherMeasure = (timeunit + splitValue.get(0).toTimeunit()) % rythmicSignature.getMeasureTimeunitLength();
					measures.get(measure + 1).addNote(note, splitValue.get(1), timeunitOnOtherMeasure, true);
				}
				timeunit += value.toTimeunit();
				if (timeunit >= rythmicSignature.getMeasureTimeunitLength()) {
					++measure;
					timeunit -= rythmicSignature.getMeasureTimeunitLength();
				}
				realNotePlaced = true;
			}
		}
		if (Parameters.NOTE_ADDING_AUDIO) {
			MidiFileManager.player.playNoteStack(notes, 0, Parameters.NOTE_ADDING_AUDIO_TEMPO);
		}
		if (Parameters.NOTE_ADDING_VISUALISATION) {
			List<List<String>> measuresToPrint = new ArrayList<List<String>>();
			measuresToPrint.add(measures.get(originalMeasure).toStringList(false));
			
			if (originalMeasure > 0) {
				measuresToPrint.add(0, measures.get(originalMeasure - 1).toStringList(true));
			}
			if (originalMeasure > 1) {
				measuresToPrint.add(0, measures.get(originalMeasure - 2).toStringList(true));
			}
			printMeasures(measuresToPrint);
		}
	}


	//////////////////////////////
	//           PRINT          //
	//////////////////////////////

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
					Log.error("Cannot fusion measure string lists as they are not of the same size.");
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
			Log.vital(builder.toString());
		}
	}

	private char getHoriBarIfNeeded(int heigthOnPartition) {
		if ((heigthOnPartition > Parameters.BOTTOM_SECTION_HEIGHT &&
				heigthOnPartition < (Parameters.BOTTOM_SECTION_HEIGHT + 10) ||
				(heigthOnPartition > (Parameters.BOTTOM_SECTION_HEIGHT + 9 + Parameters.MIDDLE_SECTION_HEIGHT) &&
						heigthOnPartition < (Parameters.BOTTOM_SECTION_HEIGHT + 9 + Parameters.MIDDLE_SECTION_HEIGHT + 10)))) {
			return '|';
		}
		return ' ';
	}


	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////

	public RythmicSignature getRythmicSignature() {
		return rythmicSignature;
	}
}
