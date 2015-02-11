package com.atompacman.lereza.profile;

import org.junit.Test;

import com.atompacman.lereza.exception.ProfileManagerException;
import com.atompacman.lereza.midi.container.MIDIFile;
import com.atompacman.lereza.midi.container.MIDIFileInfo;
import com.atompacman.lereza.piece.container.Piece;
import com.atompacman.lereza.profile.ProfileManager;

public class TestProfileManager {

	private ProfileManager profileManager = new ProfileManager();
	
	
	@Test (expected = ProfileManagerException.class)
	public void detectInexistingProfiler() throws ProfileManagerException {
		profileManager.getAppropriateProfiler(generateDummyPiece("UnexistingForm"));
	}
	
	@Test
	public void detectExistingProfiler() throws ProfileManagerException {
		profileManager.getAppropriateProfiler(generateDummyPiece("Fugue"));
	}
	
	private Piece generateDummyPiece(String formName) {
		String[] contextElement = { "", "", "", "", "", formName, "", "", "" };
		MIDIFileInfo fileInfo = new MIDIFileInfo("", "", contextElement);
		MIDIFile midifile = new MIDIFile(fileInfo);
		return new Piece(midifile);
	}
}
