package com.atompacman.lereza.core.analysis.grouper;

import java.util.Arrays;
import java.util.List;

import com.atompacman.lereza.core.piece.Part;

public final class AllPartsGrouper implements PartGrouper {
    
    public List<List<Part>> group(List<Part> parts) {
        return Arrays.asList(parts);
    }


    public String getPartGroupingName() {
        return "All parts";
    }
}
