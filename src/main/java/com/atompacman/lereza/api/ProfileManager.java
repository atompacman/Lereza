package com.atompacman.lereza.api;

import com.atompacman.lereza.exception.DatabaseException;
import com.atompacman.lereza.exception.ProfileManagerException;

public interface ProfileManager extends Device  {

	void profile(int caseID) throws DatabaseException, ProfileManagerException;
}
