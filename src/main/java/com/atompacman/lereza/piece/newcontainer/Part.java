package com.atompacman.lereza.piece.newcontainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.atompacman.lereza.Parameters.MIDI.FileReader;
import com.atompacman.lereza.api.Wizard;
import com.atompacman.lereza.midi.MIDINote;
import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.RythmicSignature;

public class Part<T extends BarNote> {

	//======================================= FIELDS =============================================\\

	private final List<Bar<T>> 		bars;
	private final RythmicSignature 	rythmicSign;


	
	//======================================= METHODS ============================================\\

	//------------------------------ PUBLIC STATIC CONSTRUCTORS ----------------------------------\\

	public static Part valueOf(RythmicSignature rythmicSign, int finalTU) {
		int tuPerBar = rythmicSign.timeunitsInABar();
		int numBars = (int) Math.ceil((double) finalTU / (double) tuPerBar);
		return Part.valueOf(numBars, rythmicSign);
	}
	
	public static Part valueOf(int numBars, RythmicSignature rythmicSignature) {
		List<Bar> bars = new ArrayList<Bar>();

		for (int i = 0; i < numBars; ++i) {
			bars.add(new Bar(rythmicSignature, i));
		}
		
		return new Part(bars, rythmicSignature);
	}

	public static Part valueOf(List<Bar> bars) {
		if (bars.isEmpty()) {
			throw new IllegalArgumentException("Cannot create a part from an empty bar list.");
		}
		return new Part(bars, bars.get(0).getRythmicSignature());
	}
	
	
	//---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

	private Part(List<Bar> bars, RythmicSignature rythmicSign) {
		this.bars = bars;
		this.rythmicSign = rythmicSign;
	}
	

	//----------------------------------------- ADD ----------------------------------------------\\

	public void addNotes(Set<MIDINote> notes, int begTU) {
		for (MIDINote midiNote : notes) {
			Pitch pitch = Pitch.thatIsMoreCommonForHexValue(midiNote.getHexNote());
			addNote(pitch, midiNote.getLength(), begTU);
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

	private void addNote(Pitch pitch, int timeunitLength, int betTU) {
		int timeunitsInBar = rythmicSign.timeunitsInABar();
		int timeunitPos = betTU % timeunitsInBar;
		int actualNoteLength = timeunitLength;

		if (timeunitPos + timeunitLength > timeunitsInBar) {
			actualNoteLength = timeunitsInBar - timeunitPos;
		}
		barAt(betTU).addNote(pitch, timeunitPos, actualNoteLength);
		
		timeunitLength -= actualNoteLength;
		
		while (timeunitLength != 0) {
			betTU += actualNoteLength;
			if (timeunitLength > timeunitsInBar) {
				actualNoteLength = timeunitsInBar;
			} else {
				actualNoteLength = timeunitLength;
			}
			barAt(betTU).addTiedNote(pitch, 0, actualNoteLength);
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
		return rythmicSign;
	}


	//---------------------------------------- STATE ---------------------------------------------\\

	public boolean isEmpty() {
		return bars.isEmpty();
	}

	public int numBars() {
		return bars.size();
	}

	public int finalTU() {
		return bars.size() * rythmicSign.timeunitsInABar();
	}
	

	//------------------------------------ PRIVATE UTILS -----------------------------------------\\

	private int barIndexAt(int timestamp) {
		return (int)((double) timestamp / (double) rythmicSign.timeunitsInABar());
	}
	
	private Bar barAt(int timestamp) {
		return bars.get(barIndexAt(timestamp));
	}
}
