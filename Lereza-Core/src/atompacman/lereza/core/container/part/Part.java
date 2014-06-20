package atompacman.lereza.core.container.part;

import atompacman.leraza.midi.container.MIDINote;
import atompacman.lereza.core.container.measure.MonophonicMeasure;
import atompacman.lereza.core.solfege.RythmicSignature;

public abstract class Part {

	protected RythmicSignature rythmicSignature;
	
	
	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////
	
	public Part(RythmicSignature rythmicSignature) {
		this.rythmicSignature = rythmicSignature;
	}
	
	
	//////////////////////////////
	//       ADD NOTATION       //
	//////////////////////////////
	
	public abstract void addNote(MIDINote note);
	
	public abstract void addRest(int midiLength);
	
	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////
	
	public abstract MonophonicMeasure lastMeasure();
	
	public abstract MonophonicMeasure beforeLastMeasure();
	
	public RythmicSignature getRythmicSignature() {
		return rythmicSignature;
	}
}
