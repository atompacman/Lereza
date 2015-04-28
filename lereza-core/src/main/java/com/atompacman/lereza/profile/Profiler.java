package com.atompacman.lereza.profile;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.atomlog.Log;
import com.atompacman.lereza.piece.container.Piece;
import com.atompacman.lereza.piece.tool.PieceNavigator;

public abstract class Profiler {

	protected Piece piece;
	protected PieceNavigator navig;
	protected List<ProfilabilityProblem> problems;
	
	
	//------------ PROFILE ------------\\

	public final Profile profile(Piece piece) {
		if (piece.numParts() == 0) {
			throw new IllegalArgumentException("Cannot profile a piece with no part.");
		}
		this.piece = piece;
		this.navig = new PieceNavigator(piece);
		this.problems = new ArrayList<ProfilabilityProblem>();
		
		if(Log.infos() && Log.title(getClass().getSimpleName() + " - Preparation", 0));

		prepare();
		
		if(Log.infos() && Log.title(getClass().getSimpleName() + " - Verifying profilability", 0));

		if (!verifyProfilability()) {
			return null;
		}
		
		if(Log.infos() && Log.title(getClass().getSimpleName() + " - Profiling", 0));
		
		return profile();
	}
	
	protected abstract void prepare();
	
	protected abstract boolean verifyProfilability();

	protected abstract Profile profile();
	
	
	//------------ GETTERS ------------\\

	public List<ProfilabilityProblem> getProfilabilityProblems() {
		return problems;
	}
}
