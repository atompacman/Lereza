package com.atompacman.lereza.api.test;

import com.atompacman.lereza.common.solfege.Context;

public class TestFile {

	public static final String TEST_INFO_FILENAME = "TestInfo.xml";
	
	private String filePath;
	private String title;
	private String artist;
	private String compositionSet;
	private Context context;
	
	
	public TestFile(String filePath, String title, String artist, String compositionSet, 
			String genre, String subgenre, String form) {
		this.filePath = filePath;
		this.title = title;
		this.artist = artist;
		this.compositionSet = compositionSet;
		this.context = Context.valueOf(genre, subgenre, form);
	}
	
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
