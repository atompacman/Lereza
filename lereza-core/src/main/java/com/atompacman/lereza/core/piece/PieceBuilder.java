package com.atompacman.lereza.core.piece;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.core.midi.sequence.MIDISequence;
import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.RythmicSignature;

public class PieceBuilder extends PieceComponentBuilder<Piece<Stack<Note>>> {
	
	//======================================= FIELDS =============================================\\

	private final List<PartBuilder> builders;
	private final RythmicSignature rythmicSign;
	private final MIDISequence midiSeq;
	
	private int  currPart;
	private int  currBegTU;
	private int  currLenTU;
	private byte currVelocity;
	
	
	
	//======================================= METHODS ============================================\\

	//------------------------------ PUBLIC STATIC CONSTRUCTORS ----------------------------------\\

	public static PieceBuilder create() {
		return new PieceBuilder(null, RythmicSignature.STANDARD_4_4, new PieceBuilderSupervisor());
	}
	
	public static PieceBuilder create(RythmicSignature rythmicSignature) {
		return new PieceBuilder(null, rythmicSignature, new PieceBuilderSupervisor());
	}
	
	public static PieceBuilder create(MIDISequence midiSequence) {
		return new PieceBuilder(midiSequence, midiSequence.getRythmicSignature(), 
				new PieceBuilderSupervisor());
	}
	
	public static PieceBuilder create(MIDISequence midiSequence,PieceBuilderSupervisor supervisor) {
		return new PieceBuilder(midiSequence, midiSequence.getRythmicSignature(), supervisor);
	}
	
	
	//---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

	private PieceBuilder(MIDISequence midiSeq, RythmicSignature rythmicSign, 
			PieceBuilderSupervisor supervisor) {
		
		super(supervisor);
		
		this.builders = new ArrayList<>();
		this.rythmicSign = rythmicSign;
		this.midiSeq = midiSeq;
		
		this.currPart = 0;
		this.currBegTU = 0;
		this.currLenTU = 32;
		this.currVelocity = 100;
	}
	

	//----------------------------------------- ADD ----------------------------------------------\\

	public PieceBuilder add(Pitch pitch) {
		while (currPart >= builders.size()) {
			builders.add(new PartBuilder(rythmicSign, supervisor));
		}
		builders.get(currPart).add(pitch, currVelocity, currBegTU, currLenTU);
		return this;
	}
	
	public PieceBuilder add(Pitch pitch, int begTU) {
		return pos(begTU).add(pitch);
	}
	
	public PieceBuilder add(Pitch pitch, int begTU, int lengthTU) {
		return pos(begTU).length(lengthTU).add(pitch);
	}
	
	public PieceBuilder add(Pitch pitch, byte velocity, int part, int begTU, int lengthTU) {
		return part(part).pos(begTU).length(lengthTU).velocity(velocity).add(pitch);
	}
	
	public PieceBuilder add(byte hexNote, byte velocity, int part, int begTU, int lengthTU) {
		Pitch pitch = Pitch.thatIsMoreCommonForHexValue(hexNote);
		return part(part).pos(begTU).length(lengthTU).velocity(velocity).add(pitch);
	}
	
	public PieceBuilder part(int partIndex) {
		this.currPart = partIndex;
		return this;
	}
	
	public PieceBuilder pos(int timeunit) {
		this.currBegTU = timeunit;
		return this;
	}
	
	public PieceBuilder length(int noteLenTU) {
		this.currLenTU = noteLenTU;
		return this;
	}

	public PieceBuilder velocity(byte velocity) {
		this.currVelocity = velocity;
		return this;
	}
	

	//---------------------------------------- BUILD ---------------------------------------------\\

	public Piece<Stack<Note>> buildComponent() {
		List<Part<Stack<Note>>> parts = new ArrayList<>();
		for (PartBuilder builder : builders) {
			parts.add(builder.build());
		}
		return new Piece<Stack<Note>>(parts, rythmicSign, midiSeq);
	}
	
	
	//---------------------------------------- RESET ---------------------------------------------\\

	protected void reset() {
		builders.clear();		
	}
}
