package com.atompacman.lereza.core.analysis.profile;

import java.util.Map;
import java.util.Map.Entry;

import com.atompacman.toolkat.collections.BiDoubleHashMap;
import com.atompacman.toolkat.collections.BiDoubleMap;

public class ProfileSet {

    //======================================= FIELDS =============================================\\

    private final BiDoubleMap<TargetPieceStructure, Class<? extends Profile>, Profile> profiles;



    //======================================= METHODS ============================================\\

    //--------------------------------- PUBLIC CONSTRUCTORS --------------------------------------\\

    public ProfileSet() {
        this.profiles = new BiDoubleHashMap<>();
    }


    //----------------------------------------- ADD ----------------------------------------------\\

    protected void addProfile(Profile profile, TargetPieceStructure tps) {
        if (profiles.put(tps, profile.getClass(), profile) != null) {
            throw new IllegalArgumentException("A " + profile.getClass().getSimpleName() + 
                    " was already added to current study.");
        }
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public Profile getProfile(Class<? extends Profile> profileClass, TargetPieceStructure tps) {
        Profile profile = profiles.get(tps, profileClass);
        if (profile == null) {
            throw new IllegalArgumentException("No " + profileClass.getSimpleName() + 
                    " found for target \"" + tps +"\".");
        }
        return profile;
    }

    public Map<Class<? extends Profile>, Profile> getProfilesCovering(TargetPieceStructure target) {
        return profiles.getSubMap(target);
    }

    public Map<TargetPieceStructure, Profile> getProfiles(Class<? extends Profile> profileClass) {
        return profiles.getAlternativeSubMap(profileClass);
    }

    public Profile getProfile(Class<? extends Profile> profileClass) {
        Map<TargetPieceStructure, Profile> choices = profiles.getAlternativeSubMap(profileClass);

        if (choices.isEmpty()) {
            throw new IllegalArgumentException("No " + profileClass.getSimpleName() +  " found.");
        }
        Entry<TargetPieceStructure, Profile> entry = choices.entrySet().iterator().next();
        if (choices.size() > 1 || !entry.getKey().equals(TargetPieceStructure.WHOLE_PIECE)) {
            throw new IllegalArgumentException(profileClass.getSimpleName() + 
                    " is not a profile targeting only the whole piece");
        }
        return entry.getValue();
    }
}
