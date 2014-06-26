package atompacman.lereza.core.builder;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.container.MIDIFile;
import atompacman.lereza.core.builder.builder.PolyphonicBuilder;
import atompacman.lereza.core.container.piece.Piece;
import atompacman.lereza.core.container.piece.PolyphonicPiece;

public class PieceFactory {


	/**
	 * Converts a {@link MIDIFile} into an object that extends {@link Piece}, which is the data structure
	 * representing a musical piece that can be processed by the program.
	 *
	 * @param midiFile | A {@link MIDIFile}
	 * @param musicalForm | A {@link Class} object indicating the musical form of the piece 
	 * @return An object that extends {@link Piece}
	 */
	public Piece build(MIDIFile midiFile, Class<? extends Piece> musicalForm) {
		if (PolyphonicPiece.class.isAssignableFrom(musicalForm)) {
			PolyphonicBuilder polyphonicBuilder = new PolyphonicBuilder(midiFile);
			return polyphonicBuilder.build(musicalForm.asSubclass(PolyphonicPiece.class));
		}
		Log.error("Trying to build a piece in a texture that isn's supported by the builders.");
		return null;
	}
}
