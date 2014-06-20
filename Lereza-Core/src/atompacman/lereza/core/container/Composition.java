package atompacman.lereza.core.container;

import java.util.ArrayList;
import java.util.List;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.container.MIDIFile;
import atompacman.lereza.core.container.piece.Piece;

public class Composition {
	
	private String name;
	private List<MusicFile> musicFiles;
	
	private class MusicFile {
		
		private MIDIFile file;
		private Piece piece;
		
		
		public MusicFile(MIDIFile file, Piece piece) {
			this.file = file;
			this.piece = piece;
		}
		
		public MIDIFile getFile() {
			return file;
		}
		
		public Piece getPiece() {
			return piece;
		}
	}
	
	
	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////
	
	public Composition(String compositionName) {
		this.name = compositionName;
		this.musicFiles = new ArrayList<MusicFile>();
		Log.normalMsg("Composition \"" + name + "\" created.");
	}

	
	//////////////////////////////
	//        ADD FILE          //
	//////////////////////////////
	
	public void addFile(MIDIFile midiFile, Piece piece) {
		musicFiles.add(new MusicFile(midiFile, piece));
		Log.normalMsg("Piece \"" + piece.getName() + "\" (" + midiFile.getFile().getPath() + ") to composition \"" + name + "\".");
	}
	
	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////
	
	public String getCompositionName() {
		return name;
	}

	public Piece getPiece() {
		return musicFiles.get(0).getPiece();
	}
	
	public Piece getPiece(int index) {
		if (index >= musicFiles.size()) {
			Log.errorMsg("Cannot get piece at index \"" + index + "\" of composition \"" + name + "\".");
			return null;
		}
		return musicFiles.get(index).getPiece();
	}
	
	public MIDIFile getFile() {
		return musicFiles.get(0).getFile();
	}
	
	public MIDIFile getFiles(int index) {
		if (index >= musicFiles.size()) {
			Log.errorMsg("Cannot get piece at index \"" + index + "\" of composition \"" + name + "\".");
			return null;
		}
		return musicFiles.get(index).getFile();
	}
	
	public int getNbPieces() {
		return musicFiles.size();
	}
}