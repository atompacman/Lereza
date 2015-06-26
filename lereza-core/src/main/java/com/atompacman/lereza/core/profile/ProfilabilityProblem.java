package com.atompacman.lereza.core.profile;

public abstract class ProfilabilityProblem {

	public enum Recoverability {
		IMPOSSIBLE, HARD, NORMAL, EASY, IGNORABLE;
	}
	
	public interface Diagnostic {
		public Recoverability recoverability();
	}
	
	protected String problemName;
	protected Diagnostic diagnostic;
	
	
	//------------ FORMAT ------------\\

	public abstract String formatProblem();
	
	
	//------------ DIAGNOSTIC ------------\\

	public abstract void diagnostic();
	
	
	//------------ RECOVER ------------\\

	public abstract void recover();
	
	
	//------------ GETTERS ------------\\

	public String getName() {
		return problemName;
	}
	
	public String getDiagnostic() {
		diagnostic();
		return diagnostic.toString().toLowerCase().replace('_', ' ');
	}
	
	public String getRecoverability() {
		return diagnostic.recoverability().name();
	}
}
