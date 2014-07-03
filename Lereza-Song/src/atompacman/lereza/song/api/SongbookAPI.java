package atompacman.lereza.song.api;


import atompacman.lereza.common.architecture.DeviceAPI;
import atompacman.lereza.common.solfege.Genre.Subgenre;

public interface SongbookAPI extends DeviceAPI {

	void addSongToSet(String songName, String composer, String setName, String filePath);
	void addSong(String songName, String composer, String filePath);
	void addSong(String songName, String filePath);

	void newCompositionSet(String setName, Subgenre subgenre);
}
