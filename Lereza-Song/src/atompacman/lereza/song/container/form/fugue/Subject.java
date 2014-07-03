package atompacman.lereza.song.container.form.fugue;

import java.util.List;

import atompacman.lereza.common.solfege.Key;
import atompacman.lereza.song.container.measure.MonophonicMeasure;

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
