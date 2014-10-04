package com.atompacman.lereza.common.xml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NodeStructure {

	private String nodeName;
	private Set<String> attributes;
	private Map<String, NodeStructure> childNodes;


	//------------ CONSTRUCTORS ------------\\

	public NodeStructure(String nodeName, String... attributesNames) {
		this.nodeName = nodeName;
		this.attributes = new HashSet<String>();
		this.childNodes = new HashMap<String, NodeStructure>();

		for (String attName : attributesNames) {
			attributes.add(attName);
		}
	}


	//------------ SETTERS ------------\\

	public void addChildNode(NodeStructure childNode) {
		if (childNodes.containsKey(childNode.nodeName)) {
			throw new IllegalArgumentException("Child node \"" + childNode.nodeName 
					+ "\" was already added to node \"" + nodeName + "\".");
		}
		childNodes.put(childNode.nodeName, childNode);
	}


	//------------ GETTERS ------------\\

	public String getName() {
		return nodeName;
	}

	public Set<String> getAttributeNames() {
		return new HashSet<String>(attributes);
	}

	public int getNbAttributes() {
		return attributes.size();
	}

	public NodeStructure getChildNode(String childNodeName) {
		NodeStructure node = childNodes.get(childNodeName);

		if (node == null) {
			throw new IllegalArgumentException("No child node named \"" 
					+ childNodeName	+ "\" for node \"" + nodeName + "\".");
		}
		return node;
	}
	
	public Set<String> getChildNodesName() {
		return childNodes.keySet();
	}
}
