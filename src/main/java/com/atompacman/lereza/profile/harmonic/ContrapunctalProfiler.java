package com.atompacman.lereza.profile.harmonic;

import com.atompacman.lereza.piece.container.Piece;
import com.atompacman.lereza.profile.Profile;
import com.atompacman.lereza.profile.Profiler;

public class ContrapunctalProfiler extends Profiler {

	private ContrapunctalProfile profile;
	
		
	//------------ VERIFY PROFILABILITY ------------\\

	public boolean verifyProfilability(Piece piece) {
		return true;
	}
	
	
	//------------ PROFILE ------------\\

	public Profile profile(Piece piece) {
		prepareProfiler(piece);
		profile = new ContrapunctalProfile();
		
		new PolyphonicProfiler().profile(piece);
		
		return profile;
	}
}
