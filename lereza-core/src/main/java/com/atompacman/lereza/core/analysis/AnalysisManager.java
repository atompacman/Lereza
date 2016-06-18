package com.atompacman.lereza.core.analysis;

import java.io.IOException;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.atompacman.lereza.core.piece.Piece;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public final class AnalysisManager {

    //
    //  ~  ANALYZE  ~  //
    //
    
    public void analyze(Piece piece) {
        
    }
    
    public Map<AnalysisComponentType, Set<Class<?>>> detect() throws IOException {
        ClassPath cp = ClassPath.from(Thread.currentThread().getContextClassLoader());
        
        Map<AnalysisComponentType, Set<Class<?>>> analysisClasses 
            = new EnumMap<>(AnalysisComponentType.class);
        
        for (AnalysisComponentType act : AnalysisComponentType.values()) {
            Set<Class<?>> actClasses = new HashSet<>();
            for (ClassInfo classInfo : cp.getTopLevelClasses(act.basePackageName())) {
                Class<?> clazz = classInfo.load();
                if (act.isComponentTypeOf(clazz)) {
                    actClasses.add(clazz);
                }
            }
            analysisClasses.put(act, actClasses);
        }

        return analysisClasses;
    }
    
    public static void main(String[] args) throws IOException {
        for (Entry<AnalysisComponentType, Set<Class<?>>> entry : new AnalysisManager().detect().entrySet()) {
            System.out.println(entry.getKey());
            for (Class<?> clazz : entry.getValue()) {
                System.out.println(clazz.getName());
            }
            System.out.println();
        }
    }
}
