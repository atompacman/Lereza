package com.atompacman.lereza.piece.tool;

import org.junit.Test;

import com.atompacman.lereza.exception.PieceNavigatorException;
import com.atompacman.lereza.piece.PieceTestsHelper;
import com.atompacman.lereza.piece.container.Part;
import com.atompacman.lereza.piece.container.Piece;
import com.atompacman.lereza.piece.container.TestPiece;
import com.atompacman.lereza.solfege.RythmicSignature;

public class TestPieceNavigator {

	@Test
	public void randomPieceCompleteTimeunitNavigation() {
		Piece randPiece = TestPiece.generateRandomPiece(1280);
		
		PieceNavigator navig = new PieceNavigator(randPiece);
		
		while (!navig.endOfPiece()) {
			while (!navig.endOfPart()) {
				navig.goToNextTimeunit();
			}
			navig.goToNextPart();
		}
	}
	
	@Test (expected = PieceNavigatorException.class)
	public void pieceWithNoPartsVerification() {
		Piece withNoParts = new Piece(null, PieceTestsHelper.standardRythmicSign());
		new PieceNavigator(withNoParts);
	}

	@Test (expected = PieceNavigatorException.class)
	public void emptyPartsVerification() {
		RythmicSignature stdRS = PieceTestsHelper.standardRythmicSign();
		Piece piece = new Piece(null, stdRS);
		piece.addPart(new Part(0, stdRS));
		new PieceNavigator(piece);
	}
}
