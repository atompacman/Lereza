package atompacman.lereza.core.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.container.MIDIFile;
import atompacman.lereza.core.builder.builder.PolyphonicBuilder;
import atompacman.lereza.core.container.piece.Piece;
import atompacman.lereza.core.container.piece.PolyphonicPiece;

public class PieceFactory {

	protected Stack<MIDIFile> midiFiles;
	
	
	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////
	
	public PieceFactory() {
		this.midiFiles = new Stack<MIDIFile>();
	}

	
	//////////////////////////////
	//           LOAD           //
	//////////////////////////////
	
	public void load(MIDIFile file) {
		this.midiFiles.push(file);
		Log.normalMsg("File \"" + file.getFile().getPath() + "\" loaded in the builder.");
	}
	
	public void load(List<MIDIFile> files) {
		for (MIDIFile file : files) {
			load(file);
		}
	}
	

	//////////////////////////////
	//          BUILD           //
	//////////////////////////////
	
	public Piece build(Class<? extends Piece> musicalForm) {
		if (midiFiles.isEmpty()) {
			Log.errorMsg("Trying to build a piece without any midi file loaded in the factory.");
			return null;
		}
		if (PolyphonicPiece.class.isAssignableFrom(musicalForm)) {
			PolyphonicBuilder polyphonicBuilder = new PolyphonicBuilder(midiFiles.pop());
			return polyphonicBuilder.build(musicalForm.asSubclass(PolyphonicPiece.class));
		}
		Log.errorMsg("Trying to build a piece in a texture that isn's supported by the builders.");
		return null;
	}
	
	public List<Piece> buildAll(List<Class<? extends Piece>> musicalForms) {
		List<Piece> pieces = new ArrayList<Piece>();
		
		for (Class<? extends Piece> musicalForm : musicalForms) {
			pieces.add(build(musicalForm));
		}
		return pieces;
	}
}
