package com.atompacman.lereza.profile.harmonic;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.piece.container.Note;
import com.atompacman.lereza.piece.container.Part;
import com.atompacman.lereza.piece.container.Note.NoteStatus;
import com.atompacman.lereza.piece.tool.PBT;
import com.atompacman.lereza.profile.ProfilabilityProblem;
import com.atompacman.lereza.solfege.Value;

public class UnmonophonicTimeunitProblem extends ProfilabilityProblem {

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
	
	private PBT pbt;
	private List<NoteStatus> noteStatus;
	private List<Value> noteValues;
	
	
	//------------ CONSTRUCTORS ------------\\

	public UnmonophonicTimeunitProblem(PBT pbt, Part part) {
		this.problemName = "Un-monophonic timeunit";
		this.pbt = pbt;
		this.noteStatus = new ArrayList<NoteStatus>();
		this.noteValues = new ArrayList<Value>();
		for (Note note : part.getBarNo(pbt.bar).getNotes().get(pbt.timeunit)) {
			noteStatus.log(note.getStatus());
			noteValues.add(note.getValue());
		}
	}
	
	
	//------------ FORMAT ------------\\

	public String formatProblem() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(pbt.toString());
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
