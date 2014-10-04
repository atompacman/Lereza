package com.atompacman.lereza.api;

import com.atompacman.lereza.exception.DatabaseException;

public interface ProfileManagerAPI {

	void profile(int caseID) throws DatabaseException;
}
