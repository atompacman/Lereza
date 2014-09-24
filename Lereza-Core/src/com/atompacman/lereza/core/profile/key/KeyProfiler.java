package com.atompacman.lereza.core.profile.key;

import java.util.List;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.common.solfege.Tone;
import com.atompacman.lereza.core.piece.container.Piece;
import com.atompacman.lereza.core.piece.tool.PieceNavigator;
import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.Profiler;

public class KeyProfiler extends Profiler {

	private KeyProfile profile;
	private KeyLister lister;
	private PieceNavigator navig;


	//------------ CONSTRUCTORS ------------\\

	public KeyProfiler(Piece piece, Profile profile) {
		super(piece, profile);
	}

	
	//------------ VERIFY ------------\\

	public void verifyProfilability() {
		
	}
	
	
	//------------ PROFILE ------------\\

	public void profile() {
		if(Log.infos() && Log.title(this.getClass().getSimpleName(), 0));
		profile = (KeyProfile) originalProfile;
		lister = new KeyLister();
		navig = new PieceNavigator(piece);
		
		detectPieceKeys();
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
