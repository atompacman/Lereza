package atompacman.lereza.song.container;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.container.MidiFile;
import atompacman.lereza.song.container.piece.Piece;

public class Composition {
	
	private String name;
	private String composer;
	private Piece piece;
	private MidiFile file;


	//////////////////////////////
	//       CONSTRUCTOR        //
	//////////////////////////////
	
	public Composition(String name, String composer, Piece piece, MidiFile file) {
		this.name = name;
		this.composer = composer;
		this.piece = piece;
		this.file = file;
		Log.infos("Composition \"" + name + "\" created from the midi file \"" + file.getFilePath() + "\".");
	}
	
	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////
	
	public String getName() {
		return name;
	}

	public String getComposer() {
		return composer;
	}
	
	public Piece getPiece() {
		return piece;
	}

	public MidiFile getFile() {
		return file;
	}
}