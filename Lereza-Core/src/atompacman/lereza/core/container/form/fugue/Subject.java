package atompacman.lereza.core.container.form.fugue;

import java.util.List;

import atompacman.lereza.core.container.measure.MonophonicMeasure;
import atompacman.lereza.core.solfege.Key;

public class Subject {
	
	private List<MonophonicMeasure> notes;
	private Key originalKey;
	
	public List<MonophonicMeasure> getNotes(){
		return notes;
	}

	public Key getKey(){
		return originalKey;
	}
}
