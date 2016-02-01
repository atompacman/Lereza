package com.atompacman.lereza.core.analysis.profile;

import java.util.Map;
import java.util.Map.Entry;

import com.atompacman.toolkat.collections.BiDoubleHashMap;
import com.atompacman.toolkat.collections.BiDoubleMap;

public class ProfileSet {

    //======================================= FIELDS =============================================\\

    private final BiDoubleMap<TargetPieceStructure, Class<? extends Profile>, Profile> profiles;
    
    
    
    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    public ProfileSet() {
        this.profiles = new BiDoubleHashMap<>();
    }
    
    
    //----------------------------------------- ADD ----------------------------------------------\\

    @SuppressWarnings("unchecked")
    public void addProfile(Profile profile, TargetPieceStructure tps) {
        if (this.profiles.put(tps, (Class<Profile>) profile.getClass(), profile) != null) {
            throw new IllegalArgumentException("A " + profile.getClass().getSimpleName() + 
                    " was already added to current piece.");
        }
    }
    
    
    //--------------------------------------- GETTERS --------------------------------------------\\

    public Profile getProfile(Class<? extends Profile> profileClass, TargetPieceStructure tps) {
        Profile elem = profiles.get(tps, profileClass);
        if (elem == null) {
            throw new IllegalArgumentException("No " + profileClass.getSimpleName() + 
                    " found for target \"" + tps +"\".");
        }
        return elem;
    }
    
    public Map<Class<? extends Profile>, Profile> getProfileCovering(TargetPieceStructure target) {
        return profiles.getSubMap(target);
    }
    
    public Map<TargetPieceStructure, Profile> getProfiles(Class<? extends Profile> dataClass) {
        return profiles.getAlternativeSubMap(dataClass);
    }
    
    public Profile getProfile(Class<? extends Profile> dataClass) {
        Map<TargetPieceStructure, Profile> choices = profiles.getAlternativeSubMap(dataClass);

        if (choices.isEmpty()) {
            throw new IllegalArgumentException("No " + dataClass.getSimpleName() + " found.");
        }
        Entry<TargetPieceStructure, Profile> entry = choices.entrySet().iterator().next();
        if (choices.size() > 1 || !entry.getKey().equals(TargetPieceStructure.WHOLE_PIECE)) {
            throw new IllegalArgumentException(dataClass.getSimpleName() + 
                    " is not a data class targeting only the whole piece");
        }
        return entry.getValue();
    }
}
