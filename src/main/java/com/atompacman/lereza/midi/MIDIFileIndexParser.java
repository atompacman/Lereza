package com.atompacman.lereza.midi;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.Parameters;
import com.atompacman.lereza.common.context.ContextElementType;
import com.atompacman.lereza.common.context.ContextElements;
import com.atompacman.lereza.common.xml.NodeContent;
import com.atompacman.lereza.common.xml.NodeStructure;
import com.atompacman.lereza.common.xml.SimpleXMLParser;
import com.atompacman.lereza.exception.MIDIFileIndexParserException;
import com.atompacman.lereza.midi.container.MIDIFileInfo;

public class MIDIFileIndexParser extends SimpleXMLParser {

	private static final String ROOT_NODE_NAME = "MidiFileIndex";
	private static final String FILE_NODE_NAME = "File";
	private static final String HIERARCHY_NODES_ATTRIBUTE = "name";
	private static final String[] FILE_NODE_ATTRIBUTES = {"id", "title", "url" };

	private static List<MIDIFileInfo> midiFilesInfo;
	

	//------------ PARSE MIDI FILE INDEX ------------\\

	public static List<MIDIFileInfo> parse() throws MIDIFileIndexParserException {
		return parseIndex(Parameters.MIDI_INDEX_XML_PATH);
	}

	protected static List<MIDIFileInfo> parseIndex(String filePath) 
			throws MIDIFileIndexParserException {
		if (midiFilesInfo != null) {
			throw new MIDIFileIndexParserException("MIDI file index was already parsed.");
		}

		midiFilesInfo = new ArrayList<MIDIFileInfo>();
		
		try {
			NodeStructure rootNode = buildXMLFileStructure();
			NodeContent contentRoot = parse(filePath, rootNode);
			String[] currContext = new String[ContextElementType.values().length];
			
			parseElement(0, contentRoot.getChildNodes().get(0), currContext);
			
		} catch (Exception e) {
			throw new MIDIFileIndexParserException("Error parsing MIDI file library at \"" 
					+ Parameters.MIDI_INDEX_XML_PATH + "\": ", e);
		}
		
		return midiFilesInfo;
	}

	private static NodeStructure buildXMLFileStructure() {
		NodeStructure rootNode = new NodeStructure(ROOT_NODE_NAME);
		NodeStructure lastNode = rootNode;

		for (ContextElementType elementType : ContextElementType.values()) {
			String nodeName = elementType.nameAsInFile();
			NodeStructure levelNode = new NodeStructure(nodeName, HIERARCHY_NODES_ATTRIBUTE);
			lastNode.addChildNode(levelNode);
			lastNode = levelNode;
		}
		lastNode.addChildNode(new NodeStructure(FILE_NODE_NAME, FILE_NODE_ATTRIBUTES));

		return rootNode;
	}

	private static void parseElement(int elemNo, NodeContent node, String[] currContext) 
			throws MIDIFileIndexParserException {
		currContext[elemNo] = node.getAttributeValue(HIERARCHY_NODES_ATTRIBUTE);

		if (elemNo == currContext.length - 1) {
			for (NodeContent child : node.getChildNodes()) {
				createMIDIFileInfo(child, currContext);
			}

		} else {
			for (NodeContent child : node.getChildNodes()) {
				parseElement(elemNo + 1, child, currContext);
			}
		}
	}

	private static void createMIDIFileInfo(NodeContent fileNode, String[] context) 
			throws MIDIFileIndexParserException {
		String idString = fileNode.getAttributeValue(FILE_NODE_ATTRIBUTES[0]);
		try {
			int id = Integer.parseInt(idString);
			if (id != midiFilesInfo.size() + 1) {
				throw new MIDIFileIndexParserException("File ids must "
						+ "be linearly order from one to N.");
			}
		} catch(Exception e) {
			throw new MIDIFileIndexParserException("Invalid id \"" + idString + "\": ", e);
		}
		String title = fileNode.getAttributeValue(FILE_NODE_ATTRIBUTES[1]);
		String url = fileNode.getAttributeValue(FILE_NODE_ATTRIBUTES[2]);
		url = Parameters.DATA_DIRECTORY + url;

		for (int i = 0; i < context.length; ++i) {
			ContextElementType type = ContextElementType.values()[i];
			String value = context[i];
			if (type.isInDatabase() && !ContextElements.contains(type, value)) {
				throw new MIDIFileIndexParserException(type.nameAsInFile() + " \"" + value + "\" is "
						+ "not a valid context element for file of id \"" + idString + "\".");
			}
		}
		midiFilesInfo.add(new MIDIFileInfo(url, title, context));
	}
}
