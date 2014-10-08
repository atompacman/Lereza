package com.atompacman.lereza.profile.key;

import java.util.List;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.common.solfege.Tone;
import com.atompacman.lereza.piece.container.Piece;
import com.atompacman.lereza.profile.Profile;
import com.atompacman.lereza.profile.Profiler;

public class KeyProfiler extends Profiler {

	private KeyProfile profile;
	private KeyLister lister;

	
	//------------ VERIFY PROFILABILITY ------------\\

	public boolean verifyProfilability(Piece piece) {
		return true;
	}
	
	
	//------------ PROFILE ------------\\

	public Profile profile(Piece piece) {
		prepareProfiler(piece);
		profile = (KeyProfile) profile;
		lister = new KeyLister();
		
		detectPieceKeys();
		
		return profile;
	}
	
	private void detectPieceKeys() {
		for (int i = 0; i < piece.getNbParts(); ++i) {
			detectPartKeys(i);
		}
	}
	
	private void detectPartKeys(int partNo) {
		if(Log.infos() && Log.title("Detecting keys for part " + partNo, 1));
		
		navig.goToBeginningOfPiece();
		
		int keyStartTimestamp = 0;
		List<Tone> tonesInKeySection;
		
//		do {
//			tonesInKeySection.addAll(navig.getCurrentNoteStack())
//		}
	}
}
