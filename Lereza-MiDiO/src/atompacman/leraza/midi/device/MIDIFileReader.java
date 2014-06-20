package atompacman.leraza.midi.device;

import java.io.File;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.MiDiO;
import atompacman.leraza.midi.Parameters;
import atompacman.leraza.midi.container.MIDIFile;
import atompacman.leraza.midi.container.MIDINote;
import atompacman.leraza.midi.exception.MIDIFileException;
import atompacman.leraza.midi.utilities.HexToNote;

public class MIDIFileReader {

	private byte[] rawFile;
	private int offset;
	private int currentTrack;

	private MIDIFile midiFile;

	
	//////////////////////////////
	//       CONSTRUCTOR        //
	//////////////////////////////
	
	public MIDIFileReader(byte[] rawFile, File file) throws MIDIFileException {
		this.rawFile = rawFile;
		this.offset = 0;
		this.currentTrack = 0;
		this.midiFile = new MIDIFile(file);
	}

	
	//////////////////////////////
	//        READ FILE         //
	//////////////////////////////
	
	public MIDIFile readFile() throws MIDIFileException {
		readHeader();
		
		offset = 14;
		while (!endOfFile()) {
			readTrack();
			offset += 3;
		}
		return midiFile;
	}

	private void readHeader() throws MIDIFileException {
		Log.normalMsg(printOffset() + "Reading header.");

		if (readInt() != 0x4d546864) {
			throw new MIDIFileException(printOffset() + "ERROR : The selected file is not of MIDI format.");
		}
		
		offset += 8;
		int midiType = readShort();
		if (midiType != 1){
			throw new MIDIFileException(printOffset() + "ERROR : MIDI type " + midiType + " is not supported.");
		}
		midiFile.setMidiType(midiType);
		Log.normalMsg(printOffset() + "  * File type: " + midiType + ".");
		
		offset += 2;
		int nbTracks = readShort();
		midiFile.setNbTracks(nbTracks);
		Log.normalMsg(printOffset() + "  * Number of tracks: " + nbTracks + ".");
		
		offset += 2;
		int divisionOfABeat = readShort();
		midiFile.setDivisionOfABeat(divisionOfABeat);
		Log.normalMsg(printOffset() + "  * Number of divisions of a beat: " + divisionOfABeat + ".");
	}

