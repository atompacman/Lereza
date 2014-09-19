package com.atompacman.lereza.api.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeDescription {

	public static final List<Class<?>> POSSIBLE_ATTRIBUTE_CLASSES = 
			Arrays.asList(String.class, int.class, List.class);	
	
	private String nodeName;
	private List<Class<?>> attributeClasses;
	private List<Object> attributeValues;
	private List<NodeDescription> childNodes;
	
	
	//------------ CONSTRUCTORS ------------\\

	public NodeDescription(String nodeName, Class<?>... attributeClasses) {
		this.nodeName = nodeName;
		this.attributeClasses = new ArrayList<Class<?>>();
		this.attributeValues = new ArrayList<Object>();
		this.childNodes = new ArrayList<NodeDescription>();
		
		for (Class<?> attClass : attributeClasses) {
			this.attributeClasses.add(attClass);
		}
	}
	
	
	//------------ SETTERS ------------\\

	public void addChildNode(NodeDescription childNode) {
		childNodes.add(childNode);
	}
	
	public void setAttributeValue(int index, Object value) {
		try {
			Class<?> attClass = attributeClasses.get(index);
			attClass.cast(value);
			
		} catch (IndexOutOfBoundsException e) {
			throw new XMLParserException("Invalid index \"" + index + 
					"\" for node \"" + nodeName + "\".", e);
		} catch (ClassCastException e) {
			// TODO
		}
	}
}
