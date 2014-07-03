package atompacman.lereza.song;

import atompacman.lereza.common.architecture.Manager;
import atompacman.lereza.song.device.Songbook;
import atompacman.lereza.song.device.PieceBuilder;

public class SongManager implements Manager {

	public static Songbook 	   songbook     = new Songbook();
	public static PieceBuilder pieceBuilder = new PieceBuilder();
}
