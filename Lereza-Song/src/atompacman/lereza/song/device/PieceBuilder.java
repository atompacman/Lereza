package atompacman.lereza.song.device;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.MidiFileManager;
import atompacman.leraza.midi.container.MidiFile;
import atompacman.leraza.midi.container.MidiNote;
import atompacman.lereza.common.formatting.Formatting;
import atompacman.lereza.common.solfege.Grouping;
import atompacman.lereza.common.solfege.Meter;
import atompacman.lereza.common.solfege.RythmicSignature;
import atompacman.lereza.common.solfege.Value;
import atompacman.lereza.song.api.PieceBuilderAPI;
import atompacman.lereza.song.container.Part;
import atompacman.lereza.song.container.Piece;
import atompacman.lereza.song.exception.BuilderException;

public class PieceBuilder implements PieceBuilderAPI {

	private Map<String, Piece> builtPieces;
	
	private Piece tempPiece;
	private MidiFile tempMidiFile;

	
	//////////////////////////////
	//       CONSTRUCTOR        //
	//////////////////////////////

	public PieceBuilder() {
		this.builtPieces = new HashMap<String, Piece>();
	}

	
	//////////////////////////////
	//          BUILD           //
	//////////////////////////////

	public void build(String filePath) {
		Log.infos(Formatting.lineSeparation("PieceBuilder", 0));
		Log.infos("Building midi file at " + filePath + ".");
		
		tempMidiFile = MidiFileManager.reader.getMidiFile(filePath);
		tempPiece = new Piece();
		
		setRythmicSignature();
		
		try {
			for (int i = 0; i < tempMidiFile.getNbTracks(); ++i) {
				buildPart(i);
			}
		} catch (BuilderException e) {
			e.printStackTrace();
		}
		builtPieces.put(filePath, tempPiece);
	}

	private void setRythmicSignature() {
		Log.infos("Rythmic signature : ");
		
		Meter meter = new Meter(tempMidiFile.getBeatPerMeasure(), tempMidiFile.getValueOfTheBeatNote());
		Log.infos(String.format(" - %-22s : %s", "Meter", meter.toString()));
		
		int midiBeatNoteValue = tempMidiFile.getDivisionOfABeat();
		Log.infos(String.format(" - %-22s : %s", "Midi beat note value", midiBeatNoteValue));
		
		int valueOfShortestNote = tempMidiFile.getValueOfShortestNote();
		Log.infos(String.format(" - %-22s : %s", "Value of shortest note", valueOfShortestNote));

		Log.infos(String.format(" - %-22s : %s", "Corrected length", midiBeatNoteValue / valueOfShortestNote));
		Log.infos(String.format(" - %-22s : %s", "Value of shortest note", Value.values()[Value.QUARTER.ordinal() - (int) Math.rint(Math.log10(midiBeatNoteValue / valueOfShortestNote) / Math.log10(2))]));

		if(meter.getDenominator() != 4) {
			Log.error("The time signature of the piece can not be used to determine the grouping of the notes.");
		}
		Grouping noteGrouping = Grouping.DUPLETS;
		Log.infos(String.format(" - %-22s : %s", "Note grouping", noteGrouping.name().toLowerCase()));

		Log.infos(String.format(" - %-22s : %s", "Final timestamp", tempMidiFile.getFinalTimestamp()));
		Log.infos(String.format(" - %-22s : %s (without counting the last note)", "Total nb of beats", (double) tempMidiFile.getFinalTimestamp() * (double) valueOfShortestNote / (double) midiBeatNoteValue));
		Log.infos(String.format(" - %-22s : %s (without counting the last note)", "Total nb of measures", (double) tempMidiFile.getFinalTimestamp() * (double) valueOfShortestNote / ((double) midiBeatNoteValue * (double) meter.getNumerator())));

		tempPiece.setRythmicSignature(new RythmicSignature(meter, noteGrouping, midiBeatNoteValue / valueOfShortestNote));
	}

	private void buildPart(int partNo) throws BuilderException {
		Log.infos(Formatting.lineSeparation("Part " + partNo, 1));
		
		Map<Integer, Stack<MidiNote>> trackNotes = tempMidiFile.getNotes().get(partNo);
		
		if (trackNotes.isEmpty()) {
			Log.infos("Discarded a track with no notes (track " + partNo + ") : no part will be created.");
			return;
		}
		Part part = new Part(tempPiece.getRythmicSignature(), tempMidiFile.getFinalTimestamp());

		List<Integer> timeUnits = new ArrayList<Integer>(trackNotes.keySet());
		Collections.sort(timeUnits);
		
		for (Integer timeUnit : timeUnits) {
			Log.extra("Midi time unit : " + timeUnit + " || Adding notes " + trackNotes.get(timeUnit).toString() + " to part.");
			part.addNotes(trackNotes.get(timeUnit), timeUnit);
		}
		
		part.fillWithRests();
		tempPiece.addPart(part);
	}
	
	
	//////////////////////////////
	//          GETTERS         //
	//////////////////////////////
	
	public Piece getPiece(String filePath) {
		return builtPieces.get(filePath);
	}
}
