package com.atompacman.lereza.common.context;

public enum ContextElementType {
	
	TRADITION	(true), 
	ERA			(true), 
	CULTURE		(true), 
	GENRE		(true), 
	SUBGENRE	(true), 
	FORM		(true), 
	ARTIST		(false), 
	SET			(false), 
	SUBSET		(false);

	private final boolean isInDatabase;
	
	
	private ContextElementType(boolean isInDatabase) {
		this.isInDatabase = isInDatabase;
	}
	
	public boolean isInDatabase() {
		return isInDatabase;
	}
	
	public String nameAsInFile() {
		String name = name().toLowerCase();
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}
}
