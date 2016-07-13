package com.atompacman.lereza.builtin.analysis.structure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.atompacman.lereza.core.analysis.structure.MusicalStructure;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableSet;

public final class Country extends MusicalStructure {

    //
    //  ~  FIELDS  ~  //
    //

    private final String  commonName;
    private final String  officialName;
    private final String  currencyID;
    private final String  continantName;
    private final String  continantSubregionName;
    private final String  capitalName;
    private final double  latitude;
    private final double  longitude;
    private final boolean isLandlocked;
    private final double  areaSqrKm;
    
    private final ImmutableSet<String> officialLanguages;

    private final Map<String, CountrySubregion> subregions;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    Country(JsonNode node) {
        this.commonName             = node.get("name").get("common").asText();
        this.officialName           = node.get("name").get("official").asText();
        this.currencyID             = node.get("currency").asText();
        this.continantName          = node.get("region").asText();
        this.continantSubregionName = node.get("subregion").asText();
        this.capitalName            = node.get("capital").asText();
        JsonNode latlon             = node.get("latlng");
        this.latitude               = latlon.size() == 2 ? latlon.get(0).asDouble() : -1;
        this.longitude              = latlon.size() == 2 ? latlon.get(1).asDouble() : -1;
        this.isLandlocked           = node.get("landlocked").asBoolean();
        this.areaSqrKm              = node.get("area").asDouble();
        
        Set<String> set = new HashSet<>();
        node.get("languages").forEach(n -> set.add(n.asText()));
        this.officialLanguages = ImmutableSet.copyOf(set);
        
        this.subregions = new HashMap<>();
    }
    
    
    //
    //  ~  SETTERS  ~  //
    //

    void addSubregion(CountrySubregion subregion) {
        subregions.put(subregion.getName(), subregion);
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //

    public String getCommonName() {
        return commonName;
    }

    public String getOfficialName() {
        return officialName;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public String getContinantName() {
        return continantName;
    }

    public String getContinantSubregionName() {
        return continantSubregionName;
    }

    public String getCapitalName() {
        return capitalName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isLandlocked() {
        return isLandlocked;
    }

    public double getAreaSqrKm() {
        return areaSqrKm;
    }

    public ImmutableSet<String> getOfficialLanguages() {
        return officialLanguages;
    }
    
    public ImmutableSet<CountrySubregion> getSubregions() {
        return ImmutableSet.copyOf(subregions.values());
    }

    public @Nullable CountrySubregion getSubregion(String name) {
        return subregions.get(name);
    }
}
