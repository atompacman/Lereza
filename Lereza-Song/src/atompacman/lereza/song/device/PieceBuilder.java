package atompacman.lereza.song.device;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.MidiFileManager;
import atompacman.leraza.midi.container.MidiFile;
import atompacman.leraza.midi.container.MidiNote;
import atompacman.lereza.common.formatting.Formatting;
import atompacman.lereza.common.solfege.Grouping;
import atompacman.lereza.common.solfege.Meter;
import atompacman.lereza.common.solfege.RythmicSignature;
import atompacman.lereza.common.solfege.Value;
import atompacman.lereza.song.Parameters;
import atompacman.lereza.song.api.PieceBuilderAPI;
import atompacman.lereza.song.container.Part;
import atompacman.lereza.song.container.Piece;
import atompacman.lereza.song.exception.BuilderException;
import atompacman.lereza.song.qa.TimestampsChecker;

public class PieceBuilder implements PieceBuilderAPI {

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
		Log.infos(Formatting.lineSeparation("PieceBuilder", 0));
		Log.infos("Building midi file at " + filePath + ".");

		tempMidiFile = MidiFileManager.reader.getMidiFile(filePath);
		tempPiece = new Piece();

		setRythmicSignature();

		for (int i = 0; i < tempMidiFile.getNbTracks(); ++i) {
			buildPart(i);
		}

		builtPieces.put(filePath, tempPiece);
	}

	private void setRythmicSignature() {
		Log.infos("Rythmic signature : ");

		Meter meter = new Meter(tempMidiFile.getBeatPerMeasure(), tempMidiFile.getValueOfTheBeatNote());
		Log.infos(String.format(" - %-22s : %s", "Meter", meter.toString()));

		int midiBeatNoteValue = tempMidiFile.getDivisionOfABeat();
		Log.infos(String.format(" - %-22s : %s", "Midi beat note value", midiBeatNoteValue));

		int valueOfShortestNote = tempMidiFile.getValueOfShortestNote();
		Log.infos(String.format(" - %-22s : %s", "Value of shortest note", valueOfShortestNote));

		Log.infos(String.format(" - %-22s : %s", "Corrected length", midiBeatNoteValue / valueOfShortestNote));
		Log.infos(String.format(" - %-22s : %s", "Value of shortest note", Value.values()[Value.QUARTER.ordinal() - (int) Math.rint(Math.log10(midiBeatNoteValue / valueOfShortestNote) / Math.log10(2))]));

		if(meter.getDenominator() != 4) {
			Log.error("The time signature of the piece can not be used to determine the grouping of the notes.");
		}
		Grouping noteGrouping = Grouping.DUPLETS;
		Log.infos(String.format(" - %-22s : %s", "Note grouping", noteGrouping.name().toLowerCase()));

		Log.infos(String.format(" - %-22s : %s", "Final timestamp", tempMidiFile.getFinalTimestamp()));
		Log.infos(String.format(" - %-22s : %s (without counting the last note)", "Total nb of beats", (double) tempMidiFile.getFinalTimestamp() * (double) valueOfShortestNote / (double) midiBeatNoteValue));
		Log.infos(String.format(" - %-22s : %s (without counting the last note)", "Total nb of measures", (double) tempMidiFile.getFinalTimestamp() * (double) valueOfShortestNote / ((double) midiBeatNoteValue * (double) meter.getNumerator())));

		tempPiece.setRythmicSignature(new RythmicSignature(meter, noteGrouping, midiBeatNoteValue / valueOfShortestNote));
	}

	private void buildPart(int partNo) throws BuilderException {
		Log.infos(Formatting.lineSeparation("Part " + partNo, 1));

		Map<Integer, Stack<MidiNote>> trackNotes = tempMidiFile.getNotes().get(partNo);

		if (trackNotes.isEmpty()) {
			Log.infos("Discarded a track with no notes (track " + partNo + ") : no part will be created.");
			return;
		}
		List<Integer> timestamps = new ArrayList<Integer>(trackNotes.keySet());
		Collections.sort(timestamps);

		Map<Integer, Integer> timestampsOffsetCorrectionMap = TimestampsChecker.createTimestampOffsetCorrectionMap(timestamps);

		int finalTimeunit = 0;
		for (int i = timestamps.size() - 1; i > timestamps.size() - 10; --i) {
			int timestamp = timestampsOffsetCorrectionMap.get(timestamps.get(i));
			int longuestLength = stackLonguestLength(trackNotes.get(timestamps.get(i)));
			if (timestamp + longuestLength > finalTimeunit) {
				finalTimeunit = timestamp + longuestLength;
			}
		}
		
		Part part = new Part(tempPiece.getRythmicSignature(), finalTimeunit);
		
		for (int i = 0; i < timestamps.size(); ++i) {
			int timestamp = timestamps.get(i);
			int correctedTimestamp = timestampsOffsetCorrectionMap.get(timestamp);
			Stack<MidiNote> noteStack = trackNotes.get(timestamp);

			printNoteAddingMessage(noteStack, timestamp, correctedTimestamp);

			//TODO remove
			if (correctedTimestamp == 2696) {
				correctedTimestamp += 0;
			}
			//TODO

			if (i < timestamps.size() - 1) {
				int nextCorrectedTimestamp = timestampsOffsetCorrectionMap.get(timestamps.get(i + 1));
				TimestampsChecker.fusionOddNotesWithTinyRests(noteStack, correctedTimestamp, nextCorrectedTimestamp);
			}

			part.addNotes(noteStack, correctedTimestamp);

			if (Parameters.NOTE_ADDING_AUDIO) {
				if (i < timestamps.size() - 1) {
					int delta = timestampsOffsetCorrectionMap.get(timestamps.get(i + 1)) - correctedTimestamp;
					MidiFileManager.player.rest(delta, Parameters.NOTE_ADDING_AUDIO_TEMPO);
				}
			}
		}
		tempPiece.addPart(part);
	}

	private void printNoteAddingMessage(Stack<MidiNote> noteStack, int timestamp, int correctedTimestamp) {
		if (timestamp == correctedTimestamp) {
			Log.extra("Midi time unit : " + correctedTimestamp + "     || Adding notes " + noteStack.toString() + " to part.");
		} else {
			String delta = (correctedTimestamp - timestamp > 0) ? "+" + (correctedTimestamp - timestamp) : Integer.toString(correctedTimestamp - timestamp);
			Log.extra("Midi time unit : " + correctedTimestamp + " (" + timestamp + delta + ") || Adding notes " + noteStack.toString() + " to part.");
		}
	}

	private int stackLonguestLength(Stack<MidiNote> noteStack) {
		int longuestLength = 0;
		
		for (MidiNote note : noteStack) {
			if (note.getLength() > longuestLength) {
				longuestLength = note.getLength();
			}
		}
		return longuestLength;
	}
	
	
	//////////////////////////////
	//          GETTERS         //
	//////////////////////////////

	public Piece getPiece(String filePath) {
		return builtPieces.get(filePath);
	}
}
