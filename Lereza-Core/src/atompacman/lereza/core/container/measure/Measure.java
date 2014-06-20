package atompacman.lereza.core.container.measure;

import atompacman.lereza.core.solfege.RythmicSignature;

public abstract class Measure {

	protected RythmicSignature rythmicSignature;
	
	
	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////

	public Measure(RythmicSignature rythmicSignature) {
		this.rythmicSignature = rythmicSignature;
	}

	
	//////////////////////////////
	//      STATUS CHECKER      //
	//////////////////////////////

	public abstract boolean noteFitsIntoMeasure(int noteLength);

	public abstract int maxNbNotes();

	public abstract int getRemainingSpace();

	public abstract boolean isFull();

	public abstract boolean isEmpty();


	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////

	public RythmicSignature getRythmicSignature() {
		return rythmicSignature;
	}
}
