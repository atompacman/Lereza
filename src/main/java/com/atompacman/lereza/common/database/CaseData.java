package com.atompacman.lereza.common.database;

import java.util.HashMap;
import java.util.Map;

import com.atompacman.lereza.exception.DatabaseException;
import com.atompacman.lereza.midi.container.MIDIFileInfo;

public class CaseData {

	private int caseID;
	private Map<Class<? extends Storable>, Storable> data;


	//------------ CONSTRUCTOR ------------\\

	public CaseData(int caseID, MIDIFileInfo fileInfo) {
		this.caseID = caseID;
		this.data = new HashMap<Class<? extends Storable>, Storable>();
		this.data.put(MIDIFileInfo.class, fileInfo);
	}


	//------------ SET DATA ------------\\

	public void setData(Class<? extends Storable> clazz, Storable element) throws DatabaseException {
		if (data.get(clazz) != null) {
			throw new DatabaseException("Cannot set storable element \"" + element.toString() 
					+ "\" of type \"" + clazz.getName() + "\" to case of id \"" + caseID + "\" : "
					+ "Such data is already set.");
		}
		data.put(clazz, element);
	}
	
	
	//------------ GET DATA ------------\\

	public Storable getData(Class<? extends Storable> clazz) throws DatabaseException {
		Storable element = data.get(clazz);
		if (data.get(clazz) == null) {
			throw new DatabaseException("Cannot get the element of type \"" + clazz.getName() 
					+ "\" for case of id \"" + caseID + "\" : " + "Inexisting data.");
		}
		return element;
	}
}
