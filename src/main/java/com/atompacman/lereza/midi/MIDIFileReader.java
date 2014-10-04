package com.atompacman.lereza.midi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.Parameters;
import com.atompacman.lereza.api.MIDIFileReaderAPI;
import com.atompacman.lereza.common.database.Database;
import com.atompacman.lereza.exception.DatabaseException;
import com.atompacman.lereza.exception.MIDIFileReaderException;
import com.atompacman.lereza.midi.container.MIDIFile;
import com.atompacman.lereza.midi.container.MIDIFileErrorSummary;
import com.atompacman.lereza.midi.container.MIDIFileInfo;
import com.atompacman.lereza.midi.container.MIDIInstrument;
import com.atompacman.lereza.midi.container.MIDINote;

public class MIDIFileReader implements MIDIFileReaderAPI {

	private static final int MIDI_FILE_FORMAT_BEGINNING = 0x4d546864;
	private static final int MIDI_TRACK_HEADER = 0x4d54726b;
	private static final int SUPPORTED_MIDI_TYPE = 1;

	private MIDIFile tempMidiFile;
	private Map<Integer, MIDIFileErrorSummary> errors;

	private byte[] rawData;

	private int offset;
	private int currentTrack;
	private int currentChannel;
	private int timestamp;
	private boolean runningStatusOn;

	private Map<Integer, Integer> noteBuffer;


	//------------ READ FILE ------------\\

	public void read(int caseID) throws MIDIFileReaderException, DatabaseException {
		MIDIFileInfo fileInfo = Database.getFileInfo(caseID);
		read(fileInfo.getFilePath());
		tempMidiFile.setFileInfo(fileInfo);
		Database.setMIDIFile(caseID, tempMidiFile);
	}
	
	protected void read(String filePath) throws MIDIFileReaderException {
		if (Log.infos() && Log.title("MIDIFileReader", 0));
		if (Log.infos() && Log.title("Loading file at \"" + filePath + "\"", 1));

		this.tempMidiFile = new MIDIFile();
		this.errors = new HashMap<Integer, MIDIFileErrorSummary>();
		this.rawData = null;
		this.offset = 0;
		this.currentTrack = 0;
		this.currentChannel = -1;
		this.timestamp = 0;
		this.runningStatusOn = false;
		this.noteBuffer = new HashMap<Integer, Integer>();

		try {
			loadRawBinaryFile(filePath);
			readBinaryFile();
		} catch (Exception e) {
			throw new MIDIFileReaderException("Error reading MIDI file \"" + filePath + "\".", e);
		}

		printErrorSummary();

		if (Log.infos() && Log.title(1));
	}

	private void loadRawBinaryFile(String filePath) throws MIDIFileReaderException {
		try {
			File file = new File(filePath);
			long fileSize = file.length();
			if (fileSize > Parameters.MAX_FILE_SIZE) {
				throw new MIDIFileReaderException("MIDI file size exceeds " 
						+ Parameters.MAX_FILE_SIZE / 1000 + " KB.");
			}
			rawData = new byte[(int) fileSize];
			FileInputStream fis = new FileInputStream(file);

			fis.read(rawData);

			fis.close();

			if (Log.infos() && Log.print("File loaded in memory (" + fileSize / 1000 + " KB)."));
		} catch (FileNotFoundException e) {
			throw new MIDIFileReaderException("Midi file not found at \"" + filePath + "\".");
		} catch (IOException e) {
			throw new MIDIFileReaderException("Error reading binary file.");
		}
	}

	private void readBinaryFile() throws MIDIFileReaderException {
		readFileHeader();

		while (!endOfFile()) {
			readTrack();
			++currentTrack;
			if (timestamp > tempMidiFile.getFinalTimestamp()) {
				tempMidiFile.setFinalTimestamp(timestamp / tempMidiFile.getValueOfShortestNote());
			}
			timestamp = 0;
		}
	}

