package atompacman.lereza.song.newContainer;

import java.util.List;

import atompacman.lereza.common.solfege.RythmicSignature;
import atompacman.lereza.song.container.notation.Notation;

public class Measure {

	private List<List<Notation>> notes;
	private RythmicSignature rythmicSignature;
	
	
	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////

	public Measure(RythmicSignature rythmicSignature) {
		this.rythmicSignature = rythmicSignature;
	}
	
	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////

	public RythmicSignature getRythmicSignature() {
		return rythmicSignature;
	}
}
