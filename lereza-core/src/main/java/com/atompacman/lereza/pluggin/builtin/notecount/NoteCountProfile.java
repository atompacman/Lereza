package com.atompacman.lereza.pluggin.builtin.notecount;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.atompacman.lereza.core.analysis.profile.Profile;
import com.atompacman.lereza.core.analysis.profile.PieceStructuralScope.PartSubstructure;
import com.atompacman.toolkat.math.NormalVariable;
import com.atompacman.toolkat.misc.EnumUtils;

public class NoteCountProfile extends Profile {

    //======================================= FIELDS =============================================\\

    private final int                                  totalNumNotes;
    private final Map<PartSubstructure, NormalVariable> subLevelsAvgNumNotes;    

    
    
    //======================================= METHODS ============================================\\

    //--------------------------------- PUBLIC CONSTRUCTORS --------------------------------------\\

    public NoteCountProfile(int totalNumNotes) {
        this.totalNumNotes        = totalNumNotes;
        this.subLevelsAvgNumNotes = new EnumMap<>(PartSubstructure.class);
    }

    
    //---------------------------------------- MERGE ---------------------------------------------\\
    
    static NoteCountProfile merge(List<NoteCountProfile> profiles) {
        // Count num notes in sub levels
        int totalNumNotes = 0;
        for (NoteCountProfile profile : profiles) {
            totalNumNotes += profile.totalNumNotes;
        }
        
        // Create merged profile
        NoteCountProfile merged = new NoteCountProfile(totalNumNotes);

        // Compute mean values for lower levels
        PartSubstructure scope = PartSubstructure.NOTE_STACK;
        for (; profiles.get(0).subLevelsAvgNumNotes.containsKey(scope); EnumUtils.prev(scope)) {
            merged.subLevelsAvgNumNotes.put(scope, NormalVariable.computeFrom(profiles, 
                    p -> (double) p.getSubLevelsAvnNumNotes(scope).mean()));
        }
        
        return new NoteCountProfile(totalNumNotes);
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public int getTotalNumNotes() {
        return totalNumNotes;
    }

    public NormalVariable getSubLevelsAvnNumNotes(PartSubstructure scope) {
        return subLevelsAvgNumNotes.get(scope);
    }
}
