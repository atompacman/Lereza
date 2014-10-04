package com.atompacman.lereza.core.profile.harmonic;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.common.solfege.Value;
import com.atompacman.lereza.core.piece.container.Note;
import com.atompacman.lereza.core.piece.container.Note.NoteStatus;
import com.atompacman.lereza.core.piece.container.Part;
import com.atompacman.lereza.core.piece.tool.PMT;
import com.atompacman.lereza.core.profile.Problem;

public class UnmonophonicTimeunitProblem extends Problem {

	public enum UnmonophonicTimeunitProblemDiagnostic implements Diagnostic {
		
		END_OF_SONG_CHORD		 (Recoverability.EASY), 
		SIMULTANEOUS_QUICK_NOTES (Recoverability.NORMAL), 
		MINOR_OVERLAPPING		 (Recoverability.NORMAL), 
		MAJOR_OVERLAPPING		 (Recoverability.HARD);
		
		private Recoverability recoverability;
		
		
		private UnmonophonicTimeunitProblemDiagnostic(Recoverability recoverability) {
			this.recoverability = recoverability;
		}
		
		public Recoverability recoverability() {
			return recoverability;
		}
	}
	
	private PMT pmt;
	private List<NoteStatus> noteStatus;
	private List<Value> noteValues;
	private int nbMeasures;
	
	
	//------------ CONSTRUCTORS ------------\\

	public UnmonophonicTimeunitProblem(PMT pmt, Part part) {
		this.problemName = "Un-monophonic timeunit";
		this.pmt = pmt;
		this.noteStatus = new ArrayList<NoteStatus>();
		this.noteValues = new ArrayList<Value>();
		for (Note note : part.getMeasureNo(pmt.measure).getNotes().get(pmt.timeunit)) {
			noteStatus.add(note.getStatus());
			noteValues.add(note.getValue());
		}
		this.nbMeasures = part.getNbMeasures();
	}
	
	
	//------------ FORMAT ------------\\

	public String formatProblem() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(pmt.toString());
		builder.append("->");
		
		for (int i = 0; i < noteStatus.size(); ++i) {
			builder.append('[');
			builder.append(noteStatus.get(i).name().toLowerCase().replace("_", " "));
			builder.append("|");
			builder.append(noteValues.get(i).toTimeunit());
			builder.append(']');
		}
		
		return builder.toString();
	}
	
	
	//------------ DIAGNOSTIC ------------\\

	public void diagnostic() {
		// TODO Auto-generated method stub
	}
	
	
	//------------ RECOVER ------------\\

	public void recover() {
		// TODO Auto-generated method stub
	}
}
