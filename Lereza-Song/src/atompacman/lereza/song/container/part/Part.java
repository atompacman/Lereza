package atompacman.lereza.song.container.part;

import atompacman.leraza.midi.container.MidiNote;
import atompacman.lereza.common.solfege.RythmicSignature;
import atompacman.lereza.song.container.measure.MonophonicMeasure;
import atompacman.lereza.song.exception.BuilderException;

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
	
	public abstract void addNote(MidiNote note) throws BuilderException;
	
	public abstract void addRest(int midiLength) throws BuilderException;
	
	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////
	
	public abstract MonophonicMeasure lastMeasure();
	
	public abstract MonophonicMeasure beforeLastMeasure();
	
	public RythmicSignature getRythmicSignature() {
		return rythmicSignature;
	}
}
