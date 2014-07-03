package atompacman.lereza.song.newContainer;

import java.util.HashMap;
import java.util.Map;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.MidiFileManager;
import atompacman.leraza.midi.container.MidiFile;
import atompacman.leraza.midi.container.MidiNote;
import atompacman.lereza.common.architecture.Device;
import atompacman.lereza.common.formatting.Formatting;
import atompacman.lereza.common.solfege.Grouping;
import atompacman.lereza.common.solfege.Meter;
import atompacman.lereza.common.solfege.RythmicSignature;
import atompacman.lereza.song.Parameters;
import atompacman.lereza.song.api.PieceBuilderAPI;
import atompacman.lereza.song.container.part.Voice;
import atompacman.lereza.song.container.piece.Piece;
import atompacman.lereza.song.exception.BuilderException;

public class PieceBuilder implements PieceBuilderAPI, Device {

	private Map<String, Piece> builtPieces;
	
	private Piece tempPiece;
	private MidiFile tempMidiFile;

	
	//////////////////////////////
	//       CONSTRUCTOR        //
	//////////////////////////////

	public PieceBuilder() {
		this.builtPieces = new HashMap<String, Piece>();
	}

	
	//////////////////////////////
	//          BUILD           //
	//////////////////////////////

	public void build(String filePath) {
		Log.infos(Formatting.lineSeparation("PolyphonicBuilder", 0));
		
		tempMidiFile = MidiFileManager.reader.getMidiFile(filePath);
		tempPiece = new Piece();
		
		setRythmicSignature();
		
		try {
			for (int i = 0; i < tempMidiFile.getNbTracks(); ++i) {
				buildPart(i);
			}
		} catch (BuilderException e) {
			e.printStackTrace();
		}
		builtPieces.put(filePath, tempPiece);
	}

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

	private int findSmallestIntegerDivisionOfBeatNoteValue() {
		double smallestIntegerDivisionOfBeatNoteValue = tempMidiFile.getDivisionOfABeat();
		while ((smallestIntegerDivisionOfBeatNoteValue / 2 == (int) smallestIntegerDivisionOfBeatNoteValue / 2)) {
			smallestIntegerDivisionOfBeatNoteValue /= 2;
		}
		return (int) smallestIntegerDivisionOfBeatNoteValue;
	}
	
	private void buildPart(int partNo) throws BuilderException {
		Log.infos("Building part nb " + partNo + " of " + tempMidiFile.getFilePath() + ".");
		
		if (tempMidiFile.getNotes().get(partNo).isEmpty()) {
			Log.infos("Discarded a track with no notes: no part will be created.");
			return;
		}
		Voice voice = new Voice(tempPiece.getRythmicSignature());

		if (tempMidiFile.getTimeBeforeFirstNote(partNo) != 0) {
			voice.addRest(tempMidiFile.getTimeBeforeFirstNote(partNo));
		}

		for (int i = 0; i < tempMidiFile.getNotes().get(partNo).size(); ++i) {
			MidiNote midiNote = tempMidiFile.getNotes().get(partNo).get(i);

			if (midiNote.isRest()) {
				voice.addRest(midiNote.getLength());
			} else {
				if (checkNoteIntegrity(midiNote, i, partNo)) {
					voice.addNote(midiNote);
				} else {
					tempMidiFile.getNotes().get(partNo).remove(i);
				}
			}
			if (Parameters.NOTE_ADDING_VISUALISATION) {
				MidiFileManager.player.startNote(midiNote.getNote());
				noteAddingVisualisation(midiNote.getLength() / tempPiece.getRythmicSignature().getValueOfShortestNote(), voice);		
			}
		}
		tempPiece.addVoice(voice);
	}
	
	
	//////////////////////////////
	//          GETTERS         //
	//////////////////////////////
	
	public Piece getPiece(String filePath) {
		return builtPieces.get(filePath);
	}
}
