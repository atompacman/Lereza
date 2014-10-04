package atompacman.lereza.prototype;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.atomLog.Log;
import com.atompacman.atomLog.Log.Verbose;

import atompacman.leraza.midi.io.MidiFilePlayer;
import atompacman.lereza.common.solfege.Interval;
import atompacman.lereza.common.solfege.Pitch;
import atompacman.lereza.common.solfege.ScaleDegree;
import atompacman.lereza.common.solfege.Value;
import atompacman.lereza.prototype.probStructures.Prob3Dmatrix;
import atompacman.lereza.prototype.probStructures.Prob3DmatrixBuilder;
import atompacman.lereza.prototype.probStructures.ProbVector;
import atompacman.lereza.prototype.probStructures.ProbVectorBuilder;

/**
 * The really first profiler/generator, tested for the first time on the melody of Frere Jacques.
 * 
 * Date of creation : 		   July 5th 2014
 * Date of last modification : July 8th 2014
 * 
 * Best song on July 8th (after 20 tries): | C3  -  E3  -  C3  -  C3 | D3  -  E3  -  F3-G3-F3 | E3  -  C3  -  D3  -  E3 | C3  -  D3  -  E3  -  C3 |
 *                                           Fre    re     Jac   ques  Dor    mez    vou ou ou  Son    nnez   les    ma   les    ma     tin    ne
 */

public class SimpleIDVmatrixMonophonicProfiler {
	
	private static final int FINAL_LENGTH = 16;
	private static final int MAX_NB_ATTEMPTS = 500;
	
	private static Prob3Dmatrix<SimpleInterval, ScaleDegree, Value> idvMatrix;
	private static ProbVector<Pitch> pitchProbVector;
	private static ProbVector<Value> valueProbVector;
		
	private static List<NoteInput> notes;
	private static double totalLength;
	
	
	private enum SimpleInterval {
		DESC_PERFECT_FOURTH,
		DESC_MAJOR_THIRD,
		DESC_MAJOR_SECOND,
		DESC_MINOR_SECOND,
		UNISON,
		MAJOR_SECOND,
		MINOR_SECOND,
		MAJOR_THIRD,
		PERFECT_FOURTH;
	}
	
	private static class NoteInput {
		
		private Pitch pitch;
		private Value value;
		
		
		public NoteInput(Pitch pitch, Value value) {
			this.pitch = pitch;
			this.value = value;
		}
		
		public Pitch getPitch() {
			return pitch;
		}
		
		public Value getValue() {
			return value;
		}

		public String toString() {
			return pitch.toString() + " (" + value.name() + ")";
		}
	}

	
	public static void main(String args[]) {
		Log.setVerbose(Verbose.EXTRA);
		buildIDVmatrix();
		buildPitchProbVector();
		buildValueProbVector();
		int tries = 0;
		do {
			putInitialInput();
			updateTotalLength();
			++tries;
			int attempt = 0;
			while (totalLength < FINAL_LENGTH && attempt < MAX_NB_ATTEMPTS) {
				++attempt;
				NoteInput newInput = generateInput();
				if (keepTheInput(newInput)) {
					notes.add(newInput);
					updateTotalLength();
					if (Log.infos() && Log.print("Note \"" + newInput.toString() + "\" added to composition. Progress : " + notes.size() + "/" + FINAL_LENGTH));
					attempt = 0;
				}
			}
			if (attempt == MAX_NB_ATTEMPTS) {
				if (Log.infos() && Log.print("Generator dead-end. Current try: " + tries));
				if (Log.infos() && Log.title(1));
			}
		} while (totalLength != FINAL_LENGTH);

		for (NoteInput input : notes) {
			if (Log.vital() && Log.print(input.toString()));
			MidiFilePlayer.getPlayer().playNote(input.pitch.getSemitoneRank(), input.value.toTimeunit(), 8d);
		}
	}
	
