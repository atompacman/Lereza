package com.atompacman.lereza.core.piece.tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.common.solfege.Grouping;
import com.atompacman.lereza.common.solfege.Meter;
import com.atompacman.lereza.common.solfege.RythmicSignature;
import com.atompacman.lereza.common.solfege.Value;
import com.atompacman.lereza.core.exception.PieceBuilderException;
import com.atompacman.lereza.core.piece.Parameters;
import com.atompacman.lereza.core.piece.PieceBuilderAPI;
import com.atompacman.lereza.core.piece.container.Part;
import com.atompacman.lereza.core.piece.container.Piece;

import atompacman.leraza.midi.container.MidiFile;
import atompacman.leraza.midi.container.MidiNote;
import atompacman.leraza.midi.io.MidiFilePlayer;
import atompacman.leraza.midi.io.MidiFileReader;

public class PieceBuilder implements PieceBuilderAPI {

	private static Map<String, Piece> builtPieces;

	private Piece tempPiece;
	private MidiFile tempMidiFile;

	static {
		builtPieces = new HashMap<String, Piece>();
	}


	//////////////////////////////
	//          BUILD           //
	//////////////////////////////

	public void build(String filePath) {
		if (Log.infos() && Log.title("PieceBuilder", 0));
		if (Log.infos() && Log.print("Building midi file at " + filePath + "."));

		tempMidiFile = MidiFileReader.getMidiFile(filePath);
		tempPiece = new Piece();

		setRythmicSignature();

		for (int i = 0; i < tempMidiFile.getNbTracks(); ++i) {
			buildPart(i);
		}

		builtPieces.put(filePath, tempPiece);
		
		if (Log.infos() && Log.print("Piece built successfully."));
	}

	private void setRythmicSignature() {
		if (Log.infos() && Log.print("Rythmic signature : "));

		Meter meter = new Meter(tempMidiFile.getBeatPerMeasure(), tempMidiFile.getValueOfTheBeatNote());
		if (Log.infos() && Log.print(String.format(" - %-22s : %s", "Meter", meter.toString())));

		int midiBeatNoteValue = tempMidiFile.getDivisionOfABeat();
		if (Log.infos() && Log.print(String.format(" - %-22s : %s", "Midi beat note value", midiBeatNoteValue)));

		int valueOfShortestNote = tempMidiFile.getValueOfShortestNote();
		if (Log.infos() && Log.print(String.format(" - %-22s : %s", "Value of shortest note", valueOfShortestNote)));

		if (Log.infos() && Log.print(String.format(" - %-22s : %s", "Corrected length", midiBeatNoteValue / valueOfShortestNote)));
		if (Log.infos() && Log.print(String.format(" - %-22s : %s", "Value of shortest note", 
				Value.values()[Value.QUARTER.ordinal() - (int) Math.rint(Math.log10(midiBeatNoteValue / valueOfShortestNote) / Math.log10(2))])));

		if(meter.getDenominator() != 4) {
			if (Log.error() && Log.print("The time signature of the piece can not be used to determine the grouping of the notes."));
		}
		Grouping noteGrouping = Grouping.DUPLETS;
		if (Log.infos() && Log.print(String.format(" - %-22s : %s", "Note grouping", noteGrouping.name().toLowerCase())));

		if (Log.infos() && Log.print(String.format(" - %-22s : %s", "Final timestamp", tempMidiFile.getFinalTimestamp())));
		if (Log.infos() && Log.print(String.format(" - %-22s : %s (without counting the last note)", "Total nb of beats", 
				(double) tempMidiFile.getFinalTimestamp() * (double) valueOfShortestNote / (double) midiBeatNoteValue)));
		if (Log.infos() && Log.print(String.format(" - %-22s : %s (without counting the last note)", "Total nb of measures", 
				(double) tempMidiFile.getFinalTimestamp() * (double) valueOfShortestNote / ((double) midiBeatNoteValue * (double) meter.getNumerator()))));

		tempPiece.setRythmicSignature(new RythmicSignature(meter, noteGrouping, midiBeatNoteValue / valueOfShortestNote));
	}

	private void buildPart(int partNo) throws PieceBuilderException {
		if (Log.infos() && Log.title("Part " + partNo, 1));

		Map<Integer, Stack<MidiNote>> trackNotes = tempMidiFile.getNotes().get(partNo);

		if (trackNotes.isEmpty()) {
			if (Log.infos() && Log.print("Discarded a track with no notes (track " + partNo + ") : no part will be created."));
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

			if (i < timestamps.size() - 1) {
				int nextCorrectedTimestamp = timestampsOffsetCorrectionMap.get(timestamps.get(i + 1));
				TimestampsChecker.fusionOddNotesWithTinyRests(noteStack, correctedTimestamp, nextCorrectedTimestamp);
			}

			part.addNotes(noteStack, correctedTimestamp);

			if (Parameters.NOTE_ADDING_AUDIO) {
				if (i < timestamps.size() - 1) {
					int delta = timestampsOffsetCorrectionMap.get(timestamps.get(i + 1)) - correctedTimestamp;
					MidiFilePlayer.getPlayer().rest(delta, Parameters.NOTE_ADDING_AUDIO_TEMPO);
				}
			}
		}
		tempPiece.addPart(part);
	}

	private void printNoteAddingMessage(Stack<MidiNote> noteStack, int timestamp, int correctedTimestamp) {
		if (timestamp == correctedTimestamp) {
			if (Log.extra() && Log.print("Midi time unit : " + correctedTimestamp + "     || Adding notes " + noteStack.toString() + " to part."));
		} else {
			String delta = (correctedTimestamp - timestamp > 0) ? "+" + (correctedTimestamp - timestamp) : Integer.toString(correctedTimestamp - timestamp);
			if (Log.extra() && Log.print("Midi time unit : " + correctedTimestamp + " (" + timestamp + delta + ") || Adding notes " + noteStack.toString() + " to part."));
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

	public static Piece getPiece(String fileName) {
		return builtPieces.get(fileName);
	}
}
