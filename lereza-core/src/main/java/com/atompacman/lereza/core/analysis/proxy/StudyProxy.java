package com.atompacman.lereza.core.analysis.proxy;

import com.atompacman.lereza.core.analysis.study.Study;

public final class StudyProxy extends AnalysisComponentProxy<Study>{

    //
    //  ~  INIT  ~  //
    //
    
    StudyProxy(Class<? extends Study> componentClass) {
        super(componentClass);
    }
}
