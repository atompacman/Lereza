package com.atompacman.lereza.core.profile;

public abstract class Problem {

	public enum Recoverability {
		IMPOSSIBLE, HARD, NORMAL, EASY, IGNORABLE;
	}
	
	public interface Diagnostic {
		public Recoverability recoverability();
	}
	
	protected String problemName;
	protected Diagnostic diagnostic;
	
	
	public String getName() {
		return problemName;
	}
	
	public abstract String formatProblem();
	
	public abstract void diagnostic();
	
	public String getDiagnostic() {
		diagnostic();
		return diagnostic.toString().toLowerCase().replace('_', ' ');
	}
	
	public String getRecoverability() {
		return diagnostic.recoverability().name();
	}
	
	public abstract void recover();
}
