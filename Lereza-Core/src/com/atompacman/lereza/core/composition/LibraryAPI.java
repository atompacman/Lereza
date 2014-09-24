package com.atompacman.lereza.core.composition;

import com.atompacman.lereza.common.solfege.Context.Form;

public interface LibraryAPI {

	void addComposition(String filePath, String compositionName, 
			String composer, Form musicalForm, String compositionSetName);
}
