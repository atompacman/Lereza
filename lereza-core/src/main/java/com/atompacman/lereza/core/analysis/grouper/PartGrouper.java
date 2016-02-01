package com.atompacman.lereza.core.analysis.grouper;

import java.util.List;

import com.atompacman.lereza.core.piece.Part;

public interface PartGrouper {

    List<List<Part>> group(List<Part> parts);
    
    String getPartGroupingName();
}
