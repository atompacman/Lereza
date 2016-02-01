package com.atompacman.lereza.pluggin.builtin.drum;

import com.atompacman.lereza.core.analysis.OLDAnalyzer;
import com.atompacman.lereza.core.analysis.OLDAnalyzerDescription;
import com.atompacman.lereza.core.analysis.profile.PieceStructuralScope.PartGrouping;
import com.atompacman.lereza.core.analysis.profile.PieceStructuralScope.PartSubstructure;
import com.atompacman.lereza.core.analysis.profile.Profile;
import com.atompacman.lereza.core.analysis.profile.ProfileSet;
import com.atompacman.lereza.core.piece.Bar;
import com.atompacman.lereza.core.piece.Part;
import com.atompacman.nrstep.PatternDetector;
import com.atompacman.nrstep.Sequence;

@OLDAnalyzerDescription(
partGrouping     = { PartGrouping.SINGLE },
partSubstructure = { PartSubstructure.PART }
)
public class PercussionAnalyzer extends OLDAnalyzer {

    //======================================= METHODS ============================================\\

    //--------------------------------------- ANALYSIS -------------------------------------------\\
    
    public Profile analyzePart(Part part, ProfileSet dependencies) {
        PatternDetector<PercussionHits> detector = new PatternDetector<>();
        detector.detect(buildPercussionHitsSequence(part));
        return null;
    }
    
    private static Sequence<PercussionHits> buildPercussionHitsSequence(Part part) {
        Sequence<PercussionHits> sequence = new Sequence<>();

        for (int i = 0; i < part.numBars(); ++i) {
            Bar bar = part.getBar(i);
            for (int j = 0; j < bar.getLengthTU(); ++j) {
                sequence.add(new PercussionHits(bar.getNoteStack(j)));
            }
        }
        return sequence;
    }
}
