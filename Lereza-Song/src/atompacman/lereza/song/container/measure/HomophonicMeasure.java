package atompacman.lereza.song.container.measure;

import java.util.ArrayList;
import java.util.List;

import atompacman.lereza.common.solfege.RythmicSignature;
import atompacman.lereza.song.container.notation.Notation;

public class HomophonicMeasure extends Measure {

	private List<List<Notation>> notes;
	
	
	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////
	
	public HomophonicMeasure(RythmicSignature rythmicSignature) {
		super(rythmicSignature);
		this.notes = new ArrayList<List<Notation>>();
	}
	

	//////////////////////////////
	//      STATUS CHECKER      //
	//////////////////////////////

	public boolean noteFitsIntoMeasure(int noteLength) {
		// TODO Auto-generated method stub
		return false;
	}

	public int maxNbNotes() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getRemainingSpace() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isFull() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////

	public List<List<Notation>> getNotes(){
		return notes;
	}
}
