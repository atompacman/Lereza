package atompacman.lereza.core.builder.builder;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.MiDiO;
import atompacman.leraza.midi.container.MIDIFile;
import atompacman.leraza.midi.container.MIDINote;
import atompacman.leraza.midi.utilities.Formatting;
import atompacman.lereza.core.Parameters;
import atompacman.lereza.core.container.part.Voice;
import atompacman.lereza.core.container.piece.Piece;
import atompacman.lereza.core.container.piece.PolyphonicPiece;
import atompacman.lereza.core.exception.BuilderException;
import atompacman.lereza.core.solfege.Grouping;
import atompacman.lereza.core.solfege.Meter;
import atompacman.lereza.core.solfege.RythmicSignature;
import atompacman.lereza.core.solfege.Value;

public class PolyphonicBuilder implements Builder {

	private PolyphonicPiece tempPiece;
	private MIDIFile tempMidiFile;

	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////

	public PolyphonicBuilder(MIDIFile midiFile) {
		this.tempMidiFile = midiFile;
	}


	//////////////////////////////
	//          BUILD           //
	//////////////////////////////

	public Piece build(Class<? extends Piece> musicalForm) {
		Log.infos(Formatting.lineSeparation("PolyphonicBuilder", 0));
		
		try {
			tempPiece = musicalForm.asSubclass(PolyphonicPiece.class).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		Log.infos("Building a " + musicalForm.getSimpleName() + ".");
		
		setRythmicSignature();

		try {
			for (int i = 0; i < tempMidiFile.getNbTracks(); ++i) {
				buildVoice(i);
			}
		} catch (BuilderException e) {
			e.printStackTrace();
		}

		return tempPiece;
	}


	//////////////////////////////
	//       PRIVATE BUILD      //
	//////////////////////////////

	private void setRythmicSignature() {
		Meter meter = new Meter(tempMidiFile.getBeatPerMeasure(), tempMidiFile.getValueOfTheBeatNote());
		int midiBeatNoteValue = tempMidiFile.getDivisionOfABeat();
		int valueOfShortestNote = findSmallestIntegerDivisionOfBeatNoteValue();
		Grouping noteGrouping = Grouping.DUPLETS;

		if(meter.getDenominator() != 4) {
			Log.error("The time signature of the piece can not be used to determine the grouping of the notes.");
		}
		tempPiece.setRythmicSignature(new RythmicSignature(meter, noteGrouping, midiBeatNoteValue, valueOfShortestNote));
	}

	private void buildVoice(int voiceNo) throws BuilderException {
		Log.infos("Building voice nb " + voiceNo + " of " + tempMidiFile.getFilePath() + ".");
		
		if (tempMidiFile.getNotes().get(voiceNo).isEmpty()) {
			Log.infos("Discarded a track with no notes: a voice won't be created.");
			return;
		}
		Voice voice = new Voice(tempPiece.getRythmicSignature());

		if (tempMidiFile.getTimeBeforeFirstNote(voiceNo) != 0) {
			voice.addRest(tempMidiFile.getTimeBeforeFirstNote(voiceNo));
		}

		for (int i = 0; i < tempMidiFile.getNotes().get(voiceNo).size(); ++i) {
			MIDINote midiNote = tempMidiFile.getNotes().get(voiceNo).get(i);

			if (midiNote.isRest()) {
				voice.addRest(midiNote.getLength());
			} else {
				if (checkNoteIntegrity(midiNote, i, voiceNo)) {
					voice.addNote(midiNote);
				} else {
					tempMidiFile.getNotes().get(voiceNo).remove(i);
				}
			}
			if (Parameters.NOTE_ADDING_VISUALISATION) {
				MiDiO.player.startNote(midiNote.getNote());
				noteAddingVisualisation(midiNote.getLength() / tempPiece.getRythmicSignature().getValueOfShortestNote(), voice);		
			}
		}
		tempPiece.addVoice(voice);
	}

	private boolean checkNoteIntegrity(MIDINote midiNote, int noteIndex, int voiceNo) {
		if (noteIndex == tempMidiFile.getNotes().get(voiceNo).size() - 1) {
			return true;
		}
		MIDINote nextNote = tempMidiFile.getNotes().get(voiceNo).get(noteIndex + 1);

		if (midiNote.getLength() == 0) {
			if (nextNote.isRest()) {
				midiNote.setLength(-(midiNote.getLength() + nextNote.getLength()));
				tempMidiFile.getNotes().get(voiceNo).remove(noteIndex + 1);
			} else {
				Log.warng("A note of length 0 that is not followed by a rest was DISCARDED (" + midiNote.toString() + ") at voice " + voiceNo + ".");
				return false;
			}
		} else if (nextNote.isRest() && hasAVeryShortMIDIValue(nextNote)) {
			midiNote.setLength(midiNote.getLength() + nextNote.getLength());
			tempMidiFile.getNotes().get(voiceNo).remove(noteIndex + 1);
		}
		return true;
	}

	private boolean hasAVeryShortMIDIValue(MIDINote note) {
		return note.getLength() < Value.SIXTEENTH.ordinal()*tempPiece.getRythmicSignature().getValueOfShortestNote();
	}


	//////////////////////////////
	//       VISUALISATION      //
	//////////////////////////////

	private void noteAddingVisualisation(int midiLength, Voice voice) {
		try {
			Log.extra(voice.beforeLastMeasure().fusionToPartition(voice.lastMeasure().toString()));
			Thread.sleep((int)(midiLength /  16d * (double)tempMidiFile.getTempo() / 1000d * Parameters.NOTE_VISUALISATION_CORRECTION));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	//////////////////////////////
	//           OTHER          //
	//////////////////////////////

	private int findSmallestIntegerDivisionOfBeatNoteValue() {
		double smallestIntegerDivisionOfBeatNoteValue = tempMidiFile.getDivisionOfABeat();
		while ((smallestIntegerDivisionOfBeatNoteValue / 2 == (int) smallestIntegerDivisionOfBeatNoteValue / 2)) {
			smallestIntegerDivisionOfBeatNoteValue /= 2;
		}
		return (int) smallestIntegerDivisionOfBeatNoteValue;
	}
}
