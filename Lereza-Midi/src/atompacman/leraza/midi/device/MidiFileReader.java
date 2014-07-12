package atompacman.leraza.midi.device;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.MidiFileManager;
import atompacman.leraza.midi.Parameters;
import atompacman.leraza.midi.api.MidiFileReaderAPI;
import atompacman.leraza.midi.container.MidiInstrument;
import atompacman.leraza.midi.container.MidiFile;
import atompacman.leraza.midi.container.MidiFileErrorSummary;
import atompacman.leraza.midi.container.MidiNote;
import atompacman.leraza.midi.exception.MidiFileException;
import atompacman.leraza.midi.utils.HexToNote;
import atompacman.lereza.common.formatting.Formatting;

public class MidiFileReader implements MidiFileReaderAPI {

	private MidiFile tempMidiFile;
	private Map<Integer, MidiFileErrorSummary> errors;

	private Map<String, MidiFile> midiFiles;

	private byte[] rawData;
	private int offset;
	private int currentTrack;
	private int currentChannel;
	private int timestamp;
	private boolean runningStatusOn;
	private Map<Integer, Integer> noteBuffer;


	//////////////////////////////
	//        READ FILE         //
	//////////////////////////////

	public void read(String filePath) {
		Log.infos(Formatting.lineSeparation("MIDIFileReader", 0));
		Log.infos(Formatting.lineSeparation("Loading file at: " + filePath, 1));

		reset();

		try {
			this.tempMidiFile = new MidiFile(filePath);
			this.rawData = loadBinaryFile(filePath);

			if (this.rawData.length > Parameters.MAX_FILE_SIZE) {
				throw new MidiFileException ("Size of selected file exceeds " + Parameters.MAX_FILE_SIZE / 1000 + " KB.");
			}
			Log.infos("File at \"" + filePath + "\" loaded in memory (" + this.rawData.length / 1000 + " KB).");

			readBinaryFile();
		} catch (MidiFileException e){
			e.printStackTrace();
		}
		printErrorSummary();
		Log.infos(Formatting.lineSeparation(1));

		midiFiles.put(filePath, tempMidiFile);
	}

	private byte[] loadBinaryFile(String filePath) throws MidiFileException {
		File file = new File(filePath);
		byte[] rawFile = new byte[(int)file.length()];

		try {
			FileInputStream fis = new FileInputStream(file);
			fis.read(rawFile);
			fis.close();
		} catch (FileNotFoundException e) {
			throw new MidiFileException("ERROR: File not found at \"" + filePath + "\".");
		} catch (IOException e) {
			throw new MidiFileException("ERROR: IOException at \"" + filePath + "\".");
		}
		return rawFile;
	}

	private void readBinaryFile() throws MidiFileException {
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

	private void readFileHeader() throws MidiFileException {
		Log.infos(printOffset() + "Reading header.");

		if (readInt() != 0x4d546864) {
			throw new MidiFileException(printOffset() + "ERROR : The selected file is not of MIDI format.");
		}
		offset += 8;

		int midiType = readShort();
		if (midiType != 1){
			throw new MidiFileException(printOffset() + "ERROR : MIDI type " + midiType + " is not supported.");
		}
		tempMidiFile.setMidiType(midiType);
		Log.infos(printOffset() + "  * MIDI File type: " + midiType + ".");
		offset += 2;

		int nbTracks = readShort();
		tempMidiFile.setNbTracks(nbTracks);
		Log.infos(printOffset() + "  * Number of tracks: " + nbTracks + ".");
		for (int i = 0; i < nbTracks; ++i) {
			//midiFile.getNotes().add(new HashMap<Integer, Stack<MIDINote>>());
		}
		offset += 2;

		int divisionOfABeat = readShort();
		tempMidiFile.setDivisionOfABeat(divisionOfABeat);
		Log.infos(printOffset() + "  * Number of divisions of a beat: " + divisionOfABeat + ".");
		offset += 2;
	}

	private void readTrack() throws MidiFileException {
		Log.infos(Formatting.lineSeparation("Reading track " + currentTrack, 1));
		errors.put(currentTrack, new MidiFileErrorSummary());	

		readTrackHeader();
		int deltaTime = readDeltaTime();

		while (!endOfTrack()) {
			handleEvent(deltaTime);
			deltaTime = readDeltaTime();
		}
		Log.infos(printOffset() + "End of Track no." + currentTrack + ": " + tempMidiFile.getNotes().get(currentTrack).size() + " notes read.");
		currentChannel = -1;
		offset += 3;
	}

	private void readTrackHeader() throws MidiFileException {
		if (readInt() != 0x4d54726b) {
			throw new MidiFileException(printOffset() + "ERROR : Error in the track header.");
		}
		offset += 8;
	}


	//////////////////////////////
	//      EVENT HANDLING      //
	//////////////////////////////

	private void handleEvent(int deltaTime) throws MidiFileException {
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
			throw new MidiFileException(printOffset() + "ERROR: Unknown event with Id \"" + readByte() + "\".");
		}
	}

