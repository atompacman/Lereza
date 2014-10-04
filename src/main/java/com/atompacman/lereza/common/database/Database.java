package com.atompacman.lereza.common.database;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.common.context.ContextElements;
import com.atompacman.lereza.core.piece.container.Piece;
import com.atompacman.lereza.core.profile.ProfiledPiece;
import com.atompacman.lereza.exception.ContextElementsException;
import com.atompacman.lereza.exception.DatabaseException;
import com.atompacman.lereza.exception.MIDIFileIndexParserException;
import com.atompacman.lereza.midi.MIDIFileIndexParser;
import com.atompacman.lereza.midi.container.MIDIFile;
import com.atompacman.lereza.midi.container.MIDIFileInfo;

public class Database {
	
	private static List<CaseData> data;
	
	
	//------------ INITIALIZE ------------\\

	public static void initialize() throws DatabaseException {
		try {
			data = new ArrayList<CaseData>();
			
			ContextElements.parse();
			List<MIDIFileInfo> filesInfo = MIDIFileIndexParser.parse();
			
			for (int caseID = 0; caseID < filesInfo.size(); ++caseID) {
				data.add(new CaseData(caseID + 1, filesInfo.get(caseID)));
			}
		} catch (MIDIFileIndexParserException | ContextElementsException e) {
			throw new DatabaseException("Database initialization failed: ", e);
		}
	}
	
	//------------ INITIALIZATION ------------\\

	public static boolean isInitialized() {
		return data != null;
	}
	
	
	//------------ SET DATA ------------\\

	public static void setFileInfo(int caseID, MIDIFileInfo midiFileInfo) throws DatabaseException {
		getCaseData(caseID).setData(MIDIFileInfo.class, midiFileInfo);
	}
	
	public static void setMIDIFile(int caseID, MIDIFile midiFile) throws DatabaseException {
		getCaseData(caseID).setData(MIDIFile.class, midiFile);
	}
	
	public static void setPiece(int caseID, Piece piece) throws DatabaseException {
		getCaseData(caseID).setData(Piece.class, piece);
	}
	
	public static void setProfiledPiece(int caseID, ProfiledPiece profiledPiece) throws DatabaseException {
		getCaseData(caseID).setData(ProfiledPiece.class, profiledPiece);
	}
	
	
	//------------ GET DATA ------------\\

	public static MIDIFileInfo getFileInfo(int caseID) throws DatabaseException {
		return (MIDIFileInfo) getCaseData(caseID).getData(MIDIFileInfo.class);
	}

	public static MIDIFile getMIDIFile(int caseID) throws DatabaseException {
		return (MIDIFile) getCaseData(caseID).getData(MIDIFile.class);
	}
	
	public static Piece getPiece(int caseID) throws DatabaseException {
		return (Piece) getCaseData(caseID).getData(Piece.class);
	}
	
	public static ProfiledPiece getProfiledPiece(int caseID) throws DatabaseException {
		return (ProfiledPiece) getCaseData(caseID).getData(ProfiledPiece.class);
	}
	
	
	//------------ GET CASE DATA ------------\\

	private static CaseData getCaseData(int caseID) throws DatabaseException {
		if (!isInitialized()) {
			throw new DatabaseException("Database was not initialized.");
		}
		
		caseID -= 1;
		
		if (caseID < 0) {
			throw new DatabaseException("Case id must be positive (got \"" + caseID + "\").");
		}
		if (caseID >= data.size()) {
			throw new DatabaseException("Case id \"" + caseID + "\"" + " is not in the "
					+ "database. Actual database size is " + data.size() + ".");
		}
		
		CaseData caseData = data.get(caseID);
		
		if (caseData == null) {
			throw new DatabaseException("Case data for id \"" + caseID + "\" should not be null.");
		}
		
		return caseData;
	}
}
