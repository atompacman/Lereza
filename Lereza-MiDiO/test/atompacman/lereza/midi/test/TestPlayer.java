package atompacman.lereza.midi.test;

import atompacman.atomLog.Log;
import atompacman.atomLog.Log.Verbose;
import atompacman.leraza.midi.container.MIDIFile;
import atompacman.leraza.midi.device.MIDIFilePlayer;
import atompacman.leraza.midi.device.MIDIFileReader;

public class TestPlayer {
	
	private static MIDIFileReader midiFileReader = new MIDIFileReader();
	private static MIDIFilePlayer midiFilePlayer = new MIDIFilePlayer();
	
	public static void main(String[] args) throws InterruptedException {
		Log.setVerbose(Verbose.INFOS);
		
		midiFilePlayer.initialize();
		
		if (args.length >= 1) {
			//readAndPlaySong(args[0]);
		}
		if (args.length >= 2) {
			readAndPlaySong(args[1]);
		}
	}
	
	public static void readAndPlaySong(String path) {
		MIDIFile midiFile = midiFileReader.readFile(path);
		midiFilePlayer.playMIDIFile(midiFile);
	}
}
