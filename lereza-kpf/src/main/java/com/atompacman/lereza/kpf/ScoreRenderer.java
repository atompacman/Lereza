package com.atompacman.lereza.kpf;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import com.xenoage.utils.exceptions.InvalidFormatException;
import com.xenoage.utils.jse.io.JseInputStream;
import com.xenoage.zong.desktop.io.musicxml.in.MusicXmlScoreDocFileInput;
import com.xenoage.zong.desktop.utils.JseZongPlatformUtils;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.renderer.awt.AwtLayoutRenderer;

public class ScoreRenderer {
    
    //======================================= METHODS ============================================\\

    //-------------------------------- STATIC INITIALIZATION -------------------------------------\\

    static {
        JseZongPlatformUtils.init("ScoreRenderer");
    }

    
    //------------------------------------ RENDER SCORE ------------------------------------------\\
    
    public static BufferedImage renderScoreFromMXMLFile(File musicXMLFile, int page, double zoom) 
            throws InvalidFormatException, IOException {
        
        return renderScoreFromJIS(new JseInputStream(musicXMLFile), page, zoom);
    }
    
    public static BufferedImage renderScoreFromMXMLString(String musicXML, int page, double zoom) 
            throws InvalidFormatException, IOException {
        
        ByteArrayInputStream bais = new ByteArrayInputStream(musicXML.getBytes());
        return renderScoreFromJIS(new JseInputStream(bais), page, zoom);
    }
    
    private static BufferedImage renderScoreFromJIS(JseInputStream jis, int page, double zoom) 
            throws InvalidFormatException, IOException {
        
        ScoreDoc doc = new MusicXmlScoreDocFileInput().read(jis, null);
        return AwtLayoutRenderer.paintToImage(doc.getLayout(), page, (float) zoom);
    }
}
