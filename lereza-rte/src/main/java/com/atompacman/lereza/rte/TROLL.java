package com.atompacman.lereza.rte;

import java.io.IOException;

import com.atompacman.lereza.core.midi.device.MIDIDeviceException;
import com.atompacman.lereza.core.midi.device.MIDIDeviceInfo;
import com.atompacman.lereza.core.midi.device.MIDIDeviceManager;
import com.atompacman.lereza.core.profile.drum.DrumBeat;
import com.atompacman.toolkat.IO;

public class TROLL {

    public static void main(String[] args) throws MIDIDeviceException, IOException {
        MIDIDeviceManager manager = MIDIDeviceManager.getInstance();
        MIDIDeviceInfo output = manager.getDeviceList().stream().filter(
                device -> device.getAPIInfo().getName().equals("Microsoft GS Wavetable Synth")
                ).findAny().get();
        DrumBeat beat = new DrumBeat(IO.getResourceByteBuffer("drum_beat_2.ldb"));
        MIDIPercussionBeatPlayer mpbp = new MIDIPercussionBeatPlayer(beat, 120);
        manager.connectTransmitterToDevice(mpbp, output, "MIDIPercussionBeatPlayer");
        mpbp.start();
    }
}
