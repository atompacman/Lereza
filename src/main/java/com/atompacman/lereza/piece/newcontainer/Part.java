package com.atompacman.lereza.piece.newcontainer;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.solfege.Dynamic;
import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.RythmicSignature;

public final class Part<T extends BarNote> {

	//======================================= FIELDS =============================================\\

	private final List<Bar<T>> 		bars;
	private final RythmicSignature 	rythmicSign;


	
	//======================================= METHODS ============================================\\

	//------------------------------ PACKAGE STATIC CONSTRUCTOR ----------------------------------\\

	static Part<BarNote> valueOf(RythmicSignature rythmicSign, int finalTU) {
		int tuPerBar = rythmicSign.timeunitsInABar();
		int numBars = (int) Math.ceil((double) finalTU / (double) tuPerBar);
		
		List<Bar<BarNote>> bars = new ArrayList<>();
		for (int i = 0; i < numBars; ++i) {
			bars.add(Bar.valueOf(rythmicSign, BarNote.class));
		}
		
		return new Part<BarNote>(bars, rythmicSign);
	}
	
	
	//---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

	private Part(List<Bar<T>> bars, RythmicSignature rythmicSign) {
		this.bars = bars;
		this.rythmicSign = rythmicSign;
	}
	

	//----------------------------------------- ADD ----------------------------------------------\\

	void add(Pitch pitch, Dynamic dynamic, int begTU, int lenTU) {
		int tuInBar = rythmicSign.timeunitsInABar();
		int barPosTU = begTU % tuInBar;
		int actualLen = lenTU;

		if (barPosTU + lenTU > tuInBar) {
			actualLen = tuInBar - barPosTU;
		}
		getBarAt(begTU).add(pitch, dynamic, barPosTU, actualLen, false);
		
		lenTU -= actualLen;
		
		while (lenTU != 0) {
			begTU += actualLen;
			if (lenTU > tuInBar) {
				actualLen = tuInBar;
			} else {
				actualLen = lenTU;
			}
			getBarAt(begTU).add(pitch, dynamic, 0, actualLen, true);
			lenTU -= actualLen;
		}
	}

	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public Bar<T> getBar(int bar) {
		if (bar < 0 || bar >= bars.size()) {
			throw new IllegalArgumentException("Cannot access bar no." + 
					bar + "\": Part has " + bars.size() + " bars.");
		}
		return bars.get(bar);
	}

	public Bar<T> getBarAt(int timestamp) {
		return bars.get((int)((double) timestamp / (double) rythmicSign.timeunitsInABar()));
	}
	
	public RythmicSignature getRythmicSignature() {
		return rythmicSign;
	}

	
	//---------------------------------------- STATE ---------------------------------------------\\

	public boolean isEmpty() {
		return bars.isEmpty();
	}

	public int numBars() {
		return bars.size();
	}

	public int finalTU() {
		return bars.size() * rythmicSign.timeunitsInABar();
	}
}
