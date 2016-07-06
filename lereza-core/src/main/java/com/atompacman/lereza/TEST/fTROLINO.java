package com.atompacman.lereza.TEST;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.atompacman.lereza.core.midi.in.MIDIFileLoader;
import com.atompacman.lereza.core.midi.in.MIDIFileLoaderException;
import com.atompacman.lereza.core.midi.in.MIDISequenceContent;
import com.atompacman.lereza.core.midi.in.MIDISequenceContentAssembler;
import com.atompacman.toolkat.Log;
import com.atompacman.toolkat.task.TaskMonitor;

public class fTROLINO {

//    public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
//        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
//            System.out.println(info.getName() + " " + info.getDescription() + " " + info.getVendor() + " " + info.getVersion());
//        }
//        
//        MidiDevice.Info info = Arrays.asList(MidiSystem.getMidiDeviceInfo()).stream().filter(i -> i.getName().contains("loop") && i.getDescription().contains("External")).findAny().get();
//        MidiDevice device = MidiSystem.getMidiDevice(info);
//        device.open();
//        System.out.println(device.getMaxReceivers());
//        System.out.println(device.getMaxTransmitters());
//        Receiver receiver = device.getReceiver();
//        receiver.send(new ShortMessage(ShortMessage.NOTE_ON, 73, 100), -1);
//        Thread.sleep(1000);
//        receiver.send(new ShortMessage(ShortMessage.NOTE_OFF, 73, 100), -1);
//    }
    
    public static void main(String[] args) throws FileNotFoundException, MIDIFileLoaderException {
        Log.setIgnoredPackagePart("com.atompacman.lereza.core");
        TaskMonitor monitor = TaskMonitor.of("Test");
        Path path = Paths.get("B:", "Documents", "Dev", "Atompacman", "Lereza", "Lereza", 
                "lereza-core", "src", "test", "resources", "com", "atompacman", "lereza", "core", 
                "piece", "Scenario_StandardMonophonic_ValidArgs_ValidReturnValue.mid");
        MIDISequenceContent content = new MIDIFileLoader().load(path.toFile(), monitor);
        new MIDISequenceContentAssembler().assemble(content, monitor);
    }
}
