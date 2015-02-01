package com.atompacman.lereza.api.cmd;

import java.util.List;

import com.atompacman.configuana.App;
import com.atompacman.configuana.Cmd;
import com.atompacman.configuana.CmdArgs;
import com.atompacman.configuana.CmdInfo;
import com.atompacman.configuana.Flag;
import com.atompacman.configuana.FlagInfo;
import com.atompacman.lereza.api.Wizard;
import com.atompacman.lereza.api.cmd.PlayMIDIFile.CmdFlag;
import com.atompacman.lereza.exception.InvalidArgumentException;
import com.atompacman.lereza.midi.MIDIManager;
import com.atompacman.toolkat.exception.Throw;

public class PlayMIDIFile implements Cmd<CmdFlag> {

	public enum CmdFlag implements Flag {
		TRACKS { public FlagInfo info() { return new FlagInfo(
				"tracks", "Played tracks", Flag.UNLIMITED_ARGS, "Play specific tracks");
		}},
		POSITION { public FlagInfo info() { return new FlagInfo(
				"pos", "Initial position", 1, "Initial playback position in milliseconds");
		}},
		BPM { public FlagInfo info() { return new FlagInfo(
				"bpm", "Tempo in BPM", 1, "Playback tempo in beats per minute");
		}},
		LOOP { public FlagInfo info() { return new FlagInfo(
				"loop", "Loop file", 0, "Enable loop playback");
		}};
	}

	public CmdInfo info() {
		return new CmdInfo("play", "Play MIDI file", 1, "Play a MIDI file on disk");
	}

	public void execute(App app, CmdArgs<CmdFlag> args) {
		MIDIManager manager = Wizard.initDevice(MIDIManager.class);
		manager.loadSequence(args.getMainArgs().get(0));

		int[] enablTracks = enabledTracks(args.getValues(CmdFlag.TRACKS));
		long playbackPos = playbackPos(args.getValue(CmdFlag.POSITION));
		Integer bpm = bpm(args.getValue(CmdFlag.BPM));
		boolean loopPlayback = args.hasFlag(CmdFlag.LOOP);

		manager.startPlayback(enablTracks, playbackPos, bpm, loopPlayback);
	}

	private int[] enabledTracks(List<String> paramValues) {
		int[] enablTracks;
		if (paramValues == null) {
			enablTracks = new int[0];
		} else {
			enablTracks = new int[paramValues.size()];
			
			for (int i = 0; i < paramValues.size(); ++i) {
				try {
					enablTracks[i] = Integer.parseInt(paramValues.get(i));
				} catch (Exception e) {
					Throw.aRuntime(InvalidArgumentException.class, 
							"Track numbers must be integers");
				}
			}
		}
		return enablTracks;
	}

	private long playbackPos(String paramValue) {
		if (paramValue == null) {
			return 0;
		}		
		try {
			return Long.parseLong(paramValue);
		} catch (Exception e) {
			Throw.aRuntime(InvalidArgumentException.class, "Initial position must be an integer");
		}
		return 0;
	}
	
	private int bpm(String paramValue) {
		if (paramValue == null) {
			return MIDIManager.DEFAULT_TEMPO;
		}	
		try {
			return Integer.parseInt(paramValue);
		} catch (Exception e) {
			Throw.aRuntime(InvalidArgumentException.class, "BPM tempo must be an integer");
		}
		return 0;
	}
}
