package com.atompacman.lereza.builtin.analysis.structure;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.atompacman.lereza.core.analysis.structure.MusicalStructure;
import com.atompacman.toolkat.IOUtils;
import com.atompacman.toolkat.JSONUtils;
import com.atompacman.toolkat.Log;
import com.atompacman.toolkat.annotations.SubMethodOf;
import com.atompacman.toolkat.task.ReportViewer;
import com.atompacman.toolkat.task.TaskMonitor;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableSet;

public final class Earth extends MusicalStructure {

    //
    //  ~  CONSTANTS  ~  //
    //

    private static final String COUNTRIES_FILE_NAME = "countries.json";
    private static final String CITIES_FILE_NAME    = "world-cities.json";
    
    
    //
    //  ~  SINGLETON  ~  //
    //

    public static final Earth INSTANCE;
    
    
    //
    //  ~  FIELDS  ~  //
    //

    private Map<String, Continent> continents;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    static {
        INSTANCE = new Earth();
        
        Map<String, Country> countries = new HashMap<>();
        
        // For each country in json file
        parseJsonTree(COUNTRIES_FILE_NAME).forEach(node -> {
            // Skip weird countries without region
            if (node.get("region").asText().isEmpty()) {
                return;
            }
            
            // Parse country node
            Country country = new Country(node);
            
            // Create continent if needed
            String continentName = country.getContinantName();
            Continent continent = INSTANCE.continents.get(continentName);
            if (continent == null) {
                continent = new Continent(continentName);
                INSTANCE.continents.put(continentName, continent);
            }
            
            // Create continent subregion is needed
            String subregionName = country.getContinantSubregionName();
            ContinentSubregion subregion = continent.getSubregion(subregionName);
            if (subregion == null) {
                subregion = new ContinentSubregion(subregionName);
                continent.addSubregion(subregion);
            }
            
            // Register country
            subregion.addCountry(country);
            countries.put(country.getCommonName(), country);
        });
        
        // For each city in json file
        parseJsonTree(CITIES_FILE_NAME).forEach(node -> {
            String countryName = node.get("country").asText();
            Country country = countries.get(countryName);
            
            if (country == null) {
                return;
            }
            
            // Create country subregion is needed
            String subregionName = node.get("subcountry").asText();
            CountrySubregion subregion = country.getSubregion(subregionName);
            if (subregion == null) {
                subregion = new CountrySubregion(subregionName);
                country.addSubregion(subregion);
            }
            
            // Register city
            City city = new City(node.get("name").asText());
            subregion.addCity(city);
        });
    }
    
    @SubMethodOf("static initialization")
    private static JsonNode parseJsonTree(String fileName) {
        try {
            File file = IOUtils.getMavenResource(Earth.class, fileName);
            return JSONUtils.parseTree(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private Earth() {
        this.continents = new HashMap<>();
    }
    
    public static void main(String args[]) {
        Log.setIgnoredPackagePart("com.atompacman");
        
        TaskMonitor monitor = TaskMonitor.of("Earth");
        
        INSTANCE.getContinents().forEach(continent -> {
            monitor.executeSubtask(continent.getName(), continent, (mon, c) -> {
                c.getSubregions().forEach(subregion -> {
                    mon.executeSubtask(subregion.getName(), subregion, (submon, s) -> {
                        s.getCountries().forEach(country -> {
                            submon.log("%40s - %f", country.getOfficialName(), country.getLatitude());
                            /*
                            submon.executeSubtask(country.getCommonName(), country, (ssmon, cn) -> {
                                cn.getSubregions().forEach(subreg -> {
                                    ssmon.executeSubtask(subreg.getName(), subreg, (sssmon, sr) -> {
                                       sr.getCities().forEach(city -> {
                                           sssmon.log(city.getName());
                                       }); 
                                    });
                                });
                            });
                            */
                        });
                    });
                });
            });
        });
        
        ReportViewer.showReportWindow(monitor);
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //
    
    public ImmutableSet<Continent> getContinents() {
        return ImmutableSet.copyOf(continents.values());
    }
}
