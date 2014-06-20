package atompacman.lereza.core.container.part;

import atompacman.leraza.midi.container.MIDINote;
import atompacman.lereza.core.container.measure.MonophonicMeasure;
import atompacman.lereza.core.solfege.RythmicSignature;

public class Hand extends Part {

	
	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////
	
	public Hand(RythmicSignature rythmicSignature) {
		super(rythmicSignature);
		// TODO Auto-generated constructor stub
	}



	//////////////////////////////
	//       ADD NOTATION       //
	//////////////////////////////
	
	public void addNote(MIDINote note) {
		// TODO Auto-generated method stub
		
	}


	public void addRest(int midiLength) {
		// TODO Auto-generated method stub
		
	}


	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////
	
	public MonophonicMeasure lastMeasure() {
		// TODO Auto-generated method stub
		return null;
	}


	public MonophonicMeasure beforeLastMeasure() {
		// TODO Auto-generated method stub
		return null;
	}

}
