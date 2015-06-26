package com.atompacman.lereza.core.piece;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.RythmicSignature;

public final class PartBuilder extends PieceComponentBuilder<Part<Stack<Note>>> {

	//======================================= FIELDS =============================================\\

	private final List<BarBuilder> 	builders;
	private final RythmicSignature 	rythmicSign;

	private int  currBegTU;
	private int  currLenTU;
	private byte currVelocity;
	
	
	
	//======================================= METHODS ============================================\\

	//------------------------------ PUBLIC STATIC CONSTRUCTORS ----------------------------------\\

	public static PartBuilder create() {
		return new PartBuilder(RythmicSignature.STANDARD_4_4, new PieceBuilderSupervisor());
	}
	
	public static PartBuilder create(RythmicSignature rythmicSign) {
		return new PartBuilder(rythmicSign, new PieceBuilderSupervisor());
	}
	
	
	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\
		
	PartBuilder(RythmicSignature rythmicSign, PieceBuilderSupervisor supervisor) {
		super(supervisor);
		
		this.builders = new ArrayList<>();
		this.rythmicSign = rythmicSign;
		
		this.currBegTU = 0;
		this.currLenTU = 32;
		this.currVelocity = 100;
	}
	

	//----------------------------------------- ADD ----------------------------------------------\\

	public PartBuilder add(Pitch pitch) {
		return addImpl(pitch, currVelocity, currBegTU, currLenTU);
	}
	
	public PartBuilder add(Pitch pitch, int begTU) {
		return pos(begTU).add(pitch);
	}
	
	public PartBuilder add(Pitch pitch, int begTU, int lengthTU) {
		return pos(begTU).length(lengthTU).add(pitch);
	}
	
	public PartBuilder add(Pitch pitch, byte velocity, int begTU, int lengthTU) {
		return pos(begTU).length(lengthTU).velocity(velocity).add(pitch);
	}
	
	private PartBuilder addImpl(Pitch pitch, byte velocity, int begTU, int lenTU) {
		int tuInBar = rythmicSign.timeunitsInABar();
		int barPosTU = begTU % tuInBar;
		int actualLen = lenTU;

		if (barPosTU + lenTU > tuInBar) {
			actualLen = tuInBar - barPosTU;
		}
		builderAt(begTU).add(pitch, velocity, barPosTU, actualLen, false);
		
		lenTU -= actualLen;
		
		while (lenTU != 0) {
			begTU += actualLen;
			if (lenTU > tuInBar) {
				actualLen = tuInBar;
			} else {
				actualLen = lenTU;
			}
			builderAt(begTU).add(pitch, velocity, 0, actualLen, true);
			lenTU -= actualLen;
		}
		return this;
	}
	
	public PartBuilder pos(int timeunit) {
		this.currBegTU = timeunit;
		return this;
	}
	
	public PartBuilder length(int noteLenTU) {
		this.currLenTU = noteLenTU;
		return this;
	}

	public PartBuilder velocity(byte velocity) {
		this.currVelocity = velocity;
		return this;
	}

	private BarBuilder builderAt(int timestamp) {
		int barNum = (int)((double) timestamp / (double) rythmicSign.timeunitsInABar());
		while (barNum >= builders.size()) {
			builders.add(new BarBuilder(rythmicSign, supervisor));
		}
		return builders.get(barNum);
	}
	
	
	//---------------------------------------- BUILD ---------------------------------------------\\

	public Part<Stack<Note>> buildComponent() {
		List<Bar<Stack<Note>>> bars = new ArrayList<>();
		for (BarBuilder builder : builders) {
			bars.add(builder.build());
		}
		return new Part<Stack<Note>>(bars, rythmicSign);
	}
	
	
	//---------------------------------------- RESET ---------------------------------------------\\

	public void reset() {
		builders.clear();
	}
}
