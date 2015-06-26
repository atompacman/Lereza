package com.atompacman.lereza.core.gui.midiFileIndex;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.apache.commons.io.FileUtils;

import com.atompacman.lereza.core.Wizard;
import com.atompacman.lereza.core.Parameters.Paths.SQL;
import com.atompacman.lereza.core.db.Database;
import com.atompacman.lereza.core.resources.context.ContextElementType;
import com.atompacman.lereza.core.resources.context.ContextElementType.DatabaseStatus;

public class ComboBoxElement extends IndexElement {

	//==================================== STATIC FIELDS =========================================\\

	private static String NO_PARENT_CONTEXT_ELEM_QUERY_TEMPL;
	private static String SING_PARENT_CONTEXT_ELEM_QUERY_TEMPL;
	private static String MULT_PARENT_CONTEXT_ELEM_QUERY_TEMPL;

	
	
	//======================================= FIELDS =============================================\\

	private JLabel 					label;
	private JComboBox<String> 		comboBox;
	private List<ComboBoxElement> 	childBoxes;
	
	
	
	//==================================== STATIC METHODS ========================================\\

	//--------------------------------- STATIC INITIALIZATION ------------------------------------\\

	static {
		try {
			NO_PARENT_CONTEXT_ELEM_QUERY_TEMPL = FileUtils.readFileToString(
			        new File(SQL.MIDIFileIndex.NO_PARENT_CONTEXT_ELEM));
			SING_PARENT_CONTEXT_ELEM_QUERY_TEMPL = FileUtils.readFileToString(
			        new File(SQL.MIDIFileIndex.SINGLE_PARENT_CONTEXT_ELEM));
			MULT_PARENT_CONTEXT_ELEM_QUERY_TEMPL = FileUtils.readFileToString(
			        new File(SQL.MIDIFileIndex.MULT_PARENT_CONTEXT_ELEM));
		} catch (IOException e) {
			throw new RuntimeException("Could not load SQL query templates: ", e);
		}
	}

	
	
	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

	ComboBoxElement(ContextElementType type, int columnWidth, int componentWidth) {
		super(type, columnWidth);
		this.label = new JLabel(getName());
		this.comboBox = new JComboBox<String>();
		this.comboBox.setMaximumSize(new Dimension(componentWidth, 20));
		this.comboBox.setPrototypeDisplayValue("");
		this.childBoxes = new ArrayList<ComboBoxElement>();
	}


	//------------------------------- LOAD CONTENT FROM DATABASE ---------------------------------\\

	public void loadContentFromDatabase() {
		if (elementType.getDatabaseStatus() != DatabaseStatus.NO_PARENT) {
			throw new RuntimeException("Only elements of type NO_PARENT don't need a parent.");
		}
		assignQueryResultsToBox(NO_PARENT_CONTEXT_ELEM_QUERY_TEMPL, getName());
	}

	public void loadContentFromDatabase(ComboBoxElement parent) {
		String template = null;
		
		if (elementType.getDatabaseStatus() == DatabaseStatus.SINGLE_PARENT) {
			template = SING_PARENT_CONTEXT_ELEM_QUERY_TEMPL;
		} else {
			template = MULT_PARENT_CONTEXT_ELEM_QUERY_TEMPL;
		}
		assignQueryResultsToBox(template, getName(), parent.getName(), parent.getTextContent());
		
		clearChildBoxes();
	}

	private void assignQueryResultsToBox(String selectQuery, String... jokers) {
		Database db = Wizard.getModule(Database.class);
		ResultSet queryResult = db.executeQuery(selectQuery, jokers);

		List<String> values = new ArrayList<String>();
		try {
			while(queryResult.next()){
				values.add(queryResult.getString(1));
			}
		} catch (SQLException e) {
			Database.throwDBExcep("Could retrieve query results", e);
		}
		
		Collections.sort(values);
		
		DefaultComboBoxModel<String> content = new DefaultComboBoxModel<String>();
		for (String value : values) {
			content.insertElementAt(value, content.getSize());
		}
		comboBox.setModel(content);
	}
	
	
	//-------------------------------------- CLEAR BOX -------------------------------------------\\

	private void clearChildBoxes() {
		for (ComboBoxElement child : childBoxes) {
			child.comboBox.removeAllItems();
			child.clearChildBoxes();
		}
	}
	
	
	//--------------------------------------- SETTERS --------------------------------------------\\

	public void addChildBox(final ComboBoxElement childBox) {
		childBoxes.add(childBox);
		final ComboBoxElement thisElement = this;
		
		this.comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {		
				if (e.getStateChange() == ItemEvent.SELECTED) {
					childBox.loadContentFromDatabase(thisElement);
				}
			}
		});
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public JLabel getLabel() {
		return label;
	}

	public Component getInputComponent() {
		return comboBox;
	}

	public String getTextContent() {
		if (comboBox.getSelectedItem() == null) {
			return null;
		}
		return comboBox.getSelectedItem().toString();
	}
}