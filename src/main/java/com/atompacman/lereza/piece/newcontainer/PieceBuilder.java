package com.atompacman.lereza.piece.newcontainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atompacman.lereza.midi.MIDISequence;
import com.atompacman.lereza.piece.container.Piece;
import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.RythmicSignature;

public class PieceBuilder {

	//==================================== STATIC FIELDS =========================================\\

	private static class NoteEntry {
		public Pitch pitch;
		public int lengthTU;
		
		public NoteEntry(Pitch pitch, int lengthTU) {
			this.pitch = pitch;
			this.lengthTU = lengthTU;
		}
	}
	
	
	
	//======================================= FIELDS =============================================\\

	private List<Map<Integer, Map<Pitch, Integer>>> entries;
	
	
	
	//======================================= METHODS ============================================\\

	//------------------------------ PUBLIC STATIC CONSTRUCTORS ----------------------------------\\

	public static PieceBuilder newPiece(RythmicSignature rythmicSignature) {
		return new PieceBuilder(Piece.valueOf(rythmicSignature));
	}
	
	public static PieceBuilder newPiece(MIDISequence midiSeq) {
		return new PieceBuilder(Piece.valueOf(midiSeq));
	}
	
	
	//---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

	private PieceBuilder(Piece piece) {
		this.entries = new ArrayList<>();
	}
	

	//----------------------------------------- ADD ----------------------------------------------\\

	public void addNote(Pitch pitch, int part, int begTU, int lengthTU) {
		while (part >= entries.size()) {
			entries.add(new HashMap<Integer, Map<Pitch, Integer>>());
		}
		Map<Pitch, Integer> tuEntries = entries.get(part).get(begTU);

		if (tuEntries == null) {
			tuEntries = new HashMap<>();
			entries.get(part).put(begTU, tuEntries);
		}
		if (tuEntries.put(pitch, lengthTU) != null) {
			throw new IllegalArgumentException("A note with the same "
					+ "pitched was already added at this position.");
		}
	}
}
