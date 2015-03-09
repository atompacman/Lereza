package com.atompacman.lereza.profile.key;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import com.atompacman.lereza.midi.MIDISequence;
import com.atompacman.lereza.piece.container.Note;
import com.atompacman.lereza.piece.container.Piece;
import com.atompacman.lereza.piece.tool.PieceNavigator;
import com.atompacman.lereza.solfege.Tone;

public class ToneMapWriter {

	//====================================== CONSTANTS ===========================================\\

	private static final String FILE_TYPE_HEADER = "LerezaToneMap";
	
	
	
	//===================================== INNER TYPES ==========================================\\

	private static class ToneEntry {
		
		//===================================== FIELDS ===========================================\\

		private byte  tone;
		private int   begTU;
		private short length;

		
		
		//===================================== METHODS ==========================================\\

		//-------------------------------- PUBLIC CONSTRUCTOR ------------------------------------\\

		public ToneEntry(Tone tone, int begTU, int length) {
			this.tone = (byte) tone.semitoneValue();
			this.begTU = begTU;
			this.length = (short) length;
		}

		
		//------------------------------------ TO BINARY -----------------------------------------\\

		public byte[] toBinary() {
			ByteBuffer bb = ByteBuffer.allocate(7);
			bb.putInt(begTU);
			bb.putShort(length);
			bb.put(tone);
			for (byte b : bb.array()) {
				System.out.println(b);
			}
			return bb.array();
		}


		//------------------------------------- EQUALS -------------------------------------------\\

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + begTU;
			result = prime * result + length;
			result = prime * result + tone;
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ToneEntry other = (ToneEntry) obj;
			if (begTU != other.begTU)
				return false;
			if (length != other.length)
				return false;
			if (tone != other.tone)
				return false;
			return true;
		}
	}
	
	
	
	//==================================== STATIC METHODS ========================================\\

	//---------------------------------------- WRITE ---------------------------------------------\\

	// TODO ne pas considerer les tracks non-musicales, comme les tracks de drums
	
	static void write(Piece piece, String name, File output) throws IOException {
		Set<ToneEntry> entries = new HashSet<>();
		int finalTU = extractToneEntriesFrom(piece, entries);
				
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(output));
		
		bos.write(FILE_TYPE_HEADER.getBytes());
		bos.write(' ');
		bos.write(name.getBytes());
		bos.write(' ');
		bos.write(finalTU);
		
		for (ToneEntry entry : entries) {
			bos.write(entry.toBinary());
		}
		
		bos.close();
	}
	
	// TODO au lieu de retourner le finalTU, stocker le finalTU dans la piece
	private static int extractToneEntriesFrom(Piece piece, Set<ToneEntry> entries) {
		PieceNavigator navig = new PieceNavigator(piece);
		int finalTU = 0;
		
		while (!navig.endOfPiece()) {
			for (Note note : navig.getCurrentStartingNoteStack()) {
				Tone tone = note.getPitch().getTone();
				int begTU = navig.getCurrentTimestamp();
				int length = note.getValue().toTimeunit();
				
				entries.add(new ToneEntry(tone, begTU, length));
				
				if (begTU + length > finalTU) {
					finalTU = begTU + length;
				}
			}
			navig.goToNextNote();
		}
		
		return finalTU;
	}
	
	public static void main(String[] args) {
		Part part = Part.v
	}
}
