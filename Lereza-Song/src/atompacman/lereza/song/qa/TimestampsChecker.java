package atompacman.lereza.song.qa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.container.MidiNote;
import atompacman.lereza.common.formatting.Formatting;
import atompacman.lereza.common.solfege.Value;
import atompacman.lereza.song.Parameters;
import atompacman.lereza.song.exception.BuilderException;

public class TimestampsChecker {

	private static List<Integer> correctedTimestamps;
	
	
	//////////////////////////////
	// CORRECT TIMESTAMPS OFFSET//
	//////////////////////////////	

	public static Map<Integer, Integer> createTimestampOffsetCorrectionMap(List<Integer> timestamps) {
		Log.infos(Formatting.lineSeparation("TimestampsChecker", 2));
		Log.infos("Correcting timestamps offset.");

		correctedTimestamps = new ArrayList<Integer>(timestamps);

		if (countOddTimestamps(0, Parameters.OFFBEAT_TIMEUNITS_QA_RADIUS) == Parameters.OFFBEAT_TIMEUNITS_QA_RADIUS) {
			adjustAllTimestampsFromOf(0, -1);
		}
		
		for (int i = Parameters.OFFBEAT_TIMEUNITS_QA_RADIUS; i + Parameters.OFFBEAT_TIMEUNITS_QA_RADIUS - 1 < timestamps.size(); ++i) {
			int delta = 0;
			if (isOdd(i)) {
				int nbOddTimestampsFound = countOddTimestamps(i, i + Parameters.OFFBEAT_TIMEUNITS_QA_RADIUS);
				
				if (nbOddTimestampsFound == Parameters.OFFBEAT_TIMEUNITS_QA_RADIUS) {
					delta = guessDeltaToFixOffset(i);
					adjustAllTimestampsFromOf(i, delta);
				}
			}
			if (delta == 0) {
				Log.extra(getTimestampModificationLine(i, timestamps.get(i), correctedTimestamps.get(i)));
			} else {
				Log.vital(getTimestampModificationLine(i, timestamps.get(i), correctedTimestamps.get(i)));
			}
		}
		return generateCorrectionMap(timestamps);
	}
	
	private static int countOddTimestamps(int beginning, int end) {
		int nbOddTimeunitFound = 0;

		for (int i = beginning; i < end; ++i) {
			if (isOdd(i)) {
				++nbOddTimeunitFound;
			}
		}
		return nbOddTimeunitFound;
	}

	private static int guessDeltaToFixOffset(int index) {
		int valueOfNoteBeforeCurrent = correctedTimestamps.get(index) - correctedTimestamps.get(index - 1);
		
		for (int i = Value.SIXTEENTH.ordinal(); i < Value.values().length; ++i) {
			Value value = Value.values()[i];
			if (valueOfNoteBeforeCurrent - 1 == value.toTimeunit()) {
				return -1;
			}
			if (valueOfNoteBeforeCurrent + 1 == value.toTimeunit()) {
				return 1;
			}
			if (valueOfNoteBeforeCurrent - 1 == (int)(value.toTimeunit() * 1.5)) {
				return -1;
			}
			if (valueOfNoteBeforeCurrent + 1 == (int)(value.toTimeunit() * 1.5)) {
				return 1;
			}
		}
		throw new BuilderException("Could not guess a delta to fix note offset at index " + index + " (offset: " + correctedTimestamps.get(index) + ") ."
				+ " Maybe the current note is a tied note with a ratio other dans 3/2 times a standard value.");
	}
	
	private static void adjustAllTimestampsFromOf(int beginning, int delta) {
		for (int i = beginning; i < correctedTimestamps.size(); ++i) {
			correctedTimestamps.set(i, correctedTimestamps.get(i) + delta);
		}
	}

	private static boolean isOdd(int index) {
		return correctedTimestamps.get(index) % 2 != 0;
	}
	

	private static String getTimestampModificationLine(int index, int originalTimestamp, int finalTimestamp) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Timestamp no." + index + ": " + originalTimestamp);
		
		int delta = finalTimestamp - originalTimestamp;
		String deltaString = (delta > 0) ? "+" + delta : Integer.toString(delta);
		
		if (delta != 0) {
			builder.append(" ---(" + deltaString + ")---> " + finalTimestamp);
		}
		return builder.toString();
	}

	private static Map<Integer, Integer> generateCorrectionMap(List<Integer> originalTimestamps) {
		Map<Integer, Integer> timestampsOffsetCorrectionMap = new HashMap<Integer, Integer>();

		for (int i = 0; i < originalTimestamps.size(); ++i) {
			timestampsOffsetCorrectionMap.put(originalTimestamps.get(i), correctedTimestamps.get(i));
		}
		return timestampsOffsetCorrectionMap;
	}
	
	
	//////////////////////////////
	//FUSION ODD NOTES AND RESTS//
	//////////////////////////////

	public static void fusionOddNotesWithTinyRests(Stack<MidiNote> notes, int timestamp, int nextTimestamp) {
		for (MidiNote note : notes) {
			int length = note.getLength();
			int gapToNextNote = nextTimestamp - timestamp - length;
			if (gapToNextNote > 0 && gapToNextNote <= Parameters.LONGEST_FUSIONNABLE_REST_LENGTH.toTimeunit()) {
				Log.vital(Formatting.lineSeparation("TimestampsChecker", 2));
				Log.vital("Fusionning note " + note.toString() + " with a rest of length " + gapToNextNote + ".");
				note.setLength(length + gapToNextNote);
			}
		}
	}
}
