package com.atompacman.lereza.piece.container;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.atompacman.lereza.Parameters.MIDI.FileReader;
import com.atompacman.lereza.api.Wizard;
import com.atompacman.lereza.midi.MIDINote;
import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.RythmicSignature;

public class Part {

	private List<Bar> bars;
	private RythmicSignature rythmicSignature;


	//------------ CONSTRUCTORS ------------\\

	public Part(RythmicSignature rythmicSignature, int finalTimestamp) {
		this((int) ((double) finalTimestamp / (double) rythmicSignature.timeunitsInABar()), 
				rythmicSignature);
	}
	
	public Part(int nbBars, RythmicSignature rythmicSignature) {
		this.bars = new ArrayList<Bar>();
		this.rythmicSignature = rythmicSignature;

		for (int i = 0; i < nbBars; ++i) {
			bars.add(new Bar(rythmicSignature, i));
		}
	}


	//------------ ADD ------------\\

	public void addNotes(Set<MIDINote> notes, int timestamp) {
		for (MIDINote midiNote : notes) {
			Pitch pitch = Pitch.thatIsMoreCommonForHexValue(midiNote.getHexNote());
			addNote(pitch, midiNote.getLength(), timestamp);
		}
		if (Wizard.getBoolean(FileReader.NOTE_PLAY_AUDIO)) {
			//int tempo = Wizard.getInt(FileReader.VISUALISATION_SPEED_CORRECTION);
			//MIDIFilePlayer.getInstance().playNoteStackAndWait(notes, tempo);
		}
		if (Wizard.getBoolean(FileReader.NOTE_VISUALISATION)) {
//			int barIndex = barIndexAt(timestamp);
//			List<List<String>> barsToPrint = new ArrayList<List<String>>();
//			barsToPrint.add(bars.get(barIndex).toStringList(false));
//
//			if (barIndex > 0) {
//				barsToPrint.add(0, bars.get(barIndex - 1).toStringList(true));
//			}
//			if (barIndex > 1) {
//				barsToPrint.add(0, bars.get(barIndex - 2).toStringList(true));
//			}
//			Piece
//			printBars(barsToPrint);
		}
	}

	private void addNote(Pitch pitch, int timeunitLength, int timestamp) {
		int timeunitsInBar = rythmicSignature.timeunitsInABar();
		int timeunitPos = timestamp % timeunitsInBar;
		int actualNoteLength = timeunitLength;

		if (timeunitPos + timeunitLength > timeunitsInBar) {
			actualNoteLength = timeunitsInBar - timeunitPos;
		}
		barAt(timestamp).addNote(pitch, timeunitPos, actualNoteLength);
		
		timeunitLength -= actualNoteLength;
		
		while (timeunitLength != 0) {
			timestamp += actualNoteLength;
			if (timeunitLength > timeunitsInBar) {
				actualNoteLength = timeunitsInBar;
			} else {
				actualNoteLength = timeunitLength;
			}
			barAt(timestamp).addTiedNote(pitch, 0, actualNoteLength);
			timeunitLength -= actualNoteLength;
		}
	}

	
	//------------ SET ------------\\

	public void setBar(Bar bar, int barPos) {
		bars.set(barPos, bar);
	}
	

	//------------ GETTERS ------------\\

	public Bar getBarNo(int barNo) {
		if (barNo < 0 || barNo >= bars.size()) {
			throw new IllegalArgumentException("Cannot access bar no." + 
					barNo + "\": Part has " + bars.size() + " bars.");
		}
		return bars.get(barNo);
	}

	public RythmicSignature getRythmicSignature() {
		return rythmicSignature;
	}


	//------------ OBSERVERS ------------\\

	public boolean isEmpty() {
		return bars.isEmpty();
	}

	public int getNbBars() {
		return bars.size();
	}

	public int finalTimestamp() {
		return bars.size() * rythmicSignature.timeunitsInABar();
	}
	

	//------------ PRIVATE UTILS ------------\\

	private int barIndexAt(int timestamp) {
		return (int)((double) timestamp / (double) rythmicSignature.timeunitsInABar());
	}
	
	private Bar barAt(int timestamp) {
		return bars.get(barIndexAt(timestamp));
	}
}
