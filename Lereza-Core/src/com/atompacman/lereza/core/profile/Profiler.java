package com.atompacman.lereza.core.profile;

import com.atompacman.lereza.core.piece.container.Piece;

public abstract class Profiler {

	protected Piece piece;
	protected Profile originalProfile;
	
	
	//------------ CONSTRUCTORS ------------\\

	public Profiler(Piece piece, Profile profile) {
		this.piece = piece;
		this.originalProfile = profile;
	}
	
	
	//------------ VERIFY ------------\\

	public abstract void verifyProfilability();
	
	
	//------------ PROFILE ------------\\

	public abstract void profile();
	
	
	//------------ GETTERS ------------\\

	public Profile getProfile() {
		return originalProfile;
	}
}
