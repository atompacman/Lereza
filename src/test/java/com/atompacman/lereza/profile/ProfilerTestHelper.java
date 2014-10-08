package com.atompacman.lereza.profile;

import com.atompacman.lereza.common.database.Database;
import com.atompacman.lereza.exception.DatabaseException;
import com.atompacman.lereza.exception.MIDIFileReaderException;
import com.atompacman.lereza.midi.MIDIFileReader;
import com.atompacman.lereza.piece.container.Piece;
import com.atompacman.lereza.piece.tool.PieceBuilder;

public class ProfilerTestHelper {

	public static Piece loadPiece(int caseID) throws DatabaseException, MIDIFileReaderException {
		Database.initialize();
		new MIDIFileReader().read(caseID);
		new PieceBuilder().build(caseID);
		return Database.getPiece(caseID);
	}
}
