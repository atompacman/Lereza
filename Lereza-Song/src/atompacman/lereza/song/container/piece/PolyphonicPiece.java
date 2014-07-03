package atompacman.lereza.song.container.piece;

import java.util.ArrayList;
import java.util.List;

import atompacman.lereza.song.container.part.Voice;

public class PolyphonicPiece extends Piece {
	
	protected List<Voice> voices;
	
	
	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////
	
	public PolyphonicPiece() {
		super();
		this.voices = new ArrayList<Voice>();
	}
	
	
	//////////////////////////////
	//         SETTERS          //
	//////////////////////////////
	
	public void addVoice(Voice voice) {
		this.voices.add(voice);
	}
	
	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////
	
	public Voice getVoice(int voiceIndex) {
		return voices.get(voiceIndex);
	}
}
