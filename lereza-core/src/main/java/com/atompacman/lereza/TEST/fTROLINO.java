package com.atompacman.lereza.TEST;

import java.io.IOException;
import java.net.URL;

import javax.swing.JFrame;

import org.jgrapht.demo.JGraphAdapterDemo;

import com.atompacman.lereza.core.analysis.AnalysisManager;
import com.atompacman.toolkat.Log;
import com.atompacman.toolkat.task.ReportViewer;
import com.atompacman.toolkat.task.TaskMonitor;
import com.google.common.collect.Sets;

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
    
//    public static void main(String[] args) throws FileNotFoundException, MIDIFileLoaderException {
//        Log.setIgnoredPackagePart("com.atompacman.lereza.core");
//        TaskMonitor monitor = TaskMonitor.of("Test");
//        Path path = Paths.get("B:", "Documents", "Dev", "Atompacman", "Lereza", "Lereza", 
//                "lereza-core", "src", "test", "resources", "com", "atompacman", "lereza", "core", 
//                "piece", "Scenario_StandardMonophonic_ValidArgs_ValidReturnValue.mid");
//        MIDISequenceContent content = new MIDIFileLoader().load(path.toFile(), monitor);
//        new MIDISequenceContentAssembler().assemble(content, monitor);
//    }
    
    public static void main(String[] args) throws IOException {
        Log.setIgnoredPackagePart("com.atompacman");
        TaskMonitor monitor = TaskMonitor.of("Lereza");
        monitor.executeSubtaskExcep("Analysis", mon -> {
            URL url = new URL("file:\\B:\\Documents\\Dev\\Atompacman\\Lereza\\Lereza\\lereza-"
                    + "builtin-analysis\\target\\lereza-builtin-analysis-0.0.1-SNAPSHOT.jar");
            return new AnalysisManager(Sets.newHashSet(url), mon);
        });
        ReportViewer.showReportWindow(monitor);
    }
}
