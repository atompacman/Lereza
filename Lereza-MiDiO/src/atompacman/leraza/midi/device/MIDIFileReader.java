package atompacman.leraza.midi.device;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.Parameters;
import atompacman.leraza.midi.container.MIDIFile;
import atompacman.leraza.midi.container.MIDIFileErrorSummary;
import atompacman.leraza.midi.container.MIDINote;
import atompacman.leraza.midi.exception.MIDIFileException;
import atompacman.leraza.midi.utilities.Formatting;
import atompacman.leraza.midi.utilities.HexToNote;
import atompacman.leraza.midi.utilities.Instrument;

public class MIDIFileReader {

	private MIDIFile midiFile;

	private byte[] rawData;
	private int offset;
	private int currentTrack;
	private int currentChannel;
	private int timestamp;
	private boolean runningStatusOn;

	private Map<Integer, MIDIFileErrorSummary> errors;
	private Map<Integer, Integer> noteBuffer;
	private MIDIFilePlayer midiFilePlayer;


	//////////////////////////////
	//        READ FILE         //
	//////////////////////////////
	/**
	 * Reads a <i>.midi</i> file on disk and generates a {@link MIDIFile}, which contains
	 * tracks of {@link MIDINote} and information about how to play them.
	 * <p>
	 * @param filePath | A string containing the path to a <i>.midi</i> file
	 * 
	 * @return A {@link MIDIFile} structure
	 */
	public MIDIFile readFile(String filePath) {
		Log.infos(Formatting.lineSeparation("MIDIFileReader", 0));
		Log.infos(Formatting.lineSeparation("Loading file at: " + filePath, 1));

		reset();

		try {
			this.midiFile = new MIDIFile(filePath);
			this.rawData = loadBinaryFile(filePath);

			if (this.rawData.length > Parameters.MAX_FILE_SIZE) {
				throw new MIDIFileException ("Size of selected file exceeds " + Parameters.MAX_FILE_SIZE / 1000 + " KB.");
			}
			Log.infos("File at \"" + filePath + "\" loaded in memory (" + this.rawData.length / 1000 + " KB).");

			readBinaryFile();
		} catch (MIDIFileException e){
			e.printStackTrace();
		}
		Log.infos(Formatting.lineSeparation(0));
		printErrorSummary();
		Log.infos(Formatting.lineSeparation(1));
		Log.infos(Formatting.lineSeparation(0));

		return this.midiFile;
	}

	private byte[] loadBinaryFile(String filePath) throws MIDIFileException {
		File file = new File(filePath);
		byte[] rawFile = new byte[(int)file.length()];

		try {
			FileInputStream fis = new FileInputStream(file);
			fis.read(rawFile);
			fis.close();
		} catch (FileNotFoundException e) {
			throw new MIDIFileException("ERROR: File not found at \"" + filePath + "\".");
		} catch (IOException e) {
			throw new MIDIFileException("ERROR: IOException at \"" + filePath + "\".");
		}
		return rawFile;
	}

	private void readBinaryFile() throws MIDIFileException {
		readFileHeader();

		while (!endOfFile()) {
			readTrack();
			++currentTrack;
			if (timestamp > midiFile.getFinalTimestamp()) {
				midiFile.setFinalTimestamp(timestamp);
			}
			timestamp = 0;
		}
	}

	private void readFileHeader() throws MIDIFileException {
		Log.infos(printOffset() + "Reading header.");

		if (readInt() != 0x4d546864) {
			throw new MIDIFileException(printOffset() + "ERROR : The selected file is not of MIDI format.");
		}
		offset += 8;

		int midiType = readShort();
		if (midiType != 1){
			throw new MIDIFileException(printOffset() + "ERROR : MIDI type " + midiType + " is not supported.");
		}
		midiFile.setMidiType(midiType);
		Log.infos(printOffset() + "  * MIDI File type: " + midiType + ".");
		offset += 2;

		int nbTracks = readShort();
		midiFile.setNbTracks(nbTracks);
		Log.infos(printOffset() + "  * Number of tracks: " + nbTracks + ".");
		for (int i = 0; i < nbTracks; ++i) {
			//midiFile.getNotes().add(new HashMap<Integer, Stack<MIDINote>>());
		}
		offset += 2;

		int divisionOfABeat = readShort();
		midiFile.setDivisionOfABeat(divisionOfABeat);
		Log.infos(printOffset() + "  * Number of divisions of a beat: " + divisionOfABeat + ".");
		offset += 2;
	}

