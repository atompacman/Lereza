package com.atompacman.lereza.core.profile;

import org.junit.Test;

import com.atompacman.lereza.core.midi.container.MIDIFile;
import com.atompacman.lereza.core.midi.container.MIDIFileInfo;
import com.atompacman.lereza.core.piece.container.Piece;
import com.atompacman.lereza.core.profile.ProfileManager;
import com.atompacman.lereza.core.profile.ProfileManagerException;

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
