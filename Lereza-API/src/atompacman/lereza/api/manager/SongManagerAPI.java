package atompacman.lereza.api.manager;

import atompacman.lereza.song.SongManager;
import atompacman.lereza.song.api.SongbookAPI;
import atompacman.lereza.song.device.PieceBuilder;

public class SongManagerAPI {

	public PieceBuilder builder  = SongManager.pieceBuilder;
	public SongbookAPI	songBook = SongManager.songbook;
}
