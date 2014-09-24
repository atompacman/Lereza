package com.atompacman.lereza.api.test;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SimpleXMLParser {

	private XMLNodeContent currNodeContent;
	private Node currNode;


	//------------ PARSE ------------\\

	public void parse(String fileName, XMLNodeContent rootNodeContent) {
		try {
			currNodeContent = rootNodeContent;
			File fXmlFile = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			currNode = doc.getFirstChild();
			parseNode();

		} catch (Exception e) {
			throw new XMLParserException("Could not parse file at \"" + fileName + "\": ", e);
		}
	}

	public void parseNode() {
		verifyNodeName();
		verifyNbAttributes();
		parseAttributes();
		
		NodeList childNodes = currNode.getChildNodes();
		Map<String, List<XMLNodeContent>> childNodesContent = currNodeContent.getChildNodes();
		
		for (int i = 0; i < childNodes.getLength(); ++i) {
			Node child = childNodes.item(i);
			if (!(child instanceof Element)) {
				continue;
			}
			String childName = child.getNodeName();
			List<XMLNodeContent> equivChildContents = childNodesContent.get(childName);
			if (equivChildContents == null) {
				throw new XMLParserException("No node named \"" + childName + "\" was "
						+ "expected for node \"" + currNodeContent.getNodeName() + "\".");
			}
			XMLNodeContent childNodeModel = childNodesContent.get(childName).get(0);
			XMLNodeContent newChildNode = new XMLNodeContent(childNodeModel);
			currNodeContent.addChildNode(newChildNode);
			currNodeContent = newChildNode;
			currNode = child;
			parseNode();
		}
	}
	
	public void parseAttributes() {
		NamedNodeMap attributes = currNode.getAttributes();
		for (String attName : currNodeContent.getAttributes().keySet()) {
			Node attValue = attributes.getNamedItem(attName);
			if (attValue == null) {
				throw new XMLParserException("Expected an attribute named \"" + attName 
						+ "\" for node \"" + currNode.getNodeName() + "\".");
			}
			currNodeContent.setAttribute(attName, attValue.getTextContent());
		}
	}

	public void verifyNodeName() {
		if (!currNode.getNodeName().equals(currNodeContent.getNodeName())) {
			throw new XMLParserException("Expected a node named \"" + currNodeContent.getNodeName() 
					+ "\" but got \"" + currNode.getNodeName() + "\".");
		}
	}

	public void verifyNbAttributes() {
		int actualNbAtt = currNode.getAttributes().getLength();
		int expectNbAtt = currNodeContent.getAttributes().size();
		if (actualNbAtt != expectNbAtt) {
			throw new XMLParserException("Expected \"" + expectNbAtt + "\" attributes for node named \"" 
					+ currNodeContent.getNodeName() + "\" but got \"" + actualNbAtt + "\".");
		}
	}
}
