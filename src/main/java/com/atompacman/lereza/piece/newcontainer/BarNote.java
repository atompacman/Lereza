package com.atompacman.lereza.piece.newcontainer;

import com.atompacman.lereza.solfege.Dynamic;
import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.Value;

public class BarNote extends Note {

	//======================================= FIELDS =============================================\\

	private boolean isTied;

	
	
	//======================================= METHODS ============================================\\
	
	//------------------------------ PUBLIC STATIC CONSTRUCTOR -----------------------------------\\
	
	public static BarNote valueOf(int hexValue, Value value, int hexVelocity) {
		return valueOf(hexValue, value, hexVelocity, false);
	}
	
	public static BarNote valueOf(int hexPitch, Value value, int hexVelocity, boolean isTied) {
		Pitch pitch = Pitch.thatIsMoreCommonForHexValue(hexPitch);
		Dynamic dynamic = Dynamic.valueOf(hexVelocity);
		return new BarNote(pitch, value, dynamic, isTied);
	}
	
	public static BarNote valueOf(Pitch pitch, Value value, Dynamic dynamic) {
		return new BarNote(pitch, value, dynamic, false);
	}
	
	public static BarNote valueOf(Pitch pitch, Value value, Dynamic dynamic, boolean isTied) {
		return new BarNote(pitch, value, dynamic, isTied);
	}
	

	//--------------------------------- PROTECTED CONSTRUCTOR ------------------------------------\\

	protected BarNote(Pitch pitch, Value value, Dynamic dynamic, boolean isTied) {
		super(pitch, value, dynamic);
		this.isTied = isTied;
	}
	
	
	//---------------------------------------- STATE ---------------------------------------------\\

	public boolean isTied() {
		return isTied;
	}
}