	private void handleMetaEvent() throws MidiFileException {
		switch(readByte()) {
		case 0x01: case 0x02: case 0x03: case 0x04: case 0x05: case 0x06: case 0x07: handleTextEvent(); return;
		case 0x21: handleMIDIportEvent(); return;
		case 0x51: handleSetTempoMetaEvent(); return;
		case 0x58: handleTimeSignatureMetaEvent(); return;
		case 0x59: handleKeySignatureMetaEvent(); return;
		default: throw new MidiFileException(printOffset() + "ERROR: Unknown meta event with Id \"" + readByte() + "\".");
		}
	}

	private void handleMIDIEvent(int deltaTime) throws MidiFileException {
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
		default: throw new MidiFileException(printOffset() + "ERROR: Unknown midi event with Id \"" + readByte() + "\".");
		}
	}

	private void handleNoteOnEvent(int deltaTime) throws MidiFileException {
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
			Log.vital(noteSpacing.toString() + HexToNote.toString(note));
			if (Parameters.NOTE_PLAY_AUDIO) {
				MidiFileManager.player.startNote(note);
			}
		}
		offset += 2;
	}

	private void handleNoteOffEvent(int deltaTime) throws MidiFileException {
		if (Parameters.NOTE_VISUALISATION) {
			sleep((int)(deltaTime * Parameters.VISUALISATION_SPEED_CORRECTION));
		}
		if (timestamp == 0) {
			throw new MidiFileException(printOffset() + "ERROR: A noteOff event came before any noteOn event.");
		}
		timestamp += deltaTime;

		int note = readByte();

		if (noteBuffer.get(note) == null) {
			throw new MidiFileException(printOffset() + "ERROR: Stopping a note that has never been started.");
		}
		Integer startTimestamp = noteBuffer.get(note);
		Integer roundedStartTimestamp = roundLength(startTimestamp);
		Integer roundedStopTimestamp = roundLength(timestamp);
		int noteDuration = roundedStopTimestamp - roundedStartTimestamp;
		noteBuffer.remove(note);
		
		int deltaLength = noteDuration - (timestamp - startTimestamp);
		if (Math.abs(deltaLength) > Parameters.NOTE_LENGTH_CORRECTION_THRESHOLD) {
			errors.get(currentTrack).incrementRoundingOverThresholdCount();
			Log.warng(printOffset() + "[Note rounding] A note of length " + noteDuration 
					+ " had to be EXCESSIVELY rounded of a value of " + deltaLength + ".");
		}
		errors.get(currentTrack).adjustNoteRoundingTotalOffset(deltaLength);

		int dividedTimestamp = (int)((double)roundedStartTimestamp / (double)tempMidiFile.getValueOfShortestNote());
		int dividedDuration = (int)((double)noteDuration / (double)tempMidiFile.getValueOfShortestNote()); 
		
		if (tempMidiFile.getNotes().get(currentTrack).get(dividedTimestamp) == null) {
			tempMidiFile.getNotes().get(currentTrack).put(dividedTimestamp, new Stack<MidiNote>());
		}

		tempMidiFile.getNotes().get(currentTrack).get(dividedTimestamp).push(new MidiNote(note, dividedDuration));
		
		if (dividedDuration == 0) {
			errors.get(currentTrack).incrementNoteLengthZeroCount();
		}
		if (Parameters.NOTE_VISUALISATION) {
			if (Parameters.NOTE_PLAY_AUDIO) {
				MidiFileManager.player.stopNote(note);
			}
		}
		Log.extra(printOffset() + "[ Adding note ] " + String.format("%-3s of length %3d (timestamps: %5d[%2s] to %5d[%2s]) at channel %d at track %d", 
				HexToNote.toString(note), dividedDuration, startTimestamp, 
				((roundedStartTimestamp - startTimestamp) > 0 ? "+" : "") + (roundedStartTimestamp - startTimestamp), timestamp,
				((roundedStopTimestamp  - timestamp     ) > 0 ? "+" : "") + (roundedStopTimestamp  - timestamp      ), currentChannel, currentTrack));
		offset += 2;
	}

	private int roundLength(int length) throws MidiFileException {
		int valueOfTheSmallestNote = tempMidiFile.getDivisionOfABeat() / tempMidiFile.getNb32thNotesPerBeat() / 2;
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
		Log.infos(printOffset() + "Processing text event.");

		offset += 2;
		int textLength = readByte();
		offset += 1;
		String info = "Track " + currentTrack + ": " + readString(textLength);
		tempMidiFile.addInfo(info);
		Log.infos(printOffset() + "  * Event text: " + info + ".");
	}

	private void handlePolyphonicPressureChangeEvent() {
		Log.infos(printOffset() + "Skipping polyphonic pressure change event.");
		offset += 3;
	}

	private void handleControllerChangeEvent() {
		Log.infos(printOffset() + "Skipping controller change event.");
		offset += 3;
	}

	private void handleProgramChangeEvent() {
		Log.infos(printOffset() + "Processing program (instrument) change event.");
		int track = readByte() % 0x10;
		offset += 1;
		int instr = readByte();
		tempMidiFile.setTrackInstrument(currentTrack, MidiInstrument.values()[instr]);
		MidiFileManager.player.setInstrument(MidiInstrument.values()[instr], track);
		Log.infos(printOffset() + "  * Track " + track + " instrument changed to " + MidiInstrument.values()[instr].name() + ".");
		offset += 1;
	}

	private void handleMIDIportEvent() {
		Log.infos(printOffset() + "Skipping MIDI port specification event.");
		offset += 3;
	}

	private void handleSetTempoMetaEvent() throws MidiFileException {
		Log.infos(printOffset() + "Processing tempo event.");

		offset += 2;
		int tempo = read3Bytes();
		tempMidiFile.setTempo(tempo);
		Log.infos(printOffset() + "  * Tempo changed to " + tempo + ".");
		offset += 3;
	}

	private void handleTimeSignatureMetaEvent() throws MidiFileException {
		Log.infos(printOffset() + "Processing time signature event.");

		offset += 2;
		int beatPerMeasure = readByte();
		tempMidiFile.setBeatPerMeasure(beatPerMeasure);
		Log.infos(printOffset() + "  * Beats per measure: " + beatPerMeasure + ".");
		offset += 1;

		int valueOfTheBeatNote = (int) Math.pow(2, readByte());
		tempMidiFile.setValueOfTheBeatNote(valueOfTheBeatNote);
		Log.infos(printOffset() + "  * Length of the beat note: " + valueOfTheBeatNote + ".");
		offset += 1;

		int clockTicksPerBeat = readByte();
		tempMidiFile.setClockTicksPerBeat(clockTicksPerBeat);
		Log.infos(printOffset() + "  * Clock ticks per beat: " + clockTicksPerBeat + ".");
		offset += 1;

		int nb32thNotesPerBeat = readByte();
		tempMidiFile.setNb32thNotesPerBeat(nb32thNotesPerBeat);
		if (nb32thNotesPerBeat != 8) {
			throw new MidiFileException(printOffset() + "ERROR : "+ nb32thNotesPerBeat + " 32th notes per beat is not supported.");
		}
		Log.infos(printOffset() + "  * Number of 32th notes per beat: " + nb32thNotesPerBeat + ".");
		
		setValueOfShortestNote();
		Log.infos(printOffset() + "  * Value of shortest note set to " + tempMidiFile.getValueOfShortestNote() + ".");
		
		offset += 1;
	}

	private void handleKeySignatureMetaEvent() throws MidiFileException {
		Log.infos(printOffset() + "Processing key signature event.");
		offset += 2;

		int nbSharps = readByte();
		tempMidiFile.setNbSharps(nbSharps);
		if (nbSharps > 7) {
			throw new MidiFileException(printOffset() + "ERROR : The key signature cannot contain "+ nbSharps + " sharps. FLAT KEYS NOT YET SUUPORTED.");
		}
		Log.infos(printOffset() + "  * Number of sharps: " + nbSharps + ".");
		offset += 1;

		int majorOrMinor = readByte();
		tempMidiFile.setIfIsInMajorKey((majorOrMinor == 0) ? true : false);
		if (majorOrMinor != 0 && majorOrMinor != 1) {
			throw new MidiFileException(printOffset() + "ERROR : The key signature must be major (0) or minor (1).");
		}
		Log.infos(printOffset() + "  * " + ((majorOrMinor == 0) ? "Major" : "Minor") + " key.");
		offset += 1;
	}


	//////////////////////////////
	//      STATUS CHECKER      //
	//////////////////////////////

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


	//////////////////////////////
	//       READ BINARY        //
	//////////////////////////////

	private int readByte() {
		return rawData[offset] & 0xFF;
	}

	private int readShort() {
		return rawData[offset]<<8 & 0xFF00 | rawData[offset + 1] & 0xFF;
	}

	private int read3Bytes() {
		int ret = 0;
		for (int i = 0; i < 3; ++i) {
			ret <<= 8;
			ret |= (int)rawData[i + offset] & 0xFF;
		}
		return ret;
	}

	private int readInt() {
		int ret = 0;
		for (int i = 0; i < 4; ++i) {
			ret <<= 8;
			ret |= (int)rawData[i + offset] & 0xFF;
		}
		return ret;
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
			deltaTime += (readByte() - 128) * Math.pow(2,7 * (timestampLength - i));
			offset += 1;
		}
		deltaTime += readByte();
		offset += 1;

		return deltaTime;
	}

	private int firstHexDigit() {
		return readByte() - (readByte() % 0x10);
	}

	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	//////////////////////////////
	//         OTHERS           //
	//////////////////////////////

	private String printOffset() {
		return String.format("[OFFSET %4d] ", offset);
	}

	private void printErrorSummary() {
		Log.infos(Formatting.lineSeparation("Error Summary", 0));

		for (int trackNo = 0; trackNo < tempMidiFile.getNbTracks(); ++trackNo) {
			if (tempMidiFile.getNotes().get(trackNo).size() != 0) {
				Log.infos(Formatting.lineSeparation("Track no." + trackNo, 1));
				errors.get(trackNo).setTotalTimestamp(tempMidiFile.getFinalTimestamp());
				errors.get(trackNo).print();
			}
		}
	}

	private void verifyChannel() throws MidiFileException {
		if (currentChannel == -1) {
			currentChannel = readByte() % 0x10;
		} else if (currentChannel != readByte() % 0x10) {
			throw new MidiFileException(printOffset() + "ERROR: A noteOn event aiming channel " + readByte() % 0x10 + " occured while all the others where "
					+ " aiming channel " + currentChannel + ".");
		}
	}

	private void setValueOfShortestNote() {
		double smallestIntegerDivisionOfBeatNoteValue = tempMidiFile.getDivisionOfABeat();
		while ((smallestIntegerDivisionOfBeatNoteValue / 2 == (int) smallestIntegerDivisionOfBeatNoteValue / 2)) {
			smallestIntegerDivisionOfBeatNoteValue /= 2;
		}
		tempMidiFile.setValueOfShortestNote((int)smallestIntegerDivisionOfBeatNoteValue);
	}

	//////////////////////////////
	//          RESET           //
	//////////////////////////////

	private void reset() {
		this.tempMidiFile = null;
		this.rawData = null;
		this.offset = 0;
		this.currentTrack = 0;
		this.currentChannel = -1;
		this.timestamp = 0;
		this.runningStatusOn = false;
		this.errors = new HashMap<Integer, MidiFileErrorSummary>();
		this.midiFiles = new HashMap<String, MidiFile>();
		this.noteBuffer = new HashMap<Integer, Integer>();
	}


	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////

	public MidiFile getMidiFile(String filePath) {
		return midiFiles.get(filePath);
	}
}
