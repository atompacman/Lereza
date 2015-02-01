package com.atompacman.lereza.api;

import com.atompacman.lereza.exception.DatabaseException;

public interface PieceBuilder extends Device {

	void build(int caseID) throws DatabaseException;
}
