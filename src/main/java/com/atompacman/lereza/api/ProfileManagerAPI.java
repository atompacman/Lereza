package com.atompacman.lereza.api;

import com.atompacman.lereza.exception.DatabaseException;
import com.atompacman.lereza.exception.ProfileManagerException;

public interface ProfileManagerAPI {

	void profile(int caseID) throws DatabaseException, ProfileManagerException;
}
