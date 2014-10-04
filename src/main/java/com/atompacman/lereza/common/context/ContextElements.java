package com.atompacman.lereza.common.context;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.atompacman.lereza.Parameters;
import com.atompacman.lereza.common.xml.NodeContent;
import com.atompacman.lereza.common.xml.NodeStructure;
import com.atompacman.lereza.common.xml.SimpleXMLParser;
import com.atompacman.lereza.exception.ContextElementsException;

public class ContextElements extends SimpleXMLParser {

	private static final String   ROOT_NODE_NAME = "ContextElements";
	private static final String   ITEM_NODE_NAME = "Item";
	private static final String[] ITEM_ATTRIBUTES = {"name"};

	private static Map<ContextElementType, Set<String>> contextElements;


	//------------ PARSE CONTEXT ELEMENTS ------------\\

	public static void parse() throws ContextElementsException {
		parseContextElements(Parameters.CONTEXT_ELEM_XML_FILE);
	}

	protected static void parseContextElements(String filePath) throws ContextElementsException {
		if (contextElements != null) {
			throw new ContextElementsException("XML context elements file was already parsed.");
		}
		contextElements = new EnumMap<ContextElementType, Set<String>>(ContextElementType.class);

		NodeStructure rootNode = new NodeStructure(ROOT_NODE_NAME);
		NodeStructure itemNode = new NodeStructure(ITEM_NODE_NAME, ITEM_ATTRIBUTES);

		for (ContextElementType secondaryNodeName : ContextElementType.values()) {
			NodeStructure secondaryNode = new NodeStructure(secondaryNodeName.nameAsInFile());
			secondaryNode.addChildNode(itemNode);
			rootNode.addChildNode(secondaryNode);
		}

		try {
			NodeContent contentRootNode = parse(filePath, rootNode);
			List<NodeContent> elementNodes = contentRootNode.getChildNodes();
			
			for (int i = 0; i < elementNodes.size(); ++i) {
				ContextElementType elementType = ContextElementType.values()[i];
				if (!elementType.isInDatabase()) {
					continue;
				}
				contextElements.put(elementType, new HashSet<String>());
				NodeContent elemNode = elementNodes.get(i);
				
				for (NodeContent item : elemNode.getChildNodes()) {
					String itemName = item.getAttributeValue(ITEM_ATTRIBUTES[0]);

					if (!contextElements.get(elementType).add(itemName)) {
						throw new ContextElementsException("Duplicate context element \"" 
								+ itemName + "\" in node \"" + elementType.nameAsInFile() 
								+ "\".");
					}
				}

			}
		} catch (Exception e) {
			throw new ContextElementsException("Error parsing context elements file at \"" 
					+ Parameters.CONTEXT_ELEM_XML_FILE + "\": ", e);
		}
	}


	//------------ CONTAINS ------------\\

	public static boolean contains(ContextElementType type, String value) {
		return type.isInDatabase() && contextElements.get(type).contains(value);
	}
}