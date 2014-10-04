package com.atompacman.lereza.common.xml;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.helper.TestFileDetector;
import com.atompacman.lereza.exception.XMLParserException;

public class TestSimpleXMLParser {

	@Test
	public void singleNode() throws XMLParserException {
		String testFile = TestFileDetector.detectSingleFileForCurrentTest();
		
		NodeStructure rootNode = new NodeStructure("Node");
		
		NodeContent fileContent = SimpleXMLParser.parse(testFile, rootNode);
		
		assertTrue(fileContent.getNbAttributes() == 0);
		assertTrue(fileContent.getChildNodes().size() == 0);
	}
	
	@Test
	public void singleNodeWithAtt() throws XMLParserException {
		String testFile = TestFileDetector.detectSingleFileForCurrentTest();
		
		NodeStructure rootNode = new NodeStructure("Node", "attribute");
		
		NodeContent fileContent = SimpleXMLParser.parse(testFile, rootNode);
		
		assertTrue(fileContent.getNbAttributes() == 1);
		String attValue = fileContent.getAttributeValue("attribute");
		assertTrue(attValue != null);
		assertTrue(attValue.equals("hello"));
	}

	@Test
	public void simpleList() throws XMLParserException {
		String testFile = TestFileDetector.detectSingleFileForCurrentTest();
		
		NodeStructure rootNode = new NodeStructure("SimpleList");
		rootNode.addChildNode(new NodeStructure("Element", "id"));
		
		NodeContent fileContent = SimpleXMLParser.parse(testFile, rootNode);
		
		assertTrue(fileContent.getChildNodes().size() == 7);
		assertTrue(fileContent.getChildNodes().get(3).getAttributeValue("id").equals("3"));
	}
	
	@Test
	public void heterogenList() throws XMLParserException {
		String testFile = TestFileDetector.detectSingleFileForCurrentTest();
		
		NodeStructure rootNode = new NodeStructure("HeterogenList");
		rootNode.addChildNode(new NodeStructure("ElementA", "id"));
		rootNode.addChildNode(new NodeStructure("ElementB", "id"));
		rootNode.addChildNode(new NodeStructure("ElementC", "id"));
		
		NodeContent fileContent = SimpleXMLParser.parse(testFile, rootNode);
		
		assertTrue(fileContent.getChildNodes().get(2).getName().equals("ElementA"));
		assertTrue(fileContent.getChildNodes().get(4).getName().equals("ElementB"));
		assertTrue(fileContent.getChildNodes().get(6).getName().equals("ElementC"));
	}
	
	public void recurrentName() throws XMLParserException {
		String testFile = TestFileDetector.detectSingleFileForCurrentTest();
		
		NodeStructure level1 = new NodeStructure("Goglu");
		NodeStructure level2 = new NodeStructure("Goglu", "yolo");
		NodeStructure level3 = new NodeStructure("Goglu", "wololo");
		NodeStructure level4 = new NodeStructure("Goglu");

		level1.addChildNode(level2);
		level2.addChildNode(level3);
		level3.addChildNode(level4);
		
		NodeContent fileContent = SimpleXMLParser.parse(testFile, level1);
		
		assertTrue(fileContent.getName().equals("Goglu"));
		NodeContent child = fileContent.getChildNodes().get(0);
		assertTrue(child.getName().equals("Goglu"));
		assertTrue(child.getAttributeValue("yolo").equals("yesy"));
		child = fileContent.getChildNodes().get(0);
		assertTrue(child.getName().equals("Goglu"));
		assertTrue(child.getAttributeValue("wololo").equals("yes"));
		child = fileContent.getChildNodes().get(0);
		assertTrue(child.getName().equals("Goglu"));
	}
	
	@Test
	public void completeTest() throws XMLParserException {
		String testFile = TestFileDetector.detectSingleFileForCurrentTest();
		
		NodeStructure rootNode = new NodeStructure("Goglu");
		NodeStructure bananeNode = new NodeStructure("banane", "role");
		NodeStructure serpentNode = new NodeStructure("Serpent", "taste");
		NodeStructure yoloNode = new NodeStructure("yolo");
		NodeStructure yodaNode = new NodeStructure("yoda", "coolness");
		
		rootNode.addChildNode(bananeNode);
		bananeNode.addChildNode(serpentNode);
		bananeNode.addChildNode(bananeNode);
		bananeNode.addChildNode(yodaNode);
		bananeNode.addChildNode(yoloNode);
		
		NodeContent fileContent = SimpleXMLParser.parse(testFile, rootNode);
		
		assertTrue(
				fileContent.getChildNodes().get(1).
				getChildNodes().get(0).
				getChildNodes().get(4).
				getAttributeValue("coolness").equals("9600"));
	}
}