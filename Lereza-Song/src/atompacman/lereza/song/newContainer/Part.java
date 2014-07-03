package atompacman.lereza.song.newContainer;

import java.util.List;

import atompacman.lereza.common.solfege.RythmicSignature;

public class Part {

	private List<Measure> measures;
	private RythmicSignature rythmicSignature;
	
	
	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////
	
	public Part(RythmicSignature rythmicSignature) {
		this.rythmicSignature = rythmicSignature;
	}
	
	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////

	public RythmicSignature getRythmicSignature() {
		return rythmicSignature;
	}
}