	private void readFileHeader() throws MIDIFileReaderException {
		if (Log.infos() && Log.print(printOffset() + "Reading header."));

		if (readInt() != MIDI_FILE_FORMAT_BEGINNING) {
			throw new MIDIFileReaderException(printOffset() + "File is not of MIDI format.");
		}
		offset += 8;

		int midiType = readShort();
		if (midiType != SUPPORTED_MIDI_TYPE){
			throw new MIDIFileReaderException(printOffset() 
					+ "MIDI type " + midiType + " is not supported.");
		}
		tempMidiFile.setMidiType(midiType);
		if (Log.infos() && Log.print(printOffset() + "  * File type: " + midiType + "."));
		offset += 2;

		int nbTracks = readShort();
		tempMidiFile.setNbTracks(nbTracks);
		if (Log.infos() && Log.print(printOffset() + "  * Number of tracks: " + nbTracks + "."));
		offset += 2;

		int divisionOfABeat = readShort();
		tempMidiFile.setDivisionOfABeat(divisionOfABeat);
		if (Log.infos() && Log.print(printOffset() 
				+ "  * Number of divisions of a beat: " + divisionOfABeat + "."));
		offset += 2;
	}

	private void readTrack() throws MIDIFileReaderException {
		if (Log.infos() && Log.title("Reading track " + currentTrack, 1));
		errors.put(currentTrack, new MIDIFileErrorSummary());	

		readTrackHeader();
		int deltaTime = readDeltaTime();

		while (!endOfTrack()) {
			handleEvent(deltaTime);
			deltaTime = readDeltaTime();
		}
		if (Log.infos() && Log.print(printOffset() + "End of Track no." + currentTrack + 
				": " + tempMidiFile.getNotes().get(currentTrack).size() + " notes read."));
		currentChannel = -1;
		offset += 3;
	}

	private void readTrackHeader() throws MIDIFileReaderException {
		if (readInt() != MIDI_TRACK_HEADER) {
			throw new MIDIFileReaderException(printOffset() + "Error in the track header.");
		}
		offset += 8;
	}


	//------------ EVENT HANDLING ------------\\

	private void handleEvent(int deltaTime) throws MIDIFileReaderException {
		if (isAMetaEvent()) {
			offset += 1;
			handleMetaEvent();
		} else if (isAMIDIEvent()){
			handleMIDIEvent(deltaTime);
			runningStatusOn = true;
		} else if (runningStatusOn) {
			int note = readByte();
			if (noteBuffer.containsKey(note)) {
				handleNoteOffEvent(deltaTime);
			} else {
				handleNoteOnEvent(deltaTime);
			}	
		} else {
			throw new MIDIFileReaderException(printOffset() + 
					"Unknown event Id \"" + readByte() + "\".");
		}
	}

	private void handleMetaEvent() throws MIDIFileReaderException {
		switch(readByte()) {
		case 0x01: case 0x02: case 0x03: case 0x04: case 0x05: case 0x06: case 0x07: 
			handleTextEvent(); return;
		case 0x21: handleMIDIportEvent(); return;
		case 0x51: handleSetTempoMetaEvent(); return;
		case 0x58: handleTimeSignatureMetaEvent(); return;
		case 0x59: handleKeySignatureMetaEvent(); return;
		default: throw new MIDIFileReaderException(printOffset() 
				+ "Unknown meta event with Id \"" + readByte() + "\".");
		}
	}

	private void handleMIDIEvent(int deltaTime) throws MIDIFileReaderException {
		switch(firstHexDigit()) {
		case 0x80:
			verifyChannel();
			offset += 1;
			handleNoteOffEvent(deltaTime);
			return;
		case 0x90: 
			verifyChannel();
			offset += 1;
			handleNoteOnEvent(deltaTime);
			return;
		case 0xa0: handlePolyphonicPressureChangeEvent(); return;
		case 0xb0: handleControllerChangeEvent(); return;
		case 0xc0: handleProgramChangeEvent(); return;
		default: throw new MIDIFileReaderException(printOffset() 
				+ "Unknown midi event Id \"" + readByte() + "\".");
		}
	}

