package com.atompacman.lereza.core.composition.container;

import java.util.HashMap;
import java.util.Map;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.common.solfege.Composer;

public class CompositionSet {

	private String setName;
	private Map<String,Composition> compositions;
	private Composer composer;


	//------------ CONSTRUCTORS ------------\\

	public CompositionSet(String setName, Composer composer) {
		this.setName = setName;
		this.composer = composer;
		this.compositions = new HashMap<String,Composition>();
	}


	//------------ ADD ------------\\

	public void add(Composition composition) {
		if (compositions.get(composition.getTitle()) != null) {
			if (Log.error() && Log.print("Composition \"" + composition.getTitle() 
					+ "\" cannot be add to set " + setName 
					+ " as it already contains one with this name."));
		}
		compositions.put(composition.getTitle(), composition);
		if (Log.infos() && Log.print("Composition \"" + composition.getTitle() 
				+ "\" added to set " + setName + "."));
	}


	//------------ GETTERS ------------\\

	public Composition get(String name) {
		if (compositions.get(name) == null) {
			if (Log.error() && Log.print("No composition named \"" + 
					name + "\" was found in set " + setName + "."));
		}
		return compositions.get(name);
	}

	public String getName() {
		return setName;
	}

	public Composer getComposer() {
		return composer;
	}

	public int getNbComposition() {
		return compositions.size();
	}
}
