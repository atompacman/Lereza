package com.atompacman.lereza.rte;

import java.io.IOException;

import com.atompacman.lereza.core.midi.device.MIDIDeviceException;
import com.atompacman.lereza.core.midi.device.MIDIDeviceProvider;
import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.Scale;
import com.atompacman.lereza.core.solfege.ScaleType;
import com.atompacman.lereza.core.solfege.Tone;
import com.atompacman.lereza.core.solfege.Value;
import com.atompacman.lereza.pluggin.builtin.drum.DrumBeat;
import com.atompacman.toolkat.IO;

public class TROLL {

    public static void main(String[] args) throws MIDIDeviceException, 
                                    IOException, InterruptedException {
        
        MIDIDeviceProvider.printAvailableDevices();
        RealTimeCompositionEngine engine = new RealTimeCompositionEngine(
            "[Unknown vendor] Microsoft GS Wavetable Synth (v1.0) - Internal software synthesizer");
        BeatPlayer beatPlayer = new BeatPlayer();
        beatPlayer.setBeat(new DrumBeat(IO.getResourceByteBuffer("drum_beat_2.ldb")));
        engine.setPercussionChannelComposer(beatPlayer);
        MonorhythmicRandomGenerator rpc = new MonorhythmicScaleRandomGenerated(
                Pitch.valueOf("C5"), Pitch.valueOf("C7"), Value.SIXTEENTH, Scale.valueOf(Tone.valueOf("C"), ScaleType.MAJOR));
        engine.setChannelComposer(0, rpc);
        engine.start();
    }
}