	private void handleNoteOnEvent(int deltaTime) {
		if (timestamp == 0) {
			if (Parameters.NOTE_VISUALISATION) {
				sleep((int)(deltaTime * Parameters.VISUALISATION_SPEED_CORRECTION));
			}
		}
		timestamp += deltaTime;
		int note = readByte();

		noteBuffer.put(note, timestamp);

		if (Parameters.NOTE_VISUALISATION) {
			StringBuilder noteSpacing = new StringBuilder();
			for (int i = 0; i < note - 30; ++i) {
				noteSpacing.append("   ");
			}
			if (Log.vital() && Log.print(noteSpacing.toString() + HexToNote.toString(note)));
			if (Parameters.NOTE_PLAY_AUDIO) {
				MIDIFilePlayer.getPlayer().startNote(note);
			}
		}
		offset += 2;
	}

	private void handleNoteOffEvent(int deltaTime) throws MIDIFileReaderException {
		if (Parameters.NOTE_VISUALISATION) {
			sleep((int)(deltaTime * Parameters.VISUALISATION_SPEED_CORRECTION));
		}

		timestamp += deltaTime;

		int note = readByte();

		if (noteBuffer.get(note) == null) {
			throw new MIDIFileReaderException(printOffset() 
					+ "Stopping a note that has never been started.");
		}
		Integer startTimestamp = noteBuffer.get(note);
		Integer roundedStartTimestamp = roundLength(startTimestamp);
		Integer roundedStopTimestamp = roundLength(timestamp);
		int noteDuration = roundedStopTimestamp - roundedStartTimestamp;
		noteBuffer.remove(note);

		int deltaLength = noteDuration - (timestamp - startTimestamp);
		if (Math.abs(deltaLength) > Parameters.NOTE_LENGTH_CORRECTION_THRESHOLD) {
			errors.get(currentTrack).incrementRoundingOverThresholdCount();
			if(Log.warng() && Log.print(printOffset() + "[Note rounding] A note "
					+ "of length " + noteDuration + " had to be EXCESSIVELY "
					+ "rounded of a value of " + deltaLength + "."));
		}
		errors.get(currentTrack).adjustNoteRoundingTotalOffset(deltaLength);

		double shortestNoteValue = (double)tempMidiFile.getValueOfShortestNote();
		int dividedTimestamp = (int)((double)roundedStartTimestamp / shortestNoteValue);
		int dividedDuration = (int)((double)noteDuration / shortestNoteValue); 

		Map<Integer, Stack<MIDINote>> currNotes = tempMidiFile.getNotes().get(currentTrack);
		if (currNotes.get(dividedTimestamp) == null) {
			currNotes.put(dividedTimestamp, new Stack<MIDINote>());
		}
		currNotes.get(dividedTimestamp).push(new MIDINote(note, dividedDuration));

		if (dividedDuration == 0) {
			errors.get(currentTrack).incrementNoteLengthZeroCount();
		}
		if (Parameters.NOTE_VISUALISATION) {
			if (Parameters.NOTE_PLAY_AUDIO) {
				MIDIFilePlayer.getPlayer().stopNote(note);
			}
		}
		if (Log.extra() && Log.print(printOffset() + "[ Adding note ] " + String.format("%-3s of "
				+ "length %3d (timestamps: %5d[%2s] to %5d[%2s]) at channel %d at track %d", 
				HexToNote.toString(note), dividedDuration, startTimestamp, 
				((roundedStartTimestamp - startTimestamp) > 0 ? "+" : "") 
				+ (roundedStartTimestamp - startTimestamp), timestamp,
				((roundedStopTimestamp  - timestamp     ) > 0 ? "+" : "") 
				+ (roundedStopTimestamp  - timestamp      ), currentChannel, currentTrack)));
		offset += 2;
	}

	private int roundLength(int length) {
		int valueOfTheSmallestNote = 
				tempMidiFile.getDivisionOfABeat() / tempMidiFile.getNb32thNotesPerBeat() / 2;
		int delta = length % valueOfTheSmallestNote;
		if (delta == 0){
			return length;
		}
		if (delta > valueOfTheSmallestNote / 2) {
			delta -= valueOfTheSmallestNote;
		}
		return length - delta;
	}

