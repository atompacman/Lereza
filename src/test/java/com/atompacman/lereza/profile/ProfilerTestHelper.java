package com.atompacman.lereza.profile;

import com.atompacman.lereza.exception.DatabaseException;
import com.atompacman.lereza.exception.MIDIFileReaderException;
import com.atompacman.lereza.midi.MIDIFileReader;
import com.atompacman.lereza.piece.container.Piece;
import com.atompacman.lereza.piece.tool.PieceBuilderImpl;
import com.atompacman.lereza.resources.database.Database;

public class ProfilerTestHelper {

	public static Piece loadPiece(int caseID) throws DatabaseException, MIDIFileReaderException {
		DatabaseImpl.initialize();
		new MIDIFileReader().read(caseID);
		new PieceBuilderImpl().build(caseID);
		return DatabaseImpl.getPiece(caseID);
	}
}
