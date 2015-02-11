package com.atompacman.lereza.gui.midiFileIndex;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.atompacman.lereza.Parameters;
import com.atompacman.lereza.Parameters.Paths.SQL;
import com.atompacman.lereza.api.ConfigManager;
import com.atompacman.lereza.api.Database;
import com.atompacman.lereza.api.Wizard;
import com.atompacman.lereza.db.Database;
import com.atompacman.lereza.resources.context.ContextElementType;
import com.atompacman.toolkat.io.TextFileReader;

public class MIDIFileIndex {

	//====================================== CONSTANTS ===========================================\\

	// Table / Input panel
	private static final IndexElement[] INDEX_ELEMENTS = {
		IndexElement.create(ContextElementType.ID, 			40),
		IndexElement.create(ContextElementType.URL, 		600, 	650),
		IndexElement.create(ContextElementType.ARTIST, 		80, 	160),
		IndexElement.create(ContextElementType.TITLE,		150, 	160),
		IndexElement.create(ContextElementType.NO, 			4, 		20),
		IndexElement.create(ContextElementType.COLLECTION, 	120, 	160),
		IndexElement.create(ContextElementType.SUBSET, 		200, 	160),
		IndexElement.create(ContextElementType.SET, 		200, 	160),
		IndexElement.create(ContextElementType.TRADITION, 	50, 	100),
		IndexElement.create(ContextElementType.ERA, 		60, 	140),
		IndexElement.create(ContextElementType.CULTURE,		70, 	180),
		IndexElement.create(ContextElementType.GENRE, 		80, 	200),
		IndexElement.create(ContextElementType.SUBGENRE, 	80, 	200),
		IndexElement.create(ContextElementType.FORM, 		80, 	200),
		IndexElement.create(ContextElementType.SUBFORM, 	80, 	200)};

	// Main window
	private static final boolean 	FULLSCREEN_AT_LAUNCH = false;
	private static final Dimension 	INITIAL_WINDOW_SIZE = new Dimension(1280, 720);
	private static final Dimension 	MINIMUM_WINDOW_SIZE = new Dimension(1024, 200);

	
	
	//==================================== STATIC FIELDS =========================================\\

	private static JFrame 				mainFrame;
	private static DefaultTableModel 	tableModel;
	private static JTable 				table;

	private static ResultSet 			updatableQueryResult;



	//==================================== STATIC METHODS ========================================\\
	
	//--------------------------------------- LAUNCH ---------------------------------------------\\

