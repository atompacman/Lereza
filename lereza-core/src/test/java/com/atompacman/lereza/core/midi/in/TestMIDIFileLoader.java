package com.atompacman.lereza.core.midi.in;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.atompacman.toolkat.IOUtils;
import com.atompacman.toolkat.task.ReportViewer;
import com.atompacman.toolkat.task.TaskMonitor;
import com.atompacman.toolkat.test.AbstractTest;

public final class TestMIDIFileLoader extends AbstractTest {

    //
    //  ~  SCENARII  ~  //
    //
    
    @Test
    public void Scenarii_MidiFiles_ValidData_NoExcep() throws IOException, MIDIFileLoaderException {
        File root = IOUtils.getResource("midi");
        MIDIFileLoader loader = MIDIFileLoader.of();
        TaskMonitor monitor = TaskMonitor.of("Test");
        File[] files = root.listFiles();
        assertNotNull(files);
        for (File file : files) {
            monitor.executeSubtaskExcep(file.getName(), mon -> {
                loader.load(file, mon);                
            });
        }
        ReportViewer.showReportWindow(monitor);
        return;
    }
}
