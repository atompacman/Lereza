package com.atompacman.lereza.piece.container;

import java.util.Stack;

import org.junit.Test;

import com.atompacman.lereza.Parameters;
import com.atompacman.lereza.midi.MIDINote;
import com.atompacman.lereza.piece.PieceTestsHelper;
import com.atompacman.lereza.solfege.Value;

public class TestPart {

	//**********************************************************************************************
	//                                         ADD NOTES
	//**********************************************************************************************
	/**
	 * <h1> Randomly add notes to a part </h1>
	 * <i> Pseudo-random test </i> <p>
	 * 
	 * Checks the absence of exceptions when random MIDI note stacks are added to a part. For 100 
	 * repetitions, random note stacks are added to a part. 1 stack on 5 contains 1 to 5 notes while
	 * the others only got 1 note. The number of stacks added depends of the progression of the 
	 * timestamp. <p>
	 * 
	 * Target: {@link Part#addNotes(Stack, int)}
	 */
	@Test
	public void randomlyAddNotes() {
		for (int i = 0; i < 100; ++i) {
			generatedRandomPart(2560);
		}
	}

	/**
	 * A helper function that generates a random part of length {@link finalTimestamp}.
	 * @param finalTimestamp
	 * @return a randomly generated part
	 */
	public static Part generatedRandomPart(int finalTimestamp) {
		Part part = PieceTestsHelper.emptyPart(finalTimestamp);
		int timestamp = 0;
		
		while (timestamp < finalTimestamp - part.getRythmicSignature().timeunitsInABar()) {
			Stack<MIDINote> notes = PieceTestsHelper.nextRandMIDINoteStack();
			part.addNotes(notes, timestamp);
			timestamp += Parameters.rand.nextInt(Value.HALF.toTimeunit()) 
					+ Value.QUARTER.toTimeunit();
		}
		
		return part;
	}
}