	public static void launch() {
		updatableQueryResult = queryDB(SQL.MIDIFileIndex.GLOBAL_SELECTION, true);
		
		mainFrame = new JFrame();
		mainFrame.add(createTable(), BorderLayout.CENTER);
		mainFrame.add(createInputPanel(), BorderLayout.SOUTH);
		mainFrame.setSize(INITIAL_WINDOW_SIZE);
		mainFrame.setMinimumSize(MINIMUM_WINDOW_SIZE);
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		if (FULLSCREEN_AT_LAUNCH) {
			mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		mainFrame.setVisible(true);
	}

	private static JScrollPane createTable() {
		String[] tableColumnNames = new String[INDEX_ELEMENTS.length];
		for (int i = 0; i < tableColumnNames.length; ++i) {
			tableColumnNames[INDEX_ELEMENTS[i].getQueryColumn()] = INDEX_ELEMENTS[i].getName();
		}
		tableModel = new DefaultTableModel(null, tableColumnNames) {
			private static final long serialVersionUID = 1L;

			public Class<?> getColumnClass(int column) {
				if (column < 0 || column >= getColumnCount()) {
					return Object.class;
				}
				return getValueAt(0, column).getClass();
			}
		};
		table = new JTable(tableModel);
		table.setAutoCreateRowSorter(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		TableColumnModel model = table.getColumnModel();
		for (int i = 0; i < INDEX_ELEMENTS.length; ++i) {
			int width = INDEX_ELEMENTS[i].getColumnWidth();
			model.getColumn(INDEX_ELEMENTS[i].getQueryColumn()).setPreferredWidth(width);
		}
		table.doLayout();
		table.addMouseListener(createTableMouseListener());

		try {
			ResultSet queryResults = queryDB(SQL.MIDIFileIndex.COMPLETE_INDEX, false);

			while(queryResults.next()){
				Object[] rowValues = new Object[INDEX_ELEMENTS.length];
				for (int i = 0; i < INDEX_ELEMENTS.length; ++i) {
					String value = queryResults.getString(i + 1);
					rowValues[i] = value == null ? " " : value;
				}
				tableModel.addRow(rowValues);
			}
		} catch (SQLException e) {
			Database.throwDBExcep("Could not iterate through query results", e);
		}

		return new JScrollPane(table);
	}
	
	private static MouseListener createTableMouseListener() {
		return new MouseAdapter() {

			public void mouseReleased(MouseEvent me) {
				if (me.getButton() != MouseEvent.BUTTON1) {
					return;
				}

				String value = JOptionPane.showInputDialog(null, "Enter value:"); 
				if (value == null) {
					return;
				}

				table.setValueAt(value, table.getSelectedRow(), table.getSelectedColumn());  

				try {
					updatableQueryResult.absolute(table.getSelectedRow() + 1);
					String updateCol = tableModel.getColumnName(table.getSelectedColumn());
					updatableQueryResult.updateString(updateCol, value);
					updatableQueryResult.updateRow();
				} catch (SQLException e) {
					Database.throwDBExcep("Could not modify value in database", e);
				} 
			} 
		};
	}

	private static JPanel createInputPanel() {
		JPanel layer2 = new JPanel();
		layer2.setLayout(new BoxLayout(layer2, BoxLayout.Y_AXIS));
		layer2.add(Box.createRigidArea(new Dimension(0, 8)));
		layer2.add(createUpperPanel());
		layer2.add(Box.createRigidArea(new Dimension(0, 8)));
		layer2.add(createLowerPanel());
		layer2.add(Box.createRigidArea(new Dimension(0, 8)));

		JPanel layer1 = new JPanel();
		layer1.setLayout(new BoxLayout(layer1, BoxLayout.X_AXIS));
		layer1.add(Box.createRigidArea(new Dimension(8, 0)));
		layer1.add(layer2);
		layer1.add(Box.createRigidArea(new Dimension(8, 0)));

		return layer1;
	}

	private static JPanel createUpperPanel() {
		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));

		JButton button = new JButton("Select URL");
		upperPanel.add(button);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				ConfigManager config = Wizard.getModule(ConfigManager.class);
				String midiDir = config.getString(Parameters.Paths.Dirs.MIDI_ROOT_DIR);
				JFileChooser chooser = new JFileChooser(midiDir);
				chooser.setFileFilter(new FileNameExtensionFilter("MIDI files (.mid)", "mid"));
				if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					String selectedFile = chooser.getSelectedFile().getAbsolutePath();
					String toRemove = new File(midiDir).getAbsolutePath();
					selectedFile = selectedFile.replace(toRemove + File.separator, "");
					((JTextField) INDEX_ELEMENTS[1].getInputComponent()).setText(selectedFile);
				}
			}
		});

		for (IndexElement column : INDEX_ELEMENTS) {
			if (column.getClass() == TextFieldElement.class) {
				upperPanel.add(Box.createRigidArea(new Dimension(15, 0)));
				upperPanel.add(column.getLabel());
				upperPanel.add(Box.createRigidArea(new Dimension(8, 0)));
				upperPanel.add(column.getInputComponent());
			} else if (column.getClass() == NoLabelTextFieldElement.class) {
				upperPanel.add(Box.createRigidArea(new Dimension(8, 0)));
				upperPanel.add(column.getInputComponent());
			}
		}

		return upperPanel;
	}

	private static JPanel createLowerPanel() {
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS));
				
		for (IndexElement element : INDEX_ELEMENTS) {
			if (element.getClass() != ComboBoxElement.class) {
				continue;
			}
			ComboBoxElement comboBox = (ComboBoxElement) element;
			lowerPanel.add(comboBox.getLabel());
			lowerPanel.add(Box.createRigidArea(new Dimension(8, 0)));
			lowerPanel.add(comboBox.getInputComponent());
			lowerPanel.add(Box.createRigidArea(new Dimension(15, 0)));

			linkParentComboBoxTo(comboBox);
		}

		lowerPanel.add(createAddFileButton());
		lowerPanel.add(Box.createRigidArea(new Dimension(15, 0)));
		lowerPanel.add(createRemoveFileButton());

		return lowerPanel;
	}

	private static void linkParentComboBoxTo(final ComboBoxElement box) {
		switch (box.getContextElementType().getDatabaseStatus()) {
		case NO_PARENT:
			box.loadContentFromDatabase();
			break;
		case MULTIPLE_PARENTS: case SINGLE_PARENT:
			ContextElementType parentElement = box.getContextElementType().getParentElement();
				
			for (final IndexElement element : INDEX_ELEMENTS) {
				if (parentElement != element.getContextElementType()) {
					continue;
				}
				((ComboBoxElement) element).addChildBox(box);
				break;
			}
			break;
		default:
			break;
		}
	}
	
	private static JButton createAddFileButton() {
		JButton addFileButton = new JButton("Add file");
		addFileButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				Object[] newRowValues = new Object[INDEX_ELEMENTS.length];

				try {
					updatableQueryResult.moveToInsertRow();

					for (int i = 0; i < INDEX_ELEMENTS.length; ++i) {
						IndexElement element = INDEX_ELEMENTS[i];
						ContextElementType type = element.getContextElementType();
						
						if (type == ContextElementType.ID) {
							continue;
						}
						String columnName = type.nameAsInDatabase();
						String fieldContent = element.getTextContent();
						
						if (fieldContent == null || !type.canBeNull() && fieldContent.isEmpty()) {
							return;
						}
						
						newRowValues[i] = fieldContent;
						
						if (element instanceof ComboBoxElement) {
							ResultSet result = queryDB(SQL.MIDIFileIndex.CONTEXT_ELEM_ID, 
									false, columnName, fieldContent);
							result.next();
							updatableQueryResult.updateInt(columnName + "_id", result.getInt(1));
						} else if (type != ContextElementType.NO) {
							updatableQueryResult.updateString(columnName, fieldContent);
						} else {
							int value = 0;
							try {
								value = Integer.parseInt(fieldContent);
							} catch (NumberFormatException e) {
								return;
							}
							updatableQueryResult.updateInt(columnName, value);
						}
					}
					updatableQueryResult.insertRow();
					updatableQueryResult.last();
					newRowValues[0] = updatableQueryResult.getInt(1);

					tableModel.addRow(newRowValues);
				} catch (SQLException e) {
					Database.throwDBExcep("Could not add new MIDI file to index", e);
				}
			}
		});
		
		return addFileButton;
	}
	
	private static JButton createRemoveFileButton() {
		JButton removeFileButton = new JButton("Remove file");
		removeFileButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				tableModel.removeRow(table.getSelectedRow());
				try {
					updatableQueryResult.absolute(table.getSelectedRow());
					updatableQueryResult.deleteRow();
				} catch (SQLException e) {
					Database.throwDBExcep("Could not remove row from database", e);
				}
			}
		});
		
		return removeFileButton;
	}


	//------------------------------------ PRIVATE UTILS -----------------------------------------\\

	private static ResultSet queryDB(SQL.MIDIFileIndex param, boolean storeQuery, String...jokers) {
		ConfigManager config = Wizard.getModule(ConfigManager.class);
		Database db = Wizard.getModule(Database.class);
		
		String sqlFilePath = config.getString(param);
		String query;
		try {
			query = TextFileReader.readAsSingleLine(sqlFilePath);
		} catch (IOException e) {
			throw new RuntimeException("Could not read SQL query "
					+ "template at \"" + sqlFilePath + "\"", e);
		}
		
		if (storeQuery) {
			return db.executeAndStoreQuery(query, jokers);
		} else {
			return db.executeQuery(query, jokers);
		}
	}
}