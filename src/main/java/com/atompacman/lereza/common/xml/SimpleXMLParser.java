package com.atompacman.lereza.common.xml;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.atompacman.lereza.exception.XMLParserException;

public class SimpleXMLParser {

	private static Node currNode;
	private static NodeStructure currStructNode;
	private static NodeContent currContentNode;
	
	
	//------------ PARSE ------------\\

	public static NodeContent parse(String fileName, NodeStructure rootNodeStructure) 
			throws XMLParserException {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File(fileName));

			currNode = doc.getFirstChild();
			currStructNode = rootNodeStructure;
			currContentNode = new NodeContent(rootNodeStructure.getName());
			
			parseNode();
		} catch (Exception e) {
			throw new XMLParserException("Could not parse file at \"" + fileName + "\": ", e);
		}
		return currContentNode;
	}

	private static void parseNode() throws XMLParserException {
		verifyNodeName();
		verifyNbAttributes();
		parseAttributes();
		
		NodeList childNodes = currNode.getChildNodes();
		
		for (int i = 0; i < childNodes.getLength(); ++i) {
			currNode = childNodes.item(i);
			if (!(currNode instanceof Element)) {
				continue;
			}
			String childName = currNode.getNodeName();
			
			NodeStructure parentStructure = currStructNode;
			NodeContent parentContent = currContentNode;
			
			currStructNode = currStructNode.getChildNode(childName);
			currContentNode = new NodeContent(childName);
			
			parseNode();
			
			parentContent.addChildNode(currContentNode);
			currStructNode = parentStructure;
			currContentNode = parentContent;
		}
	}
	
	private static void parseAttributes() throws XMLParserException {
		NamedNodeMap attributes = currNode.getAttributes();
		for (String attName : currStructNode.getAttributeNames()) {
			Node attValue = attributes.getNamedItem(attName);
			if (attValue == null) {
				throw new XMLParserException("Expected an attribute named \"" + attName 
						+ "\" for node \"" + currNode.getNodeName() + "\".");
			}
			currContentNode.setAttributeValue(attName, attValue.getTextContent());
		}
	}

	private static void verifyNodeName() throws XMLParserException {
		if (!currNode.getNodeName().equals(currStructNode.getName())) {
			throw new XMLParserException("Expected a node named \"" + currStructNode.getName() 
					+ "\" but got \"" + currNode.getNodeName() + "\".");
		}
	}

	private static void verifyNbAttributes() throws XMLParserException {
		int actualNbAtt = currNode.getAttributes().getLength();
		int expectNbAtt = currStructNode.getNbAttributes();
		if (actualNbAtt != expectNbAtt) {
			throw new XMLParserException("Expected \"" + expectNbAtt + "\" attributes for node named \"" 
					+ currStructNode.getName() + "\" but got \"" + actualNbAtt + "\".");
		}
	}
}
