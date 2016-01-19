package com.atompacman.lereza.core.rendering;

import java.awt.GraphicsEnvironment;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

import com.atompacman.lereza.core.Parameters.Paths;
import com.atompacman.toolkat.IO;
import com.atompacman.toolkat.exception.Throw;
import com.atompacman.toolkat.misc.Log;

import guidoengine.guido;

public class ScoreRenderer {

    //====================================== CONSTANTS ===========================================\\

    // GUIDO lib
    private static final String GUIDO_LIB_DIR     = IO.getPath(Paths.LIB_ROOT_DIR, "GUIDO");
    private static final String GUIDO_JNI_DLL     = IO.getPath(GUIDO_LIB_DIR, "jniGUIDOEngine.dll");
    private static final String GUIDO_DLL         = IO.getPath(GUIDO_LIB_DIR, "GUIDOEngine.dll");    
    private static final String GUIDO_FONT_FAMILY = "Guido2";
    private static final String GUIDO_FONT_NAME   = "Times";
    
    // libmusicxml lib
    private static final String LIBMUSICXML_DIR   = IO.getPath(Paths.LIB_ROOT_DIR, "libmusicxml");
    private static final String XML_2_GUIDO_EXE   = IO.getPath(LIBMUSICXML_DIR, "xml2guido.exe");
    
    
    
    //======================================= METHODS ============================================\\

    //-------------------------------- STATIC INITIALIIZATION ------------------------------------\\
    
    static {
        // Check if libraries exists
        System.load(GUIDO_DLL);
        System.load(GUIDO_JNI_DLL);
        
        // Check if GUIDO font is installed
        GraphicsEnvironment graphEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        boolean found = false;
        for (String fontFamily : graphEnv.getAvailableFontFamilyNames()) {
            if (fontFamily.equalsIgnoreCase(GUIDO_FONT_FAMILY)) {
                found = true;
                break;
            }
        }
        if (!found) {
            Throw.aRuntime(ScoreRendereringException.class,"GUIDO font is not installed on system");
        }
        
        // Init GUIDO library
        guido.Init(GUIDO_FONT_FAMILY, GUIDO_FONT_NAME);
        
        // Check if xml2guido.exe exists
        if (!new File(XML_2_GUIDO_EXE).exists()) {
            Throw.aRuntime(ScoreRendereringException.class, "Could not find xml2guido.exe");
        }
    }
    
    public static void main(String[] args) throws IOException {
        displayScore(FileUtils.readFileToString(IO.getFile("music.xml")));
    }
    
    public static void displayScore(String musicXML) {
        try {
            StringBuilder stdout = new StringBuilder();

            CommandLineUtils.executeCommandLine(new Commandline(XML_2_GUIDO_EXE + " -"), 
                                                new ByteArrayInputStream(musicXML.getBytes()), 
                                                line -> stdout.append(line).append('\n'), 
                                                line -> Log.error(line));
            
            if (stdout.toString().contains("conversion failed")) {
                Throw.aRuntime(ScoreRendereringException.class,"xml2guido.exe ended with an error");
            }
            
            new GuidoViewer(stdout.toString()).setVisible(true);
        } catch (CommandLineException e) {
            Throw.aRuntime(ScoreRendereringException.class,"Could not convert musicXML to GUIDO",e);
        }
    }
}
