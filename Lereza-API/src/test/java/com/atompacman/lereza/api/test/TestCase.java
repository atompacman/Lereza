package com.atompacman.lereza.api.test;

import com.atompacman.lereza.common.solfege.Context;

public class TestCase {

	private String	filePath;
	private String	title;
	private String	artist;
	private String	compositionSet;
	private Context	context;

	
	// ------------ CONSTRUCTORS ------------\\

	public TestCase(String filePath, String title, String artist, String compositionSet,
			String genre, String subgenre, String form) {
		this.filePath = filePath;
		this.title = title;
		this.artist = artist;
		this.compositionSet = compositionSet;
		this.context = Context.valueOf(genre, subgenre, form);
	}

	
	// ------------ GETTERS ------------\\

	public String getFilePath() {
		return filePath;
	}

	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}

	public String getCompositionSet() {
		return compositionSet;
	}

	public Context getContext() {
		return context;
	}
}
