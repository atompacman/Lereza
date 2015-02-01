package com.atompacman.lereza.piece.tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.atompacman.atomlog.Log;
import com.atompacman.lereza.Parameters;
import com.atompacman.lereza.api.PieceBuilder;
import com.atompacman.lereza.exception.DatabaseException;
import com.atompacman.lereza.exception.PieceBuilderException;
import com.atompacman.lereza.midi.MIDIFilePlayer;
import com.atompacman.lereza.midi.container.MIDIFile;
import com.atompacman.lereza.midi.container.MIDINote;
import com.atompacman.lereza.piece.container.Part;
import com.atompacman.lereza.piece.container.Piece;
import com.atompacman.lereza.resources.database.Database;
import com.atompacman.lereza.solfege.Grouping;
import com.atompacman.lereza.solfege.Meter;
import com.atompacman.lereza.solfege.RythmicSignature;
import com.atompacman.lereza.solfege.Value;

public class PieceBuilderImpl implements PieceBuilder {

	private Piece tempPiece;
	private MIDIFile tempMidiFile;


	//------------ READ ------------\\

	public void build(int caseID) throws DatabaseException {
		if (Log.infos() && Log.title("PieceBuilder", 0));
		
		tempMidiFile = DatabaseImpl.getMIDIFile(caseID);
		String filePath = tempMidiFile.getFileInfos().getFilePath();
		
		if (Log.infos() && Log.print("Building midi file at " + filePath + "."));

		tempPiece = new Piece(tempMidiFile);

		setRythmicSignature();

		for (int i = 0; i < tempMidiFile.getNbTracks(); ++i) {
			buildPart(i);
		}

		DatabaseImpl.setPiece(caseID, tempPiece);
		
		if (Log.infos() && Log.print("Piece built successfully."));
	}

	private void setRythmicSignature() {
		if (Log.infos() && Log.print("Rythmic signature : "));

		Meter meter = new Meter(tempMidiFile.getBeatPerBar(), 
				tempMidiFile.getValueOfTheBeatNote());
		printInfo("Meter", meter);

		int midiBeatNoteValue = tempMidiFile.getDivisionOfABeat();
		printInfo("Midi beat note value", midiBeatNoteValue);

		int valueOfShortestNote = tempMidiFile.getValueOfShortestNote();
		printInfo("Value of shortest note", valueOfShortestNote);

		double correctedLength = (double) midiBeatNoteValue / (double) valueOfShortestNote;
		if (correctedLength != Math.rint(correctedLength)) {
			throw new PieceBuilderException("Error with corrected length \"" + 
					correctedLength + "\": it must be an integer.");
		}
		printInfo("Corrected length", (int) correctedLength);

		double unroundedExp = Math.log10(correctedLength) / Math.log10(2);
		int roundedExp = (int) Math.rint(unroundedExp);
		if (unroundedExp != roundedExp) {
			throw new PieceBuilderException("Error with corrected length \"" + 
					correctedLength + "\": it must be a power of 2.");
		}
		int index = Value.QUARTER.ordinal() - roundedExp + 1;
		printInfo("Value of shortest note", Value.values()[index]);

		if(meter.getDenominator() != 4) {
			if (Log.error() && Log.print("The time signature of the piece can not "
					+ "be used to determine the grouping of the notes."));
		}
		Grouping noteGrouping = Grouping.DUPLETS;
		printInfo("Note grouping", noteGrouping.name().toLowerCase());
		printInfo("Final timestamp", tempMidiFile.getFinalTimestamp());

		double totalNbBeats = (double) tempMidiFile.getFinalTimestamp() / correctedLength;
		printInfo("Total nb of beats", (int) totalNbBeats + " (without counting the last note)");

		int totalNbBars = (int) (totalNbBeats / meter.getNumerator());
		printInfo("Total nb of bars", totalNbBars + " (without counting the last note)");

		RythmicSignature sign = new RythmicSignature(meter, noteGrouping);
		tempPiece.setRythmicSignature(sign);
	}

	private void printInfo(String elem, Object key) {
		if (Log.infos() && Log.print(String.format(" - %-22s : %s", elem, key)));
	}

	private void buildPart(int partNo) throws PieceBuilderException {
		if (Log.infos() && Log.title("Part " + partNo, 1));

		Map<Integer, Stack<MIDINote>> trackNotes = tempMidiFile.getNotes().get(partNo);

		if (trackNotes.isEmpty()) {
			if (Log.infos() && Log.print("Discarded a track with no notes (track " + 
					partNo + ") : no part will be created."));
			return;
		}
		List<Integer> timestamps = new ArrayList<Integer>(trackNotes.keySet());
		Collections.sort(timestamps);

		Map<Integer, Integer> timestampsOffsetCorrectionMap = 
				TimestampsChecker.createTimestampOffsetCorrecMap(timestamps);

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
			Stack<MIDINote> noteStack = trackNotes.get(timestamp);

			printNoteAddingMessage(noteStack, timestamp, correctedTimestamp);

			if (i < timestamps.size() - 1) {
				int timestmp = timestamps.get(i + 1);
				int nextCorrectedTimestamp = timestampsOffsetCorrectionMap.get(timestmp);
				TimestampsChecker.fusionOddNotesWithTinyRests(
						noteStack, correctedTimestamp, nextCorrectedTimestamp);
			}

			part.addNotes(noteStack, correctedTimestamp);

			if (Parameters.NOTE_ADDING_AUDIO) {
				if (i < timestamps.size() - 1) {
					int timestmp = timestamps.get(i + 1);
					int delta = timestampsOffsetCorrectionMap.get(timestmp) - correctedTimestamp;
					MIDIFilePlayer.getInstance().rest(delta, Parameters.NOTE_ADDING_AUDIO_TEMPO);
				}
			}
		}
		tempPiece.addPart(part);
	}

	private void printNoteAddingMessage(Stack<MIDINote> noteStack, 
			int timestamp, int correctedTimestamp) {
		if (timestamp == correctedTimestamp) {
			if (Log.extra() && Log.print("Midi time unit : " + correctedTimestamp 
					+ "     || Adding notes " + noteStack.toString() + " to part."));
		} else {
			int numDelta = correctedTimestamp - timestamp;
			String delta = (numDelta > 0) ? "+" + numDelta : Integer.toString(numDelta);
			if (Log.extra() && Log.print("Midi time unit : " + correctedTimestamp + " (" + timestamp 
					+ delta + ") || Adding notes " + noteStack.toString() + " to part."));
		}
	}

	private int stackLonguestLength(Stack<MIDINote> noteStack) {
		int longuestLength = 0;

		for (MIDINote note : noteStack) {
			if (note.getLength() > longuestLength) {
				longuestLength = note.getLength();
			}
		}
		return longuestLength;
	}
}
