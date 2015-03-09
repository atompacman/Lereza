package com.atompacman.lereza.piece.newcontainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atompacman.lereza.midi.MIDISequence;
import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.RythmicSignature;

public class PieceBuilder {
	
	//======================================= FIELDS =============================================\\

	/** Track / Timestamp / Pitch / Length */
	private List<Map<Integer, Map<Pitch, Integer>>> entries;
	
	private RythmicSignature rythmicSign;
	private MIDISequence midiSeq;
	
	// Temporary note input parameters
	private int currPart;
	private int currBegTU;
	private int currLenTU;
	
	
	
	//======================================= METHODS ============================================\\

	//------------------------------ PUBLIC STATIC CONSTRUCTORS ----------------------------------\\

	public static PieceBuilder newPiece(RythmicSignature rythmicSignature) {
		return new PieceBuilder(rythmicSignature, null);
	}
	
	public static PieceBuilder newPiece(MIDISequence midiSequence) {
		return new PieceBuilder(midiSequence.getRythmicSignature(), midiSequence);
	}
	
	
	//---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

	private PieceBuilder(RythmicSignature rythmicSign, MIDISequence midiSeq) {
		this.entries = new ArrayList<>();
		
		this.rythmicSign = rythmicSign;
		this.midiSeq = midiSeq;
		
		this.currPart = 0;
		this.currBegTU = 0;
		this.currLenTU = 32;
	}
	

	//----------------------------------------- ADD ----------------------------------------------\\

	public PieceBuilder add(Pitch pitch) {
		while (currPart >= entries.size()) {
			entries.add(new HashMap<Integer, Map<Pitch, Integer>>());
		}
		Map<Pitch, Integer> tuEntries = entries.get(currPart).get(currBegTU);

		if (tuEntries == null) {
			tuEntries = new HashMap<>();
			entries.get(currPart).put(currBegTU, tuEntries);
		}
		if (tuEntries.put(pitch, currLenTU) != null) {
			throw new IllegalArgumentException("A note with the same "
					+ "pitched was already added at this position.");
		}
		return this;
	}
	
	public PieceBuilder add(Pitch pitch, int part, int begTU, int lengthTU) {
		return part(part).pos(begTU).length(lengthTU).add(pitch);
	}
	
	public PieceBuilder part(int partIndex) {
		this.currPart = partIndex;
		return this;
	}
	
	public PieceBuilder pos(int timeunit) {
		this.currBegTU = timeunit;
		return this;
	}
	
	public PieceBuilder length(int noteLenTU) {
		this.currLenTU = noteLenTU;
		return this;
	}
}