	private void handleTextEvent() {
		offset -= 1;
		if (Log.infos() && Log.print(printOffset() + "Processing text event."));

		offset += 2;
		int textLength = readByte();
		offset += 1;
		String info = "Track " + currentTrack + ": " + readString(textLength);
		tempMidiFile.addTextInfo(info);
		if (Log.infos() && Log.print(printOffset() + "  * Event text: " + info + "."));
	}

	private void handlePolyphonicPressureChangeEvent() {
		if (Log.infos() && Log.print(printOffset() + "Skipping polyphonic pressure change event."));
		offset += 3;
	}

	private void handleControllerChangeEvent() {
		if (Log.infos() && Log.print(printOffset() + "Skipping controller change event."));
		offset += 3;
	}

	private void handleProgramChangeEvent() {
		if (Log.infos() && Log.print(printOffset() + "Processing instrument change event."));
		int track = readByte() % 0x10;
		offset += 1;
		int instr = readByte();
		tempMidiFile.setTrackInstrument(currentTrack, MIDIInstrument.values()[instr]);
		if (Parameters.NOTE_VISUALISATION) {
			MIDIFilePlayer.getPlayer().setInstrument(MIDIInstrument.values()[instr], track);
		}
		if (Log.infos() && Log.print(printOffset() + "  * Track " + track + " instrument "
				+ "changed to " + MIDIInstrument.values()[instr].name() + "."));
		offset += 1;
	}

	private void handleMIDIportEvent() {
		if (Log.infos() && Log.print(printOffset() + "Skipping MIDI port specification event."));
		offset += 3;
	}

	private void handleSetTempoMetaEvent() {
		if (Log.infos() && Log.print(printOffset() + "Processing tempo event."));

		offset += 2;
		int tempo = read3Bytes();
		tempMidiFile.setTempo(tempo);
		if (Log.infos() && Log.print(printOffset() + "  * Tempo changed to " + tempo + "."));
		offset += 3;
	}

	private void handleTimeSignatureMetaEvent() throws MIDIFileReaderException {
		if (Log.infos() && Log.print(printOffset() + "Processing time signature event."));

		offset += 2;
		int beatPerMeasure = readByte();
		tempMidiFile.setBeatPerMeasure(beatPerMeasure);
		if (Log.infos() && Log.print(printOffset() 
				+ "  * Beats per measure: " + beatPerMeasure + "."));
		offset += 1;

		int valueOfTheBeatNote = (int) Math.pow(2, readByte());
		tempMidiFile.setValueOfTheBeatNote(valueOfTheBeatNote);
		if (Log.infos() && Log.print(printOffset() 
				+ "  * Length of the beat note: " + valueOfTheBeatNote + "."));
		offset += 1;

		int clockTicksPerBeat = readByte();
		tempMidiFile.setClockTicksPerBeat(clockTicksPerBeat);
		if (Log.infos() && Log.print(printOffset() 
				+ "  * Clock ticks per beat: " + clockTicksPerBeat + "."));
		offset += 1;

		int nb32thNotesPerBeat = readByte();
		tempMidiFile.setNb32thNotesPerBeat(nb32thNotesPerBeat);
		if (nb32thNotesPerBeat != 8) {
			throw new MIDIFileReaderException(printOffset() + nb32thNotesPerBeat 
					+ " 32th notes per beat is not supported.");
		}
		if (Log.infos() && Log.print(printOffset() 
				+ "  * Number of 32th notes per beat: " + nb32thNotesPerBeat + "."));

		setValueOfShortestNote();
		if (Log.infos() && Log.print(printOffset() + "  * Value of shortest note set to " 
				+ tempMidiFile.getValueOfShortestNote() + "."));

		offset += 1;
	}

