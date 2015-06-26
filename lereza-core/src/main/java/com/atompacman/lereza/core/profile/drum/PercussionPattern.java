package com.atompacman.lereza.core.profile.drum;

import java.util.HashSet;
import java.util.Set;

public class PercussionPattern {

	//======================================= FIELDS =============================================\\

	protected final PercussionElement 	elem;
	protected final int 				length;
	protected final Set<Integer> 		hits;



	//======================================= METHODS ============================================\\

	//------------------------------------- CONSTRUCTORS -----------------------------------------\\

	public PercussionPattern(PercussionElement elem, int length) {
		this.elem = elem;
		this.length = length;
		this.hits = new HashSet<Integer>();
	}
	
	PercussionPattern(PercussionElement elem, int length, Set<Integer> hits) {
		this.elem = elem;
		this.length = length;
		this.hits = hits;
	}


	//--------------------------------------- SETTERS --------------------------------------------\\

	public void adHit(Integer hit) {
		if (hit < 0) {
			throw new IllegalArgumentException("Hit position cannot be negative");
		}
		if (hit >= length) {
			throw new IllegalArgumentException("Hit position cannot exceed pattern length");
		}
		hits.add(hit);
	}


	//--------------------------------------- GETTERS --------------------------------------------\\

	public PercussionElement getPercussionElement() {
		return elem;
	}

	public int getLength() {
		return length;
	}

	public Set<Integer> getHits() {
		return hits;
	}

	//----------------------------------- DIFFERENCE WITH ----------------------------------------\\

	public double differenceWith(PercussionPattern other) {
		//TODO
		return 0;
	}
}