	private static void buildIDVmatrix() {
		Prob3DmatrixBuilder<SimpleInterval, ScaleDegree, Value> builder = new Prob3DmatrixBuilder<SimpleInterval, ScaleDegree, Value>();
		
		builder.increment(SimpleInterval.DESC_PERFECT_FOURTH, ScaleDegree.V,   Value.QUARTER);
		builder.increment(SimpleInterval.DESC_MAJOR_THIRD, 	ScaleDegree.I,   Value.QUARTER);
		builder.increment(SimpleInterval.DESC_MAJOR_THIRD, 	ScaleDegree.I,   Value.QUARTER);
		builder.increment(SimpleInterval.DESC_MINOR_SECOND, 	ScaleDegree.III, Value.EIGHTH );
		builder.increment(SimpleInterval.DESC_MAJOR_SECOND, 	ScaleDegree.IV,  Value.EIGHTH );
		builder.increment(SimpleInterval.DESC_MAJOR_SECOND, 	ScaleDegree.V,   Value.EIGHTH );
		builder.increment(SimpleInterval.UNISON, 		  		ScaleDegree.I,   Value.QUARTER);
		builder.increment(SimpleInterval.UNISON, 		  		ScaleDegree.V,   Value.HALF   );
		builder.increment(SimpleInterval.MAJOR_SECOND,		ScaleDegree.II,  Value.QUARTER);
		builder.increment(SimpleInterval.MAJOR_SECOND, 		ScaleDegree.III, Value.QUARTER);
		builder.increment(SimpleInterval.MINOR_SECOND, 		ScaleDegree.IV,  Value.QUARTER);
		builder.increment(SimpleInterval.MAJOR_SECOND, 		ScaleDegree.V,   Value.QUARTER);
		builder.increment(SimpleInterval.MAJOR_SECOND, 		ScaleDegree.VI,  Value.EIGHTH );
		builder.increment(SimpleInterval.MAJOR_THIRD,   		ScaleDegree.III, Value.QUARTER);
		builder.increment(SimpleInterval.PERFECT_FOURTH, 		ScaleDegree.I,   Value.QUARTER);
		
		idvMatrix = builder.createProb3Dmatrix();
	}
	
	private static void buildPitchProbVector() {
		ProbVectorBuilder<Pitch> builder = new ProbVectorBuilder<Pitch>();
		
		builder.increment(Pitch.G2);
		builder.increment(Pitch.C3);
		builder.increment(Pitch.C3);
		builder.increment(Pitch.C3);
		builder.increment(Pitch.C3);
		builder.increment(Pitch.C3);
		builder.increment(Pitch.D3);
		builder.increment(Pitch.E3);
		builder.increment(Pitch.E3);
		builder.increment(Pitch.E3);
		builder.increment(Pitch.F3);
		builder.increment(Pitch.G3);
		builder.increment(Pitch.G3);
		builder.increment(Pitch.G3);
		builder.increment(Pitch.A3);
		
		pitchProbVector = builder.createProbVector();
	}
	
	private static void buildValueProbVector() {
		ProbVectorBuilder<Value> builder = new ProbVectorBuilder<Value>();

		builder.increment(Value.HALF); 
		builder.increment(Value.HALF); 
		builder.increment(Value.QUARTER); 
		builder.increment(Value.QUARTER); 
		builder.increment(Value.QUARTER); 
		builder.increment(Value.QUARTER); 
		builder.increment(Value.QUARTER); 
		builder.increment(Value.QUARTER); 
		builder.increment(Value.QUARTER); 
		builder.increment(Value.QUARTER); 
		builder.increment(Value.QUARTER); 
		builder.increment(Value.EIGHTH); 
		builder.increment(Value.EIGHTH); 
		builder.increment(Value.EIGHTH); 
		builder.increment(Value.EIGHTH);
		
		valueProbVector = builder.createProbVector();
	}
	
	private static void putInitialInput() {
		totalLength = 0;
		notes = new ArrayList<NoteInput>();
		notes.add(new NoteInput(Pitch.C3, Value.QUARTER));
	}
	
	private static NoteInput generateInput() {
		return new NoteInput(pitchProbVector.randomElement(), valueProbVector.randomElement());
	}
	
	private static boolean keepTheInput(NoteInput input) {
		if (totalLength + valueToLength(input.getValue()) > FINAL_LENGTH) {
			return false;
		}
		Interval interval = getInterval(input);
		ScaleDegree degree = getDegree(input);
		
		if (idvMatrix.keepEntry(interval, degree, lastInput().getValue())) {
			return true;
		} else {
			return false;
		}
	}
	
	private static ScaleDegree getDegree(NoteInput input) {
		return ScaleDegree.values()[input.getPitch().getTone().getNote().ordinal()];
	}
	
	private static Interval getInterval(NoteInput input) {
		return Interval.getSimpleInterval(lastInput().pitch, input.pitch);
	}
	
	private static void updateTotalLength() {
		totalLength += valueToLength(lastInput().getValue());
	}
	
	private static double valueToLength(Value value) {
		switch (value) {
		case EIGHTH:  return 0.5;
		case QUARTER: return 1;   
		case HALF:	  return 2;
		default:      
			if (Log.error() && Log.print("INVALID VALUE"));
			return 0;
		}
	}
	
	private static NoteInput lastInput() {
		return notes.get(notes.size() - 1);
	}
}
