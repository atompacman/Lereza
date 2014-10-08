package com.atompacman.lereza.profile;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.piece.container.Piece;
import com.atompacman.lereza.piece.tool.PieceNavigator;

public abstract class Profiler {

	protected Piece piece;
	protected PieceNavigator navig;
	protected List<ProfilabilityProblem> problems;
	

	//------------ CONSTRUCTOR ------------\\

	public Profiler() {
		this.problems = new ArrayList<ProfilabilityProblem>();
	}
	
	
	//------------ VERIFY PROFILABILITY ------------\\

	public abstract boolean verifyProfilability(Piece piece);

	public List<ProfilabilityProblem> getProfilabilityProblems() {
		return problems;
	}
	

	//------------ PROFILE ------------\\

	protected void prepareProfiler(Piece piece) {
		if(Log.infos() && Log.title(getClass().getSimpleName() + " - Profiling", 0));	
		this.piece = piece;
		this.navig = new PieceNavigator(piece);
	}
	
	public abstract Profile profile(Piece piece);
}
