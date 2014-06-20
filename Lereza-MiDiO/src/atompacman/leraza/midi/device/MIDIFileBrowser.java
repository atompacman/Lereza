package atompacman.leraza.midi.device;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.Parameters;
import atompacman.leraza.midi.container.MIDIFile;
import atompacman.leraza.midi.exception.MIDIFileException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MIDIFileBrowser {

	private Map<String, MIDIFile> midiFiles = new HashMap<String, MIDIFile>();


	//////////////////////////////
	//       CONSTRUCTOR        //
	//////////////////////////////
	
	public MIDIFileBrowser() {
		this.midiFiles = new HashMap<String, MIDIFile>();
	}
	
	
	//////////////////////////////
	//        LOAD FILE         //
	//////////////////////////////
	
	public MIDIFile loadFile(String filePath) throws MIDIFileException {
		Log.normalMsg("================================ MIDIFileBrowser ==================================");
		Log.normalMsg("= = = = = =  Loading file at: " + String.format("%-40s", filePath.length() > 40 ? filePath.substring(filePath.length() - 40) : filePath) + "= = = = = = = ");
		
		if (midiFiles.size() == Parameters.MAX_NB_FILES) {
			Log.errorMsg("The maximum file capacity of " + Parameters.MAX_NB_FILES + " has been reached. File will not be added.");
			return null;
		}
		
		File file = new File(filePath);
		byte[] rawData = readBinaryFile(file);

		if (rawData.length > Parameters.MAX_FILE_SIZE) {
			throw new MIDIFileException ("Size of selected file exceeds " + Parameters.MAX_FILE_SIZE / 1000 + " KB.");
		}
		
		Log.normalMsg("File at \"" + filePath + "\" loaded in memory (" + rawData.length / 1000 + " KB).");
		Log.normalMsg("Reading the MIDI file...");
		
		try {
			MIDIFileReader midiFileReader = new MIDIFileReader(rawData, file);
			midiFiles.put(filePath, midiFileReader.readFile());
		} catch (MIDIFileException e){
			e.printStackTrace();
		}
		Log.normalMsg("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");
		Log.normalMsg("===================================================================================");

		return getFile(filePath);
	}

	
	//////////////////////////////
	//       GET MIDI FILE      //
	//////////////////////////////
	
	public MIDIFile getFile(String filePath){
		return midiFiles.get(filePath);
	}

	public MIDIFile getLastFile(){
		if (midiFiles.size() == 0) {
			Log.errorMsg("Cannot get last file loaded in the MIDI file browser: No file loaded.");
			return null;
		}
		List<MIDIFile> files = new ArrayList<MIDIFile>(midiFiles.values());
		return files.get(files.size() - 1);
	}

	//////////////////////////////
	//    PRIVATE READ FILE     //
	//////////////////////////////
	
	private byte[] readBinaryFile(File file) throws MIDIFileException {
		byte[] rawFile = new byte[(int)file.length()];
		
		try {
			FileInputStream fis = new FileInputStream(file);
			fis.read(rawFile);
			fis.close();
		} catch (FileNotFoundException e) {
			throw new MIDIFileException("ERROR: File not found at \"" + file.getPath() + "\".");
		} catch (IOException e) {
			throw new MIDIFileException("ERROR: IOException at \"" + file.getPath() + "\".");
		}
		return rawFile;
	}
}
