package com.atompacman.lereza.core.solfege;

import java.util.List;

public class Intervals {

	private List<Interval> intervals;
	
	
	//------------ CONSTRUCTORS ------------\\

	public Intervals(List<Interval> intervals) {
		this.intervals = intervals;
	}


	//------------ GETTERS ------------\\

	public List<Interval> toList() {
		return intervals;
	}	
}
