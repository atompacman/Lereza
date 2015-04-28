package com.atompacman.lereza.profile.drum;

import com.atompacman.toolkat.math.Interval;

public class TUInterval {

	//======================================= FIELDS =============================================\\

	private Interval<Integer> interval;
	

	
	//======================================= METHODS ============================================\\

	//---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

	public TUInterval(int beg, int end) {
		this.interval = new Interval<>(beg, end);
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public int beg() {
		return interval.beg();
	}
	
	public int end() {
		return interval.end();
	}
}
