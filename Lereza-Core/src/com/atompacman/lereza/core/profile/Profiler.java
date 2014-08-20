package com.atompacman.lereza.core.profile;

import com.atompacman.lereza.core.piece.container.Piece;

public abstract class Profiler {

	protected Piece piece;
	protected Profile originalProfile;
	
	
	public Profiler(Piece piece, Profile profile) {
		this.piece = piece;
		this.originalProfile = profile;
	}
	
	public abstract void verifyProfilability();
	
	public abstract void profile();
	
	public Profile getProfile() {
		return originalProfile;
	}
}
