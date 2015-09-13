package com.atompacman.lereza.core.analysis;

import com.atompacman.lereza.core.analysis.profile.PieceStructuralScope.HorizontalScope;
import com.atompacman.lereza.core.analysis.profile.PieceStructuralScope.VerticalScope;

public @interface AnalyzerDescription {

    //===================================== INNER TYPES ==========================================\\

    public enum MainScope { HORIZONTAL, VERTICAL }
    
    
    
    //======================================= FIELDS =============================================\\

    public MainScope         scopeProgression() default   MainScope.VERTICAL;
    public HorizontalScope[] horizontalScopes() default { HorizontalScope.PIECE };
    public VerticalScope[]   verticalScopes()   default { VerticalScope.PIECE   };
}
