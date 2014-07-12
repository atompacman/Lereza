package atompacman.lereza.song.container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.MidiFileManager;
import atompacman.leraza.midi.container.MidiNote;
import atompacman.lereza.common.solfege.RythmicSignature;
import atompacman.lereza.common.solfege.Value;
import atompacman.lereza.song.Parameters;

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
		int measure = (int) ((double) timestamp / (double) rythmicSignature.getMeasureTimeunitLength());
		
		for (MidiNote note : notes) {
			int timeunit = timestamp % rythmicSignature.getMeasureTimeunitLength();
			boolean realNotePlaced = false;
			
			List<Value> values = Value.splitIntoValues(note.getLength());

			for (Value value : values) {
				int endTimeunit = timeunit + value.toTimeunit();
				if (endTimeunit <= rythmicSignature.getMeasureTimeunitLength()) {
					measures.get(measure).addNote(note, value, timeunit, realNotePlaced);
				} else {
					List<Value> splitValue = Value.splitInTwoAt(value, endTimeunit - rythmicSignature.getMeasureTimeunitLength());
					measures.get(measure).addNote(note, splitValue.get(0), timeunit, realNotePlaced);
					int timeunitOnOtherMeasure = (timeunit + splitValue.get(0).toTimeunit()) % rythmicSignature.getMeasureTimeunitLength();
					measures.get(measure + 1).addNote(note, splitValue.get(1), timeunitOnOtherMeasure, realNotePlaced);
				}
				timeunit += value.toTimeunit();
				if (timeunit >= rythmicSignature.getMeasureTimeunitLength()) {
					++measure;
				}
				realNotePlaced = true;
			}

		}
		if (Parameters.NOTE_ADDING_VISUALISATION) {
			if (measure != 0) {
				printMeasures(Arrays.asList(measures.get(measure - 1).toStringList(), measures.get(measure).toStringList()));
			}
		}
		if (notes.size() == 1) {
			if (Parameters.NOTE_ADDING_AUDIO) {
				MidiFileManager.player.playNote(notes.get(0).getNote(), notes.get(0).getLength(), Parameters.NOTE_ADDING_AUDIO_TEMPO);
			}
		} else {
			Log.error("Polyphony not yet implemented.");
		}
	}


	//////////////////////////////
	//          OTHERS          //
	//////////////////////////////

	public void fillWithRests() {
		//TODO
	}


	//////////////////////////////
	//           PRINT          //
	//////////////////////////////

	public void printMeasures(List<List<String>> measures) {
		if (measures == null || measures.size() < 2) {
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
			Log.infos(builder.toString());
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
