package com.atompacman.lereza.common.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeContent {

	private String nodeName;
	private Map<String, String> attributeValues;
	private List<NodeContent> childNodes;


	//------------ CONSTRUCTORS ------------\\

	public NodeContent(String nodeName) {
		this.nodeName = nodeName;
		this.attributeValues = new HashMap<String, String>();
		this.childNodes = new ArrayList<NodeContent>();
	}


	//------------ SETTERS ------------\\

	public void setAttributeValue(String attName, String attValue) {
		if (attributeValues.get(attName) != null) {
			throw new IllegalArgumentException("Value for attribute \"" + attName + 
					"\" was already set for content node \"" + nodeName + "\".");
		}
		attributeValues.put(attName, attValue);
	}

	public void addChildNode(NodeContent childNode) {
		childNodes.add(childNode);
	}


	//------------ GETTERS ------------\\

	public String getName() {
		return nodeName;
	}

	public String getAttributeValue(String attName) {
		String value = attributeValues.get(attName);
		if (value == null) {
			throw new IllegalArgumentException("No such attribute as \"" 
					+ attName + "\" for content node \"" + nodeName + "\".");
		}
		return value;
	}

	public int getNbAttributes() {
		return attributeValues.size();
	}
	
	public List<NodeContent> getChildNodes() {
		return childNodes;
	}
}
