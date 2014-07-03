package atompacman.lereza.song.device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.MidiFileManager;
import atompacman.leraza.midi.container.MidiFile;
import atompacman.leraza.midi.container.MidiNote;
import atompacman.lereza.common.architecture.Device;
import atompacman.lereza.common.architecture.Manager;
import atompacman.lereza.common.formatting.Formatting;
import atompacman.lereza.common.solfege.Grouping;
import atompacman.lereza.common.solfege.Meter;
import atompacman.lereza.common.solfege.RythmicSignature;
import atompacman.lereza.common.solfege.Value;
import atompacman.lereza.song.Parameters;
import atompacman.lereza.song.api.PieceBuilderAPI;
import atompacman.lereza.song.container.part.Voice;
import atompacman.lereza.song.container.piece.Piece;
import atompacman.lereza.song.container.piece.PolyphonicPiece;
import atompacman.lereza.song.exception.BuilderException;

public class PieceBuilder implements PieceBuilderAPI, Device {

	private Piece tempPiece;
	private MidiFile tempMidiFile;
	private Map<String, Piece> builtPieces;
	
	
	//////////////////////////////
	//       CONSTRUCTOR        //
	//////////////////////////////

	public PieceBuilder() {
		this.tempPiece = new Piece();
		this.builtPieces = new HashMap<String, Piece>();
	}

	
	//////////////////////////////
	//          BUILD           //
	//////////////////////////////

	public void build(String filePath) {
		Log.infos(Formatting.lineSeparation("PolyphonicBuilder", 0));
		
		tempMidiFile = MidiFileManager.reader.getMidiFile(filePath);
		
		setRythmicSignature();

		try {
			for (int i = 0; i < tempMidiFile.getNbTracks(); ++i) {
				buildVoice(i);
			}
		} catch (BuilderException e) {
			e.printStackTrace();
		}
		builtPieces.put(filePath, tempPiece);
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
			MidiNote midiNote = tempMidiFile.getNotes().get(voiceNo).get(i);

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
				MidiFileManager.player.startNote(midiNote.getNote());
				noteAddingVisualisation(midiNote.getLength() / tempPiece.getRythmicSignature().getValueOfShortestNote(), voice);		
			}
		}
		tempPiece.addVoice(voice);
	}

	private boolean checkNoteIntegrity(MidiNote midiNote, int noteIndex, int voiceNo) {
		if (noteIndex == tempMidiFile.getNotes().get(voiceNo).size() - 1) {
			return true;
		}
		MidiNote nextNote = tempMidiFile.getNotes().get(voiceNo).get(noteIndex + 1);

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

	private boolean hasAVeryShortMIDIValue(MidiNote note) {
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
	
	
	//////////////////////////////
	//     PROTECTED GETTER     //
	//////////////////////////////
	
	protected Piece getPiece(String filePath) {
		return builtPieces.get(filePath);
	}
}
