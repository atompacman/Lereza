package com.atompacman.lereza.core.gui.midiFileIndex;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.atompacman.lereza.core.resources.context.ContextElementType;

public class NoLabelTextFieldElement extends IndexElement {
	
	//======================================= FIELDS =============================================\\

	private JTextField textField;

	
	
	//======================================= METHODS ============================================\\

	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

	NoLabelTextFieldElement(ContextElementType type, 
			int columnWidth, int componentWidth) {
		super(type, columnWidth);
		this.textField = new JTextField();
		this.textField.setMaximumSize(new Dimension(componentWidth, 20));
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public JLabel getLabel() {
		return null;
	}
	
	public Component getInputComponent() {
		return textField;
	}
	
	public String getTextContent() {
		return textField.getText();
	}
}