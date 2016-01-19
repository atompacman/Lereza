package com.atompacman.lereza.core.analysis;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;

import com.atompacman.lereza.core.midi.sequence.MIDIFileReader;
import com.atompacman.lereza.core.midi.sequence.MIDIFileReaderException;
import com.atompacman.lereza.core.piece.AbstractPiece;
import com.atompacman.lereza.pluggin.builtin.notecount.NoteCountAnalyzer;
import com.atompacman.toolkat.IO;

public class YOLO {

    public static void main(String[] args) throws MIDIFileReaderException, 
                                                  FileNotFoundException, 
                                                  InstantiationException, 
                                                  IllegalAccessException {
        
        OLDAnalysisManager manager = new OLDAnalysisManager();
        MIDIFileReader reader = MIDIFileReader.getInstance();
        AbstractPiece piece = reader.read(IO.getResource("midi/Test/System/Simple/"
                + "MonophonicSequences/FreresJacques.mid"));
        
        manager.analyze(piece, new HashSet<>(Arrays.asList(NoteCountAnalyzer.class)));
    }
}
