package com.atompacman.lereza.analysis;

import com.atompacman.lereza.analysis.profile.target.PieceStructuralScope.HorizontalScope;
import com.atompacman.lereza.analysis.profile.target.PieceStructuralScope.VerticalScope;

public @interface AnalyzerDescription {

    //===================================== INNER TYPES ==========================================\\

    public enum MainScope { HORIZONTAL, VERTICAL }
    
    
    
    //======================================= FIELDS =============================================\\

    public MainScope         scopeProgression() default   MainScope.VERTICAL;
    public HorizontalScope[] horizontalScopes() default { HorizontalScope.PIECE };
    public VerticalScope[]   verticalScopes()   default { VerticalScope.PIECE   };
}
