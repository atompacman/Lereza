package com.atompacman.lereza.generator.advisor;

import java.time.Duration;
import java.util.List;

import com.atompacman.lereza.core.analysis.IntervalSequenceDistribution;
import com.atompacman.lereza.core.piece.Note;

public class $_$FYOLOOooPhraseLayerAdvisor extends PhraseLayerAdvisor {
    
    //======================================= FIELDS =============================================\\

    private IntervalSequenceDistribution isd;

    
    
    //======================================= METHODS ============================================\\

    //--------------------------------- PUBLIC CONSTRUCTORS --------------------------------------\\

    public $_$FYOLOOooPhraseLayerAdvisor(IntervalSequenceDistribution isd) {
        this.isd = isd;
    }
    
    
    //--------------------------------------- ADVICE ---------------------------------------------\\

    public Advice<Duration> adviceNextNote(List<Note> prevNotes) {
        isd.
        return null;
    }
}
