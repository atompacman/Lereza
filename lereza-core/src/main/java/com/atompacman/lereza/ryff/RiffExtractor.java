package com.atompacman.lereza.ryff;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;

import com.atompacman.lereza.core.analysis.AnalysisManager;
import com.atompacman.lereza.core.analysis.AnalysisMethod;
import com.atompacman.lereza.core.analysis.Analyzer;
import com.atompacman.lereza.core.analysis.profile.Profile;
import com.atompacman.lereza.core.midi.sequence.MIDIFileReader;
import com.atompacman.lereza.core.midi.sequence.MIDIFileReaderException;
import com.atompacman.lereza.core.midi.sequence.MIDISequenceContent;
import com.atompacman.lereza.core.midi.sequence.MIDISequenceContentAssembler;
import com.atompacman.lereza.core.piece.Bar;
import com.atompacman.lereza.core.piece.Piece;
import com.atompacman.toolkat.module.Module;
import com.atompacman.toolkat.module.ProcedureDescription;

public class RiffExtractor extends Module {
        
    //===================================== INNER TYPES ==========================================\\

    private enum Procedure {
        
        @ProcedureDescription(nameFormat = "Reading MIDI file content")
        READ_MIDI_FILE_CONTENT,
        
        @ProcedureDescription(nameFormat = "Assembling piece data structure from MIDI file")
        ASSEMBLE_PIECE_DATA_STRUCTURE_FROM_MIDI_FILE
    }
    
    
    
    //======================================= METHODS ============================================\\

    //----------------------------------------- RUN ----------------------------------------------\\

    public static void main(String[] args) throws MIDIFileReaderException, FileNotFoundException {
//        RiffExtractor extractor = new RiffExtractor();
//        extractor.run(IO.getFile("test_time_sig_change.mid"));
//        ReportViewer.showReportWindow(extractor.getReport());
        new AnalysisManager().createDependencyAwareMethodQueue(new HashSet<>(
                Arrays.asList(AnalyzerA.class, AnalyzerB.class, AnalyzerC.class, AnalyzerD.class)));
    }
    
    public void run(File midiFile) throws MIDIFileReaderException {
        procedure(Procedure.READ_MIDI_FILE_CONTENT);
        MIDIFileReader reader = new MIDIFileReader(this);
        MIDISequenceContent content = reader.read(midiFile);
        
        procedure(Procedure.ASSEMBLE_PIECE_DATA_STRUCTURE_FROM_MIDI_FILE);
        MIDISequenceContentAssembler assembler = new MIDISequenceContentAssembler(this);
        assembler.assemble(content);
    }
}
