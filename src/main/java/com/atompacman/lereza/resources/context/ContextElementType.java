package com.atompacman.lereza.resources.context;

public enum ContextElementType {
	
	TRADITION	(false, DatabaseStatus.NO_PARENT),
	ERA			(false, DatabaseStatus.SINGLE_PARENT, 		TRADITION),
	CULTURE		(false, DatabaseStatus.MULTIPLE_PARENTS, 	ERA),
	GENRE		(false, DatabaseStatus.MULTIPLE_PARENTS,	ERA),
	SUBGENRE	(true,  DatabaseStatus.SINGLE_PARENT,		GENRE),
	FORM		(false, DatabaseStatus.SINGLE_PARENT,		GENRE),
	SUBFORM		(true,  DatabaseStatus.SINGLE_PARENT,		FORM),
	ARTIST		(false, DatabaseStatus.TEXT_FIELD),
	SET			(true,  DatabaseStatus.TEXT_FIELD),
	SUBSET		(true,  DatabaseStatus.TEXT_FIELD),
	COLLECTION	(true,  DatabaseStatus.TEXT_FIELD),
	NO			(true,  DatabaseStatus.TEXT_FIELD),
	TITLE		(false, DatabaseStatus.TEXT_FIELD),
	URL			(false, DatabaseStatus.URL_FIELD),
	ID			(false, DatabaseStatus.PRIMARY_KEY);

	
	public enum DatabaseStatus {
		PRIMARY_KEY, URL_FIELD, TEXT_FIELD, NO_PARENT, SINGLE_PARENT, MULTIPLE_PARENTS;
	}
	
	
	private DatabaseStatus dbStatus;
	private boolean canBeNull;
	private ContextElementType childElement;
	private ContextElementType parentElement;
	
	
	//------------ PRIVATE CONSTRUCTORS ------------\\

	private ContextElementType(boolean canBeNull, DatabaseStatus status) {
		this(canBeNull, status, null);
	}
	
	private ContextElementType(boolean canBeNull, 
			DatabaseStatus status, ContextElementType parent) {
		
		this.dbStatus = status;
		this.canBeNull = canBeNull;
		if (parent != null) {
			this.parentElement = parent;
			this.parentElement.childElement = this;
		}
	}
	
	
	//------------ GETTERS ------------\\

	public DatabaseStatus getDatabaseStatus() {
		return dbStatus;
	}
	
	public int getDatabaseQueryColumn() {
		return values().length - ordinal() - 1;
	}
	
	public boolean canBeNull() {
		return canBeNull;
	}
	
	public ContextElementType getChildElement() {
		return childElement;
	}
	
	public ContextElementType getParentElement() {
		return parentElement;
	}
	
	
	//------------ STRING ------------\\

	public String formattedName() {
		if (this == NO) {
			return "#";
		}
		return name().toLowerCase();
	}
	
	public String nameAsInDatabase() {
		if (this == NO) {
			return "order_in_collection";
		} else if (this == ID) {
			return "file_id";
		}
		return name().toLowerCase();
	}
}
