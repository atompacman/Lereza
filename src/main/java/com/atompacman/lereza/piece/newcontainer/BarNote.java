package com.atompacman.lereza.piece.newcontainer;

import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.Value;

public class BarNote extends Note {

	//======================================= FIELDS =============================================\\

	private boolean isTied;

	
	
	//======================================= METHODS ============================================\\
	
	//------------------------------ PUBLIC STATIC CONSTRUCTOR -----------------------------------\\
	
	public static BarNote valueOf(int hexPitch, Value value, boolean isTied) {
		Pitch pitch = Pitch.thatIsMoreCommonForHexValue(hexPitch);
		return new BarNote(pitch, value, isTied);
	}
	
	public static BarNote valueOf(Pitch pitch, Value value, boolean isTied) {
		return new BarNote(pitch, value, isTied);
	}
	

	//--------------------------------- PROTECTED CONSTRUCTOR ------------------------------------\\

	protected BarNote(Pitch pitch, Value value, boolean isTied) {
		super(pitch, value);
		this.isTied = isTied;
	}
	
	
	//---------------------------------------- STATE ---------------------------------------------\\

	public boolean isTied() {
		return isTied;
	}
}
