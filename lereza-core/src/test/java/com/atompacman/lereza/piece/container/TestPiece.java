package com.atompacman.lereza.piece.container;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.piece.PieceTestsHelper;

public class TestPiece {
	
	//**********************************************************************************************
	//                                         ADD NOTES
	//**********************************************************************************************
	/**
	 * <h1> Add random parts to a piece </h1>
	 * <i> Pseudo-random test </i> <p>
	 * 
	 * Checks the absence of exceptions when randomly generated parts are added to a piece. For 50 
	 * repetitions, 4 randomly generated parts are added to a piece. <p>
	 * 
	 * Target: {@link Piece#addPart(Part)}
	 */
	@Test
	public void addRandomParts() {
		for (int i = 0; i < 50; ++i) {
			Piece piece = generateRandomPiece(1280);
			assertEquals(piece.numParts(), 4);
		}
	}
	
	/**
	 * A helper function that generates a random piece.
	 * @param finalTimestamp
	 * @return a randomly generated piece
	 */
	public static Piece generateRandomPiece(int finalTimestamp) {
		Piece piece = new Piece(null, PieceTestsHelper.standardRythmicSign());
		for (int j = 0; j < 4; ++j) {
			piece.addPart(TestPart.generatedRandomPart(finalTimestamp));
		}
		return piece;
	}
}
