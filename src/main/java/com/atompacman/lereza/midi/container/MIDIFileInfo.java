package com.atompacman.lereza.midi.container;

import com.atompacman.lereza.resources.context.Context;
import com.atompacman.lereza.resources.database.Storable;

public class MIDIFileInfo implements Storable {

	private String	filePath;
	private String	title;
	private Context	context;

	
	// ------------ CONSTRUCTORS ------------\\

	public MIDIFileInfo(String filePath, String title, String... contextElements) {
		this.filePath = filePath;
		this.title = title;
		this.context = new Context(contextElements);
	}

	
	// ------------ GETTERS ------------\\

	public String getFilePath() {
		return filePath;
	}

	public String getTitle() {
		return title;
	}

	public Context getContext() {
		return context;
	}
}
