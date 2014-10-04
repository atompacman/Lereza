package com.atompacman.lereza.core.profile;

import com.atompacman.lereza.common.database.Storable;
import com.atompacman.lereza.core.piece.container.Piece;

public class ProfiledPiece implements Storable {

	private Piece piece;
	private Profile profile;
	
	
	//------------ CONSTRUCTOR ------------\\

	public ProfiledPiece(Piece piece, Profile profile) {
		this.piece = piece;
		this.profile = profile;
	}

	
	//------------ GETTERS ------------\\

	public Piece getPiece() {
		return piece;
	}

	public Profile getProfile() {
		return profile;
	}	
}
