package atompacman.lereza.song;

import atompacman.lereza.common.architecture.ManagerAPI;
import atompacman.lereza.song.device.PieceBuilder;
import atompacman.lereza.song.device.Songbook;

public class SongManager implements ManagerAPI {

	public static Songbook 	   songbook     = new Songbook();
	public static PieceBuilder pieceBuilder = new PieceBuilder();
}
