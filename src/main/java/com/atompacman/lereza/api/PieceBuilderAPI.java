package com.atompacman.lereza.api;

import com.atompacman.lereza.exception.DatabaseException;

public interface PieceBuilderAPI {

	void build(int caseID) throws DatabaseException;
}
