package com.atompacman.lereza.core.profile.key;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.core.piece.PieceTestsHelper;
import com.atompacman.lereza.core.piece.container.Bar;
import com.atompacman.lereza.core.piece.container.Part;
import com.atompacman.lereza.core.piece.container.Piece;
import com.atompacman.lereza.core.solfege.Octave;
import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.RythmicSignature;
import com.atompacman.lereza.core.solfege.Tone;
import com.atompacman.toolkat.io.TextFileReader;

public class KeyProfilerTestHelper {

	private static final String TONE_SEPARATOR = " ";

	
	public static KeyProfile profileTestPiece(String filePath) {
		try {
			Piece piece = buildPiece(parseToneSequence(filePath));
			return (KeyProfile) new KeyProfiler().profile(piece);
		} catch (IOException e) {
			throw new IllegalArgumentException("Could not build test piece", e);
		}
	}
	
	public static List<List<Tone>> parseToneSequence(String filePath) throws IOException {
		List<String> sequences = TextFileReader.read(filePath);
		List<List<Tone>> pieceTones = new ArrayList<List<Tone>>();
		
		for (String sequence : sequences) {
			List<Tone> partTones = new ArrayList<Tone>();
			
			String[] toneNames = sequence.split(TONE_SEPARATOR);
			String toneName = null;
			int i = 0;
			try {
				for (; i < toneNames.length; ++i) {
					toneName = toneNames[i];
					partTones.add(Tone.valueOf(toneName));
				}
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Invalid tone sequence test file "
						+ "at \"" + filePath + "\" (\"" + toneName + "\", line no." 
						+ (partTones.size() - 1) + ", tone no." + (i + 1) + ").");
			}
			pieceTones.add(partTones);
		}
		
		return pieceTones;
	}
	
	public static Piece buildPiece(List<List<Tone>> toneSequences) {
		RythmicSignature stdRS = PieceTestsHelper.standardRythmicSign();
		Piece piece = new Piece(null, stdRS);
		
		for (List<Tone> sequence : toneSequences) {
			int nbBars = (int) (((double) sequence.size() - 1) / 64) + 1;
			Part part = new Part(nbBars, stdRS);
			Bar bar = null;
			
			for (int i = 0; i < sequence.size(); ++i) {
				int barNo = i / 64;
				int posInBar = i % 64;
				if (posInBar == 0) {
					bar = new Bar(stdRS, barNo);
					part.setBar(bar, barNo);
				}
				Pitch pitch = Pitch.valueOf(sequence.get(i), Octave.FOUR);
				bar.addNote(pitch, posInBar, 1);
			}
			piece.addPart(part);
		}
		
		return piece;
	}
}
