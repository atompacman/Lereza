package com.atompacman.lereza.piece;

import java.util.Random;
import java.util.Stack;

import com.atompacman.lereza.Parameters;
import com.atompacman.lereza.midi.MIDINote;
import com.atompacman.lereza.piece.container.Bar;
import com.atompacman.lereza.piece.container.Part;
import com.atompacman.lereza.solfege.Accidental;
import com.atompacman.lereza.solfege.Grouping;
import com.atompacman.lereza.solfege.Meter;
import com.atompacman.lereza.solfege.NoteLetter;
import com.atompacman.lereza.solfege.Octave;
import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.RythmicSignature;
import com.atompacman.lereza.solfege.Semitones;
import com.atompacman.lereza.solfege.Value;

public class PieceTestsHelper {

	private static Random rand = Parameters.rand;
	
	
	//------------ PART ------------\\

	public static MIDINote nextRandMIDINote() {
		int hexNote = rand.nextInt(100) + Semitones.IN_OCTAVE + 1;
		int noteLength = rand.nextInt(Value.HALF.toTimeunit()) + 1;
		return new MIDINote(hexNote, noteLength);
	}
	
	public static Stack<MIDINote> nextRandMIDINoteStack() {
		Stack<MIDINote> notes = new Stack<MIDINote>();
		int nbNotes = 1;
		
		if (rand.nextInt(5) == 1) {
			nbNotes = rand.nextInt(5) + 1;
		}
		for (int i = 0; i < nbNotes; ++i) {
			notes.add(nextRandMIDINote());
		}
		return notes;
	}
	
	public static Part emptyPart(int finalTimestamp) {
		return new Part(standardRythmicSign(), finalTimestamp);
	}
	
	
	//------------ BAR ------------\\

	public static class BarInput {
		public Pitch pitch;
		public int pos;
		public int length;
		public boolean isTied;
	}
	
	public static BarInput nextRandBarInput() {
		BarInput input = new BarInput();
		NoteLetter letter = NoteLetter.values()[rand.nextInt(NoteLetter.values().length)];
		Accidental alteration = Accidental.values()[rand.nextInt(Accidental.values().length)];
		Octave octave = Octave.values()[rand.nextInt(Octave.values().length)];
		input.pitch = Pitch.valueOf(letter, alteration, octave);
		
		input.length = rand.nextInt(Value.HALF.toTimeunit()) + 1;
		do {
			input.pos = rand.nextInt(64);
		} while (input.pos + input.length >= 64);
		
		input.isTied = rand.nextInt(4) == 0 ? true : false;
		
		return input;
	}
	
	public static Bar emptyBar() {
		return emptyBar(0);
	}
	
	public static Bar emptyBar(int barNo) {
		return new Bar(standardRythmicSign(), barNo);
	}

	public static Bar determinedBar() {
		Bar bar = emptyBar();
		bar.addNote(Pitch.valueOf("C3"), 0, 16);
		bar.addNote(Pitch.valueOf("E3"), 0, 16);
		bar.addNote(Pitch.valueOf("G3"), 0, 16);
		
		bar.addNote(Pitch.valueOf("A1"),  16, 4);
		bar.addNote(Pitch.valueOf("Bb1"), 20, 4);
		bar.addNote(Pitch.valueOf("B1"),  24, 4);
		bar.addNote(Pitch.valueOf("C2"),  28, 4);
		bar.addNote(Pitch.valueOf("C#2"), 32, 4);
		bar.addNote(Pitch.valueOf("D2"),  36, 4);
		bar.addNote(Pitch.valueOf("Eb2"), 40, 4);
		bar.addNote(Pitch.valueOf("E2"),  44, 4);
		
		bar.addNote(Pitch.valueOf("G2"), 46, 7);	
		bar.addNote(Pitch.valueOf("B2"), 52, 7);	
		bar.addNote(Pitch.valueOf("D2"), 58, 5);
		
		return bar;
	}

	
	//------------ RYTHMIC SIGNATURE ------------\\

	public static RythmicSignature standardRythmicSign() {
		return  new RythmicSignature(new Meter(4, 4), Grouping.DUPLETS);
	}
}
