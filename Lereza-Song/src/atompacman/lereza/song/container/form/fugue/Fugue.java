package atompacman.lereza.song.container.form.fugue;

import java.util.ArrayList;
import java.util.List;

import atompacman.lereza.song.container.piece.PolyphonicPiece;

public class Fugue extends PolyphonicPiece {
	
	public enum FugueType { CANON, FUGUE, DOUBLE_FUGUE };
	
	private List<Subject> subjects;
	private FugueType type;
	
	
	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////
	
	public Fugue() {
		super();
		this.subjects = new ArrayList<Subject>();
		this.type = FugueType.FUGUE;
	}
	

	//////////////////////////////
	//         SETTERS          //
	//////////////////////////////
	
	public void addSubjects(Subject subject) {
		this.subjects.add(subject);
	}

	public void setType(FugueType type) {
		this.type = type;
	}

	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////	
	
	public FugueType getType() {
		return type;
	}
	
	public Subject getSubject(int subjectIndex) {
		return subjects.get(subjectIndex);
	}
}
