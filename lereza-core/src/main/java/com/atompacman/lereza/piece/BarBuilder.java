package com.atompacman.lereza.piece;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.atomlog.Log;
import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.RythmicSignature;
import com.atompacman.lereza.solfege.Value;

public class BarBuilder extends PieceComponentBuilder<Bar<Stack<Note>>> {

	//======================================= FIELDS =============================================\\

	private final List<StackBuilder> builders;
	private final RythmicSignature 	 rythmicSign;
	
	private int currBegTU;
	private int currLenTU;
	private byte currVelocity;
	private boolean currIsTied;
	
	

	//======================================= METHODS ============================================\\

	//------------------------------ PUBLIC STATIC CONSTRUCTORS ----------------------------------\\

	public static BarBuilder create() {
		return new BarBuilder(RythmicSignature.STANDARD_4_4, new PieceBuilderSupervisor());
	}
	
	public static BarBuilder create(RythmicSignature rythmicSign) {
		return new BarBuilder(rythmicSign, new PieceBuilderSupervisor());
	}
	
	
	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\
		
	BarBuilder(RythmicSignature rythmicSign, PieceBuilderSupervisor supervisor) {
		super(supervisor);
		this.builders = new ArrayList<>();
		for (int i = 0; i < rythmicSign.timeunitsInABar(); ++i) {
			builders.add(new StackBuilder(supervisor));
		}
		this.rythmicSign = rythmicSign;
	}


	//----------------------------------------- ADD ----------------------------------------------\\

	public BarBuilder add(Pitch pitch) {
		int noteStartPos = 0;	
		int tuInBar = rythmicSign.timeunitsInABar();
		Piece.checkBoundedTU(currBegTU, tuInBar, "note beginning", "bar length");
		Piece.checkBoundedTU(currBegTU + currLenTU, tuInBar, "note end", "bar end");
		
		for (Value value : splitIntoValues(currBegTU, currBegTU + currLenTU)) {
			if (currIsTied) {
				getBuilder(noteStartPos).addStarted(pitch, value, currVelocity, true);
			} else {
				getBuilder(noteStartPos).add(pitch, value, currVelocity);
			}
			for (int j = noteStartPos + 1; j < value.toTimeunit(); ++j) {
				getBuilder(j).addStarted(pitch, value, currVelocity, currIsTied);
			}
			// TODO log this in the supervisor
			if (Log.extra() && Log.print(String.format("Adding note %4s at timeunit %4d.", 
					Note.valueOf(pitch, value, currIsTied).toCompleteString(),noteStartPos)));	
			
			noteStartPos += value.toTimeunit();
		}
		return this;
	}
	
	public BarBuilder add(Pitch pitch, int begTU) {
		return pos(begTU).add(pitch);
	}
	
	public BarBuilder add(Pitch pitch, int begTU, int lengthTU) {
		return pos(begTU).length(lengthTU).add(pitch);
	}
	
	public BarBuilder add(Pitch pitch, byte velocity, int begTU, int lengthTU, boolean isTied) {
		return pos(begTU).length(lengthTU).velocity(velocity).isTied(isTied).add(pitch);
	}
	
	public BarBuilder pos(int timeunit) {
		this.currBegTU = timeunit;
		return this;
	}
	
	public BarBuilder length(int noteLenTU) {
		this.currLenTU = noteLenTU;
		return this;
	}

	public BarBuilder velocity(byte velocity) {
		this.currVelocity = velocity;
		return this;
	}

	public BarBuilder isTied(boolean isTied) {
		this.currIsTied = isTied;
		return this;
	}
	
	private static List<Value> splitIntoValues(int noteStart, int noteEnd) {
		List<Value> values = new ArrayList<>();
		int noteLength = noteEnd - noteStart;

		for (int i = Value.values().length - 1; i >= 0; --i) {
			Value value = Value.values()[i];
			int valueLength = value.toTimeunit();

			if (valueLength > noteLength) {
				continue;
			}
			int valueStart = 0;
			while (valueStart < noteStart) {
				valueStart += valueLength;
			}
			int valueEnd = valueStart + valueLength;

			if (valueEnd > noteEnd) {
				continue;
			}
			if (noteStart < valueStart) {
				values.addAll(splitIntoValues(noteStart, valueStart));
			}
			values.add(value);

			if (valueEnd < noteEnd) {
				values.addAll(splitIntoValues(valueEnd, noteEnd));
			}
			break;
		}

		return values;
	}
	
	private StackBuilder getBuilder(int timeunit) {
		while (timeunit >= builders.size()) {
			builders.add(new StackBuilder(supervisor));
		}
		return builders.get(timeunit);
	}
	
	
	//---------------------------------------- BUILD ---------------------------------------------\\

	protected Bar<Stack<Note>> buildComponent() {
		List<Stack<? extends Note>> noteStacks = new ArrayList<>();	
		for (StackBuilder builder : builders) {
			noteStacks.add(builder.build());
		}
		return new Bar<Stack<Note>>(noteStacks, rythmicSign);
	}


	//---------------------------------------- RESET ---------------------------------------------\\

	protected void reset() {
		builders.clear();		
	}
}
