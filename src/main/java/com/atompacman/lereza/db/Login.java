package com.atompacman.lereza.db;

public class Login {
	
	//======================================= FIELDS =============================================\\

	private final String username;
	private final String password;

	
	
	//======================================= METHODS ============================================\\

	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

	public Login(final String username, final String password) {
		this.username = username;
		this.password = password;
	}

	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public String username() {
		return new String(username);
	}

	public String password() {
		return new String(password);
	}
}
