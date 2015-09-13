package com.atompacman.lereza.core.generator.advisor;

import java.time.Duration;
import java.util.List;

import com.atompacman.lereza.core.piece.Note;
import com.atompacman.lereza.pluggin.builtin.interval.IntervalSequenceDistribution;

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
        return null;
    }
}
