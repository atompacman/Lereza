package atompacman.lereza.song.container.part;

import atompacman.leraza.midi.container.MidiNote;
import atompacman.lereza.common.solfege.RythmicSignature;
import atompacman.lereza.song.container.measure.MonophonicMeasure;

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
	
	public void addNote(MidiNote note) {
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