	private void handleKeySignatureMetaEvent() throws MIDIFileReaderException {
		if (Log.infos() && Log.print(printOffset() + "Processing key signature event."));
		offset += 2;

		int nbSharps = readByte();
		tempMidiFile.setNbSharps(nbSharps);
		if (nbSharps > 7) {
			throw new MIDIFileReaderException(printOffset() + "The key signature cannot contain " 
					+ nbSharps + " sharps. FLAT KEYS NOT YET SUPPORTED.");
		}
		if (Log.infos() && Log.print(printOffset() + "  * Number of sharps: " + nbSharps + "."));
		offset += 1;

		int majorOrMinor = readByte();
		tempMidiFile.setIfIsInMajorKey((majorOrMinor == 0) ? true : false);
		if (majorOrMinor != 0 && majorOrMinor != 1) {
			throw new MIDIFileReaderException(printOffset() 
					+ "The key signature must be major (0) or minor (1).");
		}
		if (Log.infos() && Log.print(printOffset() + "  * " 
				+ ((majorOrMinor == 0) ? "Major" : "Minor") + " key."));
		offset += 1;
	}


	//------------ STATUS ------------\\

	private boolean isAMetaEvent() {
		return readByte() == 0xff;
	}

	private boolean isAMIDIEvent() {
		return readByte() >= 0x80 && readByte() <= 0xcf;
	}

	private boolean endOfTrack() {
		return read3Bytes() == 0xff2f00 || readInt() == 0x00ff2f00;
	}

	private boolean endOfFile() {
		return currentTrack == tempMidiFile.getNotes().size();
	}


	//------------ READ BINARY ------------\\

	private int readByte() {
		return rawData[offset] & 0xFF;
	}

	private int readShort() {
		return rawData[offset]<<8 & 0xFF00 | rawData[offset + 1] & 0xFF;
	}

	private int read3Bytes() {
		int value = 0;
		for (int i = 0; i < 3; ++i) {
			value <<= 8;
			value |= (int)rawData[i + offset] & 0xFF;
		}
		return value;
	}

	private int readInt() {
		int value = 0;
		for (int i = 0; i < 4; ++i) {
			value <<= 8;
			value |= (int)rawData[i + offset] & 0xFF;
		}
		return value;
	}

	private String readString(int length){
		String fileString = "";
		for(int i = 0; i < length; i++) {
			fileString += (char) readByte();
			offset += 1;
		}
		return fileString;
	}

	private int readDeltaTime() {
		int deltaTime = 0;
		int timestampLength = 0;

		if (isAMetaEvent()) {
			return 0;
		}

		while (readByte() >= 0x80) {
			offset += 1;
			++timestampLength;
		}
		offset -= timestampLength;

		for (int i = 0; i < timestampLength; ++i){
			deltaTime += (readByte() - 128) * Math.pow(2, 7 * (timestampLength - i));
			offset += 1;
		}
		deltaTime += readByte();
		offset += 1;

		return deltaTime;
	}

	private int firstHexDigit() {
		return readByte() - (readByte() % 0x10);
	}

	private int secondHexDigit() {
		return readByte() % 0x10;
	}


	//------------ OTHERS ------------\\

	private String printOffset() {
		return String.format("[OFFSET %4d] ", offset);
	}

	private void printErrorSummary() {
		if (Log.infos() && Log.title("Error Summary", 0));

		for (int trackNo = 0; trackNo < tempMidiFile.getNbTracks(); ++trackNo) {
			if (tempMidiFile.getNotes().get(trackNo).size() != 0) {
				if (Log.infos() && Log.title("Track no." + trackNo, 1));
				errors.get(trackNo).setTotalTimestamp(tempMidiFile.getFinalTimestamp());
				errors.get(trackNo).print();
			}
		}
	}

	private void verifyChannel() throws MIDIFileReaderException {
		int channelNo = secondHexDigit();
		if (currentChannel == -1) {
			currentChannel = channelNo;
		} else if (currentChannel != channelNo) {
			throw new MIDIFileReaderException(printOffset() + "A noteOn event on "
					+ "channel " + channelNo + " occured while all "
					+ "the others where aiming channel " + currentChannel + ".");
		}
	}

	private void setValueOfShortestNote() {
		double smallestIntegDivOfBeatNoteValue = tempMidiFile.getDivisionOfABeat();
		while ((smallestIntegDivOfBeatNoteValue / 2 == (int) smallestIntegDivOfBeatNoteValue / 2)) {
			smallestIntegDivOfBeatNoteValue /= 2;
		}
		tempMidiFile.setValueOfShortestNote((int)smallestIntegDivOfBeatNoteValue);
	}

	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
