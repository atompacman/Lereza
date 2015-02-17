package com.atompacman.lereza.report;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.toolkat.time.StopWatch;

public class Procedure extends StopWatch {

	//======================================= FIELDS =============================================\\

	private final Checkpoint 		cp;
	private final int 		 		lvl;
	private final List<Observation> obs;

	
	
	//======================================= METHODS ============================================\\

	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

	Procedure(Checkpoint cp, int lvl) {
		this.cp = cp;
		this.lvl = lvl;
		this.obs = new ArrayList<>();
	}

	
	//--------------------------------------- SETTERS --------------------------------------------\\

	void addObservation(Observation ob) {
		obs.add(ob);
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public Checkpoint getCp() {
		return cp;
	}

	public int getLvl() {
		return lvl;
	}
}