	private void readTrack() throws MIDIFileException {
		Log.normalMsg(printOffset() + "= = = = = = = = = = = = = Reading track " + currentTrack + " = = = = = = = = = = = = = =");
		int deltaTime = 0;
		
		readTrackHeader();
		deltaTime = readEventDeltaTime();
		
		while (!endOfTrack() && !isANoteStart()) {
			handleMetaEvent();
			deltaTime = readEventDeltaTime();
		}
		if (!endOfTrack()) {
			midiFile.setTimeBeforeFirstNote(deltaTime, currentTrack);
		}
		while (!endOfTrack()) {
			handleNoteEvent();
		}
		Log.normalMsg(printOffset() + "End of Track no." + currentTrack + ": " + midiFile.getNotes().get(currentTrack).size() + " notes read.");
		++currentTrack;
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
	
	private void handleMetaEvent() throws MIDIFileException {
		offset += 1;
		
		switch(readByte()) {
		case 0x01: handleTextEvent(); break;
		case 0x51: handleSetTempoMetaEvent(); break;
		case 0x58: handleTimeSignatureMetaEvent(); break;
		default: throw new MIDIFileException(printOffset() + "ERROR: Unknown meta event with Id \"" + readByte() + ".");
		}
	}

	private void handleTextEvent() {
		offset -= 1;
		Log.normalMsg(printOffset() + "Processing text event.");

		offset += 2;
		int textLength = readByte();
		offset += 1;
		String info = "Track " + currentTrack + ": " + readString(textLength);
		midiFile.addInfo(info);
		Log.normalMsg(printOffset() + "  * Event text: " + info + ".");
	}

	private void handleSetTempoMetaEvent() throws MIDIFileException {
		Log.normalMsg(printOffset() + "Processing tempo event.");

		offset += 2;
		int tempo = read3Bytes();
		midiFile.setTempo(tempo);
		offset += 3;
		Log.normalMsg(printOffset() + "  * Tempo changed to " + tempo + ".");
	}

	private void handleTimeSignatureMetaEvent() throws MIDIFileException {
		Log.normalMsg(printOffset() + "Processing time signature event.");

		offset += 2;
		int beatPerMeasure = readByte();
		midiFile.setBeatPerMeasure(beatPerMeasure);
		Log.normalMsg(printOffset() + "  * Beats per measure: " + beatPerMeasure + ".");
		offset += 1;
		
		int valueOfTheBeatNote = (int) Math.pow(2, readByte());
		midiFile.setValueOfTheBeatNote(valueOfTheBeatNote);
		Log.normalMsg(printOffset() + "  * Length of the beat note: " + valueOfTheBeatNote + ".");
		offset += 1;
		
		int clockTicksPerBeat = readByte();
		midiFile.setClockTicksPerBeat(clockTicksPerBeat);
		Log.normalMsg(printOffset() + "  * Clock ticks per beat: " + clockTicksPerBeat + ".");
		offset += 1;
		
		int nb32thNotesPerBeat = readByte();
		midiFile.setNb32thNotesPerBeat(nb32thNotesPerBeat);
		if (nb32thNotesPerBeat != 8) {
			throw new MIDIFileException(printOffset() + "ERROR : "+ nb32thNotesPerBeat + " 32th notes per beat is not supported.");
		}
		offset += 1;
		Log.normalMsg(printOffset() + "  * Number of 32th notes per beat: " + nb32thNotesPerBeat + ".");
	}

	private void handleNoteEvent() throws MIDIFileException {
		int channel = readByte() % 0x10;
		
		offset += 1;
		int note = readByte();
		offset += 2;
		MIDINote rest = new MIDINote(0, 0);
		
		int noteLength = readNoteAndRestDeltaTime(rest);
		int rawLength = noteLength;
		noteLength = roundLength(noteLength);
		
		if (rawLength == 0) {
			Log.warningMsg(printOffset() + "[RAW LENGTH 0 ] Adding a note of RAW length 0 (" + HexToNote.toString(note) + ").");
		}
		if (noteLength == 0) {
			Log.essentialMsg(printOffset() + "[Note length 0] Adding a note of length 0 (" + HexToNote.toString(note) + ").");
		}
		
		midiFile.getNotes().get(currentTrack).add(new MIDINote(note, noteLength));
		int restLength = roundLength(rest.getLength());
		
		if (restLength != 0) {
			rest.setLength(restLength);
			midiFile.getNotes().get(currentTrack).add(rest);
		}
		
		if (Parameters.NOTE_VISUALISATION) {
			StringBuilder noteSpacing = new StringBuilder();
			for (int i = 0; i < note - 30; ++i) {
				noteSpacing.append("  ");
			}
			Log.essentialMsg(noteSpacing.toString() + HexToNote.toString(note) + " (" + noteLength + " , " + rawLength + ")");
			if (Parameters.NOTE_PLAY_AUDIO) {
				MiDiO.player.playNote(note);
			}
			int lengthOfLastNote = getLengthOfLastNote(currentTrack);
			
			for (int i = 0; i < lengthOfLastNote; ++i) {
				Log.essentialMsg("");
				sleep(Parameters.VISUALISATION_DELAY);
			}
		}
		Log.unimportantMsg(printOffset() + "Adding note " + HexToNote.toString(note) + " of length " + noteLength + " (raw: " + rawLength + ") at channel " 
				+ channel + (restLength == 0 ? "." : " | Adding rest of length " + restLength + " (raw: " + rest.getLength() + ")"));
	}
	
	private int roundLength(int length) throws MIDIFileException {
		if (length % 15 == 0){
			return length;
		}
		if (length < 5 ){
			return 0;
		}
		for (int correction = 2; correction < 15; ++correction){
			for (int i = 15; i < 3000; i += 15) {
				if (length <= (i + correction) && length >= (i - correction)) {
					if (correction > Parameters.NOTE_LENGTH_CORRECTION_THRESHOLD) {
						Log.warningMsg(printOffset() + "[Note rounding] A note of length " + length + " had to be rounded of a value of " + correction + ".");
					}
					return i;
				}
			}
		}
		throw new MIDIFileException(printOffset() + "A note of length " + length + " has been read in the file. Cannot continue the reading.");
	}

	private int getLengthOfLastNote(int track) {
		int noteLength = 0;
		
		MIDINote previousNotation = midiFile.getNotes().get(track).get(midiFile.getNotes().get(track).size() - 1);
		
		for (int i = 0; previousNotation.isRest(); ++i) {
			noteLength += previousNotation.getLength();
			previousNotation = midiFile.getNotes().get(track).get(midiFile.getNotes().get(track).size() - 2 - i);
		}
		return (noteLength + previousNotation.getLength()) / 15;
	}
	
	
	//////////////////////////////
	//      STATUS CHECKER      //
	//////////////////////////////
	
	private boolean isANoteStart() {
		return readByte() >= 0x90 && readByte() <= (0x90 + midiFile.getNbTracks());
	}
	
	private boolean isARestStart() {
		return readByte() >= 0x80 && readByte() <= 0x8f;
	}

	private boolean isAnEventStart() {
		return readByte() == 0xff;
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
		return rawFile[offset] & 0xFF;
	}

	private int readShort() {
		return rawFile[offset]<<8 & 0xFF00 | rawFile[offset + 1] & 0xFF;
	}

	private int read3Bytes() {
		int ret = 0;
		for (int i = 0; i < 3; ++i) {
			ret <<= 8;
			ret |= (int)rawFile[i + offset] & 0xFF;
		}
		return ret;
	}

	private int readEventDeltaTime() {
		int deltaTime = 0;
		int length = 0;

		while (!isAnEventStart() && !isANoteStart()) {
			offset += 1;
			++length;
		}
		offset -= length;

		for (int i = 1; i < length; ++i){
			deltaTime += (readByte() - 128) * Math.pow(2,7 * (length - i));
			offset += 1;
		}
		deltaTime += readByte();
		offset += 1;
		
		return deltaTime;
	}

	private int readInt() {
		int ret = 0;
		for (int i = 0; i < 4; ++i) {
			ret <<= 8;
			ret |= (int)rawFile[i + offset] & 0xFF;
		}
		return ret;
	}

	private int readNoteAndRestDeltaTime(MIDINote rest) {
		int noteDeltaTime = readNoteDeltaTime();
		int restDeltaTime = 0;
		
		while (isARestStart()) {
			offset += 3;
			restDeltaTime += readNoteDeltaTime();
		}
		rest.setLength(restDeltaTime);
		
		return noteDeltaTime;
	}
	
	private int readNoteDeltaTime() {
		int deltaTime = 0;
		int length = 0;
		
		while (readByte() > 0x80) {
			offset += 1;
			++length;
		}
		offset -= length;
		
		for (int i = 0; i < length; ++i){
			deltaTime += (readByte() - 128) * Math.pow(2,7 * (length - i));
			offset += 1;
		}
		deltaTime += readByte();
		offset += 1;
		
		return deltaTime;
	}

	private String readString(int length){
		String fileString = "";

		for(int i = 0; i < length; i++) {
			fileString += (char) readByte();
			offset += 1;
		}
		return fileString;
	}

	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////
	
	public byte[] getRawFile() {
		return rawFile;
	}
	

	//////////////////////////////
	//         OTHERS           //
	//////////////////////////////
	
	
	private String printOffset() {
		return String.format("[OFFSET %4d] ", offset);
	}
}
