package com.atompacman.lereza.api.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLNodeContent {

	private String nodeName;
	private Map<String, String> attributes;
	private Map<String, List<XMLNodeContent>> childNodes;
	
	
	//------------ CONSTRUCTORS ------------\\

	public XMLNodeContent(String nodeName, String... attributesNames) {
		this.nodeName = nodeName;
		this.attributes = new HashMap<String, String>();
		this.childNodes = new HashMap<String, List<XMLNodeContent>>();
		
		for (String attName : attributesNames) {
			attributes.put(attName, null);
		}
	}
	
	public XMLNodeContent(XMLNodeContent model) {
		this.nodeName = new String(model.nodeName);
		this.attributes = new HashMap<String, String>(model.attributes);
		this.childNodes = new HashMap<String, List<XMLNodeContent>>(model.childNodes);
	}
	
	
	//------------ SETTERS ------------\\

	public void addChildNode(XMLNodeContent childNode) {
		List<XMLNodeContent> nodes = childNodes.get(childNode.nodeName);
		if (nodes == null) {
			nodes = new ArrayList<XMLNodeContent>();
			nodes.add(childNode);
			childNodes.put(childNode.nodeName, nodes);
		} else {
			nodes.add(childNode);
		}
	}
	
	public void setAttribute(String name, String value) {
		if (!attributes.containsKey(name)) {
			throw new XMLParserException("No attribute name \"" + name 
					+ "\" for node \"" + nodeName + "\".");
		}
		if (attributes.get(name) != null) {
			throw new XMLParserException("A value for attribute \"" + name 
					+ "\" was already set for node \"" + nodeName + "\".");
		}
		attributes.put(name, value);
	}
	
	
	//------------ GETTERS ------------\\

	public String getNodeName() {
		return nodeName;
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public Map<String, List<XMLNodeContent>> getChildNodes() {
		return childNodes;
	}
}
