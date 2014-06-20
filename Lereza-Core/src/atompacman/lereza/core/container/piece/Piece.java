package atompacman.lereza.core.container.piece;

import java.util.ArrayList;
import java.util.List;

import atompacman.lereza.core.container.form.fugue.section.Section;
import atompacman.lereza.core.container.piece.Piece;
import atompacman.lereza.core.solfege.RythmicSignature;

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
