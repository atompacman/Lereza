package com.atompacman.lereza.piece;

import java.util.Random;

import com.atompacman.lereza.Parameters;
import com.atompacman.lereza.common.solfege.Accidental;
import com.atompacman.lereza.common.solfege.Grouping;
import com.atompacman.lereza.common.solfege.Meter;
import com.atompacman.lereza.common.solfege.NoteLetter;
import com.atompacman.lereza.common.solfege.Octave;
import com.atompacman.lereza.common.solfege.Pitch;
import com.atompacman.lereza.common.solfege.RythmicSignature;
import com.atompacman.lereza.common.solfege.Tone;
import com.atompacman.lereza.common.solfege.Value;
import com.atompacman.lereza.piece.container.Bar;

public class PieceTestsHelper {

	private static Random rand = Parameters.rand;
	
	
	//------------ BAR ------------\\

	public static Bar emptyBar() {
		return emptyBar(0);
	}
	
	public static Bar emptyBar(int barNo) {
		RythmicSignature sign = new RythmicSignature(new Meter(4, 4), Grouping.DUPLETS);
		return new Bar(sign, barNo);
	}
	
	
	//------------ BAR INPUTS ------------\\

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
		input.pitch = new Pitch(new Tone(letter, alteration), octave);
		
		input.length = rand.nextInt(Value.HALF.toTimeunit()) + 1;
		do {
			input.pos = rand.nextInt(64);
		} while (input.pos + input.length >= 64);
		
		input.isTied = rand.nextInt(4) == 0 ? true : false;
		
		return input;
	}

	public static Bar determinedBar() {
		Bar bar = emptyBar();
		bar.add(Pitch.valueOf("C3"), 0, 16);
		bar.add(Pitch.valueOf("E3"), 0, 16);
		bar.add(Pitch.valueOf("G3"), 0, 16);
		
		bar.add(Pitch.valueOf("A3"),  16, 4);
		bar.add(Pitch.valueOf("Bb3"), 20, 4);
		bar.add(Pitch.valueOf("B3"),  24, 4);
		bar.add(Pitch.valueOf("C4"),  28, 4);
		bar.add(Pitch.valueOf("C#4"), 32, 4);
		bar.add(Pitch.valueOf("D4"),  36, 4);
		bar.add(Pitch.valueOf("Eb4"), 40, 4);
		bar.add(Pitch.valueOf("E4"),  44, 4);
		
		bar.add(Pitch.valueOf("G2"), 46, 7);	
		bar.add(Pitch.valueOf("B2"), 52, 7);	
		bar.add(Pitch.valueOf("D2"), 58, 5);
		
		return bar;
	}
}
