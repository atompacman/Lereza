package atompacman.lereza.song.container.piece;

import java.util.ArrayList;
import java.util.List;

import atompacman.lereza.common.solfege.RythmicSignature;
import atompacman.lereza.song.container.form.fugue.section.Section;
import atompacman.lereza.song.container.piece.Piece;

public class Piece {
	
	protected List<Section> sections;
	protected RythmicSignature rythmicSignature;
	
	
	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////
	
	public Piece() {
		this.sections = new ArrayList<Section>();
	}
	
	
	//////////////////////////////
	//         SETTERS          //
	//////////////////////////////
	
	public void addSection(Section section) {
		this.sections.add(section);
	}

	public void setRythmicSignature(RythmicSignature rythmicSignature) {
		this.rythmicSignature = rythmicSignature;
	}
	
	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////
	
	public Section getSection(int sectionIndex) {
		return sections.get(sectionIndex);
	}

	public RythmicSignature getRythmicSignature() {
		return rythmicSignature;
	}
}