	private void readTrack() throws MIDIFileException {
		Log.infos(Formatting.lineSeparation("Reading track " + currentTrack, 1));
		errors.put(currentTrack, new MIDIFileErrorSummary());	

		readTrackHeader();
		int deltaTime = readDeltaTime();

		while (!endOfTrack()) {
			handleEvent(deltaTime);
			deltaTime = readDeltaTime();
		}
		Log.infos(printOffset() + "End of Track no." + currentTrack + ": " + midiFile.getNotes().get(currentTrack).size() + " notes read.");
		currentChannel = -1;
		offset += 3;
	}

	private void readTrackHeader() throws MIDIFileException {
		if (readInt() != 0x4d54726b) {
			throw new MIDIFileException(printOffset() + "ERROR : Error in the track header.");
		}
		offset += 8;
	}


	//////////////////////////////
	//      EVENT HANDLING      //
	//////////////////////////////

	private void handleEvent(int deltaTime) throws MIDIFileException {
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
			throw new MIDIFileException(printOffset() + "ERROR: Unknown event with Id \"" + readByte() + "\".");
		}
	}

	private void handleMetaEvent() throws MIDIFileException {
		switch(readByte()) {
		case 0x01: case 0x02: case 0x03: case 0x04: case 0x05: case 0x06: case 0x07: handleTextEvent(); return;
		case 0x21: handleMIDIportEvent(); return;
		case 0x51: handleSetTempoMetaEvent(); return;
		case 0x58: handleTimeSignatureMetaEvent(); return;
		case 0x59: handleKeySignatureMetaEvent(); return;
		default: throw new MIDIFileException(printOffset() + "ERROR: Unknown meta event with Id \"" + readByte() + "\".");
		}
	}

	private void handleMIDIEvent(int deltaTime) throws MIDIFileException {
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
		default: throw new MIDIFileException(printOffset() + "ERROR: Unknown midi event with Id \"" + readByte() + "\".");
		}
	}

	private void handleNoteOnEvent(int deltaTime) throws MIDIFileException {
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
				midiFilePlayer.startNote(note);
			}
		}
		offset += 2;
	}

	private void handleNoteOffEvent(int deltaTime) throws MIDIFileException {
		if (Parameters.NOTE_VISUALISATION) {
			sleep((int)(deltaTime * Parameters.VISUALISATION_SPEED_CORRECTION));
		}
		if (timestamp == 0) {
			throw new MIDIFileException(printOffset() + "ERROR: A noteOff event came before any noteOn event.");
		}
		timestamp += deltaTime;

		int note = readByte();
		Integer startTimestamp = noteBuffer.get(note);
		noteBuffer.remove(note);

		if (startTimestamp == null) {
			throw new MIDIFileException(printOffset() + "ERROR: Stopping a note that has never been started.");
		}
		int noteDuration = roundLength(timestamp - startTimestamp);
		if (midiFile.getNotes().get(currentTrack).get(startTimestamp) == null) {
			midiFile.getNotes().get(currentTrack).put(startTimestamp, new Stack<MIDINote>());
		}
		midiFile.getNotes().get(currentTrack).get(startTimestamp).push(new MIDINote(note, noteDuration));
		if (noteDuration == 0) {
			errors.get(currentTrack).incrementNoteLengthZeroCount();
		}
		if (Parameters.NOTE_VISUALISATION) {
			if (Parameters.NOTE_PLAY_AUDIO) {
				midiFilePlayer.stopNote(note);
			}
		}
		Log.extra(printOffset() + "[ Adding note ] " + String.format("%-3s of length %3d at channel %d at track %d", HexToNote.toString(note), 
				noteDuration, currentChannel, currentTrack));

		offset += 2;
	}

	private int roundLength(int length) throws MIDIFileException {
		int valueOfTheSmallestNote = midiFile.getDivisionOfABeat() / midiFile.getNb32thNotesPerBeat() / 2;
		int delta = length % valueOfTheSmallestNote;
		if (delta == 0){
			return length;
		}
		if (delta > valueOfTheSmallestNote / 2) {
			delta -= valueOfTheSmallestNote;
		}
		if (Math.abs(delta) > Parameters.NOTE_LENGTH_CORRECTION_THRESHOLD) {
			errors.get(currentTrack).incrementRoundingOverThresholdCount();
			Log.warng(printOffset() + "[Note rounding] A note of length " + length 
					+ " had to be EXCESSIVELY rounded of a value of " + delta + ".");
		}
		errors.get(currentTrack).adjustNoteRoundingTotalOffset(delta);
		return length - delta;
	}

	private void handleTextEvent() {
		offset -= 1;
		Log.infos(printOffset() + "Processing text event.");

		offset += 2;
		int textLength = readByte();
		offset += 1;
		String info = "Track " + currentTrack + ": " + readString(textLength);
		midiFile.addInfo(info);
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
		midiFile.setTrackInstrument(currentTrack, Instrument.values()[instr]);
		midiFilePlayer.setInstrument(Instrument.values()[instr]);
		Log.infos(printOffset() + "  * Track " + track + " instrument changed to " + Instrument.values()[instr].name() + ".");
		offset += 1;
	}

	private void handleMIDIportEvent() {
		Log.infos(printOffset() + "Skipping MIDI port specification event.");
		offset += 3;
	}

	private void handleSetTempoMetaEvent() throws MIDIFileException {
		Log.infos(printOffset() + "Processing tempo event.");

		offset += 2;
		int tempo = read3Bytes();
		midiFile.setTempo(tempo);
		Log.infos(printOffset() + "  * Tempo changed to " + tempo + ".");
		offset += 3;
	}

	private void handleTimeSignatureMetaEvent() throws MIDIFileException {
		Log.infos(printOffset() + "Processing time signature event.");

		offset += 2;
		int beatPerMeasure = readByte();
		midiFile.setBeatPerMeasure(beatPerMeasure);
		Log.infos(printOffset() + "  * Beats per measure: " + beatPerMeasure + ".");
		offset += 1;

		int valueOfTheBeatNote = (int) Math.pow(2, readByte());
		midiFile.setValueOfTheBeatNote(valueOfTheBeatNote);
		Log.infos(printOffset() + "  * Length of the beat note: " + valueOfTheBeatNote + ".");
		offset += 1;

		int clockTicksPerBeat = readByte();
		midiFile.setClockTicksPerBeat(clockTicksPerBeat);
		Log.infos(printOffset() + "  * Clock ticks per beat: " + clockTicksPerBeat + ".");
		offset += 1;

		int nb32thNotesPerBeat = readByte();
		midiFile.setNb32thNotesPerBeat(nb32thNotesPerBeat);
		if (nb32thNotesPerBeat != 8) {
			throw new MIDIFileException(printOffset() + "ERROR : "+ nb32thNotesPerBeat + " 32th notes per beat is not supported.");
		}
		Log.infos(printOffset() + "  * Number of 32th notes per beat: " + nb32thNotesPerBeat + ".");
		offset += 1;
	}

	private void handleKeySignatureMetaEvent() throws MIDIFileException {
		Log.infos(printOffset() + "Processing key signature event.");
		offset += 2;

		int nbSharps = readByte();
		midiFile.setNbSharps(nbSharps);
		if (nbSharps > 7) {
			throw new MIDIFileException(printOffset() + "ERROR : The key signature cannot contain "+ nbSharps + " sharps. FLAT KEYS NOT YET SUUPORTED.");
		}
		Log.infos(printOffset() + "  * Number of sharps: " + nbSharps + ".");
		offset += 1;

		int majorOrMinor = readByte();
		midiFile.setIfIsInMajorKey((majorOrMinor == 0) ? true : false);
		if (majorOrMinor != 0 && majorOrMinor != 1) {
			throw new MIDIFileException(printOffset() + "ERROR : The key signature must be major (0) or minor (1).");
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
		return currentTrack == midiFile.getNotes().size();
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

		for (int trackNo = 0; trackNo < midiFile.getNbTracks(); ++trackNo) {
			if (midiFile.getNotes().get(trackNo).size() != 0) {
				Log.infos(Formatting.lineSeparation("Track no." + trackNo, 1));
				errors.get(trackNo).setTotalTimestamp(midiFile.getFinalTimestamp());
				errors.get(trackNo).print();
			}
		}
	}

	private void verifyChannel() throws MIDIFileException {
		if (currentChannel == -1) {
			currentChannel = readByte() % 0x10;
		} else if (currentChannel != readByte() % 0x10) {
			throw new MIDIFileException(printOffset() + "ERROR: A noteOn event aiming channel " + readByte() % 0x10 + " occured while all the others where "
					+ " aiming channel " + currentChannel + ".");
		}
	}


	//////////////////////////////
	//          RESET           //
	//////////////////////////////

	public void reset() {
		this.midiFile = null;
		this.rawData = null;
		this.offset = 0;
		this.currentTrack = 0;
		this.currentChannel = -1;
		this.timestamp = 0;
		this.runningStatusOn = false;
		this.errors = new HashMap<Integer, MIDIFileErrorSummary>();
		this.noteBuffer = new HashMap<Integer, Integer>();
		if (Parameters.NOTE_PLAY_AUDIO) {
			this.midiFilePlayer = new MIDIFilePlayer();
			this.midiFilePlayer.initialize();
		}
	}
}
